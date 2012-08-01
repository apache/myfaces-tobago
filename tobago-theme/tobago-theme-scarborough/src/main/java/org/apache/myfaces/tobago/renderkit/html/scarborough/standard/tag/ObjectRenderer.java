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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIObject;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ObjectRenderer extends LayoutComponentRendererBase {
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    UIObject object = (UIObject) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.IFRAME, object);
    writer.writeAttribute(HtmlAttributes.FRAMEBORDER, "0", false);
    writer.writeIdAttribute(object.getClientId(facesContext));
    writer.writeNameAttribute(object.getClientId(facesContext));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, object);
    Object src = object.getSrc();
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, String.valueOf(src), true);
    } else {
      writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtils.getBlankPage(facesContext), false);
    }
    writer.writeClassAttribute(Classes.create(object));
    Style style = new Style(facesContext, object);
    writer.writeStyleAttribute(style);

    String noframes = ResourceManagerUtils.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.prefix");
    writer.writeText(noframes + " ");
    writer.startElement(HtmlElements.A, object);
    if (src != null) {
      writer.writeAttributeFromComponent(HtmlAttributes.HREF, Attributes.SRC);
      writer.writeTextFromComponent(Attributes.SRC);
    }
    writer.endElement(HtmlElements.A);
    noframes = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "browser.noframe.message.postfix");
    writer.writeText(" " + noframes);

    writer.endElement(HtmlElements.IFRAME);
  }
}
