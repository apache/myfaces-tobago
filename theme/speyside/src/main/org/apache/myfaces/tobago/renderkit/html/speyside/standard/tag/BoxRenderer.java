/*
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

public class BoxRenderer extends BoxRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(BoxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    return super.getFixedHeight(facesContext, component);
  }

  public void encodeBeginTobago(FacesContext facesContext, UIComponent component) throws IOException {


    HtmlRendererUtil.prepareInnerStyle(component);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    String style = (String) component.getAttributes().get(ATTR_STYLE);

    try {
      String heightString =
          HtmlRendererUtil.getStyleAttributeValue(style, "height").replaceAll("\\D", "");

      int height = Integer.parseInt(heightString) - 1;
      style =
        HtmlRendererUtil.replaceStyleAttribute(style, "height", height + "px");
    } catch (Exception e) {
    }

    writer.startElement("div", component);
    writer.writeComponentClass();
    writer.writeAttribute("style", style, null);

    renderBoxHeader(facesContext, writer, component);


    writer.startElement("div", component);
    writer.writeClassAttribute("tobago-box-content");
//    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("div", component);
    writer.writeClassAttribute("tobago-box-content-inner");
    writer.writeAttribute("style", null, ATTR_STYLE_INNER);
  }


  protected void renderBoxHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent component) throws IOException {

    writer.startElement("div", component);
    writer.writeClassAttribute("tobago-box-header");
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    writer.startElement("span", null);
    writer.writeClassAttribute("tobago-box-header-label");
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString, null);
    }
    writer.endElement("span");

    UIPanel toolbar = (UIPanel) component.getFacet(FACET_TOOL_BAR);
    if (toolbar != null) {
      renderToolbar(facesContext, writer, toolbar);
    }
    writer.endElement("div");
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement("div");
    writer.endElement("div");

    writer.endElement("div");
  }

  protected void renderToolbar(FacesContext facesContext, TobagoResponseWriter writer, UIPanel toolbar) throws IOException {
    final Map attributes = toolbar.getAttributes();
    String className = "tobago-box-header-toolbar-div";
    if (ToolBarTag.LABEL_OFF.equals(attributes.get(ATTR_LABEL_POSITION))) {
      className += " tobago-box-header-toolbar-label_off";
    }
    writer.startElement("div", null);
    writer.writeClassAttribute(className);
    attributes.put(TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
    if (ToolBarTag.LABEL_BOTTOM.equals(attributes.get(ATTR_LABEL_POSITION))) {
      attributes.put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_RIGHT);
    }
    if (ToolBarTag.ICON_BIG.equals(attributes.get(ATTR_ICON_SIZE))) {
      attributes.put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
    }
    RenderUtil.encode(facesContext, toolbar);
    writer.endElement("div");
  }

}

