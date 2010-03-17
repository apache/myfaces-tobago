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
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxRenderer;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.DecimalFormat;

public class PopupRenderer extends LayoutComponentRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(PopupRenderer.class);

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getPopups().add((UIPopup) component);
    }
    super.prepareRender(facesContext, component);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    UIPopup popup = (UIPopup) component;

// LAYOUT Begin

    long begin = System.nanoTime();
    LayoutContext layoutContext = new LayoutContext(popup);
    layoutContext.layout();
    LOG.info("Laying out takes: " + new DecimalFormat("#,##0").format(System.nanoTime() - begin) + " ns");

// LAYOUT End

    final String clientId = popup.getClientId(facesContext);
    final String contentDivId = clientId + ComponentUtils.SUB_SEPARATOR + "content";
    //final String left = popup.getLeft();
    //final String top = popup.getTop();
    Integer zIndex = (Integer) popup.getAttributes().get(Attributes.Z_INDEX);
    if (zIndex == null) {
      zIndex = 0;
    }

    final StringBuilder contentStyle = new StringBuilder(32);
    if (popup.getCurrentWidth() != null) {
      contentStyle.append("width: ");
      contentStyle.append(popup.getCurrentWidth().getPixel());
      contentStyle.append("; ");
    }
    if (popup.getCurrentHeight() != null) {
      contentStyle.append("height: ");
      contentStyle.append(popup.getCurrentHeight().getPixel());
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
    if (popup.isModal()) {
      writer.startElement(HtmlConstants.DIV, popup);
      writer.writeIdAttribute(clientId);
      writer.writeStyleAttribute("z-index: " + (zIndex + 1) + ";");
      writer.writeClassAttribute();
      writer.writeAttribute(HtmlAttributes.ONCLICK, "Tobago.popupBlink('" + clientId + "')", null);
      if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
        String bgImage = ResourceManagerUtil.getImageWithPath(facesContext, "image/popupBg.png");
        writer.writeAttribute(HtmlAttributes.STYLE, "background: none; "
            + "filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
          + bgImage + "', sizingMethod='scale');", false);
      }
      writer.endElement(HtmlConstants.DIV);
    }
    if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
      writer.startElement(HtmlConstants.IFRAME, popup);
      writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + HtmlConstants.IFRAME);
      writer.writeClassAttribute("tobago-popup-iframe tobago-popup-none");
      writer.writeStyleAttribute("z-index: " + (zIndex + 2) + ";");
      UIPage page = (UIPage) ComponentUtils.findPage(facesContext);
      if (popup.isModal()) {
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
    writer.startElement(HtmlConstants.DIV, popup);
    writer.writeIdAttribute(contentDivId);
    StyleClasses styleClasses = new StyleClasses();
    styleClasses.addClass("popup", "content");
    styleClasses.addClass("popup", "none");
    if (popup.isModal()) {
      styleClasses.addClass("popup", "modal");
    }
    writer.writeClassAttribute(styleClasses);

    writer.writeAttribute(HtmlAttributes.STYLE, contentStyle.toString(), false);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    UIPopup popup = (UIPopup) component;
    final String clientId = popup.getClientId(facesContext);

    writer.endElement(HtmlConstants.DIV);

    writer.startJavascript();
    writer.write("Tobago.setupPopup('");
    writer.write(clientId);
    writer.write("', ");
    final Measure left = popup.getLeft();
    writer.write(left != null ? Integer.toString(left.getPixel()) : "null");
    writer.write(", ");
    final Measure top = popup.getTop();
    writer.write(top != null ? Integer.toString(top.getPixel()) : "null");
    writer.write(", ");
    writer.write(String.valueOf(popup.isModal()));
    writer.write(");");
    writer.endJavascript();
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component) throws IOException {
    AjaxInternalUtils.checkParamValidity(facesContext, component, UIPopup.class);
    RenderUtil.encode(facesContext, component);
  }
}

