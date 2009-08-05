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

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.OnComponentCreated;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.layout.grid.Cell;
import org.apache.myfaces.tobago.layout.grid.Grid;
import org.apache.myfaces.tobago.layout.grid.OriginCell;
import org.apache.myfaces.tobago.layout.math.EquationManager;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public abstract class AbstractUIGridLayout extends UILayout implements OnComponentCreated, LayoutManager {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);

  private Grid grid;

  // XXX columns and columnTokens are double/redundant
  private LayoutTokens columnTokens;

  // XXX rows and rowTokens are double/redundant
  private LayoutTokens rowTokens;

  public AbstractUIGridLayout() {
    LOG.info("**************************************************************************************"
        + " Constructor AbstractUIGridLayout");
  }

  public void onComponentCreated(FacesContext context, UIComponent component) {

    columnTokens = LayoutTokens.parse(getColumns());
    rowTokens = LayoutTokens.parse(getRows());

    grid = new Grid(columnTokens.getSize(), rowTokens.getSize());
  }

  /**
   * Phase 1: Collects the layout information from the components recursively.
   */
  public void collect(LayoutContext layoutContext, LayoutContainer container, int horizontalIndex, int verticalIndex) {

    // horizontal
    EquationManager horizontal = layoutContext.getHorizontal();
    int[] horizontalIndices = horizontal.partition(
        horizontalIndex, columnTokens.getSize(),
        getColumnSpacing(), container.getLeftOffset(), container.getRightOffset(),
        container.getClass().getSimpleName());

    // vertical
    EquationManager vertical = layoutContext.getVertical();
    int[] verticalIndices = vertical.partition(
        verticalIndex, rowTokens.getSize(), getRowSpacing(),
        container.getTopOffset(), container.getBottomOffset(), container.getClass().getSimpleName());

    List<LayoutComponent> components = container.getComponents();
    for (LayoutComponent c : components) {
      grid.add(new OriginCell(c), c.getColumnSpan(), c.getRowSpan());
      LOG.debug("\n" + grid);
    }

    addPixelConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);
    addRelativeConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);

    for (int j = 0; j < grid.getRowCount(); j++) {
      for (int i = 0; i < grid.getColumnCount(); i++) {
        Cell temp = grid.get(i, j);
        if (temp instanceof OriginCell) {
          OriginCell cell = (OriginCell) temp;
          LayoutComponent component = temp.getComponent();

          // horizontal
          int hIndex = horizontal.combine(horizontalIndices[i], cell.getColumnSpan(), getColumnSpacing(),
              component.getClass().getSimpleName());
          cell.getComponent().setHorizontalIndex(hIndex);

          // vertical
          int vIndex = vertical.combine(verticalIndices[j], cell.getRowSpan(), getRowSpacing(),
              component.getClass().getSimpleName());
          cell.getComponent().setVerticalIndex(vIndex);

          if (component instanceof LayoutContainer) {
            LayoutContainer subContainer = (LayoutContainer) component;
            LayoutManager layoutManager = subContainer.getLayoutManager();
            layoutManager.collect(layoutContext, subContainer, hIndex, vIndex);
          } else {
            // XXX is this a good idea?

            Measure preferredWidth = component.getPreferredWidth();
            if (preferredWidth != null) {
              horizontal.setFixedLength(hIndex, preferredWidth, component.getClass().getSimpleName());
            }
            Measure preferredHeight = component.getPreferredHeight();
            if (preferredHeight != null) {
              vertical.setFixedLength(vIndex, preferredHeight, component.getClass().getSimpleName());
            }
          }
        }
      }
    }
  }

  /**
   * Phase 2: Distribute the computed values into the components recursively.
   */
  public void distribute(LayoutContext layoutContext, LayoutContainer container) {

    distributeSizes(layoutContext);
    distributePositions(container);
  }

  private void distributeSizes(LayoutContext layoutContext) {

    for (int j = 0; j < grid.getRowCount(); j++) {
      for (int i = 0; i < grid.getColumnCount(); i++) {
        Cell temp = grid.get(i, j);
        if (temp instanceof OriginCell) {
          OriginCell cell = (OriginCell) temp;
          LayoutComponent component = temp.getComponent();

          component.setDisplay(Display.BLOCK);

          EquationManager horizontal = layoutContext.getHorizontal();
          EquationManager vertical = layoutContext.getVertical();

          int horizontalIndex = cell.getComponent().getHorizontalIndex();
          int verticalIndex = cell.getComponent().getVerticalIndex();

          Measure width = horizontal.getResult()[horizontalIndex];
          Measure height = vertical.getResult()[verticalIndex];

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

  private void distributePositions(LayoutContainer container) {

    // find the "left" positions
    for (int j = 0; j < grid.getRowCount(); j++) {
      PixelMeasure left = (PixelMeasure) container.getLeftOffset();
      for (int i = 0; i < grid.getColumnCount(); i++) {
        Cell cell = grid.get(i, j);
        if (cell == null) {
          continue; // XXX why this can happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof OriginCell) {
          component.setLeft(left);
        }
        if (cell.isHorizontalFirst()) {
          // XXX subject of change: may use the width of the column, and not of the component
          // XXX (the component may have a other size with offset or inset (scrollbars) etc.)
          left = (PixelMeasure) left.add(component.getWidth());
          left = (PixelMeasure) left.add(getColumnSpacing());
        }
      }
    }

    // find the "top" positions
    for (int i = 0; i < grid.getColumnCount(); i++) {
      PixelMeasure top = (PixelMeasure) container.getTopOffset();
      for (int j = 0; j < grid.getRowCount(); j++) {
        Cell cell = grid.get(i, j);
        if (cell == null) {
          continue; // XXX why this can happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof OriginCell) {
          component.setTop(top);
        }
        if (cell.isVerticalFirst()) {
          // XXX subject of change: may use the height of the row, and not of the component
          // XXX (the component may have a other size with offset or inset (scrollbars) etc.)
          top = (PixelMeasure) top.add(component.getHeight());
          top = (PixelMeasure) top.add(getRowSpacing());
        }
      }
    }
  }

  private void addPixelConstraints(LayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    for (int i = 0; i < columnTokens.getSize(); i++) {
      LayoutToken layoutToken = columnTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        Measure pixel = new PixelMeasure(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel,
            ClassUtils.getShortClassName(getParent(), "null"));
      }
/*
      if (layoutToken instanceof AutoLayoutToken) {
        Measure pixel = new PixelMeasure(100); // TODO
        LOG.warn("auto/fixed is not implemented yet and was set to 100px");
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel);
      }
*/
    }
    // vertical
    for (int i = 0; i < rowTokens.getSize(); i++) {
      LayoutToken layoutToken = rowTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
         // XXX PixelLayoutToken might be removed/changed
        Measure pixel = new PixelMeasure(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel,
            ClassUtils.getShortClassName(getParent(), "null"));
      }
/*
      if (layoutToken instanceof AutoLayoutToken) {
        Measure pixel = new PixelMeasure(25); // TODO
        LOG.warn("auto/fixed is not implemented yet and was set to 25px");
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel);
      }
*/
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
          layoutContext.getHorizontal().proportionate(
              firstIndex, i + horizontalIndexOffset, first, factor, ClassUtils.getShortClassName(getParent(), "null"));
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
          layoutContext.getVertical().proportionate(
              firstIndex, i + verticalIndexOffset, first, factor, ClassUtils.getShortClassName(getParent(), "null"));
        }
      }
    }
  }

  protected Grid getGrid() {
    return grid;
  }

  public abstract String getRows();

  public abstract String getColumns();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  @Override
  public boolean getRendersChildren() {
    return false;
  }
}
