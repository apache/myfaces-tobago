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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UINav;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIImage;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ImageRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ImageRenderer.class);

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final AbstractUIImage image = (AbstractUIImage) component;
    final String value = image.getUrl();
    final String src;
    if (value != null) {
      if (ResourceManagerUtils.isAbsoluteResource(value)) {
        // absolute Path to image : nothing to do
        src = value;
      } else {
        final int dot = ResourceManagerUtils.indexOfExtension(value);
        final boolean disabled = isDisabled(image);
        if (dot != -1) {
          src = ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, value, disabled);
        } else {
          src = ResourceManagerUtils.getImageOrDisabledImage(facesContext, value, disabled);
        }
      }
    } else {
      src = null;
    }

    String border = (String) image.getAttributes().get(Attributes.BORDER);
    if (border == null) {
      border = "0";
    }
    String alt = (String) image.getAttributes().get(Attributes.ALT);
    if (alt == null) {
      alt = "";
    }

    writer.startElement(HtmlElements.IMG, image);
    writer.writeIdAttribute(image.getClientId(facesContext));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, image);
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, src, true);
    }
    writer.writeAttribute(HtmlAttributes.ALT, alt, true);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, image);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.BORDER, border, false);
    final Style style = new Style();
    style.setWidth(image.getWidth());
    style.setHeight(image.getHeight());
    writer.writeStyleAttribute(style);
    if (ComponentUtils.findAncestor(image, UINav.class) != null) { // todo: may set a marker in the context in the
      // todo: NavRenderer, or the additional class, to avoid tree traversing
      writer.writeClassAttribute(Classes.create(image), BootstrapClass.NAVBAR_BRAND);
    } else {
      writer.writeClassAttribute(Classes.create(image));
    }
    writer.endElement(HtmlElements.IMG);
  }

  private String createSrc(final String src, final String ext) {
    final int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  private boolean isDisabled(final AbstractUIImage graphic) {
    return graphic.isDisabled() 
        || (graphic.getParent() instanceof UICommand && ((UICommand) graphic.getParent()).isDisabled());
  }
}
