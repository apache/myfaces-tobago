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
 * $Id:SelectBooleanCheckboxRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends LayoutableRendererBase {

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    boolean value = ComponentUtil.getBooleanAttribute(component, ATTR_VALUE);

    writer.startElement(HtmlConstants.SELECT, component);
    writer.writeNameAttribute(component.getClientId(facesContext));
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeAttribute("multiple", true);
    writer.startElement(HtmlConstants.OPTION, null);
    writer.writeAttribute("value", value ? "on" : "off", false);

    UIComponent label = component.getFacet(FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    writer.endElement(HtmlConstants.OPTION);
    writer.endElement(HtmlConstants.SELECT);
  }
}
