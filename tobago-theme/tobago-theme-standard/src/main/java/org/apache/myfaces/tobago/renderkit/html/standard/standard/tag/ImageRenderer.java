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
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
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

public class ImageRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ImageRenderer.class);

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final AbstractUIImage image = (AbstractUIImage) component;
    final String value = image.getUrl();
    final String src;
    boolean fontAwesome = false;
    if (value != null) {
      if (ResourceManagerUtils.isAbsoluteResource(value)) {
        // absolute Path to image : nothing to do
        src = value;
      } else {
        final boolean disabled = isDisabled(image);
        if (ResourceManagerUtils.indexOfExtension(value) > -1) { // has extension
          src = ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, value, disabled);
        } else if (value.startsWith("fa-")) {
          fontAwesome = true;
          src = null;
        } else {
          src = ResourceManagerUtils.getImageOrDisabledImage(facesContext, value, disabled);
        }
      }
    } else {
      src = null;
    }

    final String alt = ComponentUtils.getStringAttribute(image, Attributes.alt, "");

    if (fontAwesome) {
      writer.writeIcon(null, FontAwesomeIconEncoder.generateClass(value)); // todo: should not be static
    } else {
      writer.startElement(HtmlElements.IMG);
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
      // todo: may set a marker in the context in the
      // todo: NavRenderer, or the additional class, to avoid tree traversing
      writer.writeClassAttribute(
          Classes.create(image),
          ComponentUtils.findAncestor(image, UINav.class) != null ? BootstrapClass.NAVBAR_BRAND : null,
          image.getCustomClass());
      writer.endElement(HtmlElements.IMG);
    }
  }

  private boolean isDisabled(final AbstractUIImage graphic) {
    return graphic.isDisabled()
        || (graphic.getParent() instanceof UICommand && ((UICommand) graphic.getParent()).isDisabled());
  }
}
