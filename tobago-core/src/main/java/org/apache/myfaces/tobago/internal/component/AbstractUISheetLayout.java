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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.IntervalList;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutBox;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRenderer;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

/**
 * XXX: Not completely implemented yet.
 */
public abstract class AbstractUISheetLayout extends AbstractUILayoutBase implements LayoutManager {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISheetLayout.class);

  private boolean horizontalAuto;
  private boolean verticalAuto;

  public void init() {

    layoutHeader();

    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }

  public void fixRelativeInsideAuto(final Orientation orientation, final boolean auto) {

    if (orientation == Orientation.HORIZONTAL) {
      horizontalAuto = auto;
    } else {
      verticalAuto = auto;
    }

    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().fixRelativeInsideAuto(orientation, auto);
      }
    }
  }

  public void preProcessing(final Orientation orientation) {

    // process auto tokens
    final IntervalList intervals = new IntervalList();
    for (final LayoutComponent component : getLayoutContainer().getComponents()) {

      if (component != null) {
        if (component instanceof LayoutContainer && component.isRendered()) {
          ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
        }

/*
        if (orientation == Orientation.HORIZONTAL && horizontalAuto
            || orientation == Orientation.VERTICAL && verticalAuto) {
          intervals.add(new Interval(component, orientation));
        }
*/
      }
    }

/*
    if (intervals.size() >= 1) {
      intervals.evaluate();
      Measure size = intervals.getCurrent();
      size = size.add(LayoutUtils.getBorderBegin(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getBorderEnd(orientation, getLayoutContainer()));
      LayoutUtils.setCurrentSize(orientation, getLayoutContainer(), size);
    }
*/
  }

  public void mainProcessing(final Orientation orientation) {

    // find *
    if (orientation == Orientation.HORIZONTAL && !horizontalAuto
        || orientation == Orientation.VERTICAL && !verticalAuto) {

      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final LayoutContainer container = getLayoutContainer();
      final AbstractUISheet sheet = (AbstractUISheet) container;

      if (orientation == Orientation.HORIZONTAL) {

        ensureColumnWidthList(facesContext, sheet);

        final List<Integer> widthList = sheet.getWidthList();

        int index = 0;
        List<LayoutComponent> components = sheet.getComponents();
        for (int i = 0; i < components.size(); i++) {
          LayoutComponent component = components.get(i);
          if (component == null) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("todo: UIColumnSelector must be a LayoutComponent!"); // fixme
            }
            AbstractUIColumn column = sheet.getAllColumns().get(index);
            if (column instanceof UIColumnSelector && column.isRendered()) {
              // XXX here only index++ if columnSelector is rendered,
              //    because the widthList has only the rendered=true, todo: change it.
              index++;
            }
            continue;
          }
          final UIColumn column = component instanceof AbstractUIColumnNode
              ? (UIColumn) component
              : (UIColumn) ((UIComponent) component).getParent();
          if (!column.isRendered()) {
            // XXX here not index++, because the widthList has only the rendered=true, todo: change it.
            continue;
          }
          if (column instanceof LayoutBox) {
            final LayoutBox box = (LayoutBox) column;
            Measure width = Measure.valueOf(widthList.get(index));
            width = width.subtractNotNegative(LayoutUtils.getBorderBegin(orientation, box));
            width = width.subtractNotNegative(LayoutUtils.getPaddingBegin(orientation, box));
            width = width.subtractNotNegative(LayoutUtils.getPaddingEnd(orientation, box));
            width = width.subtractNotNegative(LayoutUtils.getBorderEnd(orientation, box));
            final LayoutComponentRenderer renderer = sheet.getLayoutComponentRenderer(facesContext);
            width = width.subtractNotNegative(renderer.getCustomMeasure(facesContext, sheet, "columnSeparator"));
            LayoutUtils.setCurrentSize(orientation, component, width);
            // call sub layout manager
            if (component instanceof LayoutContainer) {
              ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
            }
          }
          index++;
        }
      }
    }
  }

  public void postProcessing(final Orientation orientation) {

    final AbstractUISheet sheet = (AbstractUISheet) getLayoutContainer();

    // set positions to all sub-layout-managers

    for (final LayoutComponent component : sheet.getComponents()) {

      if (component != null) {
        // compute the position of the cell
        final Measure position = LayoutUtils.getBorderBegin(orientation, sheet);
        if (orientation == Orientation.HORIZONTAL) {
          component.setLeft(position);
        } else {
          component.setTop(position);
        }

        // call sub layout manager
        if (component instanceof LayoutContainer && component.isRendered()) {
          ((LayoutContainer) component).getLayoutManager().postProcessing(orientation);
        }

        // todo: optimize: the AutoLayoutTokens with columnSpan=1 are already called
      }
    }
  }

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return (LayoutContainer) getParent();
  }

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  private void ensureColumnWidthList(final FacesContext facesContext, final AbstractUISheet data) {
    List<Integer> currentWidthList = null;
    // TODO: Refactor: here be should use "getColumns()" instead of "getRenderedColumns()"
    final List<AbstractUIColumn> renderedColumns = data.getRenderedColumns();

    final Map attributes = data.getAttributes();
    String widthListString = null;
    final SheetState state = data.getSheetState(facesContext);
    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString = (String) attributes.get(Attributes.WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      try {
        currentWidthList = StringUtils.parseIntegerList(widthListString);
      } catch (final NumberFormatException e) {
        LOG.warn("Unexpected value for column width list: '" + widthListString + "'");
      }
    }
    if (currentWidthList != null && currentWidthList.size() != renderedColumns.size() + 1) {
      currentWidthList = null;
    }

    Measure space = data.getCurrentWidth();
    final LayoutComponentRenderer renderer = data.getLayoutComponentRenderer(facesContext);
    space = space.subtractNotNegative(renderer.getBorderLeft(facesContext, data));
    space = space.subtractNotNegative(renderer.getBorderRight(facesContext, data));
    Measure verticalScrollbarWeight = renderer.getVerticalScrollbarWeight(facesContext, data);
    boolean needVerticalScrollbar = needVerticalScrollbar(facesContext, data);
    if (needVerticalScrollbar) {
      space = space.subtractNotNegative(verticalScrollbarWeight);
    }

    if (currentWidthList == null) {
      final LayoutTokens tokens = data.getColumnLayout();
      final List<AbstractUIColumn> allColumns = data.getAllColumns();
      final LayoutTokens newTokens = new LayoutTokens();
      for (int i = 0; i < allColumns.size(); i++) {
        final AbstractUIColumn column = allColumns.get(i);
        if (column.isRendered()) {
          if (tokens == null) {
            if (column instanceof AbstractUIColumn && column.getWidth() != null) {
              newTokens.addToken(LayoutTokens.parseToken(column.getWidth().serialize()));
            } else {
              newTokens.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
            }
          } else {
            if (i < tokens.getSize()) {
              newTokens.addToken(tokens.get(i));
            } else {
              newTokens.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
            }
          }
        }
      }

/*
      // todo: not nice: 1 left + 1 right border
      space = space.subtract(renderedColumns.size() * 2);
*/
      final LayoutInfo layoutInfo =
          new LayoutInfo(newTokens.getSize(), space.getPixel(), newTokens, data.getClientId(facesContext), false);
      final Measure columnSelectorWidth
          = data.getLayoutComponentRenderer(facesContext).getCustomMeasure(facesContext, data, "columnSelectorWidth");
      parseFixedWidth(layoutInfo, renderedColumns, columnSelectorWidth);
      layoutInfo.parseColumnLayout(space.getPixel());
      currentWidthList = layoutInfo.getSpaceList();

      int freeWidth = space.getPixel();
      for (Integer width : currentWidthList) {
        freeWidth -= width;
      }

      if (needVerticalScrollbar) {
        if (freeWidth > 0) {
          currentWidthList.add(freeWidth + verticalScrollbarWeight.getPixel()); // filler column
        } else {
          currentWidthList.add(verticalScrollbarWeight.getPixel()); // filler column
        }
      } else {
        currentWidthList.add(Math.max(freeWidth, 0)); // empty filler column
      }
    }

    if (renderedColumns.size() + 1 != currentWidthList.size()) {
      LOG.warn("widthList.size() = " + currentWidthList.size()
          + " != columns.size() = " + renderedColumns.size() + " + 1. The widthList: "
          + LayoutInfo.listToTokenString(currentWidthList));
    } else {
      data.setWidthList(currentWidthList);
    }
  }

  public boolean needVerticalScrollbar(final FacesContext facesContext, final AbstractUISheet sheet) {
    // estimate need of height-scrollbar on client, if yes we have to consider
    // this when calculating column width's

    if (sheet.getNeedVerticalScrollbar() != null) {
      return sheet.getNeedVerticalScrollbar();
    }

    Boolean result = null;

    final Object forceScrollbar = sheet.getAttributes().get(Attributes.FORCE_VERTICAL_SCROLLBAR);
    if (forceScrollbar != null) {
      if ("true".equals(forceScrollbar)) {
        result = true;
      } else if ("false".equals(forceScrollbar)) {
        result = false;
      } else if (!"auto".equals(forceScrollbar)) {
        LOG.warn("Illegal value for attribute 'forceVerticalScrollbar': '" + forceScrollbar + "'");
      }
    }

    if (result == null && !sheet.hasRowCount()) {
      result = true;
    }

    if (result == null) {
      if (sheet.getCurrentHeight() != null) {
        final int first = sheet.getFirst();
        final int rows = sheet.isRowsUnlimited()
            ? sheet.getRowCount()
            : Math.min(sheet.getRowCount(), first + sheet.getRows()) - first;
        Measure heightNeeded = getRowHeight(facesContext, sheet).multiply(rows);
        if (sheet.isShowHeader()) {
          heightNeeded = heightNeeded.add(getHeaderHeight(facesContext, sheet));
        }
        if (sheet.isPagingVisible()) {
          heightNeeded = heightNeeded.add(getFooterHeight(facesContext, sheet));
        }
        result = heightNeeded.greaterThan(sheet.getCurrentHeight());
      } else {
        result = false;
      }
    }
    sheet.setNeedVerticalScrollbar(result);
    return result;
  }

  private void parseFixedWidth(
      final LayoutInfo layoutInfo, final List<AbstractUIColumn> renderedColumns, final Measure columnSelectorWidth) {
    final LayoutTokens tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.getSize(); i++) {
      final LayoutToken token = tokens.get(i);
      if (token instanceof AutoLayoutToken) {
        int width = 0;
        if (!renderedColumns.isEmpty()) {
          if (i < renderedColumns.size()) {
            final AbstractUIColumn column = renderedColumns.get(i);
            if (column instanceof AbstractUIColumnSelector) {
              width = columnSelectorWidth.getPixel();
            } else {
              for (final UIComponent component : column.getChildren()) {
                width += 100; // FIXME: make dynamic (was removed by changing the layout
                LOG.error("100; // FIXME: make dynamic (was removed by changing the layout");
              }
            }
            layoutInfo.update(width, i);
          } else {
            layoutInfo.update(0, i);
            if (LOG.isWarnEnabled()) {
              LOG.warn("More LayoutTokens found than rows! skipping!");
            }
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from 'auto' to width " + width);
        }
      }
    }
  }

  private Measure getHeaderHeight(final FacesContext facesContext, final AbstractUISheet sheet) {
    return sheet.isShowHeader()
        ? sheet.getLayoutComponentRenderer(facesContext).getCustomMeasure(facesContext, sheet, "headerHeight")
        : Measure.ZERO;
  }

  private Measure getRowHeight(final FacesContext facesContext, final AbstractUISheet sheet) {
    return sheet.getLayoutComponentRenderer(facesContext).getCustomMeasure(facesContext, sheet, "rowHeight");
  }

  private Measure getFooterHeight(final FacesContext facesContext, final AbstractUISheet sheet) {
    return sheet.isPagingVisible()
        ? sheet.getLayoutComponentRenderer(facesContext).getCustomMeasure(facesContext, sheet, "footerHeight")
        : Measure.ZERO;
  }

  private void layoutHeader() {
    final AbstractUISheet sheet = (AbstractUISheet) getLayoutContainer();
    final UIComponent header = sheet.getHeader();
    if (header == null) {
      LOG.warn("This should not happen. Please file a bug in the issue tracker to reproduce this case.");
      return;
    }
    final LayoutTokens columns = new LayoutTokens();
    final List<AbstractUIColumn> renderedColumns = sheet.getRenderedColumns();
    for (final AbstractUIColumn ignored : renderedColumns) {
      columns.addToken(RelativeLayoutToken.DEFAULT_INSTANCE);
    }
    final LayoutTokens rows = new LayoutTokens();
    rows.addToken(AutoLayoutToken.INSTANCE);
    final Grid grid = new Grid(columns, rows);

    for (final UIComponent child : header.getChildren()) {
      if (child instanceof LayoutComponent) {
        if (child.isRendered()) {
          final LayoutComponent c = (LayoutComponent) child;
          grid.add(new OriginCell(c), c.getColumnSpan(), c.getRowSpan());
        }
      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Found unknown component in header.");
        }
      }
    }
    sheet.setHeaderGrid(grid);
  }
}
