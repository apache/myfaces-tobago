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

import org.apache.myfaces.tobago.internal.component.AbstractUICollapsiblePanel;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.model.CollapseState;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class PanelRendererBase extends RendererBase {

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    super.decode(facesContext, component);

    final AbstractUICollapsiblePanel collapsible = (AbstractUICollapsiblePanel) component;
    final String clientId = collapsible.getClientId(facesContext);
    final String hiddenId = clientId + ComponentUtils.SUB_SEPARATOR + "collapse";

    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(hiddenId)) {
      final String newValue = requestParameterMap.get(hiddenId);
      if (StringUtils.isNotBlank(newValue)) {
        collapsible.setNextState(CollapseState.valueOf(newValue));
      }
    }
  }

  protected void encodeHidden(final TobagoResponseWriter writer, final String clientId) throws IOException {
    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "collapse");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "collapse");
    writer.endElement(HtmlElements.INPUT);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    if (((AbstractUICollapsiblePanel) component).getCollapsed().isSkipLifecycle()) {
      return;
    }
    super.encodeChildren(facesContext, component);
  }
}
