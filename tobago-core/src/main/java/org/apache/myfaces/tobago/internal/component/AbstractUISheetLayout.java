package org.apache.myfaces.tobago.internal.component;

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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.layout.Interval;
import org.apache.myfaces.tobago.internal.layout.IntervalList;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
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

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  private boolean horizontalAuto;
  private boolean verticalAuto;

  public void init() {
    for (LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }

  public void fixRelativeInsideAuto(Orientation orientation, boolean auto) {

    if (orientation == Orientation.HORIZONTAL) {
      horizontalAuto = auto;
    } else {
      verticalAuto = auto;
    }

    for (LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().fixRelativeInsideAuto(orientation, auto);
      }
    }
  }

  public void preProcessing(Orientation orientation) {

    // process auto tokens
    IntervalList intervals = new IntervalList();
    for (LayoutComponent component : getLayoutContainer().getComponents()) {

      if (component != null) {
        if (component instanceof LayoutContainer) {
          ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
        }

        if (orientation == Orientation.HORIZONTAL && horizontalAuto
            || orientation == Orientation.VERTICAL && verticalAuto) {
          intervals.add(new Interval(component, orientation));
        }
      }
    }

    if (intervals.size() >= 1) {
      intervals.evaluate();
      Measure size = intervals.getCurrent();
      size = size.add(LayoutUtils.getBorderBegin(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getBorderEnd(orientation, getLayoutContainer()));
      LayoutUtils.setCurrentSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(Orientation orientation) {

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
        for (LayoutComponent component : sheet.getComponents()) {
          AbstractUIColumn column = (AbstractUIColumn) ((UIComponent) component).getParent();
          Measure width = Measure.valueOf(widthList.get(index));
          width = width.subtractNotNegative(LayoutUtils.getBorderBegin(orientation, column));
          width = width.subtractNotNegative(LayoutUtils.getPaddingBegin(orientation, column));
          width = width.subtractNotNegative(LayoutUtils.getPaddingEnd(orientation, column));
          width = width.subtractNotNegative(LayoutUtils.getBorderEnd(orientation, column));
          LayoutUtils.setCurrentSize(orientation, component, width);
          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css
          // call sub layout manager
          if (component instanceof LayoutContainer) {
            ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
          }
          index++;
        }
      }
    }
  }

  public void postProcessing(Orientation orientation) {

    final AbstractUISheet sheet = (AbstractUISheet) getLayoutContainer();

    // set positions to all sub-layout-managers

    for (LayoutComponent component : sheet.getComponents()) {

      if (component != null) {
        // compute the position of the cell
        Measure position = LayoutUtils.getBorderBegin(orientation, sheet);
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

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return ((LayoutContainer) getParent());
  }

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  private void ensureColumnWidthList(FacesContext facesContext, AbstractUISheet data) {
    List<Integer> currentWidthList = null;
    List<UIColumn> renderedColumns = data.getRenderedColumns();

    final Map attributes = data.getAttributes();
    String widthListString = null;
    SheetState state = data.getSheetState(facesContext);
    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString = (String) attributes.get(Attributes.WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      currentWidthList = StringUtils.parseIntegerList(widthListString);
    }
    if (currentWidthList != null && currentWidthList.size() != renderedColumns.size()) {
      currentWidthList = null;
    }

    if (currentWidthList == null) {
      LayoutTokens tokens = data.getColumnLayout();
      List<UIColumn> allColumns = data.getAllColumns();
      LayoutTokens newTokens = new LayoutTokens();
      for (int i = 0; i < allColumns.size(); i++) {
        UIColumn column = allColumns.get(i);
        if (column.isRendered()) {
          if (tokens == null) {
            if (column instanceof AbstractUIColumn) {
              newTokens.addToken(LayoutTokens.parseToken(((AbstractUIColumn) column).getWidth()));
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

      Measure space = data.getCurrentWidth();
      final LayoutComponentRenderer renderer = data.getLayoutComponentRenderer(facesContext);
      space = space.subtractNotNegative(renderer.getBorderLeft(facesContext, data));
      space = space.subtractNotNegative(renderer.getBorderRight(facesContext, data));
      if (needVerticalScrollbar(facesContext, data)) {
        space = space.subtractNotNegative(renderer.getVerticalScrollbarWeight(facesContext, data));
      }
/*
      // todo: not nice: 1 left + 1 right border
      space = space.subtract(renderedColumns.size() * 2);
*/
      LayoutInfo layoutInfo =
          new LayoutInfo(newTokens.getSize(), space.getPixel(), newTokens, data.getClientId(facesContext), false);
      parseFixedWidth(layoutInfo, renderedColumns);
      layoutInfo.parseColumnLayout(space.getPixel());
      currentWidthList = layoutInfo.getSpaceList();
    }

    if (currentWidthList != null) {
      if (renderedColumns.size() != currentWidthList.size()) {
        LOG.warn("widthList.size() = " + currentWidthList.size()
            + " != columns.size() = " + renderedColumns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(currentWidthList));
      } else {
        data.setWidthList(currentWidthList);
      }
    }
  }

  private boolean needVerticalScrollbar(FacesContext facesContext, AbstractUISheet sheet) {
    // estimate need of height-scrollbar on client, if yes we have to consider
    // this when calculating column width's

    final Object forceScrollbar = sheet.getAttributes().get(Attributes.FORCE_VERTICAL_SCROLLBAR);
    if (forceScrollbar != null) {
      if ("true".equals(forceScrollbar)) {
        return true;
      } else if ("false".equals(forceScrollbar)) {
        return false;
      } else if (!"auto".equals(forceScrollbar)) {
        LOG.warn("Illegal value for attribute 'forceVerticalScrollbar': '" + forceScrollbar + "'");
      }
    }

    if (!sheet.hasRowCount()) {
      return true;
    }

    if (sheet.getCurrentHeight() != null) {
      int first = sheet.getFirst();
      int rows = sheet.hasRows()
          ? Math.min(sheet.getRowCount(), first + sheet.getRows()) - first
          : sheet.getRowCount();
      final LayoutComponentRenderer renderer = sheet.getLayoutComponentRenderer(facesContext);
      final Measure rowPadding = renderer.getCustomMeasure(facesContext, sheet, "rowPadding");
      LOG.error("20; // FIXME: make dynamic (was removed by changing the layout");
      Measure heightNeeded = getFooterHeight(facesContext, sheet)
              .add(rowPadding.add(20/*fixme*/).multiply(rows))
              .add(20); // FIXME: make dynamic (was removed by changing the layouting
      return heightNeeded.greaterThan(sheet.getCurrentHeight());
    } else {
      return false;
    }
  }

  private void parseFixedWidth(LayoutInfo layoutInfo, List<UIColumn> rendereredColumns) {
    LayoutTokens tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.getSize(); i++) {
      LayoutToken token = tokens.get(i);
      if (token instanceof AutoLayoutToken) {
        int width = 0;
        if (!rendereredColumns.isEmpty()) {
          if (i < rendereredColumns.size()) {
            UIColumn column = rendereredColumns.get(i);
            if (column instanceof AbstractUIColumnSelector) {
              width = 20; // FIXME: make dynamic (was removed by changing the layout
              LOG.error("20; // FIXME: make dynamic (was removed by changing the layout");

            } else {
              for (UIComponent component : (List<UIComponent>) column.getChildren()) {
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
          LOG.debug("set column " + i + " from fixed to with " + width);
        }
      }
    }
  }

  private Measure getFooterHeight(FacesContext facesContext, AbstractUISheet sheet) {
    return sheet.isPagingVisible()
        ? sheet.getLayoutComponentRenderer(facesContext).getCustomMeasure(facesContext, sheet, "footerHeight")
        : Measure.ZERO;
  }
}
