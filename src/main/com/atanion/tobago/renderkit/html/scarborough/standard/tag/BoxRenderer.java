/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.ToolBarTag;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.renderkit.BoxRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

public class BoxRenderer extends BoxRendererBase {

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {

    HtmlRendererUtil.prepareInnerStyle(component);

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);
    String style = (String) component.getAttributes().get(getAttrStyleKey());
    UIPanel toolbar = (UIPanel) component.getFacet(FACET_TOOL_BAR);
    if (toolbar != null) {
      final int padding
          = getConfiguredValue(facesContext, component, "paddingTopWhenToolbar");
      style = HtmlRendererUtil.replaceStyleAttribute(style, "padding-top",
          Integer.toString(padding) + "px");
      style = HtmlRendererUtil.replaceStyleAttribute(style, "padding-bottom", "0px");
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("fieldset", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", style, null);

    if (label != null || labelString != null) {
      writer.startElement("legend", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);

      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
      writer.endElement("legend");
    }

    String contentStyle = (String)
        component.getAttributes().get(TobagoConstants.ATTR_STYLE_INNER);
    if (toolbar != null) {
      writer.startElement("div", null);
      writer.writeAttribute("class", "tobago-box-toolbar-div", null);
      writer.startElement("div", null);
      writer.writeAttribute("class", "tobago-box-toolbar-span", null);
      final Map attributes = toolbar.getAttributes();
      attributes.put(
          TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
      if (ToolBarTag.LABEL_BOTTOM.equals(attributes.get(ATTR_LABEL_POSITION))) {
        attributes.put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_RIGHT);
      }
      if (ToolBarTag.ICON_BIG.equals(attributes.get(ATTR_ICON_SIZE))) {
        attributes.put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
      }
      RenderUtil.encode(facesContext, toolbar);
      writer.endElement("div");
      writer.endElement("div");
      if (ClientProperties.getInstance(facesContext.getViewRoot()).getUserAgent().isMsie()) {
        contentStyle
            = HtmlRendererUtil.replaceStyleAttribute(contentStyle, "top", "-10px");
      }
    }
    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", contentStyle, null);

  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement("div");
    writer.endElement("fieldset");
  }

  public boolean getRendersChildren() {
    return true;
  }

  protected String getAttrStyleKey() {
    return TobagoConstants.ATTR_STYLE;
  }

  public int getPaddingHeight(FacesContext facesContext, UIComponent component) {
    final int paddingHeight = super.getPaddingHeight(facesContext, component);
    int extraPadding = 0;
    if (component.getFacet(FACET_TOOL_BAR) != null) {
      extraPadding = getExtraPadding(facesContext, component);
    }
    return paddingHeight + extraPadding;
  }

  private int getExtraPadding(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component,
              "extraPaddingHeightWhenToolbar");
  }
}
