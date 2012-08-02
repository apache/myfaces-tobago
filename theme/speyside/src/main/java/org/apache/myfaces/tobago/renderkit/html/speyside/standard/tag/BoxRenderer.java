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

package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_TOOL_BAR;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class BoxRenderer extends BoxRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(BoxRenderer.class);

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    return super.getFixedHeight(facesContext, component);
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    HtmlStyleMap style = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE);

    if (style != null) {
      Integer styleHeight = style.getInt("height");
      if (styleHeight != null) {
        style.put("height", styleHeight - 1);
      }
    }

    String clientId = component.getClientId(facesContext);
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);
    if (style != null) {
      writer.writeStyleAttribute(style);
    }
    writer.writeJavascript("Tobago.addAjaxComponent(\"" + clientId + "\")");

    encodeBeginInner(facesContext, writer, component);
  }

  private void encodeBeginInner(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent component) throws IOException {
    HtmlStyleMap innerStyle = HtmlRendererUtil.prepareInnerStyle(component);

    renderBoxHeader(facesContext, writer, component);


    writer.startElement(HtmlConstants.DIV, component);
    StyleClasses contentClasses = new StyleClasses();
    contentClasses.addClass("box", "content");
    if (component instanceof SupportsMarkup) {
      contentClasses.addMarkupClass((SupportsMarkup) component, "box", "content");
    }
    writer.writeClassAttribute(contentClasses);

    writer.startElement(HtmlConstants.DIV, component);
    StyleClasses contentInnerClasses = new StyleClasses();
    contentInnerClasses.addClass("box", "content-inner");
    if (component instanceof SupportsMarkup) {
      contentInnerClasses.addMarkupClass((SupportsMarkup) component, "box", "content-inner");
    }
    writer.writeClassAttribute(contentInnerClasses);
    writer.writeStyleAttribute(innerStyle);
  }


  protected void renderBoxHeader(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent component) throws IOException {

    writer.startElement(HtmlConstants.DIV, component);
    StyleClasses headerClasses = new StyleClasses();
    headerClasses.addClass("box", "header");
    if (component instanceof SupportsMarkup) {
      headerClasses.addMarkupClass((SupportsMarkup) component, "box", "header");
    }
    writer.writeClassAttribute(headerClasses);
    UIComponent label = component.getFacet(FACET_LABEL);
    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeClassAttribute("tobago-box-header-label");
    String labelString
        = (String) component.getAttributes().get(ATTR_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString);
    }
    writer.endElement(HtmlConstants.SPAN);

    UIPanel toolbar = (UIPanel) component.getFacet(FACET_TOOL_BAR);
    if (toolbar != null) {
      renderToolbar(facesContext, writer, toolbar);
    }
    writer.endElement(HtmlConstants.DIV);
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    encodeEndInner(writer);

    writer.endElement(HtmlConstants.DIV);
  }

  private void encodeEndInner(TobagoResponseWriter writer) throws IOException {
    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);
  }

  protected void renderToolbar(
      FacesContext facesContext, TobagoResponseWriter writer, UIPanel toolbar) throws IOException {
    final Map attributes = toolbar.getAttributes();
    String className = "tobago-box-header-toolbar-div";
    if (ToolBarTag.LABEL_OFF.equals(attributes.get(ATTR_LABEL_POSITION))) {
      className += " tobago-box-header-toolbar-label_off";
    }
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute(className);
    toolbar.setRendererType("BoxToolBar");
    RenderUtil.encode(facesContext, toolbar);
    writer.endElement(HtmlConstants.DIV);
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIPanel.class);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    encodeBeginInner(facesContext, writer, component);
    component.encodeChildren(facesContext);
    encodeEndInner(writer);
    facesContext.responseComplete();
  }
}

