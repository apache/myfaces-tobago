/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIColumnSelector;
import com.atanion.tobago.component.UIData;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.model.SheetState;
import com.atanion.tobago.model.SortableByApplication;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutInfo;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.util.BeanComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SheetRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SheetRenderer.class);


  public static final String FIRST = "first";
  public static final String PAGE_TO_ROW = "pageToRow";
  public static final String PAGE_TO_PAGE = "pageToPage";
  public static final String LINK_TO_PAGE = "linkToPage";
  public static final String PREV = "prev";
  public static final String NEXT = "next";
  public static final String LAST = "last";
  public static final String WIDTHS_POSTFIX
      = TobagoConstants.SUBCOMPONENT_SEP + "widths";
  public static final String SELECTED_POSTFIX
      = TobagoConstants.SUBCOMPONENT_SEP + "selected";
  public static final String PAGE_TO_ROW_POSTFIX
      = TobagoConstants.SUBCOMPONENT_SEP + PAGE_TO_ROW;

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code



  private int getAscendingMarkerWidth(FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "ascendingMarkerWidth");
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    setupState(facesContext, (UIData) component);
    storeFooterHeight(facesContext, component);
    super.encodeEnd(facesContext, component);
  }

  private void setupState(FacesContext facesContext, UIData data) {
    SheetState state = data.getSheetState(facesContext);
    ensureColumnWidthList(facesContext, data, state);

  }

  private void storeFooterHeight(FacesContext facesContext,
      UIComponent component) {
    component.getAttributes().put(TobagoConstants.ATTR_FOOTER_HEIGHT,
        new Integer(getFooterHeight(facesContext, component)));
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

  private void ensureColumnWidthList(FacesContext facesContext, UIData data,
      SheetState state) {
    List widthList = null;
    List columns = getColumns(data);

    final Map attributes = data.getAttributes();
    String widthListString = null;

    if (state != null) {
      widthListString = state.getColumnWidths();
    }
    if (widthListString == null) {
      widthListString =
          (String) attributes.get(TobagoConstants.ATTR_WIDTH_LIST_STRING);
    }

    if (widthListString != null) {
      widthList = SheetState.parse(widthListString);
    }
    if (widthList != null && widthList.size() != columns.size()) {
      widthList = null;
    }


    if (widthList == null) {

      String columnLayout =
          (String) attributes.get(TobagoConstants.ATTR_COLUMNS);

      if (columnLayout == null && columns.size() > 0) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.size(); i++) {
          sb.append("1*;");
        }
        columnLayout = sb.deleteCharAt(sb.lastIndexOf(";")).toString();
        if (LOG.isWarnEnabled()) {
          LOG.warn("No columns found! Using created layout tokens: " + columnLayout);
        }
      }

      if (widthList == null) {
        int space = LayoutUtil.getInnerSpace(facesContext, data, true);
        space -= getConfiguredValue(facesContext, data, "contentBorder");
        if (needVerticalScrollbar(facesContext, data)) {
          space -= getScrollbarWidth(facesContext, data);
        }
        LayoutInfo layoutInfo = new LayoutInfo(getColumns(data).size(),
            space, columnLayout);
        layoutInfo.parseColumnLayout(space);
        widthList = layoutInfo.getSpaceList();
      }
    }

    if (widthList != null) {
      if (columns.size() != widthList.size()) {
        LOG.warn("widthList.size() = " + widthList.size() +
            " != columns.size() = " + columns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(widthList));
      } else {
        attributes.put(TobagoConstants.ATTR_WIDTH_LIST, widthList);
      }
    }
  }

  private boolean needVerticalScrollbar(FacesContext facesContext, UIData data) {
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
        TobagoConstants.ATTR_STYLE);
    String heightString = LayoutUtil.getStyleAttributeValue(style, "height");
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


  private int getScrollbarWidth(FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "scrollbarWidth");
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    UIData component = (UIData) uiComponent;
    String image1x1 = ResourceManagerUtil.getImage(facesContext, "image/1x1.gif");
    String ascending = ResourceManagerUtil.getImage(facesContext,
        "image/ascending.gif");
    String descending = ResourceManagerUtil.getImage(facesContext,
        "image/descending.gif");

    String sheetId = component.getClientId(facesContext);
    UIPage uiPage = ComponentUtil.findPage(component);
    uiPage.getScriptFiles().add("script/tobago-sheet.js");
    uiPage.getOnloadScripts().add("initSheet(\"" + sheetId + "\");");
    uiPage.getStyleFiles().add("style/tobago-sheet.css");

    String selectorDisabled
        = ResourceManagerUtil.getImage(facesContext,
            "image/sheetUncheckedDisabled.gif");
    String unchecked
        = ResourceManagerUtil.getImage(facesContext, "image/sheetUnchecked.gif");
    uiPage.getOnloadScripts().add("tobagoSheetSetUncheckedImage(\"" + sheetId +
        "\", \"" +
        unchecked
        + "\")");
    uiPage.getOnloadScripts().add("tobagoSheetSetCheckedImage(\"" + sheetId +
        "\", \""
        + ResourceManagerUtil.getImage(facesContext, "image/sheetChecked.gif") +
        "\")");
    uiPage.getOnloadScripts().add("updateSelectionView(\"" + sheetId + "\")");

    final Map attributes = component.getAttributes();
    String sheetStyle = (String) attributes.get(TobagoConstants.ATTR_STYLE);
    String headerStyle =
        (String) attributes.get(TobagoConstants.ATTR_STYLE_HEADER);
//    String sheetWidthString = LayoutUtil.getStyleAttributeValue(sheetStyle,
//        "width");
    String sheetHeightString = LayoutUtil.getStyleAttributeValue(sheetStyle,
        "height");
    int sheetHeight;
    if (sheetHeightString != null) {
      sheetHeight = Integer.parseInt(sheetHeightString.replaceAll("\\D", ""));
    } else {
      // fixme: nullpointer if height not defined
      LOG.error("no height in parent container, setting to 100");
      sheetHeight = 100;
    }
    String bodyStyle = (String) attributes.get(TobagoConstants.ATTR_STYLE_BODY);
    int footerHeight = ((Integer)
        attributes.get(TobagoConstants.ATTR_FOOTER_HEIGHT)).intValue();


    Application application = facesContext.getApplication();
    SheetState state = component.getSheetState(facesContext);
    Sorter sorter = getSorter(component);
    setStoredState(component, sorter, state);
    Pager pager = new Pager(component);
    List columnWidths = (List) attributes.get(TobagoConstants.ATTR_WIDTH_LIST);

    String selectedListString = getSelected(component, state);
    List selectedList = SheetState.parse(selectedListString);
    List columnList = getColumns(component);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("input", null);
    writer.writeAttribute("id", sheetId + WIDTHS_POSTFIX, null);
    writer.writeAttribute("name", sheetId + WIDTHS_POSTFIX, null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("id", sheetId + SELECTED_POSTFIX, null);
    writer.writeAttribute("name", sheetId + SELECTED_POSTFIX, null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", selectedListString, null);
    writer.endElement("input");

    // Outher sheet div
    writer.startElement("div", null);
    writer.writeAttribute("id", sheetId + "_outer_div", null);
    writer.writeAttribute("class", "tobago-sheet-outer-div", null);
    writer.writeAttribute("style", sheetStyle, null);

    boolean hideHeader = ComponentUtil.getBooleanAttribute(component,
        TobagoConstants.ATTR_HIDE_HEADER);
    if (!hideHeader) {
      // begin rendering header
      writer.startElement("div", null);
      writer.writeAttribute("id", sheetId + "_header_div", null);
      writer.writeAttribute("class", "tobago-sheet-header-div", null);
      writer.writeAttribute("style", headerStyle, null);

//      writer.startElement("table", component);
//      writer.writeAttribute("id", sheetId + "_header_table", null);
//      writer.writeAttribute("cellspacing", "0", null);
//      writer.writeAttribute("cellpadding", "0", null);
//      writer.writeAttribute("summary", "", null);
//      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_HEADER);
//      writer.writeAttribute("class", "tobago-sheet-header-table", null);
//
//
//      writer.startElement("tr", null);
//      writer.startElement("td", null);
//      writer.writeAttribute("style", "white-space: nowrap;", null);


      int columnCount = 0;
      final int sortMarkerWidth = getAscendingMarkerWidth(facesContext,
          component);
      for (Iterator j = columnList.iterator(); j.hasNext(); columnCount++) {
        UIColumn column = (UIColumn) j.next();
        renderColumnHeader(facesContext, writer, component, columnCount, column,
            ascending, descending, image1x1, sortMarkerWidth);

      }
      writer.startElement("div", null);
      writer.writeAttribute("id", sheetId + "_header_box_filler", null);
      writer.writeAttribute("class", "tobago-sheet-header-box", null);
      writer.writeAttribute("style", "width: 0px", null);

      writer.startElement("div", null);
      writer.writeAttribute("class", "tobago-sheet-header", null);
      writer.write("&nbsp;");
      writer.endElement("div");

      writer.endElement("div");
//      writer.endElement("td");
//      writer.endElement("tr");
//      writer.endElement("table");
      writer.endElement("div");
      // end rendering header

    }


// BEGIN RENDER BODY CONTENT
    bodyStyle = LayoutUtil.replaceStyleAttribute(bodyStyle, "height",
        (sheetHeight - footerHeight) + "px");
    String space = LayoutUtil.getStyleAttributeValue(bodyStyle, "width");
    String sheetBodyStyle;
    if (space != null) {
      int intSpace = Integer.parseInt(space.replaceAll("\\D", ""));
      intSpace -=
          ((Integer) columnWidths.get(columnWidths.size() - 1)).intValue();
      sheetBodyStyle =
          LayoutUtil.replaceStyleAttribute(bodyStyle, "width", intSpace + "px");
    } else {
      sheetBodyStyle = bodyStyle;
    }
    sheetBodyStyle = LayoutUtil.removeStyleAttribute(sheetBodyStyle, "height");

    if (hideHeader) {
      bodyStyle += " padding-top: 0px;";
    }

    writer.startElement("div", null);
    writer.writeAttribute("id", sheetId + "_data_div", null);
    writer.writeAttribute("class", "tobago-sheet-body-div ", null);
    writer.writeAttribute("style", bodyStyle, null);

    writer.startElement("table", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeAttribute("class", "tobago-sheet-body-table", null);
    writer.writeAttribute("style", sheetBodyStyle, null);

    if (columnWidths != null) {
      writer.startElement("colgroup", null);
      for (int i = 0; i < columnWidths.size(); i++) {
        writer.startElement("col", null);
        writer.writeAttribute("width", columnWidths.get(i), null);
        writer.endElement("col");
      }
      writer.endElement("colgroup");
    }

    // Print the Content

    if (LOG.isDebugEnabled()) {
      LOG.debug("first = " + component.getFirst() + "   rows = " +
          component.getRows());
    }

    Map requestMap = facesContext.getExternalContext().getRequestMap();
    String var = component.getVar();

    boolean odd = false;
    int visibleIndex = -1;
    int last = component.getFirst() + component.getRows();
    for (int rowIndex = component.getFirst(); rowIndex < last; rowIndex++) {
      visibleIndex++;
      component.setRowIndex(rowIndex);
      if (!component.isRowAvailable()) {
        break;
      }
      odd = !odd;
      String rowClass = odd ?
          "tobago-sheet-content-odd " : "tobago-sheet-content-even ";

      if (LOG.isDebugEnabled()) {
        LOG.debug("var       " + var);
        LOG.debug("list      " + component.getValue());
      }

      Object value;
      if (component.getValue() instanceof Object[]) {
        Object[] valueArray = (Object[]) component.getValue();
        value = valueArray[rowIndex];
      } else {
        List valueList = (List) component.getValue();
        value = valueList.get(rowIndex);
      }

      if (LOG.isDebugEnabled()) {
        LOG.debug("element   " + value);
      }

      requestMap.put(var, value);

      writer.startElement("tr", null);
      writer.writeAttribute("class", rowClass, null);
      writer.writeAttribute("id", sheetId + "_data_tr_" + rowIndex, null);
      writer.writeText("", null);


      int columnIndex = -1;
      for (Iterator kids = getColumns(component).iterator(); kids.hasNext();) {
        UIColumn column = (UIColumn) kids.next();
        columnIndex++;

        String style = "width: " + columnWidths.get(columnIndex) + "px;";
        String tdStyle = "";
        String align = (String) column.getAttributes().get(
            TobagoConstants.ATTR_ALIGN);
        if (align != null) {
          tdStyle = "text-align: " + align;
        }
        String cellClass = (String) column.getAttributes().get(
            TobagoConstants.ATTR_STYLE_CLASS);
        String tdClass = "tobago-sheet-cell-td " +
            (cellClass != null ? cellClass : "");


        writer.startElement("td", column);

        writer.writeAttribute("class", tdClass, null);
        writer.writeAttribute("style", tdStyle, null);

        writer.startElement("div", null);
        writer.writeAttribute("id",
            sheetId + "_data_row_" + visibleIndex + "_column" + columnIndex,
            null);
        writer.writeAttribute("class", "tobago-sheet-cell-outer", null);
        writer.writeAttribute("style", style, null);

        writer.startElement("div", null);
        writer.writeAttribute("class", "tobago-sheet-cell-inner", null);
        writer.writeText("", null);

//        if (columnStyles > 0) {
//          writer.writeAttribute("class", columnClasses[columnStyle++],
//              "columnClasses");
//          if (columnStyle >= columnStyles) {
//            columnStyle = 0;
//          }
//        }

        if (column instanceof UIColumnSelector) {
          boolean disabled
              = ComponentUtil.getBooleanAttribute(column, ATTR_DISABLED);
          writer.startElement("img", null);
          if (disabled) {
            writer.writeAttribute("src", selectorDisabled, null);
          } else {
            writer.writeAttribute("src", unchecked, null);
//            writer.writeAttribute("onclick", "tobagoSheetToggleSelectionState(\""
//                + sheetId + "\", " + rowIndex + ")", null);
            writer.writeAttribute("onclick",
                "tobagoSheetToggleSelectionState(event)", null);
          }
          writer.writeAttribute("id",
              sheetId + "_data_row_selector_" + rowIndex, null);
          writer.writeAttribute("class", "tobago-sheet-column-selector", null);
          writer.endElement("img");
        } else {
          for (Iterator grandkids = getChildren(column).iterator();
              grandkids.hasNext();) {
            UIComponent grandkid = (UIComponent) grandkids.next();
            RenderUtil.encode(facesContext, grandkid);
          }
        }

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("td");
      }

      writer.startElement("td", null);
      writer.writeAttribute("class", "tobago-sheet-cell-td", null);

      writer.startElement("div", null);
      writer.writeAttribute("id",
          sheetId + "_data_row_" + visibleIndex + "_column_filler", null);
      writer.writeAttribute("class", "tobago-sheet-cell-outer", null);
      writer.writeAttribute("style", "width: 0px;", null);

      writer.write("&nbsp;");

      writer.endElement("div");
      writer.endElement("td");

      writer.endElement("tr");
    }

    component.setRowIndex(-1);

    requestMap.remove(var);

    writer.endElement("table");
    writer.endElement("div");

// END RENDER BODY CONTENT


    String showRowRange
        = getPagingAttribute(component, ATTR_SHOW_ROW_RANGE);
    String showPageRange
        = getPagingAttribute(component, ATTR_SHOW_PAGE_RANGE);
    String showDirectLinks
        = getPagingAttribute(component, ATTR_SHOW_DIRECT_LINKS);

    if (isValidPagingValue(showRowRange)
        || isValidPagingValue(showPageRange)
        || isValidPagingValue(showDirectLinks)) {
      String footerStyle = LayoutUtil.replaceStyleAttribute(bodyStyle,
          "height", footerHeight + "px")
          + " top: " + (sheetHeight - footerHeight) + "px;";

      writer.startElement("div", component);
      writer.writeAttribute("class", "tobago-sheet-footer", null);
      writer.writeAttribute("style", footerStyle, null);


      if (isValidPagingValue(showRowRange)) {


        UICommand pagerCommand = (UICommand) component.getFacet(FACET_PAGER_ROW);
        if (pagerCommand == null) {
          pagerCommand
              = createPagingCommand(application, PAGE_TO_ROW, false, pager );
          component.getFacets().put(FACET_PAGER_ROW, pagerCommand);
        }
        String pagingOnClick
            = ButtonRenderer.createOnClick(facesContext, pagerCommand);
        pagingOnClick = pagingOnClick.replaceAll("'", "\"");
        String pagerCommandId = pagerCommand.getClientId(facesContext);

        String className = "tobago-sheet-paging-rows-span"
            + " tobago-sheet-paging-span-" + showRowRange;

        writer.startElement("span", null);
        writer.writeAttribute("onclick", "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')" , null);
        writer.writeAttribute("class", className, null);
        writer.writeAttribute("title", ResourceManagerUtil.getProperty(
            facesContext, "tobago", "sheetPagingInfoRowPagingTip"), null);
        writer.write(createSheetPagingInfo(
            component, facesContext, pagerCommandId, pager, true));
        writer.endElement("span");
      }


      if (isValidPagingValue(showDirectLinks)) {
        String className = "tobago-sheet-paging-links-span"
            + " tobago-sheet-paging-span-" + showDirectLinks;

        writer.startElement("span", null);
        writer.writeAttribute("class", className, null);
        writeDirectPagingLinks(writer, facesContext, application, pager);
        writer.endElement("span");
      }

      if (isValidPagingValue(showPageRange)) {
        UICommand pagerCommand
            = (UICommand) component.getFacet(FACET_PAGER_PAGE);
        if (pagerCommand == null) {
          pagerCommand
              = createPagingCommand(application, PAGE_TO_PAGE, false, pager);
          component.getFacets().put(FACET_PAGER_PAGE, pagerCommand);
        }
        String  pagingOnClick
            = ButtonRenderer.createOnClick(facesContext, pagerCommand);
        pagingOnClick = pagingOnClick.replaceAll("'", "\"");
        String pagerCommandId = pagerCommand.getClientId(facesContext);

        String className = "tobago-sheet-paging-pages-span"
            + " tobago-sheet-paging-span-" + showPageRange;


        writer.startElement("span", null);
        writer.writeAttribute("class", className, null);
        writer.writeText("", null);



        link(facesContext, application, pager, pager.isAtBeginning(), FIRST);
        link(facesContext, application, pager, pager.isAtBeginning(), PREV);
        writer.startElement("span", null);
        writer.writeAttribute("class", "tobago-sheet-paging-pages-text", null);
        writer.writeAttribute("onclick", "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')" , null);
        writer.writeAttribute("title", ResourceManagerUtil.getProperty(
            facesContext, "tobago", "sheetPagingInfoPagePagingTip"), null);
        writer.write(createSheetPagingInfo(
            component, facesContext, pagerCommandId, pager, false));
        writer.endElement("span");
        link(facesContext, application, pager, pager.isAtEnd(), NEXT);
        link(facesContext, application, pager, pager.isAtEnd(), LAST);
      }

      writer.endElement("span");
      writer.endElement("div");
    }

    writer.endElement("div");

  }

  private String getPagingAttribute(UIData component, String name) {
    String value = (String) ComponentUtil.getAttribute(component, name);
    if (isValidPagingValue(value)) {
      return value;
    } else {
      if (! "none".equals(value)) {
        LOG.warn("illegal value in sheet' paging attribute : \"" + value + "\"");
      }
      return "none";
    }
  }

  private boolean isValidPagingValue(String value) {
    return "left".equals(value) || "center".equals(value) || "right".equals(value);
  }

  private boolean isValidPagingAttribute(UIData component, String name) {
    return isValidPagingValue(getPagingAttribute(component, name));
  }

  private void writeDirectPagingLinks(ResponseWriter writer,
    FacesContext facesContext, Application application, Pager pager)
      throws IOException {
    final UIData data = pager.getComponent();
    UICommand pagerCommand = (UICommand) data.getFacet(FACET_PAGER_PAGE);
    if (pagerCommand == null) {
      pagerCommand = createPagingCommand(application, PAGE_TO_PAGE, false, pager);
      data.getFacets().put(FACET_PAGER_PAGE, pagerCommand);
    }
    String pagerCommandId = pagerCommand.getClientId(facesContext);
    String hrefPostfix = "', '" + ButtonRenderer.createOnClick(
        facesContext, pagerCommand).replaceAll("'", "\"") + "');";

    int linkCount = ComponentUtil.getIntAttribute(data, ATTR_DIRECT_LINK_COUNT);
    linkCount --;  // current page needs no link
    ArrayList prevs = new ArrayList(linkCount);
    int page = pager.getPage();
    for (int i = 0; i < linkCount && page > 1; i++) {
      page--;
      if (page > 0) {
        prevs.add(0, new Integer(page));
      }
    }
    LOG.info("hier sind " + prevs.size() + " prevs");

    ArrayList nexts = new ArrayList(linkCount);
    page = pager.getPage();
    for (int i = 0; i < linkCount && page < pager.getPages(); i++) {
      page++;
      if (page > 1) {
        nexts.add(new Integer(page));
      }
    }
    LOG.info("hier sind " + nexts.size() + " nexts");

    if (prevs.size() > (linkCount/2) && nexts.size() > (linkCount - (linkCount/2))) {
      LOG.info("1");
      while (prevs.size() > (linkCount/2)) {
        prevs.remove(0);
      }
      while (nexts.size() > (linkCount - (linkCount/2))) {
        nexts.remove(nexts.size() -1);
      }
    } else if (prevs.size() <= (linkCount/2)) {
      LOG.info("2");
      while (prevs.size() + nexts.size() > linkCount) {
        nexts.remove(nexts.size() -1);
      }
    } else {
      LOG.info("3");
      while (prevs.size() + nexts.size() > linkCount) {
        prevs.remove(0);
      }
    }
    LOG.info("hier sind " + prevs.size() + " prevs");
    LOG.info("hier sind " + nexts.size() + " nexts");

    String name;
    int skip = prevs.size() > 0 ?  ((Integer)prevs.get(0)).intValue() : 1;
    LOG.info("skip ist " + skip );
    if (skip > 1) {
      skip -= (linkCount - (linkCount/2));
      skip--;
      name = "...";
      if (skip < 1) {
        skip = 1;
        if (((Integer)prevs.get(0)).intValue() == 2) {
          name = "1";
        }
      }
      writeLinkElement(writer, name, Integer.toString(skip),
          pagerCommandId, hrefPostfix, true);
    }
    for (Iterator iter = prevs.iterator(); iter.hasNext();) {
      name = ((Integer) iter.next()).toString();
      writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, true);
    }
    name = Integer.toString(pager.getPage());
    writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, false);

    for (Iterator iter = nexts.iterator(); iter.hasNext();) {
      name = ((Integer) iter.next()).toString();
      writeLinkElement(writer, name, name, pagerCommandId, hrefPostfix, true);
    }

    skip = nexts.size() > 0
        ? ((Integer)nexts.get(nexts.size()-1)).intValue() : pager.getPages();
    LOG.info("skip ist " + skip );
    if (skip < pager.getPages()) {
      skip += linkCount / 2;
      skip++;
      name = "...";
      if (skip > pager.getPages()) {
        skip = pager.getPages();
        if (((Integer)nexts.get(nexts.size()-1)).intValue() == skip - 1) {
          name = Integer.toString(skip);
        }
      }
      writeLinkElement(writer, name, Integer.toString(skip), pagerCommandId,
          hrefPostfix, true);
    }
  }

  private void writeLinkElement(ResponseWriter writer, String str, String skip,
                                String id, String hrefPostfix, boolean makeLink)
      throws IOException {
    String type = makeLink ? "a" : "span";
    writer.startElement(type, null);
    writer.writeAttribute("class", "tobago-sheet-paging-links-link", null);
    if (makeLink) {
      writer.writeAttribute("id", id + SUBCOMPONENT_SEP + "link_" + skip, null);
      writer.writeAttribute("href", "javascript: tobagoSheetSetPagerPage('"
          + id + "', '" + skip + hrefPostfix, null);
    }
    writer.write(str);
    writer.endElement(type);
  }

  private String createSheetPagingInfo(UIData component,
      FacesContext facesContext, String pagerCommandId, Pager pager, boolean row) {
    String sheetPagingInfo;
    if (component.getRowCount() > 0) {
      Locale locale = facesContext.getViewRoot().getLocale();
      int first;
      int last;
      if (row) {
        first  = component.getFirst() + 1;
        last = pager.getLast();
      } else { // page
        first = pager.getPage();
        last = pager.getPages();
      }
      String key;
      if (first != last) {
        key = ResourceManagerUtil.getProperty(facesContext, "tobago",
            "sheetPagingInfo" + (row ? "Rows" : "Pages"));
      } else {
        key = ResourceManagerUtil.getProperty(facesContext, "tobago",
            "sheetPagingInfoSingle" + (row ? "Row" : "Page"));
      }
      MessageFormat detail = new MessageFormat(key, locale);
      Object[] args = {
        new Integer(first),
        new Integer(last),
        new Integer(component.getRowCount()),
        pagerCommandId + SUBCOMPONENT_SEP + "text"};
      sheetPagingInfo = detail.format(args);
    } else {
      sheetPagingInfo = ResourceManagerUtil.getProperty(
          facesContext, "tobago", "sheetPagingInfoEmpty" + (row ? "Row" : "Page"));
    }
    return sheetPagingInfo;
  }

  private void renderColumnHeader(FacesContext facesContext,
      ResponseWriter writer, UIData component,
      int columnCount, UIColumn column, String ascending, String descending,
      String image1x1, int sortMarkerWidth) throws IOException {

    String sheetId = component.getClientId(facesContext);
    Application application = facesContext.getApplication();
    Sorter sorter = getSorter(component);

    List columnWidths
        = (List) component.getAttributes().get(TobagoConstants.ATTR_WIDTH_LIST);
    String divWidth = "width: " +
        (((Integer) columnWidths.get(columnCount)).intValue()) +
        "px;";


    writer.startElement("div", null);
    writer.writeAttribute("id", sheetId + "_header_box_" + columnCount, null);
    writer.writeAttribute("class", "tobago-sheet-header-box", null);
    writer.writeAttribute("style", divWidth, null);

// ############################################
// ############################################

    String sorterImage = null;
    String sorterClass = "";
    String sortTitle = "";
    boolean sortable =
        ComponentUtil.getBooleanAttribute(column,
            TobagoConstants.ATTR_SORTABLE);
    if (sortable && !(column instanceof UIColumnSelector)) {
      String sorterId = Sorter.ID_PREFIX + columnCount;
      String onclick = "submitAction('"
          + ComponentUtil.findPage(component).getFormId(facesContext)
          + "','" + component.getClientId(facesContext) + ":" + sorterId +
          "')";
      writer.writeAttribute("onclick", onclick, null);
      UICommand sortCommand = (UICommand)
          application.createComponent(UICommand.COMPONENT_TYPE);
      sortCommand.setActionListener(sorter);
      sortCommand.setId(sorterId);
      component.getFacets().put(sorterId, sortCommand);
      sortCommand.getClientId(facesContext); // this must called here to fix the ClientId

      writer.writeAttribute("title",
          ResourceManagerUtil.getProperty(facesContext, "tobago",
              "sheetTipSorting"),
          null);

      if (sorter.getColumn() == columnCount) {
        if (sorter.isAscending()) {
          sorterImage = ascending;
          sortTitle = ResourceManagerUtil.getProperty(facesContext,
              "tobago", "sheetAscending");
        } else {
          sorterImage = descending;
          sortTitle = ResourceManagerUtil.getProperty(facesContext,
              "tobago", "sheetDescending");
        }
      }
      sorterClass = " tobago-sheet-header-sortable";
    }

// ############################################
// ############################################

    String align
        = (String) column.getAttributes().get(TobagoConstants.ATTR_ALIGN);

    writer.startElement("div", null);
    writer.writeAttribute("id", sheetId + "_header_outer_" + columnCount,
        null);
    writer.writeAttribute("class", "tobago-sheet-header" + sorterClass, null);
    if (align != null) {
      writer.writeAttribute("style", "text-align: " + align + ";", null);
    }

    String resizerClass;
    if (column instanceof UIColumnSelector) {
      resizerClass = "tobago-sheet-header-resize";
      renderColumnSelectorHeader(facesContext, writer, column, columnCount,
          sorter);
    } else {
      resizerClass = "tobago-sheet-header-resize tobago-sheet-header-resize-cursor";
      renderColumnHeaderLabel(writer, column, columnCount, sorter, sortMarkerWidth, align,
          image1x1);
    }
    writer.endElement("div");

    writer.startElement("div", null);
    writer.writeAttribute("id", sheetId + "_header_resizer_" + columnCount,
        null);
    writer.writeAttribute("class", resizerClass, null);
    writer.write("&nbsp;");
    writer.endElement("div");

// ############################################
// ############################################
    if (sorterImage != null) {
      writer.startElement("div", null);
      writer.writeAttribute("class", "tobago-sheet-header-sort-div", null);
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
      ResponseWriter writer, UIColumn column, int columnCount, Sorter sorter)
      throws IOException {
    UIPanel menu = (UIPanel) column.getFacet(FACET_MENUPOPUP);
    if (menu == null) {
      final Application application = facesContext.getApplication();
      menu = (UIPanel) application.createComponent(UIPanel.COMPONENT_TYPE);
      column.getFacets().put(FACET_MENUPOPUP, menu);
      menu.setRendererType(RENDERER_TYPE_MENUBAR);
      menu.getAttributes().put(ATTR_MENU_POPUP, Boolean.TRUE);
      menu.getAttributes().put(ATTR_MENU_POPUP_TYPE, "SheetSelector");
      menu.getAttributes().put(ATTR_MENU_TYPE, "menu");
      menu.getAttributes().put(ATTR_IMAGE, "image/sheetSelectorMenu.gif");

      String sheetId = column.getParent().getClientId(facesContext);
      String action = "tobagoSheetSelectAll('" + sheetId + "')";
      String label = ResourceManagerUtil.getProperty(facesContext, "tobago",
          "sheetMenuSelect");
      menu.getChildren().add(createMenuItem(application, label, action));
      action = "tobagoSheetUnselectAll('" + sheetId + "')";
      label = ResourceManagerUtil.getProperty(facesContext, "tobago",
          "sheetMenuUnselect");
      menu.getChildren().add(createMenuItem(application, label, action));
      action = "tobagoSheetToggleAllSelections('" + sheetId + "')";
      label = ResourceManagerUtil.getProperty(facesContext, "tobago",
          "sheetMenuToggleselect");
      menu.getChildren().add(createMenuItem(application, label, action));

    }

    writer.startElement("div", null);
    writer.writeAttribute("id", column.getClientId(facesContext), null);
    writer.writeAttribute("class", "tobago-sheet-selector-menu", null);
    writer.endElement("div");
    RenderUtil.encode(facesContext, menu);
  }

  private UICommand createMenuItem(final Application application, String label,
      String action) {
    UICommand menuItem
        = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    menuItem.setRendererType(RENDERER_TYPE_MENUCOMMAND);
    menuItem.getAttributes().put(ATTR_MENU_TYPE, "menuItem");
    menuItem.getAttributes().put(ATTR_TYPE, COMMAND_TYPE_SCRIPT);
    menuItem.getAttributes().put(ATTR_ACTION_STRING, action);
    menuItem.getAttributes().put(ATTR_LABEL, label);
    return menuItem;
  }

  private void renderColumnHeaderLabel(ResponseWriter writer, UIColumn column,
      int columnCount, Sorter sorter, int sortMarkerWidth, String align,
      String image1x1) throws IOException {

    String label
        = (String) column.getAttributes().get(TobagoConstants.ATTR_LABEL);
    if (label != null) {
      writer.writeText(label, null);
      if (sorter.getColumn() == columnCount && "right".equals(align)) {
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

  private String getSelected(UIData data, SheetState state) {
    String selected = (String)
        data.getAttributes().get(TobagoConstants.ATTR_SELECTED_LIST_STRING);
    if (selected == null && state != null) {
      selected = state.getSelected();
    }
    return selected != null ? selected : "";
  }

  private void setStoredState(UIData component, Sorter sorter,
      SheetState state) {
    if (state != null) {
      component.setFirst(state.getFirst());
      sorter.setColumn(state.getSortedColumn());
      sorter.setAscending(state.isAscending());
    }
  }

  private List getColumns(UIData sheet) {

    List columns = new ArrayList();
    for (Iterator kids = sheet.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (kid instanceof UIColumn && kid.isRendered()) {
        columns.add(kid);
      }
    }
    return columns;
  }

  private List getChildren(UIColumn column) {

    List columns = new ArrayList();
    for (Iterator kids = column.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (kid.isRendered()) {
        columns.add(kid);
      }
    }
    return columns;
  }

  private static void link(FacesContext facesContext, Application application,
      SheetRenderer.Pager pager, boolean disabled, String command)
      throws IOException {
    UICommand link;
//    UIGraphic image;
    link = createPagingCommand(application, command, disabled, pager);

//    image = (UIGraphic) application.createComponent(UIGraphic.COMPONENT_TYPE);
//    image.setRendererType("Image"); //fixme: use constant ?
//    image.setRendered(true);
//    image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
//    if (disabled) {
//      image.setValue(command + "Disabled.gif");
//    } else {
//      image.setValue(command + ".gif");
//    }
//    image.getAttributes().put(TobagoConstants.ATTR_TITLE,
//        ResourceManagerUtil.getProperty(facesContext, "tobago",
//            "sheet" + command));
//    link.getChildren().add(image);




    pager.getComponent().getFacets().put(command, link);
//    RenderUtil.encode(facesContext, link);


    String tip = ResourceManagerUtil.getProperty(facesContext, "tobago",
        "sheet" + command);
    String image = ResourceManagerUtil.getImage(facesContext,
        "image/" + command + (disabled ? "Disabled" : "") + ".gif");
    String onClick = ButtonRenderer.createOnClick(facesContext, link);

    ResponseWriter writer = facesContext.getResponseWriter();
    writer.startElement("img", null);
    writer.writeAttribute("class", "tobago-sheet-footer-pager-button", null);
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("title", tip, null);
    writer.writeAttribute("onclick", onClick, null);
    writer.endElement("img");

  }

  private static UICommand createPagingCommand(
      Application application, String command, boolean disabled, Pager pager) {
    UICommand link;
    link = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    link.setRendererType(TobagoConstants.RENDERER_TYPE_LINK);
    link.setRendered(true);
    link.setId(command);
    link.getAttributes().put(TobagoConstants.ATTR_ACTION_STRING, command);
    link.getAttributes().put(TobagoConstants.ATTR_INLINE, Boolean.TRUE);
    link.getAttributes().put(TobagoConstants.ATTR_DISABLED,
        Boolean.valueOf(disabled));
    link.setActionListener(pager);
    return link;
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);


    String key = component.getClientId(facesContext) + WIDTHS_POSTFIX;

    Map requestParameterMap = facesContext.getExternalContext()
        .getRequestParameterMap();
    if (requestParameterMap.containsKey(key)) {
      String widths = (String) requestParameterMap.get(key);
      if (widths.trim().length() > 0) {
        component.getAttributes().put(TobagoConstants.ATTR_WIDTH_LIST_STRING,
            widths);
      }
    }

    key = component.getClientId(facesContext) + SELECTED_POSTFIX;
    if (requestParameterMap.containsKey(key)) {
      String selected = (String) requestParameterMap.get(key);
      if (LOG.isDebugEnabled()) {
        LOG.debug("selected = " + selected);
      }
      component.getAttributes().put(TobagoConstants.ATTR_SELECTED_LIST_STRING,
          selected);
    }
  }

  private Sorter getSorter(UIData component) {
    Sorter sorter = (Sorter)
        component.getAttributes().get(TobagoConstants.ATTR_SHEET_SORTER);
    if (sorter == null) {
      sorter = new Sorter(component);
      component.getAttributes().put(TobagoConstants.ATTR_SHEET_SORTER, sorter);
    }
    return sorter;
  }



// ///////////////////////////////////////////// bean getter + setter


  public class Sorter extends MethodBinding {

    public static final String ID_PREFIX = "sorter_";

    private UIData data;

    private int column;
    private boolean ascending;

    private Comparator comparator;

    public Sorter(UIData data) {
      this.data = data;
      column = -1;
      ascending = true;
    }

    public Object invoke(FacesContext facescontext, Object aobj[])
        throws EvaluationException, MethodNotFoundException {

      if (aobj[0] instanceof ActionEvent) {
        UICommand command = (UICommand) ((ActionEvent) aobj[0]).getSource();
        if (LOG.isDebugEnabled()) {
          LOG.debug("sorterId = " + command.getId());
        }

        Object value = data.getValue();
        if (value instanceof DataModel) {
          value = ((DataModel) value).getWrappedData();
        }


        if (value instanceof SortableByApplication
            || value instanceof List
            || value instanceof Object[]) {

          String sortProperty;

          if (command.getId() != null && command.getId().startsWith(ID_PREFIX)) {
            UIColumn uiColumn = null;
            try {
              int actualColumn =
                  Integer.parseInt(
                      command.getId().substring(ID_PREFIX.length()));
              if (actualColumn == column) {
                ascending = !ascending;
              } else {
                ascending = true;
                column = actualColumn;
              }


              List columns = getColumns(data);
              uiColumn = (UIColumn) columns.get(column);
              UIComponent child = getFirstSortableChild(uiColumn.getChildren());
              if (child != null) {
                ValueBinding valueBinding = child.getValueBinding("value");
                String expressionString = valueBinding.getExpressionString();
                if (expressionString.startsWith("#{") &&
                    expressionString.endsWith("}")) {
                  expressionString =
                      expressionString.substring(2,
                          expressionString.length() - 1);
                }
                String var = data.getVar();
                sortProperty = expressionString.substring(var.length() + 1);
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Sort property is " + sortProperty);
                }
              } else {
                LOG.error("No sortable component found!");
                removeSortableAttribute(uiColumn);
                return null;
              }
            } catch (Exception e) {
              LOG.error("Error while extracting sortMethod :" + e.getMessage(),
                  e);
              if (uiColumn != null) {
                removeSortableAttribute(uiColumn);
              }
              return null;
            }
          } else {
            LOG.error(
                "Sorter.invoke() with illegal id in ActionEvent's source");
            return null;
          }

          if (value instanceof SortableByApplication) {
            ((SortableByApplication) value).sortBy(sortProperty);
          } else {

            // todo: locale / comparator parameter?
            // don't compare numbers with Collator.getInstance() comparator
//          comparator = Collator.getInstance();
            comparator = null;
            Comparator beanComparator
                = new BeanComparator(sortProperty, comparator, !ascending);
//          comparator = new RowComparator(ascending, method);

            if (value instanceof List) {
              Collections.sort((List) value, beanComparator);
            } else { // if (value instanceof Object[]) {
              Arrays.sort((Object[]) value, beanComparator);
            }
          }
          data.updateSheetState(facescontext);
        } else {  // DataModel?, ResultSet, Result or Object
          LOG.warn("Sorting not supported for type "
              + (value != null ? value.getClass().toString() : "null"));
        }
      }
      return null;
    }

    private void removeSortableAttribute(UIColumn uiColumn) {
      LOG.warn("removing attribute sortable from column " + column);
      uiColumn.getAttributes().remove(TobagoConstants.ATTR_SORTABLE);
    }

    private UIComponent getFirstSortableChild(List children) {
      UIComponent child = null;

      for (Iterator iter = children.iterator(); iter.hasNext();) {
        child = (UIComponent) iter.next();
        if (child instanceof UICommand
            || child instanceof UIPanel) {
          child = getFirstSortableChild(child.getChildren());
        }
        if (child instanceof UISelectMany
            || child instanceof UISelectOne
            || child instanceof UISelectBoolean) {
          continue;
        } else if (child instanceof UIInput &&
            RENDERER_TYPE_HIDDEN.equals(child.getRendererType())) {
          continue;
        } else if (child instanceof UIOutput) {
          break;
        }
      }
      return child;
    }

    public Class getType(FacesContext facescontext)
        throws MethodNotFoundException {
      return String.class;
    }

    public int getColumn() {
      return column;
    }

    public boolean isAscending() {
      return ascending;
    }

    public void setColumn(int column) {
      this.column = column;
    }

    public void setAscending(boolean ascending) {
      this.ascending = ascending;
    }
  }

  public class Pager extends MethodBinding {

    private UIData data;

    public Pager(UIData data) {
      this.data = data;
    }

    public Object invoke(FacesContext facesContext, Object aobj[])
        throws EvaluationException, MethodNotFoundException {

      if (aobj[0] instanceof ActionEvent) {
        UICommand command = (UICommand) ((ActionEvent) aobj[0]).getSource();
        String action = (String)
            command.getAttributes().get(TobagoConstants.ATTR_ACTION_STRING);

        if (LOG.isDebugEnabled()) {
          LOG.debug("action = '" + action + "'");
        }

        if (FIRST.equals(action)) {
          data.setFirst(0);
        } else if (PREV.equals(action)) {
          int start = data.getFirst() - data.getRows();
          data.setFirst(start < 0 ? 0 : start);
        } else if (NEXT.equals(action)) {
          int start = data.getFirst() + data.getRows();
          int last = getLastPageIndex();
          data.setFirst(start > data.getRowCount() ? last : start);
        } else if (LAST.equals(action)) {
          data.setFirst(getLastPageIndex());
        } else if (PAGE_TO_ROW.equals(action) ) {
           String startRow = (String)
               facesContext.getExternalContext().getRequestParameterMap().get(
               command.getClientId(facesContext) + SUBCOMPONENT_SEP + "value");
          if (startRow != null) {
            try {
              int start = Integer.parseInt(startRow) - 1;
              data.setFirst(start < getLastPageIndex() ? start : getLastPageIndex());
            } catch (NumberFormatException e) {
              LOG.error("Catched: " + e.getMessage());
            }
          }
          else {
            LOG.error("Can't find 'PageToRow' parameter : " + command.getClientId(facesContext) + SUBCOMPONENT_SEP + "value");
          }
        } else if (PAGE_TO_PAGE.equals(action) ) {
           String startRow = (String)
               facesContext.getExternalContext().getRequestParameterMap().get(
               command.getClientId(facesContext) + SUBCOMPONENT_SEP + "value");
          if (startRow != null) {
            try {
              int start = Integer.parseInt(startRow) - 1;
              LOG.info("start = " + start + "  data.getRows() = " + data.getRows() + " => start = " + (start * data.getRows()));
              start = start * data.getRows();
              data.setFirst(start < getLastPageIndex() ? start : getLastPageIndex());

            } catch (NumberFormatException e) {
              LOG.error("Catched: " + e.getMessage());
            }
          }
          else {
            LOG.error("Can't find 'PageToRow' parameter : " + command.getClientId(facesContext) + SUBCOMPONENT_SEP + "value");
          }
        } else {
          LOG.error("Unkown commandName :" + action);
        }

      }
      else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("aobj[0] instanceof '" + aobj[0] + "'");
        }
      }

      data.updateSheetState(facesContext);


      return null;
    }

    private int getLastPageIndex() {
      int tail = data.getRowCount() % data.getRows();
      return data.getRowCount() - (tail != 0 ? tail : data.getRows());
    }


    public int getLast() {
      int last = data.getFirst() + data.getRows();
      return last < data.getRowCount() ? last : data.getRowCount();
    }

    public int getPage() {
        int first = data.getFirst() + 1;
        int rows = data.getRows();
        if ((first % rows) == 1) {
          return (first / rows) + 1;
        } else {
          return (first / rows) + 2;
        }
    }

    public int getPages() {
      return (data.getRowCount() / data.getRows()) + 1;
    }

    public Class getType(FacesContext facescontext)
        throws MethodNotFoundException {
      return String.class;
    }

    public boolean isAtBeginning() {
      return data.getFirst() == 0;
    }

    public boolean isAtEnd() {
      return data.getFirst() >= getLastPageIndex();
    }

    public UIData getComponent() {
      return data;
    }
  }


}

