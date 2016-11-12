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

import org.apache.myfaces.tobago.component.UIObject;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ObjectRenderer extends RendererBase {
  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIObject object = (UIObject) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.IFRAME);
    writer.writeAttribute(HtmlAttributes.FRAMEBORDER, "0", false);
    final String clientId = object.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    String name = object.getName();
    if (name == null) {
      name = clientId;
    }
    writer.writeNameAttribute(name);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, object);
    final String src = object.getSrc();
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, src, true);
    } else {
      writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtils.getBlankPage(facesContext), false);
    }
    writer.writeClassAttribute(Classes.create(object), object.getCustomClass());
    writer.writeStyleAttribute(object.getStyle());

    String noframes = ResourceManagerUtils.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.prefix");
    writer.writeText(noframes);
    writer.writeText(" ");
    if (src != null) {
      writer.writeText(src);
    }
    noframes = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "browser.noframe.message.postfix");
    writer.writeText(" " + noframes);

    writer.endElement(HtmlElements.IFRAME);
  }
}
