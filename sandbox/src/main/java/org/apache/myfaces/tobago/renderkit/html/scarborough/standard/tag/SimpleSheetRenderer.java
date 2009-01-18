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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.util.StringUtil;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIColumn;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class SimpleSheetRenderer extends SheetRenderer {

  private final Log LOG = LogFactory.getLog(SimpleSheetRenderer.class);

  @SuppressWarnings(value = "unchecked")
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UIData sheet = (UIData) component;
    if (sheet.getRowIndex() != -1) {
      // TODO: find why this is needed
      LOG.warn("reset RowIndex");
      sheet.setRowIndex(-1);
    }

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, sheet);
    final String sheetId = component.getClientId(facesContext);
    HtmlStyleMap sheetStyle = (HtmlStyleMap) sheet.getAttributes().get(TobagoConstants.ATTR_STYLE);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    UIPage page = ComponentUtil.findPage(facesContext, sheet);
    final String[] styles = new String[]{"style/tobago-sheet.css"};
    page.getStyleFiles().add(styles[0]);

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      //page.getOnloadScripts().add(cmds[0]);
    } else {
      HtmlRendererUtil.writeStyleLoader(facesContext, styles);
    }

    // Outher list div
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_content");
    writer.writeClassAttribute("tobago-simpleSheet-content");
    writer.writeStyleAttribute(sheetStyle);

    renderListContent(facesContext, component);

    writer.endElement(HtmlConstants.DIV);

  }

  @SuppressWarnings(value = "unchecked")
  private void renderListContent(FacesContext facesContext, UIComponent component) throws IOException {
    UIData data = (UIData) component;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    String sheetId = data.getClientId(facesContext);
    SheetState sheetState = data.getSheetState(facesContext);
    List<UIColumn> renderedColumnList = data.getRenderedColumns();

    HtmlStyleMap sheetStyle = (HtmlStyleMap) component.getAttributes().get(TobagoConstants.ATTR_STYLE);

    HtmlStyleMap bodyStyle = (HtmlStyleMap) component.getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
    List<Integer> columnWidths = (List<Integer>) component.getAttributes().get(TobagoConstants.ATTR_WIDTH_LIST);
    Integer sheetHeight = HtmlRendererUtil.getStyleAttributeIntValue(sheetStyle, "height");
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeIdAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeNameAttribute(sheetId + WIDTHS_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeAttribute(HtmlAttributes.VALUE, StringUtil.toString(columnWidths), false);
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
      writer.writeAttribute(HtmlAttributes.VALUE, StringUtil.toString(selectedRows), false);
      writer.endElement(HtmlConstants.INPUT);
    }
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();

    //String checkedImage = contextPath + resourceManager.getImage(viewRoot, "image/sheetChecked.gif");
    //String uncheckeImage = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");
    String selectorDisabled = contextPath + resourceManager.getImage(viewRoot, "image/sheetUncheckedDisabled.gif");
    String unchecked = contextPath + resourceManager.getImage(viewRoot, "image/sheetUnchecked.gif");
    // Outher list div
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(sheetId + "_div");
    writer.writeClassAttribute("tobago-simpleSheet-list");
    writer.writeStyleAttribute(bodyStyle);

    int top = 20;



    final int last = data.getFirst() + data.getRows();
    for (int row = data.getFirst(); row < last; row++) {
      data.setRowIndex(row);
      if (!data.isRowAvailable()) {
        break;
      }
      boolean rowSelected = selectedRows.contains(row);

      int columnIndex = -1;
      int currentLeft = 0;
      for (UIColumn column : renderedColumnList) {
        columnIndex++;
        List<UIComponent> childs = data.getRenderedChildrenOf(column);
        if (childs.isEmpty()) {
          continue;
        }
        writer.startElement(HtmlConstants.DIV, null);
        writer.writeClassAttribute("tobabo-simpleSheet-cell"
            + (rowSelected ? " tobabo-simpleSheet-cell-selected" : ""));
        writer.writeIdAttribute(sheetId + "_" + row + "_" + columnIndex);
        final String align = (String) column.getAttributes().get(TobagoConstants.ATTR_ALIGN);
        writer.writeAttribute(HtmlAttributes.STYLE, "top: " + top + "px; left: " + currentLeft + "px; width: "
            + columnWidths.get(columnIndex) + "px; "
            + (align!=null?HtmlRendererUtil.toStyleString("text-align", align):""), false);
        if (column instanceof UIColumnSelector) {
          final boolean disabled = ComponentUtil.getBooleanAttribute(column, TobagoConstants.ATTR_DISABLED);
          writer.startElement(HtmlConstants.IMG, null);
          if (disabled) {
            writer.writeAttribute(HtmlAttributes.SRC, selectorDisabled, false);
          } else {
            writer.writeAttribute(HtmlAttributes.SRC, unchecked, false);
          }
          writer.writeIdAttribute(sheetId + "_data_row_selector_" + row);
          writer.writeClassAttribute("tobago-sheet-column-selector");
          writer.endElement(HtmlConstants.IMG);
        } else {
          for (UIComponent grandkid : childs) {
            // set height to 0 to prevent use of layoutheight from parent
            grandkid.getAttributes().put(TobagoConstants.ATTR_LAYOUT_HEIGHT, 0);
            RenderUtil.encode(facesContext, grandkid);
          }
        }
        writer.endElement(HtmlConstants.DIV);
        currentLeft += columnWidths.get(columnIndex);
      }
      top += 20;
    }


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
    writer.writeClassAttribute("tobago-simpleSheet-header");
    for (UIColumn column : renderedColumnList) {
      renderColumnHeader(facesContext, writer, data, columnCount, column,
          imageAscending, imageDescending, imageUnsorted, image1x1, sortMarkerWidth, false);
      columnCount++;
    }
    writer.endElement(HtmlConstants.DIV);
    renderFooter(facesContext, data, writer, sheetId, sheetHeight, getFooterHeight(facesContext, data), bodyStyle);

  }

  public void encodeAjax(FacesContext facesContext, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIData.class);
    renderListContent(facesContext, component);
    facesContext.responseComplete();    
  }
}
