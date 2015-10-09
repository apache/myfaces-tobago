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
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PanelRenderer extends RendererBase {

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIPanel panel = (UIPanel) component;
    for (final UIComponent child : panel.getChildren()) {
      RenderUtils.encode(facesContext, child);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIPanel panel = (UIPanel) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = panel.getClientId(facesContext);
    writer.startElement(HtmlElements.DIV, panel);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(panel), panel.getCustomClass());
    writer.writeStyleAttribute(panel.getStyle());

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, panel);
    if (panel.getTip() != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, panel.getTip(), true);
    }

    // TODO check ajax id?
    if (!FacesContextUtils.isAjax(facesContext)) {
      final UIComponent facetReload = panel.getFacet(Facets.RELOAD);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        final UIReload update = (UIReload) facetReload;
        writer.writeAttribute(DataAttributes.RELOAD, Integer.toString(update.getFrequency()), false);
      }
    }
    HtmlRendererUtils.renderCommandFacet(panel, facesContext, writer);
    HtmlRendererUtils.encodeContextMenu(facesContext, writer, panel);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlElements.DIV);
  }
}
