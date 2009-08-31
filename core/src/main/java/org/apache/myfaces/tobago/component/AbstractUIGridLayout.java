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
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.FactorList;
import org.apache.myfaces.tobago.layout.Interval;
import org.apache.myfaces.tobago.layout.IntervalList;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.LayoutUtils;
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
import java.util.Arrays;
import java.util.List;

public abstract class AbstractUIGridLayout extends UILayout implements OnComponentCreated, LayoutManager {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);

  private Grid grid;

  public void onComponentCreated(FacesContext context, UIComponent component) {
    grid = new Grid(LayoutTokens.parse(getColumns()), LayoutTokens.parse(getRows()));
  }

  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////

  public void init() {
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

  public void fixRelativeInsideAuto(boolean orientation, boolean auto) {

    LayoutTokens tokens = grid.getTokens(orientation);

    if (auto) {
      for (int i = 0; i < tokens.getSize(); i++) {
        if (tokens.get(i) instanceof RelativeLayoutToken) {
          LOG.warn("Fixing layout token from * to auto, because a * in not allowed inside of a auto.");
          tokens.set(i, AutoLayoutToken.INSTANCE);
        }
      }
    }

    for (int i = 0; i < tokens.getSize(); i++) {
      for (int j = 0; j < grid.getTokens(!orientation).getSize(); j++) {
        Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          OriginCell origin = (OriginCell) cell;
          LayoutComponent component = cell.getComponent();
          if (component instanceof LayoutContainer) {
            LayoutManager layoutManager = ((LayoutContainer) component).getLayoutManager();
            // TODO: may be improved
            boolean childAuto = origin.getSpan(orientation) == 1 && tokens.get(i) instanceof AutoLayoutToken;
            layoutManager.fixRelativeInsideAuto(orientation, childAuto);
          }
        }
      }
    }
  }

  public void preProcessing(boolean orientation) {

    // process auto tokens
    int i = 0;
    for (LayoutToken token : grid.getTokens(orientation)) {

      if (token instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) token).getPixel();
        grid.getSizes(orientation)[i] = new PixelMeasure(pixel);// XXX refactor
      }

      IntervalList intervals = new IntervalList();
      for (int j = 0; j < grid.getTokens(!orientation).getSize(); j++) {
        Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          OriginCell origin = (OriginCell) cell;
          LayoutComponent component = cell.getComponent();

          if (component instanceof LayoutContainer) {
            ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
          }

          if (token instanceof AutoLayoutToken) {
            if (origin.getSpan(orientation) == 1) {
              intervals.add(new Interval(component, orientation));
            } else {
              LOG.info("ignored"); // todo
            }
          }
        }
      }
      if (intervals.size() >= 1) {
        Measure auto = intervals.computeAuto();
        grid.getSizes(orientation)[i] = (PixelMeasure) auto;
      }
// todo: what when we cannot find a good value for "auto"?
      i++;
    }

    // set the size if all sizes are set
    Measure size = PixelMeasure.ZERO;
    PixelMeasure[] sizes = grid.getSizes(orientation);
    for (int j = 0; j < sizes.length; j++) {
      if (sizes[j] == null) {
        size = null; // set to invalid
        break;
      }
      size = size.add(sizes[j]);
      if (j < sizes.length - 1) {
        size = size.add(getSpacing(orientation));
      }
    }
    if (size != null) {
      LayoutUtils.setSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(boolean orientation) {

    // find *
    {
      FactorList list = new FactorList();
      for (LayoutToken token : grid.getTokens(orientation)) {
        if (token instanceof RelativeLayoutToken) {
          list.add(((RelativeLayoutToken) token).getFactor());
        }
      }
      if (!list.isEmpty()) {
        // find rest
        LayoutContainer container = getLayoutContainer();
        Measure available = LayoutUtils.getSize(orientation, container);
        if (available != null) {
          for (PixelMeasure value : grid.getSizes(orientation)) {
            available = available.substractNotNegative(value);
          }
          PixelMeasure spacing = new PixelMeasure(
              getSpacing(orientation).getPixel() * (grid.getSizes(orientation).length - 1));
          available = available.substractNotNegative(spacing);

          available = available.substractNotNegative(LayoutUtils.getBeginOffset(orientation, container));
          available = available.substractNotNegative(LayoutUtils.getEndOffset(orientation, container));

          List<Measure> partition = list.partition(available);

          // write values back into the header
          int i = 0;
          int j = 0;
          for (LayoutToken token : grid.getTokens(orientation)) {
            if (token instanceof RelativeLayoutToken) {
              grid.getSizes(orientation)[i] = (PixelMeasure) partition.get(j);
              j++;
            }
            i++;
          }
        } else {
          LOG.warn("No width/height set but needed for *!");// todo: more information
        }
      }
    }

    // call manage sizes for all sub-layout-managers
    {
      for (int i = 0; i < grid.getTokens(orientation).getSize(); i++) {
        for (int j = 0; j < grid.getTokens(!orientation).getSize(); j++) {
          Cell cell = grid.getCell(i, j, orientation);
          if (cell instanceof OriginCell) {
            LayoutComponent component = cell.getComponent();

            component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

            Integer span = ((OriginCell) cell).getSpan(orientation);
            PixelMeasure[] pixelMeasures = grid.getSizes(orientation);
            
            // compute the size of the cell
            {
              Measure size = pixelMeasures[i];
              for (int k = 1; k < span; k++) {
                size = size.add(pixelMeasures[i + k]);
                size = size.add(getSpacing(orientation));
              }
              setSize(orientation, component, size);
            }

            // call sub layout manager
            if (component instanceof LayoutContainer) {
              ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
            }
          }
        }
      }
    }
  }

  public void postProcessing(boolean orientation) {

    // call manage sizes for all sub-layout-managers
    for (int i = 0; i < grid.getTokens(orientation).getSize(); i++) {
      for (int j = 0; j < grid.getTokens(!orientation).getSize(); j++) {
        Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          LayoutComponent component = cell.getComponent();

          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

          Integer span = ((OriginCell) cell).getSpan(orientation);
          PixelMeasure[] pixelMeasures = grid.getSizes(orientation);


          // compute the position of the cell
          {
            Measure position = LayoutUtils.getBeginOffset(orientation, getLayoutContainer());
            for (int k = 0; k < i; k++) {
              position = position.add(pixelMeasures[k]);
              position = position.add(getSpacing(orientation));
            }
            if (orientation) {
              component.setLeft(position);
            } else {
              component.setTop(position);
            }
          }

          // call sub layout manager
          if (component instanceof LayoutContainer) {
            ((LayoutContainer) component).getLayoutManager().postProcessing(orientation);
          }

          // todo: optimize: the AutoLayoutTokens with columnSpan=1 are already called
        }
      }
    }
  }

  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Phase 1: Collects the layout information from the components recursively.
   */
  public void collect(LayoutContext layoutContext, LayoutContainer container, int horizontalIndex, int verticalIndex) {

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
            LayoutManager layoutManager = subContainer.getLayoutManager();
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
  public void distribute(LayoutContext layoutContext, LayoutContainer container) {

    distributeSizes(layoutContext);
    distributePositions(layoutContext, container);
  }

  private void distributeSizes(LayoutContext layoutContext) {

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
            LayoutManager layoutManager = subContainer.getLayoutManager();
            if (layoutManager != null) {
              layoutManager.distribute(layoutContext, subContainer);
            }
          }
        }
      }
    }
  }

  private void distributePositions(LayoutContext layoutContext, LayoutContainer container) {

    // find the "left" positions
    for (int j = 0; j < grid.getRows().getSize(); j++) {
      PixelMeasure left = (PixelMeasure) container.getLeftOffset();
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
        left = (PixelMeasure) left.add(horizontal.getResult()[grid.getHorizontalIndices()[i]]);
        left = (PixelMeasure) left.add(getColumnSpacing());
      }
    }

    // find the "top" positions
    for (int i = 0; i < grid.getColumns().getSize(); i++) {
      PixelMeasure top = (PixelMeasure) container.getTopOffset();
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
        top = (PixelMeasure) top.add(vertical.getResult()[grid.getVerticalIndices()[j]]);
        top = (PixelMeasure) top.add(getRowSpacing());
      }
    }
  }

  private void addPixelConstraints(LayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
    // horizontal
    LayoutTokens columnTokens = grid.getColumns();
    for (int i = 0; i < columnTokens.getSize(); i++) {
      LayoutToken layoutToken = columnTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        Measure pixel = new PixelMeasure(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getHorizontal().setFixedLength(i + horizontalIndexOffset, pixel, this);
      }
    }
    // vertical
    LayoutTokens rowTokens = grid.getRows();
    for (int i = 0; i < rowTokens.getSize(); i++) {
      LayoutToken layoutToken = rowTokens.get(i);
      if (layoutToken instanceof PixelLayoutToken) {
        // XXX PixelLayoutToken might be removed/changed
        Measure pixel = new PixelMeasure(((PixelLayoutToken) layoutToken).getPixel());
        layoutContext.getVertical().setFixedLength(i + verticalIndexOffset, pixel, this);
      }
    }
  }

  private void addRelativeConstraints(LayoutContext layoutContext, int horizontalIndexOffset, int verticalIndexOffset) {
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
              firstIndex, i + horizontalIndexOffset, first, factor, getParent());
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
              firstIndex, i + verticalIndexOffset, first, factor, getParent());
        }
      }
    }
  }

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return ((LayoutContainer) getParent());
  }

  private void setSize(boolean orientation, LayoutComponent component, Measure size) {
    if (orientation) {
      if (size.greaterThan(component.getMaximumWidth())) {
        size = component.getMaximumWidth();
      }
      if (size.lessThan(component.getMinimumWidth())) {
        size = component.getMinimumWidth();
      }
      component.setWidth(size);
    } else {
      if (size.greaterThan(component.getMaximumHeight())) {
        size = component.getMaximumHeight();
      }
      if (size.lessThan(component.getMinimumHeight())) {
        size = component.getMinimumHeight();
      }
      component.setHeight(size);
    }
  }

  protected Grid getGrid() {
    return grid;
  }

  public Measure getSpacing(boolean horizontal) {
    return horizontal ? getColumnSpacing() : getRowSpacing();
  }

  public abstract String getRows();

  public abstract String getColumns();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName()
        + "(" + Arrays.toString(grid.getWidths()) + ", " + Arrays.toString(grid.getHeights()) + ")";
  }
}
