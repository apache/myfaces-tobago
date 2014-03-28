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

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.internal.layout.BankHead;
import org.apache.myfaces.tobago.internal.layout.Cell;
import org.apache.myfaces.tobago.internal.layout.FactorList;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.Interval;
import org.apache.myfaces.tobago.internal.layout.IntervalList;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.List;

public abstract class AbstractUIGridLayout extends AbstractUILayoutBase implements LayoutManager, SupportsMarkup {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.GridLayout";

  private Grid grid;

  /**
   * Initialize the grid and remove the current width and height values from the component, recursively.
   */
  public void init() {

    if (!getLayoutContainer().isLayoutChildren()) {
      return;
    }

    grid = new Grid(LayoutTokens.parse(getColumns()), LayoutTokens.parse(getRows()));

    final List<LayoutComponent> components = getLayoutContainer().getComponents();
    for (final LayoutComponent component : components) {
      component.setCurrentHeight(null);
      component.setCurrentWidth(null);
      grid.add(new OriginCell(component), component.getColumnSpan(), component.getRowSpan());
      if (LOG.isDebugEnabled()) {
        LOG.debug("\n" + grid);
      }
      if (component instanceof LayoutContainer && (component.isRendered() || isRigid())) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }

    grid.setColumnOverflow(isColumnOverflow());
    grid.setRowOverflow(isRowOverflow());
  }

  public void fixRelativeInsideAuto(final Orientation orientation, final boolean auto) {

    if (!getLayoutContainer().isLayoutChildren()) {
      return;
    }

    final BankHead[] heads = grid.getBankHeads(orientation);
    final BankHead[] heads2 = grid.getBankHeads(orientation.other());

    if (auto) {
      for (int i = 0; i < heads.length; i++) {
        if (heads[i].getToken() instanceof RelativeLayoutToken) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Fixing layout token from * to auto, because a * in not allowed inside of a auto. "
                + "Token * at index=" + i + ", orientation=" + orientation + ", grid=\n" + grid);
          }
          heads[i].setToken(AutoLayoutToken.INSTANCE);
        }
      }
    }

    for (int i = 0; i < heads.length; i++) {
      boolean neitherRendered = true;
      for (int j = 0; j < heads2.length; j++) {
        final Cell cell = grid.getCell(i, j, orientation);
        // check rendered = false
        if (cell != null && cell.getComponent().isRendered()) {
          neitherRendered = false;
        }
        // recursion
        if (cell instanceof OriginCell) {
          final OriginCell origin = (OriginCell) cell;
          final LayoutComponent component = cell.getComponent();
          if (component instanceof LayoutContainer && component.isRendered()) {
            final LayoutManager layoutManager = ((LayoutContainer) component).getLayoutManager();
            // TODO: may be improved
            final boolean childAuto
                = origin.getSpan(orientation) == 1 && heads[i].getToken() instanceof AutoLayoutToken;
            layoutManager.fixRelativeInsideAuto(orientation, childAuto);
          }
        }
      }
      if (neitherRendered && !isRigid()) {
        heads[i].setRendered(false);
      }
    }
  }

  public void preProcessing(final Orientation orientation) {

    if (!getLayoutContainer().isLayoutChildren()) {
      return;
    }

    final BankHead[] heads = grid.getBankHeads(orientation);
    final BankHead[] heads2 = grid.getBankHeads(orientation.other());

    // process auto tokens
    int i = 0;

    for (final BankHead head : heads) {
      final LayoutToken token = head.getToken();

      if (token instanceof PixelLayoutToken) {
        if (head.isRendered() || isRigid()) {
          heads[i].setCurrent(((PixelLayoutToken) token).getMeasure());
        } else {
          heads[i].setCurrent(Measure.ZERO);
        }
      }

      final IntervalList intervalList = new IntervalList();
      for (int j = 0; j < heads2.length; j++) {
        final Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          final OriginCell origin = (OriginCell) cell;
          final LayoutComponent component = cell.getComponent();

          if (component instanceof LayoutContainer && (component.isRendered() || isRigid())) {
            ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
          }

          if (token instanceof AutoLayoutToken || token instanceof RelativeLayoutToken) {
            if ((component.isRendered() || isRigid())) {
              if (origin.getSpan(orientation) == 1) {
                intervalList.add(new Interval(component, orientation));
              } else {
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Components with span > 1 will be ignored in 'auto' layout rows/columns.");
                }
              }
            }
          }
        }
      }

      intervalList.evaluate();
      if (token instanceof AutoLayoutToken || token instanceof RelativeLayoutToken) {
        heads[i].setIntervalList(intervalList);
      }
      if (token instanceof AutoLayoutToken) {
        if (heads[i].isRendered()) {
          if (intervalList.size() > 0) {
            heads[i].setCurrent(intervalList.getCurrent());
          } else {
            heads[i].setCurrent(Measure.valueOf(100));
            LOG.warn("Found an 'auto' token in {} definition, but there is no component inside with span = 1! "
                + "So the value for 'auto' can't be evaluated (clientId={}). Using 100px.",
                orientation == Orientation.HORIZONTAL ? "columns" : "rows",
                getClientId(getFacesContext()));
          }
        } else {
          heads[i].setCurrent(Measure.ZERO);
        }
      }
      i++;
    }

/*
    IntervalList relatives = new IntervalList();
    for (BankHead head : heads) {
      LayoutToken token = head.getToken();
      if (token instanceof RelativeLayoutToken) {
        final int factor = ((RelativeLayoutToken) token).getFactor();
        for (Interval interval : head.getIntervalList()) {
          relatives.add(new Interval(interval, factor));
        }
      }
    }
    relatives.evaluate();
*/

    // set the size if all sizes of the grid are set
    Measure sum = Measure.ZERO;
    for (final BankHead head : heads) {
      Measure size = null;
      final LayoutToken token = head.getToken();
      if (token instanceof RelativeLayoutToken) {
//        final int factor = ((RelativeLayoutToken) token).getFactor();
//        size = relatives.getCurrent().multiply(factor);
      } else {
        size = head.getCurrent();
      }
      if (size == null) {
        sum = null; // set to invalid
        break;
//        LOG.error("TODO: Should not happen!");
      }
      sum = sum.add(size);
    }
    if (sum != null) {
      // adding the space between the cells
      sum = sum.add(LayoutUtils.getBorderBegin(orientation, getLayoutContainer()));
      sum = sum.add(LayoutUtils.getPaddingBegin(orientation, getLayoutContainer()));
      sum = sum.add(getMarginBegin(orientation));
      sum = sum.add(computeSpacing(orientation, 0, heads.length));
      sum = sum.add(getMarginEnd(orientation));
      sum = sum.add(LayoutUtils.getPaddingEnd(orientation, getLayoutContainer()));
      sum = sum.add(LayoutUtils.getBorderEnd(orientation, getLayoutContainer()));
      LayoutUtils.setCurrentSize(orientation, getLayoutContainer(), sum);
    }
  }

  public void mainProcessing(final Orientation orientation) {

    if (!getLayoutContainer().isLayoutChildren()) {
      return;
    }

    final BankHead[] heads = grid.getBankHeads(orientation);
    final BankHead[] heads2 = grid.getBankHeads(orientation.other());

    // find *
    final FactorList factorList = new FactorList();
    for (final BankHead head : heads) {
      if (head.getToken() instanceof RelativeLayoutToken && head.isRendered()) {
        factorList.add(((RelativeLayoutToken) head.getToken()).getFactor());
      }
    }
    if (!factorList.isEmpty()) {
      // find rest
      final LayoutContainer container = getLayoutContainer();
      Measure available = LayoutUtils.getCurrentSize(orientation, container);
      if (available != null) {
        for (final BankHead head : heads) {
          available = available.subtractNotNegative(head.getCurrent());
        }
        available = available.subtractNotNegative(LayoutUtils.getBorderBegin(orientation, container));
        available = available.subtractNotNegative(LayoutUtils.getPaddingBegin(orientation, container));
        available = available.subtractNotNegative(getMarginBegin(orientation));
        available = available.subtractNotNegative(computeSpacing(orientation, 0, heads.length));
        available = available.subtractNotNegative(getMarginEnd(orientation));
        available = available.subtractNotNegative(LayoutUtils.getPaddingEnd(orientation, container));
        available = available.subtractNotNegative(LayoutUtils.getBorderEnd(orientation, container));

        if (grid.isOverflow(orientation.other())) {
          final ClientProperties client
              = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance());
          final Measure scrollbar = orientation
              == Orientation.HORIZONTAL ? client.getVerticalScrollbarWeight() : client.getHorizontalScrollbarWeight();
          available = available.subtractNotNegative(scrollbar);
        }

        final List<Measure> partition = factorList.partition(available);

        // write values back into the header
        int i = 0; // index of head
        int j = 0; // index of partition
        for (final BankHead head : heads) {
          if (head.getToken() instanceof RelativeLayoutToken && head.isRendered()) {
            // respect the minimum
            heads[i].setCurrent(Measure.max(partition.get(j), heads[i].getIntervalList().getMinimum()));
            j++;
          }
          i++;
        }
      } else {
        LOG.warn("No width/height set but needed for *!"); // todo: more information
      }
    }

    // call manage sizes for all sub-layout-managers
    for (int i = 0; i < heads.length; i++) {
      for (int j = 0; j < heads2.length; j++) {
        final Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          final LayoutComponent component = cell.getComponent();

          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and tobago.css

          final Integer span = ((OriginCell) cell).getSpan(orientation);

          // compute the size of the cell
          Measure size = Measure.ZERO;
          for (int k = 0; k < span; k++) {
            size = size.add(heads[i + k].getCurrent());
          }
          size = size.add(computeSpacing(orientation, i, span));
          final Measure current = LayoutUtils.getCurrentSize(orientation, component);
          if (current == null) {
            LayoutUtils.setCurrentSize(orientation, component, size);
          }

          // call sub layout manager
          if (component instanceof LayoutContainer && (component.isRendered() || isRigid())) {
            ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
          }
        }
      }
    }

    Measure size = Measure.ZERO;
    size = size.add(LayoutUtils.getPaddingBegin(orientation, getLayoutContainer()));
    size = size.add(getMarginBegin(orientation));
    size = size.add(computeSpacing(orientation, 0, heads.length));
    for (final BankHead head : heads) {
      size = size.add(head.getCurrent());
    }
    size = size.add(getMarginEnd(orientation));
    size = size.add(LayoutUtils.getPaddingEnd(orientation, getLayoutContainer()));
    if (size.greaterThan(LayoutUtils.getCurrentSize(orientation, getLayoutContainer()))) {
      grid.setOverflow(true, orientation);
    }
  }

  public void postProcessing(final Orientation orientation) {

    if (!getLayoutContainer().isLayoutChildren()) {
      return;
    }

    final BankHead[] heads = grid.getBankHeads(orientation);
    final BankHead[] heads2 = grid.getBankHeads(orientation.other());

    // call manage sizes for all sub-layout-managers
    for (int i = 0; i < heads.length; i++) {
      for (int j = 0; j < heads2.length; j++) {
        final Cell cell = grid.getCell(i, j, orientation);
        if (cell instanceof OriginCell) {
          final LayoutComponent component = cell.getComponent();

          component.setDisplay(Display.BLOCK);

          // compute the position of the cell
          Measure position = Measure.ZERO;
          position = position.add(LayoutUtils.getPaddingBegin(orientation, getLayoutContainer()));
          position = position.add(getMarginBegin(orientation));
          for (int k = 0; k < i; k++) {
            if (heads[k] != null
                && heads[k].getCurrent() != null
                && heads[k].isRendered()
                && heads[k].getCurrent().greaterThan(Measure.ZERO)) {
              position = position.add(heads[k].getCurrent());
              position = position.add(getSpacing(orientation));
            }
          }
          if (orientation == Orientation.HORIZONTAL) {
            component.setLeft(position);
          } else {
            component.setTop(position);
          }

          // call sub layout manager
          if (component instanceof LayoutContainer && (component.isRendered() || isRigid())) {
            ((LayoutContainer) component).getLayoutManager().postProcessing(orientation);
          }

          // set scrolling type
          final boolean scroll = grid.isOverflow(orientation);
          if (orientation == Orientation.HORIZONTAL) {
            getLayoutContainer().setOverflowX(scroll);
          } else {
            getLayoutContainer().setOverflowY(scroll);
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

  public Measure getSpacing(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getColumnSpacing() : getRowSpacing();
  }

  public Measure getMarginBegin(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getMarginLeft() : getMarginTop();
  }

  public Measure getMarginEnd(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getMarginRight() : getMarginBottom();
  }

  /**
   * Compute the sum of the space between the cells.
   * There is one "space" less than cells that are not void.
   */
  private Measure computeSpacing(final Orientation orientation, final int startIndex, final int length) {

    final BankHead[] heads = grid.getBankHeads(orientation);

    int count = 0;
    for (int i = startIndex; i < startIndex + length; i++) {
      if ((heads[i].isRendered())
          && (heads[i].getCurrent() == null || heads[i].getCurrent().greaterThan(Measure.ZERO))) {
        count++;
      }
    }
    if (count > 0) {
      return getSpacing(orientation).multiply(count - 1);
    } else {
      return Measure.ZERO;
    }
  }

  public abstract String getRows();

  public abstract void setRows(String rows);

  public abstract String getColumns();

  public abstract void setColumns(String columns);

  @Deprecated
  public abstract Measure getCellspacing();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  public abstract Measure getMarginLeft();

  public abstract Measure getMarginTop();

  public abstract Measure getMarginRight();

  public abstract Measure getMarginBottom();

  public abstract boolean isColumnOverflow();

  public abstract boolean isRowOverflow();

  public abstract boolean isRigid();

  public Grid getGrid() {
    return grid;
  }

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  public String toString(final int depth) {
    final StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName()).append("#");
    builder.append(getClientId(FacesContext.getCurrentInstance()));
    builder.append("\n");
    if (grid != null) {
      builder.append(StringUtils.repeat("  ", depth + 4));
      builder.append("horiz.: ");
      BankHead[] heads = grid.getBankHeads(Orientation.HORIZONTAL);
      for (int i = 0; i < heads.length; i++) {
        if (i != 0) {
          builder.append(StringUtils.repeat("  ", depth + 4 + 4));
        }
        builder.append(heads[i]);
        builder.append("\n");
      }
      builder.append(StringUtils.repeat("  ", depth + 4));
      builder.append("verti.: ");
      heads = grid.getBankHeads(Orientation.VERTICAL);
      for (int i = 0; i < heads.length; i++) {
        if (i != 0) {
          builder.append(StringUtils.repeat("  ", depth + 4 + 4));
        }
        builder.append(heads[i]);
        builder.append("\n");
      }
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
  }

  @Override
  public String toString() {
    return toString(0);
  }
}
