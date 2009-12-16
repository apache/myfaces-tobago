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
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.FactorList;
import org.apache.myfaces.tobago.layout.Interval;
import org.apache.myfaces.tobago.layout.IntervalList;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.layout.grid.Cell;
import org.apache.myfaces.tobago.layout.grid.Grid;
import org.apache.myfaces.tobago.layout.grid.OriginCell;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractUIGridLayout extends UILayout implements LayoutManager, SupportsMarkup {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);

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

  public void fixRelativeInsideAuto(Orientation orientation, boolean auto) {

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
      for (int j = 0; j < grid.getTokens(orientation.other()).getSize(); j++) {
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

  public void preProcessing(Orientation orientation) {

    // process auto tokens
    int i = 0;
    for (LayoutToken token : grid.getTokens(orientation)) {

      if (token instanceof PixelLayoutToken) {
        int pixel = ((PixelLayoutToken) token).getPixel();
        grid.getSizes(orientation)[i] = new PixelMeasure(pixel); // XXX refactor
      }

      IntervalList intervals = new IntervalList();
      for (int j = 0; j < grid.getTokens(orientation.other()).getSize(); j++) {
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
    Measure size = Measure.ZERO;
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
      size = size.add(LayoutUtils.getBeginOffset(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getEndOffset(orientation, getLayoutContainer()));
      LayoutUtils.setSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(Orientation orientation) {

    // find *
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
          available = available.subtractNotNegative(value);
        }
        PixelMeasure spacing = new PixelMeasure(
            getSpacing(orientation).getPixel() * (grid.getSizes(orientation).length - 1));
        available = available.subtractNotNegative(spacing);

        available = available.subtractNotNegative(LayoutUtils.getBeginOffset(orientation, container));
        available = available.subtractNotNegative(LayoutUtils.getEndOffset(orientation, container));

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
        LOG.warn("No width/height set but needed for *!"); // todo: more information
      }
    }

    // call manage sizes for all sub-layout-managers
    for (int i = 0; i < grid.getTokens(orientation).getSize(); i++) {
      for (int j = 0; j < grid.getTokens(orientation.other()).getSize(); j++) {
        Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          LayoutComponent component = cell.getComponent();

          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

          Integer span = ((OriginCell) cell).getSpan(orientation);
          PixelMeasure[] pixelMeasures = grid.getSizes(orientation);

          // compute the size of the cell
          Measure size = pixelMeasures[i];
          if (size != null) {
            for (int k = 1; k < span; k++) {
              size = size.add(pixelMeasures[i + k]);
              size = size.add(getSpacing(orientation));
            }
            LayoutUtils.setSize(orientation, component, size);
          } else {
            LOG.warn("Size is null, should be debugged... i=" + i + " grid=" + grid, new RuntimeException());
          }

          // call sub layout manager
          if (component instanceof LayoutContainer) {
            ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
          }
        }
      }
    }
  }

  public void postProcessing(Orientation orientation) {

    // call manage sizes for all sub-layout-managers
    for (int i = 0; i < grid.getTokens(orientation).getSize(); i++) {
      for (int j = 0; j < grid.getTokens(orientation.other()).getSize(); j++) {
        Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          LayoutComponent component = cell.getComponent();

          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

          PixelMeasure[] pixelMeasures = grid.getSizes(orientation);

          // compute the position of the cell
          Measure position = LayoutUtils.getBeginOffset(orientation, getLayoutContainer());
          for (int k = 0; k < i; k++) {
            if (pixelMeasures[k] == null) {
              LOG.warn("Measure is null, should be debugged... i=" + i + " k=" + k + " grid=" + grid, 
                  new RuntimeException());
            } else {
              position = position.add(pixelMeasures[k]);
            }
            position = position.add(getSpacing(orientation));
          }
          if (orientation == Orientation.HORIZONTAL) {
            component.setLeft(position);
          } else {
            component.setTop(position);
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

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return ((LayoutContainer) getParent());
  }

  protected Grid getGrid() {
    return grid;
  }

  public Measure getSpacing(Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getColumnSpacing() : getRowSpacing();
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
