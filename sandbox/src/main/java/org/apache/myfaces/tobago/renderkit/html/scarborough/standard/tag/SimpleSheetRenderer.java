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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIColumnEvent;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.util.StringUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class SimpleSheetRenderer extends SheetRenderer {

  private static final Log LOG = LogFactory.getLog(SimpleSheetRenderer.class);

  @SuppressWarnings(value = "unchecked")
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UIData data = (UIData) component;
    storeFooterHeight(facesContext, data);

    if (data.getRowIndex() != -1) {
      // TODO: find why this is needed
      LOG.warn("reset RowIndex");
      data.setRowIndex(-1);
    }

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, data);

    final String sheetId = data.getClientId(facesContext);
    HtmlStyleMap sheetStyle = (HtmlStyleMap) data.getAttributes().get(TobagoConstants.ATTR_STYLE);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    // Outher list div
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_outer_div");
    writer.writeClassAttribute("tobago-simpleSheet-content");
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

    final String[] styles = new String[]{"style/tobago-sheet.css"};
    final String[] scripts = new String[]{"script/tobago-sheet.js"};
    Integer frequency = null;
    UIComponent facetReload = data.getFacet(TobagoConstants.FACET_RELOAD);
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
            + ", true);"
    };
    UIPage page = ComponentUtil.findPage(facesContext, data);

    page.getStyleFiles().add(styles[0]);
    page.getScriptFiles().add(scripts[0]);

    if (!ajaxEnabled) {
      page.getOnloadScripts().add(cmds[0]);
    } else {
      HtmlRendererUtil.writeStyleLoader(facesContext, styles);
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);
    }
  }

  @SuppressWarnings(value = "unchecked")
  protected void renderSheet(FacesContext facesContext, UIData data, boolean hasClickAction) throws IOException {


    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    String sheetId = data.getClientId(facesContext);
    SheetState sheetState = data.getSheetState(facesContext);
    List<UIColumn> renderedColumnList = data.getRenderedColumns();

    HtmlStyleMap sheetStyle = (HtmlStyleMap) data.getAttributes().get(TobagoConstants.ATTR_STYLE);
    Integer sheetHeight = HtmlRendererUtil.getStyleAttributeIntValue(sheetStyle, "height");
    if (sheetHeight == null) {
      // FIXME: nullpointer if height not defined
      LOG.error("no height in parent container, setting to 100");
      sheetHeight = 100;
    }
    int footerHeight = (Integer) data.getAttributes().get(TobagoConstants.ATTR_FOOTER_HEIGHT);

    HtmlStyleMap bodyStyle = (HtmlStyleMap) data.getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
    HtmlRendererUtil.replaceStyleAttribute(data, TobagoConstants.ATTR_STYLE_BODY, "height",
        (sheetHeight - footerHeight));


    List<Integer> columnWidths = data.getWidthList();
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeIdAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeNameAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.toString(columnWidths), false);
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
    List<Integer> selectedRows = sheetState.getSelectedRows();
    if (!UIData.NONE.equals(data.getSelectable())) {
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeIdAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeNameAttribute(sheetId + SELECTED_POSTFIX);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.toString(selectedRows), false);
      writer.endElement(HtmlConstants.INPUT);
    }
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();

    //String checkedImage = contextPath + resourceManager.getImage(viewRoot, "image/sheetChecked.gif");
    //String uncheckeImage = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");
    String selectorDisabled = contextPath + resourceManager.getImage(viewRoot, "image/sheetUncheckedDisabled.gif");
    String unchecked = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");
    // Outer list div
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_data_div");
    writer.writeClassAttribute("tobago-simpleSheet-list");
    writer.writeStyleAttribute(bodyStyle);

    int top = 20;


    boolean odd = false;
    final int last = data.getFirst() + data.getRows();
    for (int row = data.getFirst(); row < last; row++) {
      data.setRowIndex(row);
      if (!data.isRowAvailable()) {
        break;
      }

      odd = !odd;
      final String rowClass = odd ? "tobago-sheet-content-odd " : "tobago-sheet-content-even ";

      //TODO make markup toago compatible
      String[] rowMarkups = (String[]) data.getAttributes().get("rowMarkup");
      String rowMarkup = "";
      if (rowMarkup != null) {
        rowMarkup = " " + org.apache.commons.lang.StringUtils.join(rowMarkups, " ");
      }

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute("tobago-simpleSheet-row " + rowClass + rowMarkup);
      writer.writeIdAttribute(sheetId + "_data_tr_" + row);
      writer.writeAttribute(HtmlAttributes.STYLE, "top: "+ top+ "px; left: 0px;", false);
      writer.flush();

      int columnIndex = -1;
      int currentLeft = 0;
      for (UIColumn column : renderedColumnList) {
        columnIndex++;
        writer.startElement(HtmlConstants.DIV, null);
        writer.writeClassAttribute("tobago-simpleSheet-cell");
        // todo cell markup
           // + (rowSelected ? " tobabo-simpleSheet-cell-selected" : ""));
        writer.writeIdAttribute(sheetId + "_" + row + "_" + columnIndex);
        final String align = (String) column.getAttributes().get(TobagoConstants.ATTR_ALIGN);
        writer.writeAttribute(HtmlAttributes.STYLE, "top: 0px; left: " + currentLeft + "px; width: "
            + columnWidths.get(columnIndex) + "px; "
            + (align!=null?HtmlRendererUtil.toStyleString("text-align", align):""), false);
        if (column instanceof UIColumnSelector) {
          final boolean disabled = ComponentUtil.getBooleanAttribute(column, TobagoConstants.ATTR_DISABLED);
          writer.startElement(HtmlConstants.INPUT, null);
          writer.writeAttribute(HtmlAttributes.TYPE, "checkbox", false);
          writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
          writer.writeIdAttribute(sheetId + "_data_row_selector_" + row);
          writer.writeClassAttribute("tobago-selectBooleanCheckbox-default");
          writer.endElement(HtmlConstants.INPUT);
        } else {
          List<UIComponent> childs = data.getRenderedChildrenOf(column);
          for (UIComponent grandkid : childs) {
            // set height to 0 to prevent use of layoutheight from parent
            grandkid.getAttributes().put(TobagoConstants.ATTR_LAYOUT_HEIGHT, 0);
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
        currentLeft += columnWidths.get(columnIndex);
      }

      writer.endElement(HtmlConstants.DIV);
      top += 20;
    }

    data.setRowIndex(-1);

    writer.endElement(HtmlConstants.DIV);

    String image1x1 = contextPath + resourceManager.getImage(viewRoot, "image/1x1.gif");

    int columnCount = 0;
    final int sortMarkerWidth = getAscendingMarkerWidth(facesContext, data);
    String imageAscending = contextPath + resourceManager.getImage(viewRoot, "image/ascending.gif");
    String imageDescending = contextPath + resourceManager.getImage(viewRoot, "image/descending.gif");
    String img = resourceManager.getImage(viewRoot, "image/unsorted.gif", true);
    String imageUnsorted = image1x1;
    if (img != null) {
      imageUnsorted = contextPath + img;
    }
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_header_div");
    writer.writeClassAttribute("tobago-simpleSheet-header");
    for (UIColumn column : renderedColumnList) {
      renderColumnHeader(facesContext, writer, data, columnCount, column,
          imageAscending, imageDescending, imageUnsorted, image1x1, sortMarkerWidth, false);
      columnCount++;
    }
    writer.endElement(HtmlConstants.DIV);
    renderFooter(facesContext, data, writer, sheetId, sheetHeight, getFooterHeight(facesContext, data), bodyStyle);

  }

}
