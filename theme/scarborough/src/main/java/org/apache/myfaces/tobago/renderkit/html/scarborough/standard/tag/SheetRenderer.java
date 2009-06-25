package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIColumnEvent;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIData;
import static org.apache.myfaces.tobago.component.UIData.NONE;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.layout.FixedLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.util.StringUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SheetRenderer extends LayoutableRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(SheetRenderer.class);

  private static final String[] SCRIPTS = new String[]{"script/tobago-sheet.js"};

  public static final String WIDTHS_POSTFIX = SUBCOMPONENT_SEP + "widths";
  public static final String SCROLL_POSTFIX = SUBCOMPONENT_SEP + "scrollPosition";
  public static final String SELECTED_POSTFIX = SUBCOMPONENT_SEP + "selected";

  private static final Integer HEIGHT_0 = 0;

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getScriptFiles().add(SCRIPTS[0]);
    } 
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    storeFooterHeight(facesContext, uiComponent);
    UIData data = (UIData) uiComponent;

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, data);

    final String sheetId = data.getClientId(facesContext);
    HtmlStyleMap sheetStyle = (HtmlStyleMap) data.getAttributes().get(Attributes.STYLE);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    // Outher sheet div
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_outer_div");
    writer.writeClassAttribute("tobago-sheet-outer-div");
    writer.writeStyleAttribute(sheetStyle);
    UICommand clickAction = null;
    UICommand dblClickAction = null;
    int columnSelectorIndex = -1;
    int i = 0;
    for (UIComponent child : (List<UIComponent>) data.getChildren()) {
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

    renderSheet(facesContext, data, (clickAction != null || dblClickAction != null));

    writer.endElement(HtmlConstants.DIV);

    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();

    String unchecked = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");
    String checked = contextPath + resourceManager.getImage(viewRoot, "image/sheetChecked.gif");
    boolean ajaxEnabled = TobagoConfig.getInstance(facesContext).isAjaxEnabled();

    Integer frequency = null;
    UIComponent facetReload = data.getFacet(Facets.RELOAD);
    if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
      UIReload update = (UIReload) facetReload;
      frequency = update.getFrequency();
    }
    final String[] cmds = {
        "new Tobago.Sheet(\"" + sheetId + "\", " + ajaxEnabled
            + ", \"" + checked + "\", \"" + unchecked + "\", \"" + data.getSelectable()
            + "\", " + columnSelectorIndex + ", "+ frequency
            + ",  " + (clickAction!=null?HtmlRendererUtil.getJavascriptString(clickAction.getId()):null)
            + ",  " + HtmlRendererUtil.getRenderedPartiallyJavascriptArray(facesContext, clickAction)
            + ",  " + (dblClickAction!=null?HtmlRendererUtil.getJavascriptString(dblClickAction.getId()):null)
            + ",  " + HtmlRendererUtil.getRenderedPartiallyJavascriptArray(facesContext, dblClickAction)
            + ");"
    };

    HtmlRendererUtil.writeScriptLoader(facesContext, SCRIPTS, cmds);
  }

  private void renderSheet(FacesContext facesContext, UIData data, boolean hasClickAction) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String sheetId = data.getClientId(facesContext);

    String image1x1 = contextPath + resourceManager.getImage(viewRoot, "image/1x1.gif");
    String selectorDisabled = contextPath + resourceManager.getImage(viewRoot, "image/sheetUncheckedDisabled.gif");
    String unchecked = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");

    Map attributes = data.getAttributes();
    HtmlStyleMap sheetStyle = (HtmlStyleMap) attributes.get(Attributes.STYLE);
    //HtmlStyleMap headerStyle = (HtmlStyleMap) attributes.get(STYLE_HEADER);
//    String sheetWidthString = LayoutUtil.getStyleAttributeValue(sheetStyle,
//        "width");
    Integer sheetHeight = HtmlRendererUtil.getStyleAttributeIntValue(sheetStyle, "height");
    if (sheetHeight == null) {
      // FIXME: nullpointer if height not defined
      LOG.error("no height in parent container, setting to 100");
      sheetHeight = 100;
    }
    //HtmlStyleMap bodyStyle = (HtmlStyleMap) attributes.get(STYLE_BODY);
    int footerHeight = (Integer) attributes.get(Attributes.FOOTER_HEIGHT);

    String selectable = data.getSelectable();

    Application application = facesContext.getApplication();
    SheetState state = data.getSheetState(facesContext);
    List<Integer> columnWidths = data.getWidthList();

    String selectedRows = StringUtils.joinWithSurroundingSeparator(getSelectedRows(data, state));
    List<UIColumn> renderedColumnList = data.getRenderedColumns();


    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeIdAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeNameAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.joinWithSurroundingSeparator(columnWidths), false);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeIdAttribute(sheetId + SCROLL_POSTFIX);
    writer.writeNameAttribute(sheetId + SCROLL_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    Integer[] scrollPosition = data.getScrollPosition();
    if (scrollPosition != null) {
      String scroll = scrollPosition[0] + ";" + scrollPosition[1];
      writer.writeAttribute(HtmlAttributes.VALUE, scroll, false);
    } else {
      writer.writeAttribute(HtmlAttributes.VALUE, "", false);
    }
    writer.endElement(HtmlConstants.INPUT);

    if (!NONE.equals(selectable)) {
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeIdAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeNameAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeAttribute(HtmlAttributes.VALUE, selectedRows, true);
      writer.endElement(HtmlConstants.INPUT);
    }


    final boolean showHeader = data.isShowHeader();
    if (showHeader) {
      // begin rendering header
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(sheetId + "_header_div");
      writer.writeClassAttribute("tobago-sheet-header-div");
      HtmlStyleMap headerStyle = (HtmlStyleMap) attributes.get(Attributes.STYLE_HEADER);
      if (headerStyle != null) {
        writer.writeStyleAttribute(headerStyle);
      }

      int columnCount = 0;
      final int sortMarkerWidth = getAscendingMarkerWidth(facesContext, data);
      String imageAscending = contextPath + resourceManager.getImage(viewRoot, "image/ascending.gif");
      String imageDescending = contextPath + resourceManager.getImage(viewRoot, "image/descending.gif");
      String img = resourceManager.getImage(viewRoot, "image/unsorted.gif", true);
      String imageUnsorted = image1x1;
      if (img != null) {
        imageUnsorted = contextPath + img;
      }
      for (UIColumn column : renderedColumnList) {
        renderColumnHeader(facesContext, writer, data, columnCount, column,
            imageAscending, imageDescending, imageUnsorted, image1x1, sortMarkerWidth);
        columnCount++;
      }
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(sheetId + "_header_box_filler");
      writer.writeClassAttribute("tobago-sheet-header-box");
      writer.writeAttribute(HtmlAttributes.STYLE, "width: 0px", false);

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute("tobago-sheet-header");
      writer.flush();
      writer.write("&nbsp;");
      writer.endElement(HtmlConstants.DIV);

      writer.endElement(HtmlConstants.DIV);
      writer.endElement(HtmlConstants.DIV);
      // end rendering header
    }

// BEGIN RENDER BODY CONTENT
    HtmlStyleMap bodyStyle = (HtmlStyleMap) attributes.get(Attributes.STYLE_BODY);
    HtmlRendererUtil.replaceStyleAttribute(data, Attributes.STYLE_BODY, "height", (sheetHeight - footerHeight));
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_data_div");
    writer.writeClassAttribute("tobago-sheet-body-div ");
    writer.writeAttribute(HtmlAttributes.STYLE, bodyStyle.toString() + (showHeader?"":" padding-top: 0px;"), false);
    Integer space = HtmlRendererUtil.getStyleAttributeIntValue(bodyStyle, "width");
    HtmlStyleMap sheetBodyStyle = (HtmlStyleMap) bodyStyle.clone();
    //String sheetBodyStyle;
    if (space != null) {
//      intSpace -= columnWidths.get(columnWidths.size() - 1);
      space -= getContentBorder(facesContext, data);
      if (needVerticalScrollbar(facesContext, data)) {
        space -= getScrollbarWidth(facesContext, data);
      }
      sheetBodyStyle.put("width", space);
    }
    sheetBodyStyle.remove("height");



    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeClassAttribute("tobago-sheet-body-table");
    writer.writeStyleAttribute(sheetBodyStyle);

    if (columnWidths != null) {
      writer.startElement(HtmlConstants.COLGROUP, null);
      for (Integer columnWidth : columnWidths) {
        writer.startElement(HtmlConstants.COL, null);
        writer.writeAttribute(HtmlAttributes.WIDTH, columnWidth);
        writer.endElement(HtmlConstants.COL);
      }
      writer.endElement(HtmlConstants.COLGROUP);
    }

    // Print the Content

    if (LOG.isDebugEnabled()) {
      LOG.debug("first = " + data.getFirst() + "   rows = " + data.getRows());
    }

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

      writer.startElement(HtmlConstants.TR, null);
      writer.writeClassAttribute(rowClass);
      writer.writeIdAttribute(sheetId + "_data_tr_" + rowIndex);
      writer.flush();


      int columnIndex = -1;
      for (UIColumn column : renderedColumnList) {
        columnIndex++;

        StyleClasses tdClass = new StyleClasses();
        tdClass.addClass("sheet", "cell-td"); // XXX not a standard compliant name
        tdClass.addMarkupClass(column, "column");
        if (columnIndex == 0) {
          tdClass.addClass("sheet", "cell-first-column"); // XXX not a standard compliant name
        }
        if (hasClickAction) {
          tdClass.addClass("sheet", "cell-clickable");
        }
        StyleClasses cellClass = StyleClasses.ensureStyleClasses(column);
        tdClass.addClasses(cellClass);

        writer.startElement(HtmlConstants.TD, column);

        writer.writeClassAttribute(tdClass.toString());
        final String align = (String) column.getAttributes().get(Attributes.ALIGN);
        if (align != null) {
          writer.writeStyleAttribute(HtmlRendererUtil.toStyleString("text-align", align));
        }
        writer.startElement(HtmlConstants.DIV, null);
        writer.writeIdAttribute(
            sheetId + "_data_row_" + visibleIndex + "_column" + columnIndex);
        writer.writeClassAttribute("tobago-sheet-cell-outer");
        writer.writeStyleAttribute(HtmlRendererUtil.toStyleString("width", columnWidths.get(columnIndex)));

        writer.startElement(HtmlConstants.DIV, null);
        writer.writeClassAttribute("tobago-sheet-cell-inner");
        writer.flush();

        if (column instanceof UIColumnSelector) {
          final boolean disabled
              = ComponentUtil.getBooleanAttribute(column, Attributes.DISABLED);
          writer.startElement(HtmlConstants.IMG, null);
          if (disabled) {
            writer.writeAttribute(HtmlAttributes.SRC, selectorDisabled, false);
          } else {
            writer.writeAttribute(HtmlAttributes.SRC, unchecked, false);
          }
          writer.writeIdAttribute(sheetId + "_data_row_selector_" + rowIndex);
          writer.writeClassAttribute("tobago-sheet-column-selector");
          writer.endElement(HtmlConstants.IMG);
        } else {
          List<UIComponent> childs = data.getRenderedChildrenOf(column);
          for (UIComponent grandkid : childs) {
            // set height to 0 to prevent use of layoutheight from parent
            grandkid.getAttributes().put(Attributes.LAYOUT_HEIGHT, HEIGHT_0);
            RenderUtil.prepareRendererAll(facesContext, grandkid);
            RenderUtil.encode(facesContext, grandkid);
          }
          if (childs.size() > 1) {
            if (LOG.isInfoEnabled()) {
              LOG.info(
                  "Column should not contain more than one child. Please surround the components with a tc:panel.");
            }
          }
        }

        writer.endElement(HtmlConstants.DIV);
        writer.endElement(HtmlConstants.DIV);
        writer.endElement(HtmlConstants.TD);
      }

      writer.startElement(HtmlConstants.TD, null);
      writer.writeClassAttribute("tobago-sheet-cell-td");

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(
          sheetId + "_data_row_" + visibleIndex + "_column_filler");
      writer.writeClassAttribute("tobago-sheet-cell-outer");
      writer.writeStyleAttribute("width: 0px;");
      writer.flush();
      writer.write("&nbsp;");

      writer.endElement(HtmlConstants.DIV);
      writer.endElement(HtmlConstants.TD);

      writer.endElement(HtmlConstants.TR);
    }

    data.setRowIndex(-1);


    writer.endElement(HtmlConstants.TABLE);
    writer.endElement(HtmlConstants.DIV);

// END RENDER BODY CONTENT


    final String showRowRange
        = getPagingAttribute(data, Attributes.SHOW_ROW_RANGE);
    final String showPageRange
        = getPagingAttribute(data, Attributes.SHOW_PAGE_RANGE);
    final String showDirectLinks
        = getPagingAttribute(data, Attributes.SHOW_DIRECT_LINKS);

    if (isValidPagingValue(showRowRange)
        || isValidPagingValue(showPageRange)
        || isValidPagingValue(showDirectLinks)) {
      final HtmlStyleMap footerStyle = (HtmlStyleMap) bodyStyle.clone();
      footerStyle.put("height", footerHeight);
      footerStyle.put("top", (sheetHeight - footerHeight));

        //  "height", MessageFormat.format("{0}px", footerHeight));
        //  + " top: " + (sheetHeight - footerHeight) + "px;";

      writer.startElement(HtmlConstants.DIV, data);
      writer.writeClassAttribute("tobago-sheet-footer");
      writer.writeStyleAttribute(footerStyle);


      if (isValidPagingValue(showRowRange)) {
        UICommand pagerCommand = (UICommand) data.getFacet(Facets.PAGER_ROW);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(application, PageAction.TO_ROW, false);
          data.getFacets().put(Facets.PAGER_ROW, pagerCommand);
        }
        String pagingOnClick = new CommandRendererHelper(facesContext, pagerCommand).getOnclickDoubleQuoted();
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        final String className = "tobago-sheet-paging-rows-span"
            + " tobago-sheet-paging-span-" + showRowRange;

        writer.startElement(HtmlConstants.SPAN, null);
        writer.writeAttribute(HtmlAttributes.ONCLICK, "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')", true);
        writer.writeClassAttribute(className);
        writer.writeAttribute(HtmlAttributes.TITLE, ResourceManagerUtil.getPropertyNotNull(
            facesContext, "tobago", "sheetPagingInfoRowPagingTip"), true);
        writer.writeText("");
        writer.write(createSheetPagingInfo(data, facesContext,
            pagerCommandId, true));
        writer.endElement(HtmlConstants.SPAN);
      }


      if (isValidPagingValue(showDirectLinks)) {
        final String className = "tobago-sheet-paging-links-span"
            + " tobago-sheet-paging-span-" + showDirectLinks;

        writer.startElement(HtmlConstants.SPAN, null);
        writer.writeClassAttribute(className);
        writer.writeIdAttribute(sheetId + SUBCOMPONENT_SEP + "pagingLinks");
        writeDirectPagingLinks(writer, facesContext, application, data);
        writer.endElement(HtmlConstants.SPAN);
      }

      if (isValidPagingValue(showPageRange)) {
        UICommand pagerCommand
            = (UICommand) data.getFacet(Facets.PAGER_PAGE);
        if (pagerCommand == null) {
          pagerCommand = createPagingCommand(
              application, PageAction.TO_PAGE, false);
          data.getFacets().put(Facets.PAGER_PAGE, pagerCommand);
        }
        String pagingOnClick = new CommandRendererHelper(facesContext, pagerCommand).getOnclickDoubleQuoted();
        final String pagerCommandId = pagerCommand.getClientId(facesContext);

        final String className = "tobago-sheet-paging-pages-span"
            + " tobago-sheet-paging-span-" + showPageRange;


        writer.startElement(HtmlConstants.SPAN, null);
        writer.writeClassAttribute(className);
        writer.writeIdAttribute(sheetId + SUBCOMPONENT_SEP + "pagingPages");
        writer.writeText("");

        boolean atBeginning = data.isAtBeginning();
        link(facesContext, application, atBeginning, PageAction.FIRST, data);
        link(facesContext, application, atBeginning, PageAction.PREV, data);
        writer.startElement(HtmlConstants.SPAN, null);
        writer.writeClassAttribute("tobago-sheet-paging-pages-text");
        writer.writeAttribute(HtmlAttributes.ONCLICK, "tobagoSheetEditPagingRow(this, '"
            + pagerCommandId + "', '" + pagingOnClick + "')", true);
        writer.writeAttribute(HtmlAttributes.TITLE, ResourceManagerUtil.getPropertyNotNull(
            facesContext, "tobago", "sheetPagingInfoPagePagingTip"), true);
        writer.writeText("");
        writer.write(createSheetPagingInfo(
            data, facesContext, pagerCommandId, false));
        writer.endElement(HtmlConstants.SPAN);
        boolean atEnd = data.isAtEnd();
        link(facesContext, application, atEnd, PageAction.NEXT, data);
        link(facesContext, application, atEnd||!data.hasRowCount(), PageAction.LAST, data);
        writer.endElement(HtmlConstants.SPAN);
      }

      writer.endElement(HtmlConstants.DIV);
    }
  }

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
        component.getAttributes().put(Attributes.WIDTH_LIST_STRING,
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
        selectedRows = StringUtils.parseIntegerList(selected);
      } catch (NumberFormatException e) {
        LOG.warn(selected, e);
        selectedRows = Collections.emptyList();
      }

      component.getAttributes().put(
          Attributes.SELECTED_LIST_STRING, selectedRows);
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

  public boolean needVerticalScrollbar(FacesContext facesContext, UIData data) {
    // estimate need of height-scrollbar on client, if yes we have to consider
    // this when calculating column width's

    final Object forceScroolbar
        = data.getAttributes().get(Attributes.FORCE_VERTICAL_SCROLLBAR);
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

    HtmlStyleMap style = (HtmlStyleMap) data.getAttributes().get(Attributes.STYLE);
    Integer height = HtmlRendererUtil.getStyleAttributeIntValue(style, "height");
    if (height != null) {
      int first = data.getFirst();
      int rows = Math.min(data.getRowCount(), first + data.getRows()) - first;
      int heightNeeded = getHeaderHeight(facesContext, data)
          + getFooterHeight(facesContext, data)
          + (rows * (getFixedHeight(facesContext, null)
          + getRowPadding(facesContext, data)));

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
    component.getAttributes().put(Attributes.FOOTER_HEIGHT,
        getFooterHeight(facesContext, component));
  }

  private int getFooterHeight(FacesContext facesContext, UIComponent component) {
    int footerHeight;
    if (isValidPagingAttribute((UIData) component, Attributes.SHOW_ROW_RANGE)
        || isValidPagingAttribute((UIData) component, Attributes.SHOW_PAGE_RANGE)
        || isValidPagingAttribute((UIData) component, Attributes.SHOW_DIRECT_LINKS)) {
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
                           boolean disabled, PageAction command, UIData data)
      throws IOException {
    UICommand link= createPagingCommand(application, command, disabled);

    data.getFacets().put(command.getToken(), link);


    String tip = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
        "sheet" + command.getToken());
    String image = ResourceManagerUtil.getImageWithPath(facesContext,
        "image/sheet" + command.getToken() + (disabled ? "Disabled" : "") + ".gif");

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeIdAttribute(data.getClientId(facesContext)
        + SUBCOMPONENT_SEP + "pagingPages" + SUBCOMPONENT_SEP + command.getToken());
    writer.writeClassAttribute("tobago-sheet-footer-pager-button"
        + (disabled ? " tobago-sheet-footer-pager-button-disabled" : ""));
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    if (!disabled) {
      CommandRendererHelper helper = new CommandRendererHelper(facesContext, link);
      String onClick = helper.getOnclick();
      writer.writeAttribute(HtmlAttributes.ONCLICK, onClick, true);
    }
    writer.endElement(HtmlConstants.IMG);
  }

  private void renderColumnHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIData component,
      int columnIndex, UIColumn column, String imageAscending, String imageDescending, String imageUnsorted,
      String image1x1, int sortMarkerWidth) throws IOException {
    String sheetId = component.getClientId(facesContext);
    Application application = facesContext.getApplication();

    List columnWidths = (List) component.getWidthList();
    String divWidth = "width: " + columnWidths.get(columnIndex) + "px;";

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_header_box_" + columnIndex);
    writer.writeClassAttribute("tobago-sheet-header-box");
    writer.writeAttribute(HtmlAttributes.STYLE, divWidth, false);
    String tip = (String) column.getAttributes().get(Attributes.TIP);
    if (tip == null) {
      tip = "";
    }

// ############################################
// ############################################

    String sorterImage = null;
    String sorterClass = "";
    String sortTitle = null;
    boolean sortable = ComponentUtil.getBooleanAttribute(column, Attributes.SORTABLE);
    if (sortable && !(column instanceof UIColumnSelector)) {
      UICommand sortCommand = (UICommand) column.getFacet(Facets.SORTER);
      if (sortCommand == null) {
        String columnId = column.getClientId(facesContext);
        String sorterId = columnId.substring(columnId.lastIndexOf(":") + 1) + "_" + UIData.SORTER_ID;
        sortCommand = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
        sortCommand.setRendererType(RendererTypes.LINK);
        sortCommand.setId(sorterId);
        column.getFacets().put(Facets.SORTER, sortCommand);
      }

      String onclick = "Tobago.submitAction(this, '" + sortCommand.getClientId(facesContext) + "')";
      writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, false);

      if (org.apache.commons.lang.StringUtils.isNotEmpty(tip)) {
        tip +=  " - ";
      }
      tip += ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago", "sheetTipSorting");

      SheetState sheetState = component.getSheetState(facesContext);
      if (column.getId().equals(sheetState.getSortedColumnId())) {
        if (sheetState.isAscending()) {
          sorterImage = imageAscending;
          sortTitle = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago", "sheetAscending");
        } else {
          sorterImage = imageDescending;
          sortTitle = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago", "sheetDescending");
        }
      }
      sorterClass = " tobago-sheet-header-sortable";
    }

// ############################################
// ############################################

    writer.writeAttribute(HtmlAttributes.TITLE, tip, true);

    String align = (String) column.getAttributes().get(Attributes.ALIGN);

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_header_outer_" + columnIndex);
    //if (columnIndex == 0) {
    //  writer.writeClassAttribute("tobago-sheet-header"+ sorterClass + " tobago-sheet-header-first-column");
    //} else {
      writer.writeClassAttribute("tobago-sheet-header" + sorterClass);
    //}
    if (align != null) {
      writer.writeStyleAttribute("text-align: " + align + ";");
    }

    String resizerClass;
    if (column instanceof UIColumnSelector) {
      resizerClass = "tobago-sheet-header-resize";
      renderColumnSelectorHeader(facesContext, writer, component, column);
    } else {
      resizerClass = "tobago-sheet-header-resize tobago-sheet-header-resize-cursor";
      renderColumnHeaderLabel(facesContext, writer, column, sortMarkerWidth, align, image1x1);
    }
    writer.endElement(HtmlConstants.DIV);

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_header_resizer_" + columnIndex);
    writer.writeClassAttribute(resizerClass);
    writer.flush();
    writer.write("&nbsp;");
    writer.endElement(HtmlConstants.DIV);
// ############################################
// ############################################
    if (sortable && !(column instanceof UIColumnSelector)) {
      if (sorterImage == null && imageUnsorted != null) {
        sorterImage = imageUnsorted;
      }
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute("tobago-sheet-header-sort-div");
      if (sortTitle != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, sortTitle, true);
      }
      if (sorterImage != null) {
        writer.startElement(HtmlConstants.IMG, null);
        writer.writeAttribute(HtmlAttributes.SRC, sorterImage, false);
        writer.writeAttribute(HtmlAttributes.ALT, "", false);
        if (sortTitle != null) {
          writer.writeAttribute(HtmlAttributes.TITLE, sortTitle, true);
        }
        writer.endElement(HtmlConstants.IMG);
      }
      writer.endElement(HtmlConstants.DIV);
    }
// ############################################
// ############################################

    writer.endElement(HtmlConstants.DIV);
  }


  protected void renderColumnSelectorHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIData component, UIColumn column)
      throws IOException {
    UIPanel menu = (UIPanel) column.getFacet(Facets.MENUPOPUP);
    if (menu == null) {
      final Application application = facesContext.getApplication();
      menu = (UIPanel) application.createComponent(UIMenu.COMPONENT_TYPE);
      menu.setId("selectorMenu");
      column.getFacets().put(Facets.MENUPOPUP, menu);
      menu.setRendererType(RendererTypes.MENU_BAR);
      menu.getAttributes().put(Attributes.MENU_POPUP, Boolean.TRUE);
      menu.getAttributes().put(Attributes.MENU_POPUP_TYPE, "SheetSelector");
      menu.getAttributes().put(Attributes.IMAGE, "image/sheetSelectorMenu.gif");

      String sheetId = column.getParent().getClientId(facesContext);

      String action = "Tobago.Sheets.selectAll('" + sheetId + "')";
      String label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuSelect");
      UIMenuCommand menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuSelectAll");
      menu.getChildren().add(menuItem);

      action = "Tobago.Sheets.unSelectAll('" + sheetId + "')";
      label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuUnselect");
      menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuUnselectAll");
      menu.getChildren().add(menuItem);

      action = "Tobago.Sheets.toggleAllSelections('" + sheetId + "')";
      label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago",
          "sheetMenuToggleselect");
      menuItem = createMenuItem(application, label, action);
      menuItem.setId("menuToggleSelections");
      menu.getChildren().add(menuItem);
    }

    menu.setRendered(UIData.MULTI.equals(component.getSelectable()));

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(column.getClientId(facesContext));
    writer.writeClassAttribute("tobago-sheet-selector-menu");
    writer.endElement(HtmlConstants.DIV);
    RenderUtil.encode(facesContext, menu);
  }

  private UIMenuCommand createMenuItem(final Application application, String label,
      String action) {
    UIMenuCommand menuItem
        = (UIMenuCommand) application.createComponent(UIMenuCommand.COMPONENT_TYPE);
    menuItem.setRendererType(RendererTypes.MENU_COMMAND);
    menuItem.getAttributes().put(Attributes.ONCLICK, action);
    menuItem.getAttributes().put(Attributes.LABEL, label);
    return menuItem;
  }

  private void renderColumnHeaderLabel(FacesContext facesContext,
                                       TobagoResponseWriter writer, UIColumn column,
                                       int sortMarkerWidth, String align,
                                       String image1x1) throws IOException {
    String label
        = (String) column.getAttributes().get(Attributes.LABEL);
    if (label != null) {
      writer.writeText(label, null);
      if (ComponentUtil.getBooleanAttribute(column, Attributes.SORTABLE) && "right".equals(align)) {
        writer.startElement(HtmlConstants.IMG, null);
        writer.writeAttribute(HtmlAttributes.SRC, image1x1, false);
        writer.writeAttribute(HtmlAttributes.ALT, "", false);
        writer.writeAttribute(HtmlAttributes.WIDTH, sortMarkerWidth);
        writer.writeAttribute(HtmlAttributes.HEIGHT, 1);
        writer.endElement(HtmlConstants.IMG);
      }
    } else {
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image1x1, false);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlConstants.IMG);
    }
  }

  private void writeDirectPagingLinks(
      TobagoResponseWriter writer, FacesContext facesContext, Application application, UIData data)
      throws IOException {
    UICommand pagerCommand = (UICommand) data.getFacet(Facets.PAGER_PAGE);
    if (pagerCommand == null) {
      pagerCommand = createPagingCommand(
          application, PageAction.TO_PAGE, false);
      data.getFacets().put(Facets.PAGER_PAGE, pagerCommand);
    }
    String pagerCommandId = pagerCommand.getClientId(facesContext);
    String onclick = new CommandRendererHelper(facesContext, pagerCommand).getOnclickDoubleQuoted();
    String hrefPostfix = "', '" + onclick + "');";

    int linkCount = ComponentUtil.getIntAttribute(data, Attributes.DIRECT_LINK_COUNT);
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

  private UICommand createPagingCommand(Application application,
                                               PageAction command, boolean disabled) {
    UICommand link;
    link = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
    link.setRendererType(RendererTypes.SHEET_PAGE_COMMAND);
    link.setRendered(true);
    link.setId(command.getToken());
    link.getAttributes().put(Attributes.INLINE, Boolean.TRUE);
    link.getAttributes().put(Attributes.DISABLED, disabled);
    return link;
  }

  private void writeLinkElement(TobagoResponseWriter writer, String str, String skip,
      String id, String hrefPostfix, boolean makeLink)
      throws IOException {
    String type = makeLink ? HtmlConstants.A : HtmlConstants.SPAN;
    writer.startElement(type, null);
    writer.writeClassAttribute("tobago-sheet-paging-links-link");
    if (makeLink) {
      writer.writeIdAttribute(id + SUBCOMPONENT_SEP + "link_" + skip);
      writer.writeAttribute(HtmlAttributes.HREF, "javascript: tobagoSheetSetPagerPage('"
          + id + "', '" + skip + hrefPostfix, null);
    }
    writer.flush();
    writer.write(str);
    writer.endElement(type);
  }

  public int getContentBorder(FacesContext facesContext, UIData data) {
    return getConfiguredValue(facesContext, data, "contentBorder");
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component)
      throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIData.class);
    // TODO find a better way
    UICommand clickAction = null;
    UICommand dblClickAction = null;
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
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
      }
    }
    renderSheet(facesContext, (UIData) component, (clickAction != null || dblClickAction != null));
  }

  @Override
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {

    // todo: why this will be called?
    if (component == null) {
      return 0;
    }

    UIData data = (UIData) component;

//    int headerHeight = getConfiguredValue(facesContext, component, "headerHeight");
//    int footerHeight = getConfiguredValue(facesContext, component, "footerHeight");
    int headerHeight = getHeaderHeight(facesContext, component);
    int footerHeight = getFooterHeight(facesContext, component);

    int rowHeight = getConfiguredValue(facesContext, component, "rowHeight");

    int rows = data.getRows();

    if (LOG.isInfoEnabled()) {
      LOG.info(headerHeight + " " + footerHeight + " " + rowHeight + " " + rows);
    }

    return headerHeight + rows * rowHeight + footerHeight + 2; // XXX hotfix: + 1
  }

  public void encodeChildren(FacesContext context,
                               UIComponent component)
            throws IOException {
    // DO Nothing
  }

  public void layoutBegin(FacesContext context, UIComponent component) throws IOException {
    UILayout.prepareDimension(context, component);
  }

  public void layoutEnd(FacesContext context, UIComponent component) throws IOException {
    if (component instanceof UIData) {
      UIData data = (UIData) component;
      ensureColumnWidthList(context, data);
      prepareDimensions(context, data);
    }
  }

  private void ensureColumnWidthList(FacesContext facesContext, UIData data) {
    List<Integer> currentWidthList = null;
    List<UIColumn> rendererdColumns = data.getRenderedColumns();

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
    if (currentWidthList != null && currentWidthList.size() != rendererdColumns.size()) {
      currentWidthList = null;
    }


    if (currentWidthList == null) {
      LayoutTokens tokens = data.getColumnLayout();
      List<UIColumn> allColumns = data.getAllColumns();
      LayoutTokens newTokens = new LayoutTokens();
      if (allColumns.size() > 0) {
        for (int i = 0; i < allColumns.size(); i++) {
          UIColumn column = allColumns.get(i);
          if (column.isRendered()) {
            if (tokens == null) {
              if (column instanceof org.apache.myfaces.tobago.component.UIColumn) {
                newTokens.addToken(
                    LayoutTokens.parseToken(((org.apache.myfaces.tobago.component.UIColumn) column).getWidth()));
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
      }


      int space = LayoutUtil.getInnerSpace(facesContext, data, true);
      space -= getContentBorder(facesContext, data);
      if (needVerticalScrollbar(facesContext, data)) {
        space -= getScrollbarWidth(facesContext, data);
      }
      LayoutInfo layoutInfo =
          new LayoutInfo(newTokens.getSize(), space, newTokens, data.getClientId(facesContext), false);
      parseFixedWidth(facesContext, layoutInfo, rendererdColumns);
      layoutInfo.parseColumnLayout(space);
      currentWidthList = layoutInfo.getSpaceList();
    }

    if (currentWidthList != null) {
      if (rendererdColumns.size() != currentWidthList.size()) {
        LOG.warn("widthList.size() = " + currentWidthList.size()
            + " != columns.size() = " + rendererdColumns.size() + "  widthList : "
            + LayoutInfo.listToTokenString(currentWidthList));
      } else {
        data.setWidthList(currentWidthList);
      }
    }
  }

  private void parseFixedWidth(FacesContext facesContext, LayoutInfo layoutInfo, List<UIColumn> rendereredColumns) {
    LayoutTokens tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.getSize(); i++) {
      LayoutToken token = tokens.get(i);
      if (token instanceof FixedLayoutToken) {
        int width = 0;
        if (!rendereredColumns.isEmpty()) {
          if (i < rendereredColumns.size()) {
            UIColumn column = rendereredColumns.get(i);
            if (column instanceof UIColumnSelector) {
              LayoutInformationProvider renderer
                  = ComponentUtil.getRenderer(facesContext, column);
              if (renderer == null) {
                LOG.warn("can't find renderer for " + column.getClass().getName());
                renderer = ComponentUtil.getRenderer(facesContext,
                    org.apache.myfaces.tobago.component.UIPanel.COMPONENT_FAMILY, RendererTypes.OUT);
              }
              width = renderer.getFixedWidth(facesContext, column);

            } else {
              for (UIComponent component : (List<UIComponent>) column.getChildren()) {
                LayoutInformationProvider renderer
                    = ComponentUtil.getRenderer(facesContext, component);
                width += renderer.getFixedWidth(facesContext, component);
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

  private void prepareDimensions(FacesContext facesContext, UIData data) {
     // prepare width's in column's children components

     List<Integer> columnWidths = data.getWidthList();
     int i = 0;
     for (UIColumn column : data.getRenderedColumns()) {
       if (i < columnWidths.size()) {
         Integer width = columnWidths.get(i);
         if (!(column instanceof UIColumnSelector)) {
           if (column.getChildCount() == 1) {
             UIComponent child = (UIComponent) column.getChildren().get(0);
             int cellPaddingWidth = getConfiguredValue(facesContext, data, "cellPaddingWidth");
             child.getAttributes().put(
                 Attributes.LAYOUT_WIDTH, width - cellPaddingWidth);
             child.getAttributes().remove(Attributes.INNER_WIDTH);
           } else {
             LOG.warn("More or less than 1 child in column! "
                 + "Can't set width for column " + i + " to " + width);
           }
         }
       } else {
         LOG.warn("More columns than columnSizes! "
             + "Can't set width for column " + i);
       }
       i++;
     }
   }

  @Override
  public boolean getPrepareRendersChildren() {
    return true;
  }

  @Override
  public void prepareRendersChildren(FacesContext context, UIComponent component) {
  }
}
