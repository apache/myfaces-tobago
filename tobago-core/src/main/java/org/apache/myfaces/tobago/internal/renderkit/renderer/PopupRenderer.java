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
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import java.io.IOException;

public class PopupRenderer<T extends AbstractUIPopup> extends CollapsiblePanelRendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final boolean collapsed = component.isCollapsed();
    final Markup markup = component.getMarkup();
    final boolean modal = component.isModal();
    final UIComponent labelFacet = ComponentUtils.getFacet(component, Facets.label);
    final UIComponent barFacet = ComponentUtils.getFacet(component, Facets.bar);
    final UIComponent footerFacet = ComponentUtils.getFacet(component, Facets.footer);

    if (modal) {
      ComponentUtils.putDataAttribute(component, "bs-backdrop", "static");
      ComponentUtils.putDataAttribute(component, "bs-keyboard", "false");
    }

    writer.startElement(HtmlElements.TOBAGO_POPUP);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        BootstrapClass.MODAL,
        BootstrapClass.FADE,
        component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DIALOG.toString(), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    // todo: aria-labelledby
    writer.startElement(HtmlElements.DIV);
    // this id is needed for the <tobago-overlay> in tobago-behaviour.ts
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "dialog");
    writer.writeClassAttribute(
        BootstrapClass.MODAL_DIALOG,
        markup != null && markup.contains(Markup.EXTRA_LARGE) ? BootstrapClass.MODAL_XL : null,
        markup != null && markup.contains(Markup.LARGE) ? BootstrapClass.MODAL_LG : null,
        markup != null && markup.contains(Markup.SMALL) ? BootstrapClass.MODAL_SM : null);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DOCUMENT.toString(), false);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.MODAL_CONTENT);

    if (component.getCollapsedMode() != CollapseMode.none) {
      encodeHidden(writer, clientId, collapsed);
    }

    if (labelFacet != null || barFacet != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.MODAL_HEADER);

      writer.startElement(HtmlElements.H5);
      writer.writeClassAttribute(BootstrapClass.MODAL_TITLE);
      insideBegin(facesContext, Facets.label);
      if (labelFacet != null) {
        for (final UIComponent child : RenderUtils.getFacetChildren(labelFacet)) {
          child.encodeAll(facesContext);
        }
      }
      insideEnd(facesContext, Facets.label);
      writer.endElement(HtmlElements.H5);

      if (barFacet != null) {
        insideBegin(facesContext, Facets.bar);
        for (final UIComponent child : RenderUtils.getFacetChildren(barFacet)) {
          child.encodeAll(facesContext);
        }
        insideEnd(facesContext, Facets.bar);
      }

      writer.endElement(HtmlElements.DIV);
    }
    if (labelFacet != null || barFacet != null || footerFacet != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.MODAL_BODY);
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final UIComponent labelFacet = ComponentUtils.getFacet(component, Facets.label);
    final UIComponent barFacet = ComponentUtils.getFacet(component, Facets.bar);
    final UIComponent footerFacet = ComponentUtils.getFacet(component, Facets.footer);

    if (labelFacet != null || barFacet != null || footerFacet != null) {
      writer.endElement(HtmlElements.DIV);
    }
    if (footerFacet != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.MODAL_FOOTER);
      insideBegin(facesContext, Facets.footer);
      for (final UIComponent child : RenderUtils.getFacetChildren(footerFacet)) {
        child.encodeAll(facesContext);
      }
      insideEnd(facesContext, Facets.footer);
      writer.endElement(HtmlElements.DIV);
    }
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_POPUP);

  }
}
