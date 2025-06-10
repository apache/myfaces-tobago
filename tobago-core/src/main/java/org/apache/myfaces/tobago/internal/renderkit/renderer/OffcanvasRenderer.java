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

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIOffcanvas;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.layout.OffcanvasPlacement;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import java.io.IOException;

public class OffcanvasRenderer<T extends AbstractUIOffcanvas> extends CollapsiblePanelRendererBase<T> {
  private static final String SUFFIX_LABEL = ComponentUtils.SUB_SEPARATOR + "label";

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final boolean collapsed = component.isCollapsed();
    final OffcanvasPlacement placement = component.getPlacement();
    final UIComponent labelFacet = ComponentUtils.getFacet(component, Facets.label);
    final UIComponent barFacet = ComponentUtils.getFacet(component, Facets.bar);

    writer.startElement(HtmlElements.TOBAGO_OFFCANVAS);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(BootstrapClass.OFFCANVAS,
        OffcanvasPlacement.top.equals(placement) ? BootstrapClass.OFFCANVAS_TOP : null,
        OffcanvasPlacement.left.equals(placement) ? BootstrapClass.OFFCANVAS_START : null,
        OffcanvasPlacement.right.equals(placement) ? BootstrapClass.OFFCANVAS_END : null,
        OffcanvasPlacement.bottom.equals(placement) ? BootstrapClass.OFFCANVAS_BOTTOM : null);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeAttribute(Arias.LABELLEDBY, component.getClientId(facesContext), false);

    if (component.getCollapsedMode() != CollapseMode.none) {
      encodeHidden(writer, clientId, collapsed);
    }

    if (labelFacet != null || barFacet != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.OFFCANVAS_HEADER);

      writer.startElement(HtmlElements.H1);
      writer.writeIdAttribute(component.getClientId(facesContext) + SUFFIX_LABEL);
      writer.writeClassAttribute(BootstrapClass.OFFCANVAS_TITLE);
      if (labelFacet != null) {
        insideBegin(facesContext, Facets.label);
        for (final UIComponent child : RenderUtils.getFacetChildren(labelFacet)) {
          child.encodeAll(facesContext);
        }
        insideEnd(facesContext, Facets.label);
      }
      writer.endElement(HtmlElements.H1);

      if (barFacet != null) {
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(TobagoClass.BAR);

        insideBegin(facesContext, Facets.bar);
        for (final UIComponent child : RenderUtils.getFacetChildren(barFacet)) {
          child.encodeAll(facesContext);
        }
        insideEnd(facesContext, Facets.bar);
        writer.endElement(HtmlElements.DIV);
      }

      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.OFFCANVAS_BODY);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_OFFCANVAS);
  }
}
