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

import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIImage;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
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
    boolean fontAwesome = StringUtils.startsWith(value, "fa-");
    if (fontAwesome) {
      // todo: should not be static
      writer.writeIcon(null, image.getStyle(), FontAwesomeIconEncoder.generateClass(value));
    } else {
      final String alt = image.getAlt();
      writer.startElement(HtmlElements.IMG);
      writer.writeIdAttribute(image.getClientId(facesContext));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, image);
      writer.writeAttribute(HtmlAttributes.SRC, value, true);
      writer.writeAttribute(HtmlAttributes.ALT, alt != null ? alt : "", true);
      final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, image);
      if (title != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      }
      writer.writeClassAttribute(
          Classes.create(image),
          isDisabled(image) ? BootstrapClass.DISABLED : null,
          image.getCustomClass());
      writer.writeStyleAttribute(image.getStyle());
      writer.endElement(HtmlElements.IMG);
    }
  }

  private boolean isDisabled(final AbstractUIImage image) {
    return image.isDisabled()
        || (image.getParent() instanceof AbstractUICommandBase
        && ((AbstractUICommandBase) image.getParent()).isDisabled());
  }
}
