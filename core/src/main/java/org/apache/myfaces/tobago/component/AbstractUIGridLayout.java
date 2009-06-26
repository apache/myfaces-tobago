package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.OnComponentCreated;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.FixedLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.layout.grid.Cell;
import org.apache.myfaces.tobago.layout.grid.Grid;
import org.apache.myfaces.tobago.layout.grid.RealCell;
import org.apache.myfaces.tobago.layout.math.EquationManager;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.LayoutUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUIGridLayout extends UILayout implements OnComponentCreated, LayoutManager {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);


// LAYOUT Begin
  // XXX new layout manager begin

  private Grid grid;

  // XXX columns and columnTokens are double/redundant
  private LayoutTokens columnTokens;

  // XXX rows and rowTokens are double/redundant
  private LayoutTokens rowTokens;

  public void onComponentCreated(FacesContext context, UIComponent component) {

    columnTokens = LayoutTokens.parse(getColumns());
    rowTokens = LayoutTokens.parse(getRows());

    grid = new Grid(columnTokens.getSize(), rowTokens.getSize());
  }

  public void collect(LayoutContext layoutContext, LayoutContainer container, int horizontalIndex, int verticalIndex) {

    // horizontal
    EquationManager horizontal = layoutContext.getHorizontal();
    int[] horizontalIndices = horizontal.divide(horizontalIndex, columnTokens.getSize());

    // vertical
    EquationManager vertical = layoutContext.getVertical();
    int[] verticalIndices = vertical.divide(verticalIndex, rowTokens.getSize());

    List<LayoutComponent> components = container.getComponents();
    for (LayoutComponent component1 : components) {
      grid.add(new RealCell(component1), component1.getColumnSpan(), component1.getRowSpan());
      LOG.debug("\n" + grid);
    }

    addFixedConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);
    addRelativeConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);

    for (int j = 0; j < grid.getRowCount(); j++) {
      for (int i = 0; i < grid.getColumnCount(); i++) {
        org.apache.myfaces.tobago.layout.grid.Cell temp = grid.get(i, j);
        if (temp instanceof RealCell) {
          RealCell cell = (RealCell) temp;
          LayoutComponent component = temp.getComponent();

          // horizontal
          int hIndex = horizontal.addComponent(horizontalIndices[i], cell.getColumnSpan());
          cell.getComponent().setHorizontalIndex(hIndex);

          // vertical
          int vIndex = vertical.addComponent(verticalIndices[j], cell.getRowSpan());
          cell.getComponent().setVerticalIndex(vIndex);

          if (component instanceof LayoutContainer) {
            LayoutContainer subContainer = (LayoutContainer) component;
            LayoutManager layoutManager = subContainer.getLayoutManager();
            layoutManager.collect(layoutContext, subContainer, hIndex, vIndex);
          }
        }
      }
    }
  }

  public void distribute(LayoutContext layoutContext, LayoutContainer container) {

    distributeSizes(layoutContext);
    distributePositions();
  }

  private void distributeSizes(LayoutContext layoutContext) {

    for (int j = 0; j < grid.getRowCount(); j++) {
      for (int i = 0; i < grid.getColumnCount(); i++) {
        Cell temp = grid.get(i, j);
        if (temp instanceof RealCell) {
          RealCell cell = (RealCell) temp;
          LayoutComponent component = temp.getComponent();

          component.setDisplay(Display.BLOCK);

          EquationManager horizontal = layoutContext.getHorizontal();
          EquationManager vertical = layoutContext.getVertical();

          int horizontalIndex = cell.getComponent().getHorizontalIndex();
          int verticalIndex = cell.getComponent().getVerticalIndex();

          PixelMeasure width = new PixelMeasure(horizontal.getResult()[horizontalIndex]);
          PixelMeasure height = new PixelMeasure(vertical.getResult()[verticalIndex]);

          component.setWidth(width);
          component.setHeight(height);

          if (component instanceof LayoutContainer) {

            LayoutContainer subContainer = (LayoutContainer) component;
            LayoutManager layoutManager = subContainer.getLayoutManager();
            if (layoutManager != null) {
              layoutManager.distribute(layoutContext, subContainer);
            }
          } 
        }
      }
    }
  }

  private void distributePositions() {

    // find the "left" positions
    for (int j = 0; j < grid.getRowCount(); j++) {
      PixelMeasure left = new PixelMeasure(0);
      for (int i = 0; i < grid.getColumnCount(); i++) {
        Cell cell = grid.get(i, j);
        if (cell == null) {
          continue; // XXX why this can happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof RealCell) {
          component.setLeft(left);
        }
        if (cell.isInFirstColumn()) {
          left = (PixelMeasure) left.add(component.getWidth());
        }
      }
    }

    // find the "top" positions
    for (int i = 0; i < grid.getColumnCount(); i++) {
      PixelMeasure top = new PixelMeasure(0);
      for (int j = 0; j < grid.getRowCount(); j++) {
        Cell cell = grid.get(i, j);
        if (cell == null) {
          continue; // XXX why this can happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof RealCell) {
          component.setTop(top);
        }
        if (cell.isInFirstRow()) {
          top = (PixelMeasure) top.add(component.getHeight());
        }
      }
    }
  }

  private void addFixedConstraints(LayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    for (int i = 0; i < columnTokens.getSize(); i++) {
      LayoutToken layoutToken = columnTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) layoutToken).getPixel();
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel);
      }
      if (layoutToken instanceof FixedLayoutToken) {
        int pixel = 100;
        LOG.warn("auto/fixed is not implemented yet and was set to 100px");
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel);
      }
    }
    // vertical
    for (int i = 0; i < rowTokens.getSize(); i++) {
      LayoutToken layoutToken = rowTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) layoutToken).getPixel();
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel);
      }
      if (layoutToken instanceof FixedLayoutToken) {
        int pixel = 25;
        LOG.warn("auto/fixed is not implemented yet and was set to 25px");
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel);
      }
    }
  }

  private void addRelativeConstraints(LayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    Integer first = null;
    Integer firstIndex = null;
    for (int i = 0; i < columnTokens.getTokens().size(); i++) {
      LayoutToken token = columnTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i + horizontalIndexOffset;
        } else {
          layoutContext.getHorizontal().setProportion(firstIndex, i + horizontalIndexOffset, first, factor);
        }
      }
    }
    // vertical
    first = null;
    firstIndex = null;
    for (int i = 0; i < rowTokens.getTokens().size(); i++) {
      LayoutToken token = rowTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i + verticalIndexOffset;
        } else {
          layoutContext.getVertical().setProportion(firstIndex, i + verticalIndexOffset, first, factor);
        }
      }
    }
  }

  protected Grid getGrid() {
    return grid;
  }

  // XXX new layout manager end
// LAYOUT End

  public static final Marker FREE = new Marker("free");
  public static final String USED = "used";

  private boolean ignoreFree;
  private transient LayoutTokens columnLayout;
  private transient LayoutTokens rowLayout;

  private List<Row> layoutRows;

// LAYOUT Begin

  public AbstractUIGridLayout() {
    LOG.info("**************************************************************************************"
        + " Constructor AbstractUIGridLayout");
  }

  // LAYOUT End

  public LayoutTokens getRowLayout() {
    if (rowLayout == null) {
      rowLayout = LayoutTokens.parse(getRows());
    }
    return rowLayout;
  }

  public LayoutTokens getColumnLayout() {
    if (columnLayout == null) {
      columnLayout = LayoutTokens.parse(getColumns());
    }
    return columnLayout;
  }


  public abstract String getRows();

  public abstract String getColumns();


  public Object saveState(FacesContext context) {
    clearRows();
    return super.saveState(context);
  }


  @Override
  public boolean getRendersChildren() {
    return false;
  }

//  @Override
//  public void encodeChildren(FacesContext context)
//      throws IOException {
    // do nothing here
//  }

  @Override
  public void encodeChildrenOfComponent(
      FacesContext facesContext, UIComponent component) throws IOException {
    super.encodeChildrenOfComponent(facesContext, component);
    clearRows();
  }

  private void clearRows() {
    layoutRows = null;
  }

  public int getColumnCount() {
    return getColumnLayout().getSize();
  }

  @Deprecated
  public List<Row> ensureRows() {
    if (layoutRows == null) {
      layoutRows = createRows();
    }
    return layoutRows;
  }

  @Deprecated
  private List<Row> createRows() {
    // XXX
    LOG.warn("This code is deprecated and should be removed!");
    List<Row> rows = new ArrayList<Row>();
    int columnCount = getColumnCount();
    List<UIComponent> children
        = LayoutUtils.addChildren(new ArrayList<UIComponent>(), getParent());

    for (UIComponent component : children) {
      int spanX = getSpanX(component);
      int spanY = getSpanY(component);

      int r = nextFreeRow(rows);
      if (r == rows.size()) {
        rows.add(new Row(columnCount));
      }
      int c = rows.get(r).nextFreeColumn();
      rows.get(r).addControl(component, spanX);
      rows.get(r).fill(c + 1, c + spanX, component.isRendered());

      for (int i = r + 1; i < r + spanY; i++) {

        if (i == rows.size()) {
          rows.add(new Row(columnCount));
        }
        rows.get(i).fill(c, c + spanX, component.isRendered());
      }
    }
    return rows;
  }

  private int nextFreeRow(List rows) {
    int i = 0;
    for (; i < rows.size(); i++) {
      if (((Row) rows.get(i)).nextFreeColumn() != -1) {
        return i;
      }
    }
    return i;
  }

  public static int getSpanX(UIComponent component) {
    return ComponentUtil.getIntAttribute(
        component, Attributes.SPAN_X, 1);
  }

  public static int getSpanY(UIComponent component) {
    return ComponentUtil.getIntAttribute(
        component, Attributes.SPAN_Y, 1);
  }

  public boolean isIgnoreFree() {
    return ignoreFree;
  }

  public void setIgnoreFree(boolean ignoreFree) {
    this.ignoreFree = ignoreFree;
  }

  public static class Row implements Serializable {
    private static final long serialVersionUID = 1511693677519052045L;
    private int columns;
    private List cells;
    private boolean hidden;

    public Row(int columns) {
      setColumns(columns);
    }

    private void addControl(UIComponent component, int spanX) {

      int i = nextFreeColumn();

      cells.set(i, component);
      fill(i + 1, i + spanX, component.isRendered());
    }

    private void fill(int start, int end, boolean rendered) {

      if (end > columns) {
        LOG.error("Error in Jsp (end > columns). "
            + "Try to insert more spanX as possible.");
        LOG.error("start:   " + start);
        LOG.error("end:     " + end);
        LOG.error("columns: " + columns);
        LOG.error("Actual cells:");
        for (Object component : cells) {
          if (component instanceof UIComponent) {
            LOG.error("Cell-ID: " + ((UIComponent) component).getId()
                + " " + ((UIComponent) component).getRendererType());
          } else {
            LOG.error("Cell:    " + component); // e.g. marker
          }
        }

        end = columns; // fix the "end" parameter to continue the processing.
      }

      for (int i = start; i < end; i++) {
        cells.set(i, new Marker(USED, rendered));
      }
    }

    private int nextFreeColumn() {
      for (int i = 0; i < columns; i++) {
        if (FREE.equals(cells.get(i))) {
          return i;
        }
      }
      return -1;
    }

    public List getElements() {
      return cells;
    }

    public int getColumns() {
      return columns;
    }

    private void setColumns(int columns) {
      this.columns = columns;
      cells = new ArrayList(columns);
      for (int i = 0; i < columns; i++) {
        cells.add(FREE);
      }
    }

    public boolean isHidden() {
      return hidden;
    }

    public void setHidden(boolean hidden) {
      this.hidden = hidden;
    }
  }

  public static class Marker implements Serializable {
    private static final long serialVersionUID = 2505999420762504893L;
    private final String name;
    private boolean rendered;

    private Marker(String name) {
      this.name = name;
    }

    public Marker(String name, boolean rendered) {
      this.name = name;
      this.rendered = rendered;
    }

    @Override
    public String toString() {
      return name;
    }

    public boolean isRendered() {
      return rendered;
    }
  }
}
