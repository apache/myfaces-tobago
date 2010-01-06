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
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;

public class PopupRenderer extends LayoutComponentRendererBase implements AjaxRenderer {

  @SuppressWarnings({"UnusedDeclaration"})
  private static final Log LOG = LogFactory.getLog(PopupRenderer.class);

  public static final String CONTENT_ID_POSTFIX = SUBCOMPONENT_SEP + "content";

  public boolean getRendersChildren() {
    return true;
  }

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getPopups().add((UIPopup) component);
    }
    super.prepareRender(facesContext, component);
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
  }

  public void encodeBegin(
      FacesContext facesContext, UIComponent uiComponent) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    UIPopup component = (UIPopup) uiComponent;
    final String clientId = component.getClientId(facesContext);
    final String contentDivId = clientId + CONTENT_ID_POSTFIX;
    //final String left = component.getLeft();
    //final String top = component.getTop();
    Integer zIndex = (Integer) component.getAttributes().get(Attributes.Z_INDEX);
    if (zIndex == null) {
      zIndex = 0;
    }

    final StringBuilder contentStyle = new StringBuilder(32);
    if (component.getCurrentWidth() != null) {
      contentStyle.append("width: ");
      contentStyle.append(component.getCurrentWidth().getPixel());
      contentStyle.append("; ");
    }
    if (component.getCurrentHeight() != null) {
      contentStyle.append("height: ");
      contentStyle.append(component.getCurrentHeight().getPixel());
      contentStyle.append("; ");
    }
    contentStyle.append("z-index: ");
    contentStyle.append(zIndex + 3);
    contentStyle.append("; ");
    //contentStyle.append("left: ");
    //contentStyle.append(left);
    //contentStyle.append("; ");
    //contentStyle.append("top: ");
    //contentStyle.append(top);
    //contentStyle.append("; ");
    if (component.getModal()) {
      writer.startElement(HtmlConstants.DIV, component);
      writer.writeIdAttribute(clientId);
      writer.writeStyleAttribute("z-index: " + (zIndex + 1) + ";");
      writer.writeClassAttribute();
      writer.writeAttribute(HtmlAttributes.ONCLICK, "Tobago.popupBlink('" + clientId + "')", null);
      if (VariableResolverUtil.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
        String bgImage = ResourceManagerUtil.getImageWithPath(facesContext, "image/popupBg.png");
        writer.writeAttribute(HtmlAttributes.STYLE, "background: none; "
            + "filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
          + bgImage + "', sizingMethod='scale');", false);
      }
      writer.endElement(HtmlConstants.DIV);
    }
    if (VariableResolverUtil.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
      writer.startElement(HtmlConstants.IFRAME, component);
      writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + HtmlConstants.IFRAME);
      writer.writeClassAttribute("tobago-popup-iframe tobago-popup-none");
      writer.writeStyleAttribute("z-index: " + (zIndex + 2) + ";");
      UIPage page = (UIPage) ComponentUtils.findPage(facesContext);
      if (component.getModal()) {
        final StringBuilder frameSize = new StringBuilder(32);
        // full client area
        frameSize.append("width: ");
        frameSize.append(page.getCurrentWidth().getPixel());
        frameSize.append("; ");
        frameSize.append("height: ");
        frameSize.append(page.getCurrentHeight().getPixel());
        frameSize.append("; ");
        writer.writeAttribute(HtmlAttributes.STYLE, frameSize.toString(), false);
      } else {
        writer.writeAttribute(HtmlAttributes.STYLE, contentStyle.toString(), false); // size of the popup
      }

      writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtil.getBlankPage(facesContext), false);
      writer.writeAttribute(HtmlAttributes.FRAMEBORDER, "0", false);
      writer.endElement(HtmlConstants.IFRAME);
    }
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeIdAttribute(contentDivId);
    StyleClasses styleClasses = new StyleClasses();
    styleClasses.addClass("popup", "content");
    styleClasses.addClass("popup", "none");
    if (component.getModal()) {
      styleClasses.addClass("popup", "modal");
    }
    writer.writeClassAttribute(styleClasses);

    writer.writeAttribute(HtmlAttributes.STYLE, contentStyle.toString(), false);
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    UIPopup component = (UIPopup) uiComponent;
    final String clientId = component.getClientId(facesContext);

    writer.endElement(HtmlConstants.DIV);

    writer.startJavascript();
    writer.write("Tobago.setupPopup('");
    writer.write(clientId);
    writer.write("', '");
    writer.write(component.getLeft().getPixel());
    writer.write("', '");
    writer.write(component.getTop().getPixel());
    writer.write("', ");
    writer.write(String.valueOf(component.getModal()));
    writer.write(");");
    writer.endJavascript();
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIPopup.class);
    RenderUtil.encode(facesContext, component);
  }
}

