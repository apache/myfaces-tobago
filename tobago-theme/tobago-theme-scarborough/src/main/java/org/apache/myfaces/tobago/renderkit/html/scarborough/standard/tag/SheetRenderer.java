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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIColumnEvent;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SheetRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SheetRenderer.class);

  public static final String WIDTHS_POSTFIX = ComponentUtils.SUB_SEPARATOR + "widths";
  public static final String SCROLL_POSTFIX = ComponentUtils.SUB_SEPARATOR + "scrollPosition";
  public static final String SELECTED_POSTFIX = ComponentUtils.SUB_SEPARATOR + "selected";

  private static final Integer HEIGHT_0 = 0;


  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    UISheet sheet = (UISheet) uiComponent;

    Style style = new Style(facesContext, sheet);

    final String sheetId = sheet.getClientId(facesContext);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    // Outer sheet div
    writer.startElement(HtmlElements.DIV, sheet);
    writer.writeIdAttribute(sheetId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, sheet);
    final String css = Classes.create(sheet).getStringValue() + " " + Classes.create(sheet, "outer").getStringValue();
    // todo: please remove the "outer" sub in the future,
    // todo: it was renamed from tobago-sheet-outer to tobago-sheet because of naming conventions.
    writer.writeClassAttribute(css);
    writer.writeStyleAttribute(style);
    UICommand clickAction = null;
    UICommand dblClickAction = null;
    int columnSelectorIndex = -1;
    int i = 0;
    for (UIComponent child : (List<UIComponent>) sheet.getChildren()) {
      if (child instanceof UIColumnEvent) {
        UIColumnEvent columnEvent = (UIColumnEvent) child;
        if (columnEvent.isRendered()) {
          UIComponent selectionChild = (UIComponent) child.getChildren().get(0);
          if (selectionChild != null && selectionChild instanceof UICommand && selectionChild.isRendered()) {
            UICommand action = (UICommand) selectionChild;
            if ("click".equals(columnEvent.getEvent())) {
              clickAction = action;
            }
            if ("dblclick".equals(columnEvent.getEvent())) {
              dblClickAction = action;
            }
          }
        }
      } else if (child instanceof UIColumnSelector) {
        columnSelectorIndex = i;
      }
      i++;
    }

    renderSheet(facesContext, sheet, (clickAction != null || dblClickAction != null), style);

    writer.endElement(HtmlElements.DIV);
    // TODO check ajax id
    if (!(FacesContextUtils.isAjax(facesContext)
        && sheetId.equals(FacesContextUtils.getAjaxComponentId(facesContext)))) {

      Integer frequency = null;
      UIComponent facetReload = sheet.getFacet(Facets.RELOAD);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        UIReload update = (UIReload) facetReload;
        frequency = update.getFrequency();
      }
      final String[] cmds = {
          "new Tobago.Sheet('" + sheetId
              + "', " + sheet.getFirst()
              + ", '" + sheet.getSelectable()
              + "', " + columnSelectorIndex
              + ", " + frequency
              + ", " + (clickAction != null ? HtmlRendererUtils.getJavascriptString(clickAction.getId()) : null)
              + ", " + HtmlRendererUtils.getRenderedPartiallyJavascriptArray(facesContext, clickAction)
              + ", " + (dblClickAction != null ? HtmlRendererUtils.getJavascriptString(dblClickAction.getId()) : null)
              + ", " + HtmlRendererUtils.getRenderedPartiallyJavascriptArray(facesContext, dblClickAction)
              + ", " + HtmlRendererUtils.getRenderedPartiallyJavascriptArray(facesContext, sheet, sheet) + ");"
      };

      HtmlRendererUtils.writeScriptLoader(facesContext, null, cmds);
    }
  }

  private void renderSheet(FacesContext facesContext, UISheet sheet, boolean hasClickAction, Style style)
      throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String sheetId = sheet.getClientId(facesContext);

    Measure sheetHeight;
    if (style.getHeight() == null) {
      // FIXME: nullpointer if height not defined
      LOG.error("no height in parent container, setting to 100");
      sheetHeight = Measure.valueOf(100);
    } else {
      sheetHeight = style.getHeight();
    }
    Measure footerHeight = getFooterHeight(facesContext, sheet);
    Measure headerHeight = getHeaderHeight(facesContext, sheet);
    String selectable = sheet.getSelectable();

    Application application = facesContext.getApplication();
    SheetState state = sheet.getSheetState(facesContext);
    List<Integer> columnWidths = sheet.getWidthList();

    final List<Integer> selectedRows = getSelectedRows(sheet, state);
    final List<UIColumn> renderedColumnList = sheet.getRenderedColumns();

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeIdAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeNameAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.joinWithSurroundingSeparator(columnWidths), false);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeIdAttribute(sheetId + SCROLL_POSTFIX);
    writer.writeNameAttribute(sheetId + SCROLL_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    Integer[] scrollPosition = sheet.getScrollPosition();
    if (scrollPosition != null) {
      String scroll = scrollPosition[0] + ";" + scrollPosition[1];
      writer.writeAttribute(HtmlAttributes.VALUE, scroll, false);
    } else {
      writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    }
    writer.endElement(HtmlElements.INPUT);

    if (!UISheet.NONE.equals(selectable)) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeIdAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeNameAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeAttribute(
          HtmlAttributes.VALUE, StringUtils.joinWithSurroundingSeparator(selectedRows), true);
      writer.endElement(HtmlElements.INPUT);
    }

    final boolean showHeader = sheet.isShowHeader();
    final boolean ie6SelectOneFix = showHeader
        && ClientProperties.getInstance(facesContext).getUserAgent().isMsie6()
            && ComponentUtils.findDescendant(sheet, UISelectOne.class) != null;
 
// BEGIN RENDER BODY CONTENT
    Style bodyStyle = new Style();
    bodyStyle.setPosition(Position.RELATIVE);
    Measure tableBodyWidth = sheet.getCurrentWidth().subtractNotNegative(getContentBorder(facesContext, sheet));
    bodyStyle.setWidth(tableBodyWidth);
    if (sheet.isPagingVisible()) {
      sheetHeight = sheetHeight.subtract(footerHeight);
    }
    if (ie6SelectOneFix) {
      bodyStyle.setTop(headerHeight);
    }
    if (showHeader) {
      sheetHeight = sheetHeight.subtract(headerHeight);
    }
    bodyStyle.setHeight(sheetHeight);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "data_div");
    writer.writeClassAttribute(Classes.create(sheet, "body"));

    if (!showHeader || ie6SelectOneFix) {
      bodyStyle.setPaddingTop(Measure.ZERO);
    }

    writer.writeStyleAttribute(bodyStyle);
    bodyStyle.setHeight(null);
    bodyStyle.setTop(null);
    Style sheetBodyStyle = new Style(bodyStyle);
    if (sheet.getNeedVerticalScrollbar() == null) {
      LOG.warn("Value of needVerticalScrollbar undefined!"); // why this value isn't set by the layout manager?
    } else {
      if (sheet.getNeedVerticalScrollbar()) {
        tableBodyWidth = tableBodyWidth.subtractNotNegative(getVerticalScrollbarWeight(facesContext, sheet));
      }
    }
    sheetBodyStyle.setWidth(tableBodyWidth);

    writer.startElement(HtmlElements.TABLE, null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeClassAttribute(Classes.create(sheet, "bodyTable"));
    writer.writeStyleAttribute(sheetBodyStyle);

    if (columnWidths != null) {
      writer.startElement(HtmlElements.COLGROUP, null);
      for (Integer columnWidth : columnWidths) {
        writer.startElement(HtmlElements.COL, null);
        writer.writeAttribute(HtmlAttributes.WIDTH, columnWidth);
        writer.endElement(HtmlElements.COL);
      }
      // filler column, which normally is not seen, it appears when resizing the columns
//      writer.startElement(HtmlElements.COL, null);
//      writer.writeAttribute(HtmlAttributes.WIDTH, 0);
//      writer.endElement(HtmlElements.COL);
      writer.endElement(HtmlElements.COLGROUP);
    }

    // Print the Content

    if (LOG.isDebugEnabled()) {
      LOG.debug("first = " + sheet.getFirst() + "   rows = " + sheet.getRows());
    }

    final String var = sheet.getVar();

    boolean odd = false;
    boolean emptySheet = true;
    // rows = 0 means: show all
    final int last = sheet.isRowsUnlimited() ? Integer.MAX_VALUE : sheet.getFirst() + sheet.getRows();
    for (int rowIndex = sheet.getFirst(); rowIndex < last; rowIndex++) {
      sheet.setRowIndex(rowIndex);
      if (!sheet.isRowAvailable()) {
        break;
      }
      emptySheet = false;
      odd = !odd;

      if (LOG.isDebugEnabled()) {
        LOG.debug("var       " + var);
        LOG.debug("list      " + sheet.getValue());
      }

      writer.startElement(HtmlElements.TR, null);
      Markup rowMarkup = odd ? Markup.ODD : Markup.EVEN;
      final boolean selected = selectedRows.contains(rowIndex);
      if (selected) {
        rowMarkup = rowMarkup.add(Markup.SELECTED);
      }
      writer.writeClassAttribute(Classes.create(sheet, "row", rowMarkup));
      if (rowIndex == sheet.getFirst()) {
        writer.writeAttribute("rowIndexInModel", Integer.toString(sheet.getFirst()), false);
      }

      int columnIndex = -1;
      for (UIColumn column : renderedColumnList) {
        columnIndex++;

        writer.startElement(HtmlElements.TD, column);

        Markup markup = column instanceof SupportsMarkup ? ((SupportsMarkup) column).getMarkup() : Markup.NULL;
        if (markup == null) {
          markup = Markup.NULL;
        }
        if (columnIndex == 0) {
          markup = markup.add(Markup.FIRST);
        }
        if (hasClickAction) {
          markup = markup.add(Markup.CLICKABLE);
        }
        if (isPure(column)) {
          markup = markup.add(Markup.PURE);
        }
        writer.writeClassAttribute(Classes.create(sheet, "cell", markup));
        final TextAlign align = TextAlign.parse((String) column.getAttributes().get(Attributes.ALIGN));
        if (align != null) {
          Style alignStyle = new Style();
          alignStyle.setTextAlign(align);
          writer.writeStyleAttribute(alignStyle);
        }

        if (column instanceof UIColumnSelector) {
          final boolean disabled = ComponentUtils.getBooleanAttribute(column, Attributes.DISABLED);
          writer.startElement(HtmlElements.INPUT, null);
          writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX, false);
          writer.writeAttribute(HtmlAttributes.CHECKED, selected);
          writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
          writer.writeIdAttribute(sheetId + "_data_row_selector_" + rowIndex);
          writer.writeClassAttribute(Classes.create(sheet, "columnSelector"));
          writer.endElement(HtmlElements.INPUT);
        } else {
          List<UIComponent> children = sheet.getRenderedChildrenOf(column);
          for (UIComponent grandKid : children) {
            // set height to 0 to prevent use of layoutheight from parent
            grandKid.getAttributes().put(Attributes.LAYOUT_HEIGHT, HEIGHT_0);
            // XXX hotfix
            if (grandKid instanceof LayoutBase) {
              LayoutBase base = (LayoutBase) grandKid;
              if (base.getLeft() != null) {
                base.setLeft(null);
              }
              if (base.getTop() != null) {
                base.setTop(null);
              }
            }
            RenderUtils.prepareRendererAll(facesContext, grandKid);
            RenderUtils.encode(facesContext, grandKid);
          }
        }

        writer.endElement(HtmlElements.TD);
      }

      writer.startElement(HtmlElements.TD, null);
      writer.writeClassAttribute(Classes.create(sheet, "cell", Markup.FILLER));
//      writer.write("&nbsp;");
      writer.startElement(HtmlElements.DIV, null);
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.TD);

      writer.endElement(HtmlElements.TR);
    }

    sheet.setRowIndex(-1);

    if (emptySheet && showHeader) {
      writer.startElement(HtmlElements.TR, null);
      int columnIndex = -1;
      for (UIColumn column : renderedColumnList) {
        columnIndex++;
        writer.startElement(HtmlElements.TD, null);
        writer.startElement(HtmlElements.DIV, null);
        Integer divWidth = sheet.getWidthList().get(columnIndex);
        Style divStyle = new Style();
        divStyle.setWidth(Measure.valueOf(divWidth));
        writer.writeStyleAttribute(divStyle);
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.TD);
      }
      writer.startElement(HtmlElements.TD, null);
      writer.writeClassAttribute(Classes.create(sheet, "cell", Markup.FILLER));
//      writer.write("&nbsp;");
      writer.startElement(HtmlElements.DIV, null);
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.TD);
      writer.endElement(HtmlElements.TR);
    }

    writer.endElement(HtmlElements.TABLE);
    writer.endElement(HtmlElements.DIV);

// END RENDER BODY CONTENT

    if (showHeader) {
      renderColumnHeaders(
          facesContext, sheet, writer, resourceManager, contextPath, sheetId, renderedColumnList, tableBodyWidth);
    }

    if (sheet.isPagingVisible()) {
      Style footerStyle = new Style();
      footerStyle.setWidth(sheet.getCurrentWidth());
      if (ie6SelectOneFix) {
        footerStyle.setTop(headerHeight);
      }
      writer.startElement(HtmlElements.DIV, sheet);
      writer.writeClassAttribute(Classes.create(sheet, "footer"));
      writer.writeStyleAttribute(footerStyle);

      // show row range
      final Markup showRowRange = markupForLeftCenterRight(sheet.getShowRowRange());
      if (showRowRange != Markup.NULL) {
        UICommand pagerCommand = (UICommand) sheet.getFacet(Facets.PAGER_ROW);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(application, PageAction.TO_ROW, false);
          sheet.getFacets().put(Facets.PAGER_ROW, pagerCommand);
        }
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(Classes.create(sheet, "pagingOuter", showRowRange));
        writer.writeAttribute(HtmlAttributes.TITLE,
            ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoRowPagingTip"), true);
        writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
        writer.write(createSheetPagingInfo(sheet, facesContext, pagerCommandId, true));
        writer.endElement(HtmlElements.SPAN);
      }

      // show direct links
      final Markup showDirectLinks = markupForLeftCenterRight(sheet.getShowDirectLinks());
      if (showDirectLinks != Markup.NULL) {
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(Classes.create(sheet, "pagingOuter", showDirectLinks));
        writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "pagingLinks");
        writeDirectPagingLinks(writer, facesContext, application, sheet);
        writer.endElement(HtmlElements.SPAN);
      }

      // show page range
      final Markup showPageRange = markupForLeftCenterRight(sheet.getShowPageRange());
      if (showPageRange != Markup.NULL) {
        UICommand pagerCommand = (UICommand) sheet.getFacet(Facets.PAGER_PAGE);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(application, PageAction.TO_PAGE, false);
          sheet.getFacets().put(Facets.PAGER_PAGE, pagerCommand);
        }
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(Classes.create(sheet, "pagingOuter", showPageRange));
        writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "pagingPages");
        writer.writeText("");

        boolean atBeginning = sheet.isAtBeginning();
        link(facesContext, application, atBeginning, PageAction.FIRST, sheet);
        link(facesContext, application, atBeginning, PageAction.PREV, sheet);
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(Classes.create(sheet, "pagingText"));
        writer.writeAttribute(HtmlAttributes.TITLE,
            ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetPagingInfoPagePagingTip"), true);
        writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
        writer.write(createSheetPagingInfo(sheet, facesContext, pagerCommandId, false));
        writer.endElement(HtmlElements.SPAN);
        boolean atEnd = sheet.isAtEnd();
        link(facesContext, application, atEnd, PageAction.NEXT, sheet);
        link(facesContext, application, atEnd || !sheet.hasRowCount(), PageAction.LAST, sheet);
        writer.endElement(HtmlElements.SPAN);
      }

      writer.endElement(HtmlElements.DIV);
    }
  }

  /**
   * Differ between simple content and complex content.
   * Decide if the content of a cell needs usually the whole possible space or
   * is the character of the content like flowing text.
   * In the second case, the style usually sets a padding.<br/>
   * Pure is needed for &lt;tc:panel>,  &lt;tc:in>, etc.<br/>
   * Pure is not needed for  &lt;tc:out> and &lt;tc:link>
   */
  private boolean isPure(UIColumn column) {
    for (UIComponent child : (List<UIComponent>) column.getChildren()) {
      if (!(child instanceof UIOut) && !(child instanceof UILink)) {
        return true;
      }
    }
    return false;
  }

  private String createSheetPagingInfo(UISheet sheet, FacesContext facesContext, String pagerCommandId, boolean row) {
    String sheetPagingInfo;
    if (sheet.getRowCount() != 0) {
      Locale locale = facesContext.getViewRoot().getLocale();
      int first = row ? sheet.getFirst() + 1 : sheet.getCurrentPage() + 1;
      int last = sheet.hasRowCount()
          ? row ? sheet.getLastRowIndexOfCurrentPage() : sheet.getPages()
          : -1;
      final boolean unknown = !sheet.hasRowCount();
      final String key = "sheetPagingInfo"
          + (unknown ? "Undefined" : "")
          + (first == last ? "Single" : "") 
          + (row ? "Row" : "Page")
          + (first == last ? "" : "s"); // plural
      final String message = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", key);
      final MessageFormat detail = new MessageFormat(message, locale);
      Object[] args = {
          first,
          last == -1 ? "?" : last,
          unknown ? "" : sheet.getRowCount(),
          pagerCommandId + ComponentUtils.SUB_SEPARATOR + "text"
      };
      sheetPagingInfo = detail.format(args);
    } else {
      sheetPagingInfo =
          ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago",
              "sheetPagingInfoEmpty" + (row ? "Row" : "Page"));
    }
    return sheetPagingInfo;
  }

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);

    String key = component.getClientId(facesContext) + WIDTHS_POSTFIX;

    Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(key)) {
      String widths = (String) requestParameterMap.get(key);
      if (widths.trim().length() > 0) {
        component.getAttributes().put(Attributes.WIDTH_LIST_STRING, widths);
      }
    }

    key = component.getClientId(facesContext) + SELECTED_POSTFIX;
    if (requestParameterMap.containsKey(key)) {
      String selected = (String) requestParameterMap.get(key);
      if (LOG.isDebugEnabled()) {
        LOG.debug("selected = " + selected);
      }
      List<Integer> selectedRows;
      try {
        selectedRows = StringUtils.parseIntegerList(selected);
      } catch (NumberFormatException e) {
        LOG.warn(selected, e);
        selectedRows = Collections.emptyList();
      }

      component.getAttributes().put(Attributes.SELECTED_LIST_STRING, selectedRows);
    }

    key = component.getClientId(facesContext) + SCROLL_POSTFIX;
    String value = (String) requestParameterMap.get(key);
    if (value != null) {
      Integer[] scrollPosition = SheetState.parseScrollPosition(value);
      if (scrollPosition != null) {
        //noinspection unchecked
        component.getAttributes().put(Attributes.SCROLL_POSITION, scrollPosition);
      }
    }

  }

  private Measure getHeaderHeight(FacesContext facesContext, UISheet sheet) {
    return sheet.isShowHeader()
        ? getResourceManager().getThemeMeasure(facesContext, sheet, "headerHeight")
        : Measure.ZERO;
  }

  private Measure getRowHeight(FacesContext facesContext, UISheet sheet) {
    return getResourceManager().getThemeMeasure(facesContext, sheet, "rowHeight");
  }

  private Measure getFooterHeight(FacesContext facesContext, UISheet sheet) {
    return sheet.isPagingVisible()
        ? getResourceManager().getThemeMeasure(facesContext, sheet, "footerHeight")
        : Measure.ZERO;
  }

  private Markup markupForLeftCenterRight(String name) {
    if ("left".equals(name)) {
      return Markup.LEFT;
    }
    if ("center".equals(name)) {
      return Markup.CENTER;
    }
    if ("right".equals(name)) {
      return Markup.RIGHT;
    }
    return Markup.NULL;
  }

  private String checkPagingAttribute(String name) {
    if (isNotNone(name)) {
      return name;
    } else {
      if (!"none".equals(name)) {
        LOG.warn("Illegal value in sheets paging attribute: '" + name + "'");
      }
      return "none";
    }
  }

  private boolean isNotNone(String value) {
    // todo: use enum type instead of string
    return "left".equals(value) || "center".equals(value) || "right".equals(value);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  private List<Integer> getSelectedRows(UISheet data, SheetState state) {
    List<Integer> selected = (List<Integer>)
        data.getAttributes().get(Attributes.SELECTED_LIST_STRING);
    if (selected == null && state != null) {
      selected = state.getSelectedRows();
    }
    if (selected == null) {
      selected = Collections.emptyList();
    }
    return selected;
  }

  private void link(FacesContext facesContext, Application application,
                    boolean disabled, PageAction command, UISheet data)
      throws IOException {
    UICommand link = createPagingCommand(application, command, disabled);

    data.getFacets().put(command.getToken(), link);


    String tip = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago",
        "sheet" + command.getToken());
    String image = ResourceManagerUtils.getImageWithPath(facesContext,
        "image/sheet" + command.getToken() + (disabled ? "Disabled" : "") + ".gif");

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.IMG, null);
    writer.writeIdAttribute(data.getClientId(facesContext)
        + ComponentUtils.SUB_SEPARATOR + "pagingPages" + ComponentUtils.SUB_SEPARATOR + command.getToken());
    Classes pagerClasses = Classes.create(data, "footerPagerButton", disabled ? Markup.DISABLED : null);
    writer.writeClassAttribute(pagerClasses);
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(DataAttributes.DISABLED, disabled);
    writer.endElement(HtmlElements.IMG);
  }

  private void renderColumnHeaders(
      FacesContext facesContext, UISheet sheet, TobagoResponseWriter writer, ResourceManager resourceManager,
      String contextPath, String sheetId, List<UIColumn> renderedColumnList, Measure headerWidth) throws IOException {
    // begin rendering header
    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "header_div");
    writer.writeClassAttribute(Classes.create(sheet, "headerDiv"));
    Style style = new Style();
    style.setWidth(headerWidth);
    writer.writeStyleAttribute(style);

    int columnCount = 0;
    for (UIColumn column : renderedColumnList) {
      renderColumnHeader(facesContext, writer, sheet, columnCount, column, resourceManager, contextPath);
      columnCount++;
    }
    writer.startElement(HtmlElements.SPAN, null);
    writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "header_box_filler");
    writer.writeClassAttribute(Classes.create(sheet, "header"));
    writer.writeStyleAttribute("width:0px");

    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.DIV);
    // end rendering header
  }

  private void renderColumnHeader(
      FacesContext facesContext, TobagoResponseWriter writer, UISheet sheet, int columnIndex, UIColumn column,
      ResourceManager resourceManager, String contextPath)
      throws IOException {
    String sheetId = sheet.getClientId(facesContext);
    Application application = facesContext.getApplication();

    Integer divWidth = sheet.getWidthList().get(columnIndex);
    Style divStyle = new Style();
    divWidth = divWidth - 6; // leftBorder + leftPadding + rightPadding + rightBorder = 6, todo: use Style Constructor
    divStyle.setWidth(Measure.valueOf(divWidth));
    TextAlign align = TextAlign.parse((String) column.getAttributes().get(Attributes.ALIGN));
    divStyle.setTextAlign(align);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "header_box_" + columnIndex);
    writer.writeStyleAttribute(divStyle);
    String tip = (String) column.getAttributes().get(Attributes.TIP);
    if (tip == null) {
      tip = "";
    }

    final UIComponent dropDownMenu = FacetUtils.getDropDownMenu(column);
    if (dropDownMenu != null) {
      LOG.error("Drop down menu is not implemented in sheets yet!");
      // Todo: implement it!
      // Todo: change description in ColumnTagDeclaration after implementing it.
    }

    Markup markup = Markup.NULL;

    // sorting

    String sorterImage = null;
    boolean sortable = ComponentUtils.getBooleanAttribute(column, Attributes.SORTABLE);
    if (sortable && !(column instanceof UIColumnSelector)) {
      UICommand sortCommand = (UICommand) column.getFacet(Facets.SORTER);
      if (sortCommand == null) {
        String columnId = column.getClientId(facesContext);
        String sorterId = columnId.substring(columnId.lastIndexOf(":") + 1) + "_" + UISheet.SORTER_ID;
        sortCommand = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
        sortCommand.setRendererType(RendererTypes.LINK);
        sortCommand.setId(sorterId);
        column.getFacets().put(Facets.SORTER, sortCommand);
      }

      writer.writeAttribute("sorterId", sortCommand.getClientId(facesContext), false);

      if (org.apache.commons.lang.StringUtils.isNotEmpty(tip)) {
        tip += " - ";
      }
      tip += ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetTipSorting");

      markup.add(Markup.SORTABLE);

      SheetState sheetState = sheet.getSheetState(facesContext);
      if (column.getId().equals(sheetState.getSortedColumnId())) {
        String sortTitle;
        if (sheetState.isAscending()) {
          sorterImage = contextPath + resourceManager.getImage(facesContext, "image/ascending.gif");
          sortTitle = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetAscending");
          markup.add(Markup.ASCENDING);
        } else {
          sorterImage = contextPath + resourceManager.getImage(facesContext, "image/descending.gif");
          sortTitle = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "sheetDescending");
          markup.add(Markup.DESCENDING);
        }
        if (sortTitle != null) {
          tip += " - " + sortTitle;
        }
      }
    }
    if (columnIndex == 0) {
      markup = markup.add(Markup.FIRST);
    }
    writer.writeClassAttribute(Classes.create(sheet, "header", markup));
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);

    if (column instanceof UIColumnSelector) {
      renderColumnSelectorHeader(facesContext, writer, sheet, (UIColumnSelector) column);
    } else {
      String label = (String) column.getAttributes().get(Attributes.LABEL);
      if (label != null) {
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeText(label);
        writer.endElement(HtmlElements.SPAN);
      }
    }

    if (sorterImage != null) {
      writer.startElement(HtmlElements.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, sorterImage, false);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlElements.IMG);
    }

    writer.endElement(HtmlElements.SPAN);

    // resizing
    if (ComponentUtils.getBooleanAttribute(column, Attributes.RESIZABLE)) {
      writer.startElement(HtmlElements.SPAN, null);
      writer.writeClassAttribute(Classes.create(sheet, "headerSpacerOuter"));
      writer.startElement(HtmlElements.SPAN, null);
      writer.writeIdAttribute(sheetId + ComponentUtils.SUB_SEPARATOR + "header_spacer_" + columnIndex);
      Markup resizeMarkup = column instanceof UIColumnSelector ? Markup.NULL : Markup.RESIZABLE;
      writer.writeClassAttribute(Classes.create(sheet, "headerSpacer", resizeMarkup));
      writer.write("&nbsp;&nbsp;"); // is needed for IE6
      writer.endElement(HtmlElements.SPAN);
      writer.endElement(HtmlElements.SPAN);
    }
  }

  protected void renderColumnSelectorHeader(
      FacesContext facesContext, TobagoResponseWriter writer, UISheet sheet, UIColumnSelector column)
      throws IOException {

    final UIToolBar toolBar = createToolBar(facesContext, sheet);
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(sheet, "toolBar"));

    if (UISheet.MULTI.equals(sheet.getSelectable())) {
      RenderUtils.prepareRendererAll(facesContext, toolBar);
      RenderUtils.encode(facesContext, toolBar);
    }

    writer.endElement(HtmlElements.DIV);
  }

  private UIToolBar createToolBar(FacesContext facesContext, UISheet sheet) {
    final Application application = facesContext.getApplication();
    final UICommand dropDown = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, null, "dropDown");
    final UIMenu menu = (UIMenu) CreateComponentUtils.createComponent(
        facesContext, UIMenu.COMPONENT_TYPE, RendererTypes.MENU, "menu");
    FacetUtils.setDropDownMenu(dropDown, menu);
    final String sheetId = sheet.getClientId(facesContext);

    createMenuItem(facesContext, menu, "sheetMenuSelect", Markup.SHEET_SELECT_ALL, sheetId);
    createMenuItem(facesContext, menu, "sheetMenuUnselect", Markup.SHEET_DESELECT_ALL, sheetId);
    createMenuItem(facesContext, menu, "sheetMenuToggleselect", Markup.SHEET_TOGGLE_ALL, sheetId);

    final UIToolBar toolBar = (UIToolBar) application.createComponent(UIToolBar.COMPONENT_TYPE);
    toolBar.setId(facesContext.getViewRoot().createUniqueId());
    toolBar.setRendererType("TabGroupToolBar");
    toolBar.setTransient(true);
    toolBar.getChildren().add(dropDown);
    sheet.getFacets().put(Facets.TOOL_BAR, toolBar);
    return toolBar;
  }

  private void createMenuItem(
      final FacesContext facesContext, UIMenu menu, String label, Markup markup, String sheetId) {
    final String id = markup.toString();
    final UIMenuCommand menuItem = (UIMenuCommand) CreateComponentUtils.createComponent(
        facesContext, UIMenuCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND, id);
    menuItem.setLabel(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", label));
    menuItem.setMarkup(markup);
    menuItem.setOnclick("/**/"); // XXX avoid submit
    ComponentUtils.putDataAttributeWithPrefix(menuItem, DataAttributes.SHEETID, sheetId);
    menu.getChildren().add(menuItem);
  }

  private void writeDirectPagingLinks(
      TobagoResponseWriter writer, FacesContext facesContext, Application application, UISheet sheet)
      throws IOException {
    UICommand pagerCommand = (UICommand) sheet.getFacet(Facets.PAGER_PAGE);
    if (pagerCommand == null) {
      pagerCommand = createPagingCommand(application, PageAction.TO_PAGE, false);
      sheet.getFacets().put(Facets.PAGER_PAGE, pagerCommand);
    }
    String pagerCommandId = pagerCommand.getClientId(facesContext);
    int linkCount = ComponentUtils.getIntAttribute(sheet, Attributes.DIRECT_LINK_COUNT);
    linkCount--;  // current page needs no link
    ArrayList<Integer> prevs = new ArrayList<Integer>(linkCount);
    int page = sheet.getCurrentPage() + 1;
    for (int i = 0; i < linkCount && page > 1; i++) {
      page--;
      if (page > 0) {
        prevs.add(0, page);
      }
    }

    ArrayList<Integer> nexts = new ArrayList<Integer>(linkCount);
    page = sheet.getCurrentPage() + 1;
    final int pages = sheet.hasRowCount() || sheet.isRowsUnlimited() ? sheet.getPages() : Integer.MAX_VALUE;
    for (int i = 0; i < linkCount && page < pages; i++) {
      page++;
      if (page > 1) {
        nexts.add(page);
      }
    }

    if (prevs.size() > (linkCount / 2)
        && nexts.size() > (linkCount - (linkCount / 2))) {
      while (prevs.size() > (linkCount / 2)) {
        prevs.remove(0);
      }
      while (nexts.size() > (linkCount - (linkCount / 2))) {
        nexts.remove(nexts.size() - 1);
      }
    } else if (prevs.size() <= (linkCount / 2)) {
      while (prevs.size() + nexts.size() > linkCount) {
        nexts.remove(nexts.size() - 1);
      }
    } else {
      while (prevs.size() + nexts.size() > linkCount) {
        prevs.remove(0);
      }
    }

    String name;
    int skip = prevs.size() > 0 ? prevs.get(0) : 1;
    if (skip > 1) {
      skip -= (linkCount - (linkCount / 2));
      skip--;
      name = "...";
      if (skip < 1) {
        skip = 1;
        if (prevs.get(0) == 2) {
          name = "1";
        }
      }
      writeLinkElement(writer, sheet, name, Integer.toString(skip), pagerCommandId, true);
    }
    for (Integer prev : prevs) {
      name = prev.toString();
      writeLinkElement(writer, sheet, name, name, pagerCommandId, true);
    }
    name = Integer.toString(sheet.getCurrentPage() + 1);
    writeLinkElement(writer, sheet, name, name, pagerCommandId, false);

    for (Integer next : nexts) {
      name = next.toString();
      writeLinkElement(writer, sheet, name, name, pagerCommandId, true);
    }

    skip = nexts.size() > 0 ? nexts.get(nexts.size() - 1) : pages;
    if (skip < pages) {
      skip += linkCount / 2;
      skip++;
      name = "...";
      if (skip > pages) {
        skip = pages;
        if ((nexts.get(nexts.size() - 1)) == skip - 1) {
          name = Integer.toString(skip);
        }
      }
      writeLinkElement(writer, sheet, name, Integer.toString(skip), pagerCommandId, true);
    }
  }

  private UICommand createPagingCommand(Application application, PageAction command, boolean disabled) {
    UICommand link;
    link = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    link.setRendererType(RendererTypes.SHEET_PAGE_COMMAND);
    link.setRendered(true);
    link.setId(command.getToken());
    link.getAttributes().put(Attributes.INLINE, Boolean.TRUE);
    link.getAttributes().put(Attributes.DISABLED, disabled);
    return link;
  }

  private void writeLinkElement(
      TobagoResponseWriter writer, UISheet sheet, String str, String skip, String id, boolean makeLink)
      throws IOException {
    String type = makeLink ? HtmlElements.A : HtmlElements.SPAN;
    writer.startElement(type, null);
    writer.writeClassAttribute(Classes.create(sheet, "pagingLink"));
    if (makeLink) {
      writer.writeIdAttribute(id + ComponentUtils.SUB_SEPARATOR + "link_" + skip);
      writer.writeAttribute(HtmlAttributes.HREF, "#", true);
    }
    writer.flush();
    writer.write(str);
    writer.endElement(type);
  }

  private Measure getContentBorder(FacesContext facesContext, UISheet data) {
    return getBorderLeft(facesContext, data).add(getBorderRight(facesContext, data));
  }

  @Override
  public Measure getPreferredHeight(FacesContext facesContext, Configurable component) {
    final UISheet sheet = (UISheet) component;
    Measure headerHeight = getHeaderHeight(facesContext, sheet);
    Measure rowHeight = getRowHeight(facesContext, sheet);
    Measure footerHeight = getFooterHeight(facesContext, sheet);
    int rows = sheet.getRows();
    if (rows == 0) {
      rows = sheet.getRowCount();
    }
    if (rows == -1) {
      rows = 10; // estimating something to get a valid value...
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug(headerHeight + " " + footerHeight + " " + rowHeight + " " + rows);
    }

    return headerHeight.add(rowHeight.multiply(rows)).add(footerHeight);
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    // DO Nothing
  }

  @Override
  public boolean getPrepareRendersChildren() {
    return true;
  }

  @Override
  public void prepareRendersChildren(FacesContext facesContext, UIComponent component) throws IOException {
    UISheet sheet = (UISheet) component;
    for (UIColumn column : sheet.getRenderedColumns()) {
      RenderUtils.prepareRendererAll(facesContext, column);
    }
  }
}
