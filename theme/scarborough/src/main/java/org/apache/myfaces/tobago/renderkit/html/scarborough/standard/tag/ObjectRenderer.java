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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SRC;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_NAME;

public class ObjectRenderer extends LayoutableRendererBase {
  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.IFRAME, component);
    writer.writeAttribute(HtmlAttributes.FRAMEBORDER, "0", false);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    String name = (String) component.getAttributes().get(ATTR_NAME);
    if (name == null) {
      name = clientId;
    }
    writer.writeNameAttribute(name);
    Object src = component.getAttributes().get(ATTR_SRC);
    if (src != null) {
      writer.writeAttribute(HtmlAttributes.SRC, String.valueOf(src), true);
    } else {
      writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtil.getBlankPage(facesContext), false);
    }
    writer.writeClassAttribute();
    writer.writeStyleAttribute();

    String noframes = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.prefix");
    writer.writeText(noframes + " ");
    writer.startElement(HtmlConstants.A, component);
    if (component.getAttributes().get(ATTR_SRC) != null) {
      writer.writeAttributeFromComponent(HtmlAttributes.HREF, ATTR_SRC);
      writer.writeTextFromComponent(ATTR_SRC);
    }
    writer.endElement(HtmlConstants.A);
    noframes = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.postfix");
    writer.writeText(" " + noframes);

    writer.endElement(HtmlConstants.IFRAME);
  }
}
