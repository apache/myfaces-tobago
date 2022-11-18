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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIImage;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class ImageRenderer<T extends AbstractUIImage> extends RendererBase<T> {

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String value = component.getUrl();
    final boolean isIcon = Icons.matches(value);
    final boolean disabled = component.isDisabled()
        || component.getParent() instanceof AbstractUICommandBase
        && ((AbstractUICommandBase) component.getParent()).isDisabled();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final Markup markup = component.getMarkup();
    if (isIcon) {
      writer.startElement(HtmlElements.I);
      writer.writeIdAttribute(component.getClientId(facesContext));
      writer.writeClassAttribute(
          Icons.custom(value),
          disabled ? BootstrapClass.DISABLED : null,
          component.getCustomClass());
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      writer.endElement(HtmlElements.I);
    } else {
      final String alt = component.getAlt();
      writer.startElement(HtmlElements.TOBAGO_IMAGE);
      writer.writeIdAttribute(component.getClientId(facesContext));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
      writer.writeClassAttribute(
          disabled ? BootstrapClass.DISABLED : null,
          component.getCustomClass());
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      writer.startElement(HtmlElements.IMG);
      writer.writeAttribute(HtmlAttributes.SRC, value, true);
      writer.writeAttribute(HtmlAttributes.ALT, alt != null ? alt : "", true);
      writer.endElement(HtmlElements.IMG);
      writer.endElement(HtmlElements.TOBAGO_IMAGE);
    }
  }
}
