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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.layout.Cell;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.EquationLayoutContext;
import org.apache.myfaces.tobago.layout.EquationLayoutManager;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.layout.math.EquationManager;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractUIEquationGridLayout extends UILayout 
    implements EquationLayoutManager, Configurable {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIEquationGridLayout.class);

  private Grid grid;

  public void init() {
    grid = new Grid(LayoutTokens.parse(getColumns()), LayoutTokens.parse(getRows()));

    List<LayoutComponent> components = getLayoutContainer().getComponents();
    for (LayoutComponent component : components) {
      grid.add(new OriginCell(component), component.getColumnSpan(), component.getRowSpan());
      if (LOG.isDebugEnabled()) {
        LOG.debug("\n" + grid);
      }
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }
  
  /**
   * Phase 1: Collects the layout information from the components recursively.
   */
  public void collect(EquationLayoutContext layoutContext, LayoutContainer container, int horizontalIndex, int verticalIndex) {

    init();

    // horizontal
    EquationManager horizontal = layoutContext.getHorizontal();
    int[] horizontalIndices = horizontal.partition(
        horizontalIndex, grid.getColumns().getSize(), getColumnSpacing(),
        container.getLeftOffset(), container.getRightOffset(), grid.getColumns(), container);
    grid.setHorizontalIndices(horizontalIndices);

    // vertical
    EquationManager vertical = layoutContext.getVertical();
    int[] verticalIndices = vertical.partition(
        verticalIndex, grid.getRows().getSize(), getRowSpacing(),
        container.getTopOffset(), container.getBottomOffset(), grid.getRows(), container);
    grid.setVerticalIndices(verticalIndices);

    addPixelConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);
    addRelativeConstraints(layoutContext, horizontalIndex + 1, verticalIndex + 1);

    for (int j = 0; j < grid.getRows().getSize(); j++) {
      for (int i = 0; i < grid.getColumns().getSize(); i++) {
        Cell temp = grid.getCell(i, j);
        if (temp instanceof OriginCell) {
          OriginCell cell = (OriginCell) temp;
          LayoutComponent component = temp.getComponent();

          // horizontal
          int hIndex = horizontal.combine(
              horizontalIndices[i], cell.getColumnSpan(), getColumnSpacing(), grid.getColumns().get(i), component);
          cell.getComponent().setHorizontalIndex(hIndex);

          // vertical
          int vIndex = vertical.combine(
              verticalIndices[j], cell.getRowSpan(), getRowSpacing(), grid.getRows().get(j), component);
          cell.getComponent().setVerticalIndex(vIndex);

          if (component instanceof LayoutContainer) {
            LayoutContainer subContainer = (LayoutContainer) component;
            EquationLayoutManager layoutManager = (EquationLayoutManager) subContainer.getLayoutManager();
            layoutManager.collect(layoutContext, subContainer, hIndex, vIndex);
          } else {
            // XXX is this a good idea?

            Measure preferredWidth = component.getPreferredWidth();
            if (preferredWidth != null) {
              horizontal.setFixedLength(hIndex, preferredWidth, component);
            }
            Measure preferredHeight = component.getPreferredHeight();
            if (preferredHeight != null) {
              vertical.setFixedLength(vIndex, preferredHeight, component);
            }
          }
        }
      }
    }
  }

  /**
   * Phase 2: Distribute the computed values into the components recursively.
   */
  public void distribute(EquationLayoutContext layoutContext, LayoutContainer container) {

    distributeSizes(layoutContext);
    distributePositions(layoutContext, container);
  }

  private void distributeSizes(EquationLayoutContext layoutContext) {

    for (int j = 0; j < grid.getRows().getSize(); j++) {
      for (int i = 0; i < grid.getColumns().getSize(); i++) {
        Cell temp = grid.getCell(i, j);
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
            EquationLayoutManager layoutManager = (EquationLayoutManager) subContainer.getLayoutManager();
            if (layoutManager != null) {
              layoutManager.distribute(layoutContext, subContainer);
            }
          }
        }
      }
    }
  }

  private void distributePositions(EquationLayoutContext layoutContext, LayoutContainer container) {

    // find the "left" positions
    for (int j = 0; j < grid.getRows().getSize(); j++) {
      Measure left = container.getLeftOffset();
      for (int i = 0; i < grid.getColumns().getSize(); i++) {
        Cell cell = grid.getCell(i, j);
        if (cell == null) {
          continue; // XXX why this can happen? Can it still happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof OriginCell) {
          component.setLeft(left);
        }
        EquationManager horizontal = layoutContext.getHorizontal();
        left = left.add(horizontal.getResult()[grid.getHorizontalIndices()[i]]);
        left = left.add(getColumnSpacing());
      }
    }

    // find the "top" positions
    for (int i = 0; i < grid.getColumns().getSize(); i++) {
      Measure top = container.getTopOffset();
      for (int j = 0; j < grid.getRows().getSize(); j++) {
        Cell cell = grid.getCell(i, j);
        if (cell == null) {
          continue; // XXX why this can happen? Can it still happen?
        }
        LayoutComponent component = cell.getComponent();
        if (cell instanceof OriginCell) {
          component.setTop(top);
        }
        EquationManager vertical = layoutContext.getVertical();
        top = top.add(vertical.getResult()[grid.getVerticalIndices()[j]]);
        top = top.add(getRowSpacing());
      }
    }
  }

  private void addPixelConstraints(EquationLayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    LayoutTokens columnTokens = grid.getColumns();
    for (int i = 0; i < columnTokens.getSize(); i++) {
      LayoutToken layoutToken = columnTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        Measure pixel = Measure.valueOf(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel, this);
      }
    }
    // vertical
    LayoutTokens rowTokens = grid.getRows();
    for (int i = 0; i < rowTokens.getSize(); i++) {
      LayoutToken layoutToken = rowTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        // XXX PixelLayoutToken might be removed/changed
        Measure pixel = Measure.valueOf(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel, this);
      }
    }
  }

  private void addRelativeConstraints(EquationLayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    Integer first = null;
    Integer firstIndex = null;
    LayoutTokens columnTokens = grid.getColumns();
    for (int i = 0; i < columnTokens.getTokens().size(); i++) {
      LayoutToken token = columnTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i + horizontalIndexOffset;
        } else {
          layoutContext.getHorizontal().proportionate(
              firstIndex, i + horizontalIndexOffset, first, factor, getLayoutContainer());
        }
      }
    }
    // vertical
    first = null;
    firstIndex = null;
    LayoutTokens rowTokens = grid.getRows();
    for (int i = 0; i < rowTokens.getTokens().size(); i++) {
      LayoutToken token = rowTokens.getTokens().get(i);
      if (token instanceof RelativeLayoutToken) {
        int factor = ((RelativeLayoutToken) token).getFactor();
        if (first == null) {
          first = factor;
          firstIndex = i + verticalIndexOffset;
        } else {
          layoutContext.getVertical().proportionate(
              firstIndex, i + verticalIndexOffset, first, factor, getLayoutContainer());
        }
      }
    }
  }

  public void fixRelativeInsideAuto(Orientation orientation, boolean auto) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void preProcessing(Orientation orientation) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void mainProcessing(Orientation orientation) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void postProcessing(Orientation orientation) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return ((LayoutContainer) getParent());
  }

  protected Grid getGrid() {
    return grid;
  }

  public Measure getSpacing(boolean horizontal) {
    return horizontal ? getColumnSpacing() : getRowSpacing();
  }

  public abstract String getRows();

  public abstract String getColumns();

  @Deprecated
  public abstract Measure getCellspacing();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + (grid != null
        ? "(" + Arrays.toString(grid.getWidths()) + ", " + Arrays.toString(grid.getHeights()) + ")" 
        : "");
  }
}
