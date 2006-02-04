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


import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_INNER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_TOOL_BAR;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

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

    UIComponent label = component.getFacet(FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(ATTR_LABEL);
    String style = (String) component.getAttributes().get(getAttrStyleKey());
    UIPanel toolbar = (UIPanel) component.getFacet(FACET_TOOL_BAR);
    if (toolbar != null) {
      final int padding
          = getConfiguredValue(facesContext, component, "paddingTopWhenToolbar");
      style = HtmlRendererUtil.replaceStyleAttribute(style, "padding-top",
          Integer.toString(padding) + "px");
      style = HtmlRendererUtil.replaceStyleAttribute(style, "padding-bottom", "0px");
    }

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("fieldset", component);
    writer.writeComponentClass();
    writer.writeAttribute("style", style, null);

    if (label != null || labelString != null) {
      writer.startElement("legend", component);
      writer.writeComponentClass();

      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
      writer.endElement("legend");
    }

    String contentStyle = (String)
        component.getAttributes().get(ATTR_STYLE_INNER);
    if (toolbar != null) {
      writer.startElement("div", null);
      writer.writeClassAttribute("tobago-box-toolbar-div");
      writer.startElement("div", null);
      writer.writeClassAttribute("tobago-box-toolbar-span");
      final Map attributes = toolbar.getAttributes();
      attributes.put(
          ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
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
    writer.writeComponentClass();
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
    return ATTR_STYLE;
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
