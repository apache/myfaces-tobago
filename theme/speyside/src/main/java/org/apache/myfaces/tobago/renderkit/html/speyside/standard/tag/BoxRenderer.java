package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

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
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class BoxRenderer extends BoxRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(BoxRenderer.class);
  public static final String CONTENT_INNER = SUBCOMPONENT_SEP + "content-inner";
  public static final String HEADER = SUBCOMPONENT_SEP + "header";

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    HtmlRendererUtil.renderDojoDndSource(facesContext, component);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    HtmlStyleMap style = (HtmlStyleMap) component.getAttributes().get(Attributes.STYLE);

    if (style != null) {
      Integer styleHeight = style.getInt("height");
      if (styleHeight != null) {
        style.put("height", styleHeight-1);
      }
    }

    String clientId = component.getClientId(facesContext);
    writer.startElement(HtmlConstants.DIV, component);
    HtmlRendererUtil.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);
//    if (style != null) {
      writer.writeStyleAttribute(style);
//    }
    // XXX what is abrout the writeStyleAttribute(style) method? is it useful or should it be deprecated?
    writer.writeStyleAttribute();
    writer.writeJavascript("Tobago.addAjaxComponent(\"" + clientId + "\");");
    
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
    String id = component.getClientId(facesContext) + CONTENT_INNER; 
    writer.writeIdAttribute(id);
    StyleClasses contentInnerClasses = new StyleClasses();
    contentInnerClasses.addClass("box", "content-inner");
    contentInnerClasses.addFullQualifiedClass("dojoDndItem");
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
    String id = component.getClientId(facesContext) + HEADER;
    writer.writeIdAttribute(id);
    UIComponent label = component.getFacet(Facets.LABEL);
    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeClassAttribute("tobago-box-header-label");
    String labelString
        = (String) component.getAttributes().get(Attributes.LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString);
    }
    writer.endElement(HtmlConstants.SPAN);

    UIPanel toolbar = (UIPanel) component.getFacet(Facets.TOOL_BAR);
    if (toolbar != null) {
      renderToolbar(facesContext, writer, toolbar);
    }
    writer.endElement(HtmlConstants.DIV);
  }

  @Override
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
    if (UIToolBar.LABEL_OFF.equals(attributes.get(Attributes.LABEL_POSITION))) {
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
