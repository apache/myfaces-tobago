package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALIGN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COMMAND_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DIRECT_LINK_COUNT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOOTER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FORCE_VERTICAL_SCROLLBAR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DIRECT_LINKS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_PAGE_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_ROW_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH_LIST;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH_LIST_STRING;
import static org.apache.myfaces.tobago.TobagoConstants.COMMAND_TYPE_SCRIPT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUPOPUP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PAGER_PAGE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PAGER_ROW;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUCOMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.SheetRendererWorkaround;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import static org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.SheetPageCommandRenderer.PAGE_RENDERER_TYPE;
import org.apache.myfaces.tobago.taglib.component.MenuCommandTag;
import org.apache.myfaces.tobago.util.StringUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SheetRenderer extends RendererBase
  implements SheetRendererWorkaround, AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(SheetRenderer.class);

  public static final String WIDTHS_POSTFIX
      = SUBCOMPONENT_SEP + "widths";
  public static final String SELECTED_POSTFIX
      = SUBCOMPONENT_SEP + "selected";
  private static final Integer HEIGHT_0 = 0;

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    UIData data = (UIData) uiComponent;

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, data);

    final String sheetId = data.getClientId(facesContext);
    String sheetStyle = (String) data.getAttributes().get(ATTR_STYLE);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    // Outher sheet div
    writer.startElement("div", null);
    writer.writeIdAttribute(sheetId + "_outer_div");
    writer.writeClassAttribute("tobago-sheet-outer-div");
    writer.writeAttribute("style", sheetStyle, null);

    renderSheet(facesContext, data);

    writer.endElement("div");

    ResourceManager resourceManager
        = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();

    String unchecked = contextPath
        + resourceManager .getImage(viewRoot, "image/sheetUnchecked.gif");
    String checked = contextPath
        + resourceManager .getImage(viewRoot, "image/sheetChecked.gif");

    final String[] styles = new String[]{"style/tobago-sheet.css"};
    final String[] scripts = new String[]{"script/tobago-sheet.js"};
    final String[] cmds = {
        "initSheet(\"" + sheetId + "\");",
        "tobagoSheetSetUncheckedImage(\"" + sheetId + "\", \"" + unchecked + "\");",
        "tobagoSheetSetCheckedImage(\"" + sheetId + "\", \"" + checked + "\");",
        "updateSelectionView(\"" + sheetId + "\");",
        "" // placeholder for ajax sheet object creation
    };

    ComponentUtil.addStyles(data, styles);
    ComponentUtil.addScripts(data, scripts);

    if (!TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      ComponentUtil.addOnloadCommands(data, cmds);
    } else {
      HtmlRendererUtil.writeStyleLoader(facesContext, styles);
      // add creation of ajax object
      cmds[cmds.length -1] = "new Tobago.Sheet(\"" + sheetId + "\");";
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);
    }
  }

  private void renderSheet(FacesContext facesContext, UIData data) throws IOException {
    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String sheetId = data.getClientId(facesContext);

    String image1x1 = contextPath + resourceManager
        .getImage(viewRoot, "image/1x1.gif");
    String ascending = contextPath + resourceManager
        .getImage(viewRoot, "image/ascending.gif");
    String descending = contextPath + resourceManager
        .getImage(viewRoot, "image/descending.gif");
    String selectorDisabled = contextPath + resourceManager
        .getImage(viewRoot, "image/sheetUncheckedDisabled.gif");
    String unchecked = contextPath + resourceManager
        .getImage(viewRoot, "image/sheetUnchecked.gif");

    Map attributes = data.getAttributes();
    String sheetStyle = (String) attributes.get(ATTR_STYLE);
    String headerStyle =
        (String) attributes.get(ATTR_STYLE_HEADER);
//    String sheetWidthString = LayoutUtil.getStyleAttributeValue(sheetStyle,
//        "width");
    String sheetHeightString
        = HtmlRendererUtil.getStyleAttributeValue(sheetStyle, "height");
    int sheetHeight;
    if (sheetHeightString != null) {
      sheetHeight = Integer.parseInt(sheetHeightString.replaceAll("\\D", ""));
    } else {
      // FIXME: nullpointer if height not defined
      LOG.error("no height in parent container, setting to 100");
      sheetHeight = 100;
    }
    String bodyStyle = (String) attributes.get(ATTR_STYLE_BODY);
    int footerHeight = (Integer) attributes.get(ATTR_FOOTER_HEIGHT);


    Application application = facesContext.getApplication();
    SheetState state = data.getSheetState(facesContext);
    List<Integer> columnWidths = data.getWidthList();

    String selectedRows = StringUtil.toString(getSelectedRows(data, state));
    List<UIColumn> columnList = data.getRendererdColumns();


    writer.startElement("input", null);
    writer.writeIdAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeNameAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeIdAttribute(sheetId + SELECTED_POSTFIX);
    writer.writeNameAttribute(sheetId + SELECTED_POSTFIX);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", selectedRows, null);
    writer.endElement("input");


    final boolean showHeader = data.isShowHeader();
    if (showHeader) {
      // begin rendering header
      writer.startElement("div", null);
      writer.writeIdAttribute(sheetId + "_header_div");
      writer.writeClassAttribute("tobago-sheet-header-div");
      writer.writeAttribute("style", headerStyle, null);

      int columnCount = 0;
      final int sortMarkerWidth = getAscendingMarkerWidth(facesContext, data);
      for (UIColumn column : columnList) {
        renderColumnHeader(facesContext, writer, data, columnCount, column,
            ascending, descending, image1x1, sortMarkerWidth);
        columnCount++;
      }
      writer.startElement("div", null);
      writer.writeIdAttribute(sheetId + "_header_box_filler");
      writer.writeClassAttribute("tobago-sheet-header-box");
      writer.writeAttribute("style", "width: 0px", null);

      writer.startElement("div", null);
      writer.writeClassAttribute("tobago-sheet-header");
      writer.write("&nbsp;");
      writer.endElement("div");

      writer.endElement("div");
      writer.endElement("div");
      // end rendering header
    }

// BEGIN RENDER BODY CONTENT
    bodyStyle = HtmlRendererUtil.replaceStyleAttribute(bodyStyle, "height",
        (sheetHeight - footerHeight) + "px");
    String space = HtmlRendererUtil.getStyleAttributeValue(bodyStyle, "width");
    String sheetBodyStyle;
    if (space != null) {
      int intSpace = Integer.parseInt(space.replaceAll("\\D", ""));
//      intSpace -= columnWidths.get(columnWidths.size() - 1);
      intSpace -= getContentBorder(facesContext, data);
      if (needVerticalScrollbar(facesContext, data)) {
        intSpace -= getScrollbarWidth(facesContext, data);
      }
      sheetBodyStyle =
          HtmlRendererUtil.replaceStyleAttribute(bodyStyle, "width", intSpace + "px");
    } else {
      sheetBodyStyle = bodyStyle;
    }
    sheetBodyStyle = HtmlRendererUtil.removeStyleAttribute(sheetBodyStyle, "height");

    if (!showHeader) {
      bodyStyle += " padding-top: 0px;";
    }

    writer.startElement("div", null);
    writer.writeIdAttribute(sheetId + "_data_div");
    writer.writeClassAttribute("tobago-sheet-body-div ");
    writer.writeAttribute("style", bodyStyle, null);

    writer.startElement("table", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeClassAttribute("tobago-sheet-body-table");
    writer.writeAttribute("style", sheetBodyStyle, null);

    if (columnWidths != null) {
      writer.startElement("colgroup", null);
      for (Integer columnWidth : columnWidths) {
        writer.startElement("col", null);
        writer.writeAttribute("width", columnWidth, null);
        writer.endElement("col");
      }
      writer.endElement("colgroup");
    }

    // Print the Content

    if (LOG.isDebugEnabled()) {
      LOG.debug("first = " + data.getFirst() + "   rows = " + data.getRows());
    }

    //final Map requestMap = facesContext.getExternalContext().getRequestMap();
    final String var = data.getVar();

    boolean odd = false;
    int visibleIndex = -1;
    final int last = data.getFirst() + data.getRows();
    for (int rowIndex = data.getFirst(); rowIndex < last; rowIndex++) {
      visibleIndex++;
      data.setRowIndex(rowIndex);
      if (!data.isRowAvailable()) {
        break;
      }
      odd = !odd;
      final String rowClass = odd ? "tobago-sheet-content-odd " : "tobago-sheet-content-even ";

      if (LOG.isDebugEnabled()) {
        LOG.debug("var       " + var);
        LOG.debug("list      " + data.getValue());
      }

      //final Object value = data.getRowData();

      //*if (LOG.isDebugEnabled()) {
      //  LOG.debug("element   " + value);
      //}

      //requestMap.put(var, value);

      writer.startElement("tr", null);
      writer.writeClassAttribute(rowClass);
      writer.writeIdAttribute(sheetId + "_data_tr_" + rowIndex);
      writer.writeText("", null);


      int columnIndex = -1;
      for (UIColumn column : data.getRendererdColumns()) {
        columnIndex++;

        final String style = "width: " + columnWidths.get(columnIndex) + "px;";
        String tdStyle = "";
        final String align = (String) column.getAttributes().get(
            ATTR_ALIGN);
        if (align != null) {
          tdStyle = "text-align: " + align;
        }
        final String cellClass = (String) column.getAttributes().get(
            ATTR_STYLE_CLASS);
        final String tdClass = "tobago-sheet-cell-td "
            + (cellClass != null ? cellClass : "");


        writer.startElement("td", column);

        writer.writeClassAttribute(tdClass);
        writer.writeAttribute("style", tdStyle, null);

        writer.startElement("div", null);
        writer.writeIdAttribute(
            sheetId + "_data_row_" + visibleIndex + "_column" + columnIndex);
        writer.writeClassAttribute("tobago-sheet-cell-outer");
        writer.writeAttribute("style", style, null);

        writer.startElement("div", null);
        writer.writeClassAttribute("tobago-sheet-cell-inner");
        writer.writeText("", null);

        if (column instanceof UIColumnSelector) {
          final boolean disabled
              = ComponentUtil.getBooleanAttribute(column, ATTR_DISABLED);
          writer.startElement("img", null);
          if (disabled) {
            writer.writeAttribute("src", selectorDisabled, null);
          } else {
            writer.writeAttribute("src", unchecked, null);
          }
          writer.writeIdAttribute(sheetId + "_data_row_selector_" + rowIndex);
          writer.writeClassAttribute("tobago-sheet-column-selector");
          writer.endElement("img");
        } else {
          for (UIComponent grandkid : data.getRenderedChildrenOf(column)) {
            // set height to 0 to prevent use of layoutheight from parent
            grandkid.getAttributes().put(ATTR_LAYOUT_HEIGHT, HEIGHT_0);
            RenderUtil.encode(facesContext, grandkid);
          }
        }

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("td");
      }

      writer.startElement("td", null);
      writer.writeClassAttribute("tobago-sheet-cell-td");

      writer.startElement("div", null);
      writer.writeIdAttribute(
          sheetId + "_data_row_" + visibleIndex + "_column_filler");
      writer.writeClassAttribute("tobago-sheet-cell-outer");
      writer.writeAttribute("style", "width: 0px;", null);

      writer.write("&nbsp;");

      writer.endElement("div");
      writer.endElement("td");

      writer.endElement("tr");
    }

    data.setRowIndex(-1);

    //requestMap.remove(var);

    writer.endElement("table");
    writer.endElement("div");

// END RENDER BODY CONTENT


    final String showRowRange
        = getPagingAttribute(data, ATTR_SHOW_ROW_RANGE);
    final String showPageRange
        = getPagingAttribute(data, ATTR_SHOW_PAGE_RANGE);
    final String showDirectLinks
        = getPagingAttribute(data, ATTR_SHOW_DIRECT_LINKS);

    if (isValidPagingValue(showRowRange)
        || isValidPagingValue(showPageRange)
        || isValidPagingValue(showDirectLinks)) {
      final String footerStyle = HtmlRendererUtil.replaceStyleAttribute(bodyStyle,
          "height", footerHeight + "px")
          + " top: " + (sheetHeight - footerHeight) + "px;";

      writer.startElement("div", data);
      writer.writeClassAttribute("tobago-sheet-footer");
      writer.writeAttribute("style", footerStyle, null);


      if (isValidPagingValue(showRowRange)) {
        UICommand pagerCommand = (UICommand) data.getFacet(
            FACET_PAGER_ROW);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(
                  application, PageAction.TO_ROW, false);
          data.getFacets().put(FACET_PAGER_ROW, pagerCommand);
        }
        String pagingOnClick
            = ButtonRenderer.createOnClick(facesContext, pagerCommand);
        pagingOnClick = pagingOnClick.replaceAll("'", "\"");
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        final String className = "tobago-sheet-paging-rows-span"
            + " tobago-sheet-paging-span-" + showRowRange;

        writer.startElement("span", null);
        writer.writeAttribute("onclick", "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')", null);
        writer.writeClassAttribute(className);
        writer.writeAttribute("title", ResourceManagerUtil.getPropertyNotNull(
            facesContext, "tobago", "sheetPagingInfoRowPagingTip"), null);
        writer.write(createSheetPagingInfo(data, facesContext,
            pagerCommandId, true));
        writer.endElement("span");
      }


      if (isValidPagingValue(showDirectLinks)) {
        final String className = "tobago-sheet-paging-links-span"
            + " tobago-sheet-paging-span-" + showDirectLinks;

        writer.startElement("span", null);
        writer.writeClassAttribute(className);
        writer.writeIdAttribute(sheetId + SUBCOMPONENT_SEP + "pagingLinks");
        writeDirectPagingLinks(writer, facesContext, application, data);
        writer.endElement("span");
      }

      if (isValidPagingValue(showPageRange)) {
        UICommand pagerCommand
            = (UICommand) data.getFacet(FACET_PAGER_PAGE);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(
              application, PageAction.TO_PAGE, false);
          data.getFacets().put(FACET_PAGER_PAGE, pagerCommand);
        }
        String pagingOnClick
            = ButtonRenderer.createOnClick(facesContext, pagerCommand);
        pagingOnClick = pagingOnClick.replaceAll("'", "\"");
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        final String className = "tobago-sheet-paging-pages-span"
            + " tobago-sheet-paging-span-" + showPageRange;


        writer.startElement("span", null);
        writer.writeClassAttribute(className);
        writer.writeIdAttribute(sheetId + SUBCOMPONENT_SEP + "pagingPages");
        writer.writeText("", null);

        boolean atBeginning = data.isAtBeginning();
        link(facesContext, application, atBeginning, PageAction.FIRST, data);
        link(facesContext, application, atBeginning, PageAction.PREV, data);
        writer.startElement("span", null);
        writer.writeClassAttribute("tobago-sheet-paging-pages-text");
        writer.writeAttribute("onclick", "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')", null);
        writer.writeAttribute("title", ResourceManagerUtil.getPropertyNotNull(
            facesContext, "tobago", "sheetPagingInfoPagePagingTip"), null);
        writer.write(createSheetPagingInfo(
            data, facesContext, pagerCommandId, false));
        writer.endElement("span");
        boolean atEnd = data.isAtEnd();
        link(facesContext, application, atEnd, PageAction.NEXT, data);
        link(facesContext, application, atEnd||!data.hasRowCount(), PageAction.LAST, data);
      }

      writer.endElement("span");
      writer.endElement("div");
    }
  }

// ----------------------------------------------------------- business methods

  private String createSheetPagingInfo(UIData data,
      FacesContext facesContext, String pagerCommandId, boolean row) {
    String sheetPagingInfo;
    if (data.getRowCount() > 0) {
      Locale locale = facesContext.getViewRoot().getLocale();
      int first;
      int last;
      if (row) {
        first = data.getFirst() + 1;
        last = data.getLast();
      } else { // page
        first = data.getPage();
        last = data.getPages();
      }
      String key;
      if (first != last) {
        key = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
            "sheetPagingInfo" + (row ? "Rows" : "Pages"));
      } else {
        key = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
            "sheetPagingInfoSingle" + (row ? "Row" : "Page"));
      }
      MessageFormat detail = new MessageFormat(key, locale);
      Object[] args = {
          first,
          last,
          data.getRowCount(),
          pagerCommandId + SUBCOMPONENT_SEP + "text"
      };
      sheetPagingInfo = detail.format(args);
    } else {
      sheetPagingInfo =
          ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
              "sheetPagingInfoEmpty" + (row ? "Row" : "Page"));
    }
    return sheetPagingInfo;
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);

    String key = component.getClientId(facesContext) + WIDTHS_POSTFIX;

    Map requestParameterMap = facesContext.getExternalContext()
        .getRequestParameterMap();
    if (requestParameterMap.containsKey(key)) {
      String widths = (String) requestParameterMap.get(key);
      if (widths.trim().length() > 0) {
        component.getAttributes().put(ATTR_WIDTH_LIST_STRING,
            widths);
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
        selectedRows = StringUtil.parseIntegerList(selected);
      } catch (NumberFormatException e) {
        LOG.warn(selected, e);
        selectedRows = Collections.emptyList();
      }

      component.getAttributes().put(
          ATTR_SELECTED_LIST_STRING, selectedRows);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    storeFooterHeight(facesContext, component);
    super.encodeEnd(facesContext, component);
  }


  public boolean needVerticalScrollbar(FacesContext facesContext, UIData data) {
    // estimate need of height-scrollbar on client, if yes we have to consider
    // this when calculating column width's

    final Object forceScroolbar
        = data.getAttributes().get(ATTR_FORCE_VERTICAL_SCROLLBAR);
    if (forceScroolbar != null) {
      if ("true".equals(forceScroolbar)) {
        return true;
      } else if ("false".equals(forceScroolbar)) {
        return false;
      } else if (!"auto".equals(forceScroolbar)) {
        LOG.warn("Illegal value for attibute 'forceVerticalScrollbar' : \""
            + forceScroolbar + "\"");
      }
    }

    String style = (String) data.getAttributes().get(
        ATTR_STYLE);
    String heightString = HtmlRendererUtil.getStyleAttributeValue(style, "height");
    if (heightString != null) {
      int first = data.getFirst();
      int rows = Math.min(data.getRowCount(), first + data.getRows()) - first;
      int heightNeeded = getHeaderHeight(facesContext, data)
          + getFooterHeight(facesContext, data)
          + (rows * (getFixedHeight(facesContext, null)
          + getRowPadding(facesContext, data)));

      int height = Integer.parseInt(heightString.replaceAll("\\D", ""));
      return heightNeeded > height;
    } else {
      return false;
    }
  }

  private int getRowPadding(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "rowPadding");
  }

  public int getScrollbarWidth(FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "scrollbarWidth");
  }

  private void storeFooterHeight(FacesContext facesContext,
      UIComponent component) {
    component.getAttributes().put(ATTR_FOOTER_HEIGHT,
        getFooterHeight(facesContext, component));
  }

  private int getFooterHeight(FacesContext facesContext, UIComponent component) {
    int footerHeight;
    if (isValidPagingAttribute((UIData) component, ATTR_SHOW_ROW_RANGE)
        || isValidPagingAttribute((UIData) component, ATTR_SHOW_PAGE_RANGE)
        || isValidPagingAttribute((UIData) component, ATTR_SHOW_DIRECT_LINKS)) {
      footerHeight =
          getConfiguredValue(facesContext, component, "footerHeight");
    } else {
      footerHeight = 0;
    }
    return footerHeight;
  }

  private boolean isValidPagingAttribute(UIData component, String name) {
    return isValidPagingValue(getPagingAttribute(component, name));
  }

  private String getPagingAttribute(UIData component, String name) {
    String value = ComponentUtil.getStringAttribute(component, name);
    if (isValidPagingValue(value)) {
      return value;
    } else {
      if (!"none".equals(value)) {
        LOG.warn(
            "illegal value in sheet' paging attribute : \"" + value + "\"");
      }
      return "none";
    }
  }

  private boolean isValidPagingValue(String value) {
    return "left".equals(value) || "center".equals(value)
        || "right".equals(value);
  }

  private int getAscendingMarkerWidth(FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "ascendingMarkerWidth");
  }

  public boolean getRendersChildren() {
    return true;
  }

  private List<Integer> getSelectedRows(UIData data, SheetState state) {
    List<Integer> selected = (List<Integer>)
        data.getAttributes().get(ATTR_SELECTED_LIST_STRING);
    if (selected == null && state != null) {
      selected = state.getSelectedRows();
    }
    if (selected == null) {
      selected = Collections.emptyList();
    }
    return selected;
  }

  private static void link(FacesContext facesContext, Application application,
                           boolean disabled, PageAction command, UIData data)
      throws IOException {
    UICommand link= createPagingCommand(application, command, disabled);

    data.getFacets().put(command.getToken(), link);


    String tip = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
        "sheet" + command.getToken());
    String image = ResourceManagerUtil.getImageWithPath(facesContext,
        "image/sheet" + command.getToken() + (disabled ? "Disabled" : "") + ".gif");

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement("img", null);
    writer.writeIdAttribute(data.getClientId(facesContext)
        + SUBCOMPONENT_SEP + "pagingPages" + SUBCOMPONENT_SEP + command.getToken());
    writer.writeClassAttribute("tobago-sheet-footer-pager-button"
        + (disabled ? " tobago-sheet-footer-pager-button-disabled" : ""));
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("title", tip, null);
    if (!disabled) {
      String onClick = ButtonRenderer.createOnClick(facesContext, link);
      writer.writeAttribute("onclick", onClick, null);
    }
    writer.endElement("img");
  }

  private void renderColumnHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIData component,
      int columnCount, UIColumn column, String ascending, String descending,
      String image1x1, int sortMarkerWidth) throws IOException {
    String sheetId = component.getClientId(facesContext);
    Application application = facesContext.getApplication();

    List columnWidths
        = (List) component.getAttributes().get(ATTR_WIDTH_LIST);
    String divWidth = "width: " + columnWidths.get(columnCount) + "px;";


    writer.startElement("div", null);
    writer.writeIdAttribute(sheetId + "_header_box_" + columnCount);
    writer.writeClassAttribute("tobago-sheet-header-box");
    writer.writeAttribute("style", divWidth, null);

// ############################################
// ############################################

    String sorterImage = null;
    String sorterClass = "";
    String sortTitle = "";
    boolean sortable =
        ComponentUtil.getBooleanAttribute(column,
            ATTR_SORTABLE);
    if (sortable && !(column instanceof UIColumnSelector)) {
      UICommand sortCommand = (UICommand) column.getFacet(UIData.FACET_SORTER);
      if (sortCommand == null) {
        String columnId = column.getClientId(facesContext);
        String sorterId = columnId.substring(columnId.lastIndexOf(":") + 1)
            + "_" + UIData.SORTER_ID;
        sortCommand
            = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
        sortCommand.setRendererType(RENDERER_TYPE_LINK);
        sortCommand.setId(sorterId);
        column.getFacets().put(UIData.FACET_SORTER, sortCommand);
      }

      String onclick = "Tobago.submitAction('"
          + sortCommand.getClientId(facesContext) + "')";
      writer.writeAttribute("onclick", onclick, null);

      writer.writeAttribute("title",
          ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
              "sheetTipSorting"),
          null);

      SheetState sheetState = component.getSheetState(facesContext);
      if (column.getId().equals(sheetState.getSortedColumnId())) {
        if (sheetState.isAscending()) {
          sorterImage = ascending;
          sortTitle = ResourceManagerUtil.getPropertyNotNull(facesContext,
              "tobago", "sheetAscending");
        } else {
          sorterImage = descending;
          sortTitle = ResourceManagerUtil.getPropertyNotNull(facesContext,
              "tobago", "sheetDescending");
        }
      }
      sorterClass = " tobago-sheet-header-sortable";
    }

// ############################################
// ############################################

    String align
        = (String) column.getAttributes().get(ATTR_ALIGN);

    writer.startElement("div", null);
    writer.writeIdAttribute(sheetId + "_header_outer_" + columnCount);
    writer.writeClassAttribute("tobago-sheet-header" + sorterClass);
    if (align != null) {
      writer.writeAttribute("style", "text-align: " + align + ";", null);
    }

    String resizerClass;
    if (column instanceof UIColumnSelector) {
      resizerClass = "tobago-sheet-header-resize";
      renderColumnSelectorHeader(facesContext, writer, component, column);
    } else {
      resizerClass =
          "tobago-sheet-header-resize tobago-sheet-header-resize-cursor";
      renderColumnHeaderLabel(facesContext, writer, column, sortMarkerWidth, align,
          image1x1);
    }
    writer.endElement("div");

    writer.startElement("div", null);
    writer.writeIdAttribute(sheetId + "_header_resizer_" + columnCount);
    writer.writeClassAttribute(resizerClass);
    writer.write("&nbsp;");
    writer.endElement("div");

// ############################################
// ############################################
    if (sorterImage != null) {
      writer.startElement("div", null);
      writer.writeClassAttribute("tobago-sheet-header-sort-div");
      writer.writeAttribute("title", sortTitle, null);

      writer.startElement("img", null);
      writer.writeAttribute("src", sorterImage, null);
      writer.writeAttribute("alt", "", null);
      writer.writeAttribute("title", sortTitle, null);
      writer.endElement("img");

      writer.endElement("div");
    }
// ############################################
// ############################################

    writer.endElement("div");
  }


  private void renderColumnSelectorHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIData component, UIColumn column)
      throws IOException {
    UIPanel menu = (UIPanel) column.getFacet(FACET_MENUPOPUP);
    if (menu == null) {
      final Application application = facesContext.getApplication();
      menu = (UIPanel) application.createComponent(UIPanel.COMPONENT_TYPE);
      menu.setId("selectorMenu");
      column.getFacets().put(FACET_MENUPOPUP, menu);
      menu.setRendererType(RENDERER_TYPE_MENUBAR);
      menu.getAttributes().put(ATTR_MENU_POPUP, Boolean.TRUE);
      menu.getAttributes().put(ATTR_MENU_POPUP_TYPE, "SheetSelector");
      menu.getAttributes().put(ATTR_COMMAND_TYPE, "menu");
      menu.getAttributes().put(ATTR_IMAGE, "image/sheetSelectorMenu.gif");

      String sheetId = column.getParent().getClientId(facesContext);
      String action = "tobagoSheetSelectAll('" + sheetId + "')";
      String label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuSelect");
      UICommand menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuSelectAll");
      menu.getChildren().add(menuItem);
      action = "tobagoSheetUnselectAll('" + sheetId + "')";
      label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuUnselect");
      menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuUnselectAll");
      menu.getChildren().add(menuItem);
      action = "tobagoSheetToggleAllSelections('" + sheetId + "')";
      label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuToggleselect");
      menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuToggleSelections");
      menu.getChildren().add(menuItem);
    }

    writer.startElement("div", null);
    writer.writeIdAttribute(column.getClientId(facesContext));
    writer.writeClassAttribute("tobago-sheet-selector-menu");
    writer.endElement("div");
    RenderUtil.encode(facesContext, menu);
  }

  private UICommand createMenuItem(final Application application, String label,
      String action) {
    UICommand menuItem
        = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    menuItem.setRendererType(RENDERER_TYPE_MENUCOMMAND);
    menuItem.getAttributes().put(ATTR_COMMAND_TYPE, MenuCommandTag.COMMAND_TYPE);
    menuItem.getAttributes().put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    menuItem.getAttributes().put(ATTR_ACTION_STRING, action);
    menuItem.getAttributes().put(ATTR_LABEL, label);
    return menuItem;
  }

  private void renderColumnHeaderLabel(FacesContext facesContext,
                                       ResponseWriter writer, UIColumn column,
                                       int sortMarkerWidth, String align,
                                       String image1x1) throws IOException {
    String label
        = (String) column.getAttributes().get(ATTR_LABEL);
    if (label != null) {
      writer.writeText(label, null);
      SheetState sheetState
          = ((UIData) column.getParent()).getSheetState(facesContext);
      if (column.getId().equals(sheetState.getSortedColumnId())
          && "right".equals(align)) {
        writer.startElement("img", null);
        writer.writeAttribute("src", image1x1, null);
        writer.writeAttribute("alt", "", null);
        writer.writeAttribute("width", Integer.toString(sortMarkerWidth), null);
        writer.writeAttribute("height", "1", null);
        writer.endElement("img");
      }
    } else {
      writer.startElement("img", null);
      writer.writeAttribute("src", image1x1, null);
      writer.writeAttribute("alt", "", null);
      writer.endElement("img");
    }
  }

  private void writeDirectPagingLinks(TobagoResponseWriter writer,
      FacesContext facesContext, Application application, UIData data)
      throws IOException {
    UICommand pagerCommand = (UICommand) data.getFacet(FACET_PAGER_PAGE);
    if (pagerCommand == null) {
      pagerCommand = createPagingCommand(
          application, PageAction.TO_PAGE, false);
      data.getFacets().put(FACET_PAGER_PAGE, pagerCommand);
    }
    String pagerCommandId = pagerCommand.getClientId(facesContext);
    String hrefPostfix = "', '" + ButtonRenderer.createOnClick(facesContext,
        pagerCommand).replaceAll("'", "\"") + "');";

    int linkCount = ComponentUtil.getIntAttribute(data, ATTR_DIRECT_LINK_COUNT);
    linkCount--;  // current page needs no link
    ArrayList<Integer> prevs = new ArrayList<Integer>(linkCount);
    int page = data.getPage();
    for (int i = 0; i < linkCount && page > 1; i++) {
      page--;
      if (page > 0) {
        prevs.add(0, page);
      }
    }

    ArrayList<Integer> nexts = new ArrayList<Integer>(linkCount);
    page = data.getPage();
    for (int i = 0; i < linkCount && page < data.getPages(); i++) {
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
    int skip = prevs.size() > 0 ? ((Integer) prevs.get(0)) : 1;
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
      writeLinkElement(writer, name, Integer.toString(skip),
          pagerCommandId, hrefPostfix, true);
    }
    for (Integer prev : prevs) {
      name = prev.toString();
      writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, true);
    }
    name = Integer.toString(data.getPage());
    writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, false);

    for (Integer next : nexts) {
      name = next.toString();
      writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, true);
    }

    skip = nexts.size() > 0
        ? ((Integer) nexts.get(nexts.size() - 1)) : data.getPages();
    if (skip < data.getPages()) {
      skip += linkCount / 2;
      skip++;
      name = "...";
      if (skip > data.getPages()) {
        skip = data.getPages();
        if ((nexts.get(nexts.size() - 1)) == skip - 1) {
          name = Integer.toString(skip);
        }
      }
      writeLinkElement(writer, name, Integer.toString(skip), pagerCommandId,
          hrefPostfix, true);
    }
  }

  private static UICommand createPagingCommand(Application application,
                                               PageAction command, boolean disabled) {
    UICommand link;
    link = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    link.setRendererType(PAGE_RENDERER_TYPE);
    link.setRendered(true);
    link.setId(command.getToken());
//    link.getAttributes().put(ATTR_ACTION_STRING, command);
    link.getAttributes().put(ATTR_INLINE, Boolean.TRUE);
    link.getAttributes().put(ATTR_DISABLED, disabled);
    return link;
  }

  private void writeLinkElement(TobagoResponseWriter writer, String str, String skip,
      String id, String hrefPostfix, boolean makeLink)
      throws IOException {
    String type = makeLink ? "a" : "span";
    writer.startElement(type, null);
    writer.writeClassAttribute("tobago-sheet-paging-links-link");
    if (makeLink) {
      writer.writeIdAttribute(id + SUBCOMPONENT_SEP + "link_" + skip);
      writer.writeAttribute("href", "javascript: tobagoSheetSetPagerPage('"
          + id + "', '" + skip + hrefPostfix, null);
    }
    writer.write(str);
    writer.endElement(type);
  }

  public int getContentBorder(FacesContext facesContext, UIData data) {
    return getConfiguredValue(facesContext, data, "contentBorder");
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component)
      throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIData.class);
    renderSheet(facesContext, (UIData) component);
    facesContext.responseComplete();
  }

}

