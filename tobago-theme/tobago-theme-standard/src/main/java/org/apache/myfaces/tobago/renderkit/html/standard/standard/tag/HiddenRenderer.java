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

import org.apache.myfaces.tobago.component.UIHidden;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

// TODO: Its not nice, that the base class use layout
public class HiddenRenderer extends InputRendererBase {

  protected void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException {

    final String clientId = component.getClientId(facesContext);
    final String value = RenderUtils.currentValue(component);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.INPUT);
    if (component instanceof UIHidden && ((UIHidden) component).isDisabled()) {
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
      final Style style = new Style();
      style.setDisplay(Display.none);
      writer.writeStyleAttribute(style);
      writer.writeAttribute(HtmlAttributes.DISABLED, true);
    } else {
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    }
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.VALUE, value != null ? value : "", true);
    writer.endElement(HtmlElements.INPUT);
  }

  protected void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException {
  }
}
