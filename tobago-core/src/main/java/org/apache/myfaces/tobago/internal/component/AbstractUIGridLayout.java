/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.layout.GridSpan;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A grid layout manager.
 * </p>
 * <p>
 * {@link org.apache.myfaces.tobago.internal.taglib.component.GridLayoutTagDeclaration}
 * </p>
 */
@Preliminary
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public abstract class AbstractUIGridLayout extends AbstractUILayoutBase
    implements Visual, ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.GridLayout";

  private static final int STEP = 5;

  protected static final UIComponent SPAN = new UIPanel();

  /**
   * Initialize the grid and remove the current width and height values from the component, recursively.
   */
  @Override
  public void processEvent(final ComponentSystemEvent event) throws AbortProcessingException {

    super.processEvent(event);

    if (!isRendered()) {
      return;
    }

    if (event instanceof PreRenderComponentEvent) {

      layout(
          MeasureList.parse(getColumns()).getSize(),
          MeasureList.parse(getRows()).getSize(),
          ComponentUtils.findLayoutChildren(this));

    }
  }

  public abstract String getRows();

  public abstract void setRows(String rows);

  public abstract String getColumns();

  public abstract void setColumns(String columns);

  protected UIComponent[][] layout(
      final int columnsCount, final int initialRowsCount, final List<UIComponent> components) {
    assert columnsCount > 0;
    assert initialRowsCount > 0;

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    UIComponent[][] cells = new UIComponent[initialRowsCount][columnsCount];

    // #1 put all components with "gridRow" and "gridColumn" set into the grid cells
    for (final UIComponent component : components) {
      final Map<String, Object> attributes = component.getAttributes();
      final Integer gridColumn = (Integer) attributes.get(Attributes.gridColumn.getName());
      final Integer gridRow = (Integer) attributes.get(Attributes.gridRow.getName());
      if (gridColumn != null && gridRow != null) {
        if (gridColumn > columnsCount) {
          // ignore wrong columns
          LOG.warn("gridColumn {} > columnsCount {} in component '{}'!", gridColumn, columnsCount,
              component.getClientId(facesContext));
        } else {
          if (gridRow > cells.length) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("expanding, because gridRow {} > rowCount {} in component '{}'!", gridRow, cells.length,
                  component.getClientId(facesContext));
            }
            // ensure enough rows
            cells = expand(cells, gridRow);
          }
          cells = set(cells, gridColumn - 1, gridRow - 1, component);
        }
      } else if (gridColumn != null) {
        LOG.warn("gridColumn is set to {}, but gridRow not in component '{}'!", gridColumn,
            component.getClientId(facesContext));
      } else if (gridRow != null) {
        LOG.warn("gridRow is set to {}, but gridColumn not in component '{}'!", gridRow,
            component.getClientId(facesContext));
      }
    }

    // #2 distribute the rest of the components to the free grid cells
    int j = 0;
    int i = 0;
    for (final UIComponent component : components) {
      final Map<String, Object> attributes = component.getAttributes();
      final Integer gridColumn = (Integer) attributes.get(Attributes.gridColumn.getName());
      final Integer gridRow = (Integer) attributes.get(Attributes.gridRow.getName());
      // find a component without a position
      // if only one value is defined, treat as undefined
      if (gridColumn == null || gridRow == null) {
        // find next free cell
        while (cells[j][i] != null) {
          i++;
          if (i >= columnsCount) {
            i = 0;
            j++;
          }
          if (j >= cells.length) {
            cells = expand(cells, j + STEP);
          }
        }
        cells = set(cells, i, j, component);
      }
    }

    // #3 create UIStyle children. TODO: There might be a better way...
    for (final UIComponent component : components) {
      final Map<String, Object> attributes = component.getAttributes();

      UIStyle style = ComponentUtils.findChild(component, UIStyle.class);
      if (style == null) {
        style = (UIStyle) ComponentUtils.createComponent(
            facesContext, UIStyle.COMPONENT_TYPE, RendererTypes.Style, null);
        component.getChildren().add(style);
      }
      // Style must be transient to avoid creating a new instance of GridSpan while restore state
      // https://issues.apache.org/jira/browse/TOBAGO-1909
      style.setTransient(true);

      final Integer gridColumn = (Integer) attributes.get(Attributes.gridColumn.getName());
      final Integer columnSpan = (Integer) attributes.get(Attributes.columnSpan.getName());
      if (gridColumn != null) {
        style.setGridColumn(GridSpan.valueOf(gridColumn, columnSpan));
      }

      final Integer gridRow = (Integer) attributes.get(Attributes.gridRow.getName());
      final Integer rowSpan = (Integer) attributes.get(Attributes.rowSpan.getName());
      if (gridRow != null) {
        style.setGridRow(GridSpan.valueOf(gridRow, rowSpan));
      }
    }

    return cells;
  }

  /**
   * Set the component to the cells grid.
   * Also mark the span-area as occupied.
   *
   * @param initialCells The cells area
   * @param column       Zero based column position
   * @param row          Zero based row position
   * @param component    Component to set
   * @return The result cells, possibly the initial array or a new array
   */
  private UIComponent[][] set(
      final UIComponent[][] initialCells, final Integer column, final Integer row, final UIComponent component) {

    UIComponent[][] cells = initialCells;

    final Map<String, Object> attributes = component.getAttributes();
    Integer rowSpan = (Integer) attributes.get(Attributes.rowSpan.getName());
    if (rowSpan == null) {
      rowSpan = 1;
    }
    Integer columnSpan = (Integer) attributes.get(Attributes.columnSpan.getName());
    if (columnSpan == null) {
      columnSpan = 1;
    }

    // the span area
    for (int j = row; j < rowSpan + row; j++) {
      for (int i = column; i < columnSpan + column; i++) {
        if (i >= cells[0].length) {
          LOG.warn("column {} + columnSpan {} - 1 >= columnsCount {} in component '{}'!",
              column + 1, columnSpan, cells[0].length, component.getClientId(FacesContext.getCurrentInstance()));
          break;
        }
        if (j >= cells.length) {
          cells = expand(cells, j + STEP);
        }
        if (j == row && i == column) {
          cells[j][i] = component;
          attributes.put(Attributes.gridRow.getName(), j + 1);
          attributes.put(Attributes.gridColumn.getName(), i + 1);
        } else {
          cells[j][i] = SPAN;
        }
      }
    }

    return cells;
  }

  /**
   * @deprecated since 4.3.0, please use {@link #expand(UIComponent[][], int)}
   */
  @Deprecated
  protected UIComponent[][] expand(final UIComponent[][] cells, final Integer minRows, final int step) {
    return expand(cells, minRows);
  }

  protected UIComponent[][] expand(final UIComponent[][] cells, final int rows) {
    final int columns = cells[0].length;

    final UIComponent[][] result = new UIComponent[rows][columns];
    for (int j = 0; j < cells.length; j++) {
      System.arraycopy(cells[j], 0, result[j], 0, columns);
    }
    return result;
  }

}
