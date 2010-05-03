package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

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

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InRenderer extends LayoutComponentRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String clientId = component.getClientId(facesContext);

    AbstractUIPage uiPage = ComponentUtils.findPage(facesContext, component);

    if (uiPage != null) {
      uiPage.getPostfields().add(new DefaultKeyValue(clientId, clientId));
    }

    UIComponent label = component.getFacet(Facets.LABEL);
    if (label != null) {
      RenderUtils.encode(facesContext, label);
    }

    String currentValue = RenderUtils.currentValue(component);

    String type = ComponentUtils.getBooleanAttribute(
        component, Attributes.PASSWORD) ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;

    writer.startElement("input", component);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("value", currentValue, true);
    writer.writeAttribute("type", type, false);
    writer.endElement("input");


  }
}

