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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.model.CollapseState;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class PanelRenderer extends PanelRendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIPanel panel = (UIPanel) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final String clientId = panel.getClientId(facesContext);
    final CollapseState collapsed = panel.getCollapsed();

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        Classes.create(panel),
        panel.getCustomClass(),
        collapsed == CollapseState.visible ? null : TobagoClass.COLLAPSED);
    writer.writeStyleAttribute(panel.getStyle());

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, panel);
    if (panel.getTip() != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, panel.getTip(), true);
    }

    // TODO check ajax id?
    if (!FacesContextUtils.isAjax(facesContext)) {
      final UIComponent facetReload = ComponentUtils.getFacet(panel, Facets.reload);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        final UIReload update = (UIReload) facetReload;
        writer.writeAttribute(DataAttributes.RELOAD, Integer.toString(update.getFrequency()), false);
      }
    }
    HtmlRendererUtils.renderCommandFacet(panel, facesContext, writer);
    HtmlRendererUtils.encodeContextMenu(facesContext, writer, panel);

    encodeHidden(writer, clientId);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}
