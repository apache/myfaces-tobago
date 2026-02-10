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
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ImageRenderer extends RendererBase {

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final AbstractUIImage image = (AbstractUIImage) component;
    final String value = image.getUrl();
    final boolean fontAwesome = StringUtils.startsWith(value, "fa-");
    final boolean disabled = image.isDisabled()
        || image.getParent() instanceof AbstractUICommandBase
        && ((AbstractUICommandBase) image.getParent()).isDisabled();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, image);
    final Markup markup = image.getMarkup();
    if (fontAwesome) {
      writer.startElement(HtmlElements.I);
      writer.writeIdAttribute(image.getClientId(facesContext));
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
      writer.writeClassAttribute(
          Icons.FA,
          Icons.custom(value),
          disabled ? BootstrapClass.DISABLED : null,
          image.getCustomClass());
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      writer.endElement(HtmlElements.I);
    } else {
      final String alt = image.getAlt();
      writer.startElement(HtmlElements.IMG);
      writer.writeIdAttribute(image.getClientId(facesContext));
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, image);
      writer.writeAttribute(HtmlAttributes.SRC, value, true);
      writer.writeAttribute(HtmlAttributes.ALT, alt != null ? alt : "", true);
      writer.writeClassAttribute(
          TobagoClass.IMAGE,
          TobagoClass.IMAGE.createMarkup(markup),
          disabled ? BootstrapClass.DISABLED : null,
          image.getCustomClass());
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      writer.endElement(HtmlElements.IMG);
    }
  }
}
