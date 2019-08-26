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

import org.apache.myfaces.tobago.internal.component.AbstractUIObject;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ObjectRenderer extends RendererBase {
  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIObject object = (AbstractUIObject) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = object.getMarkup();

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
    writer.writeAttribute(HtmlAttributes.SRC, object.getSrc(), true);
    writer.writeClassAttribute(
        TobagoClass.OBJECT,
        TobagoClass.OBJECT.createMarkup(markup),
        object.getCustomClass(),
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);

    writer.writeText(ResourceUtils.getString(facesContext, "object.noframe"));
    writer.writeText(" ");
    if (object.getSrc() != null) {
      writer.writeText(object.getSrc());
    }

    writer.endElement(HtmlElements.IFRAME);
  }
}
