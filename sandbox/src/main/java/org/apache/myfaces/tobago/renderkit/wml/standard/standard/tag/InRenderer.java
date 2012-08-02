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

package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

/*
 * Created 07.02.2003 16:00:00.
 * : $
 */

import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PASSWORD;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InRenderer extends LayoutableRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    String clientId = component.getClientId(facesContext);

    UIPage uiPage = ComponentUtil.findPage(facesContext, component);

    if (uiPage != null) {
      uiPage.getPostfields().add(new DefaultKeyValue(clientId, clientId));
    }

    UIComponent label = component.getFacet(FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    String currentValue = ComponentUtil.currentValue(component);

    String type = ComponentUtil.getBooleanAttribute(
        component, ATTR_PASSWORD) ? "password" : "text";

    writer.startElement("input", component);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("value", currentValue, true);
    writer.writeAttribute("type", type, false);
    writer.endElement("input");


  }
}

