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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIImage;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class ImageRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ImageRenderer.class);

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    HtmlRendererUtils.renderDojoDndSource(facesContext, component);
  }

  public void encodeEnd(FacesContext facesContext,      UIComponent component) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    UIImage image = (UIImage) component;
    final String value = image.getUrl();
    String src = value;
    if (src != null) {
      final String ucSrc = src.toUpperCase(Locale.ENGLISH);
      if (ucSrc.startsWith("HTTP:") || ucSrc.startsWith("FTP:")
          || ucSrc.startsWith("/")) {
        // absolute Path to image : nothing to do
      } else {
        src = null;
        if (isDisabled(image)) {
          src = ResourceManagerUtils.getImageWithPath(facesContext,
              HtmlRendererUtils.createSrc(value, "Disabled"), true);
        }
        if (src == null) {
          src = ResourceManagerUtils.getImageWithPath(facesContext, value);
        }
      }
    }

    String border = (String) image.getAttributes().get(Attributes.BORDER);
    if (border == null) {
      border = "0";
    }
    String alt = (String) image.getAttributes().get(Attributes.ALT);
    if (alt == null) {
      alt = "";
    }

    writer.startElement(HtmlConstants.IMG, image);
    final String clientId = image.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, src, true);
    }
    writer.writeAttribute(HtmlAttributes.ALT, alt, true);
    HtmlRendererUtils.renderTip(image, writer);
    writer.writeAttribute(HtmlAttributes.BORDER, border, false);
    Style style = new Style(facesContext, image);
    writer.writeStyleAttribute(style);
    HtmlRendererUtils.renderDojoDndItem(image, writer, true);
    writer.writeClassAttribute();
    writer.endElement(HtmlConstants.IMG);
  }

  private String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  private boolean isDisabled(UIImage graphic) {
    return graphic.isDisabled() 
        || (graphic.getParent() instanceof UICommand && ((UICommand) graphic.getParent()).isDisabled());
  }
}
