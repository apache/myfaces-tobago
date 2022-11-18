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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIBar;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class BarRenderer<T extends AbstractUIBar> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    insideBegin(facesContext, HtmlElements.TOBAGO_BAR);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String navbarId = clientId + "::navbar";
    final Markup markup = component.getMarkup();

    writer.startElement(HtmlElements.TOBAGO_BAR);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        BootstrapClass.NAVBAR,
        getNavbarExpand(markup),
        getNavbarColorScheme(markup),
        component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CONTAINER_FLUID);

    encodeOpener(facesContext, component, writer, navbarId);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(navbarId);
    writer.writeClassAttribute(
        BootstrapClass.COLLAPSE,
        BootstrapClass.NAVBAR_COLLAPSE,
        BootstrapClass.ALIGN_ITEMS_CENTER);
  }

  private BootstrapClass getNavbarExpand(final Markup markup) {
    if (markup != null) {
      if (markup.contains(Markup.EXTRA_LARGE)) {
        return BootstrapClass.NAVBAR_EXPAND_XL;
      } else if (markup.contains(Markup.LARGE)) {
        return BootstrapClass.NAVBAR_EXPAND_LG;
      } else if (markup.contains(Markup.MEDIUM)) {
        return BootstrapClass.NAVBAR_EXPAND_MD;
      } else if (markup.contains(Markup.SMALL)) {
        return BootstrapClass.NAVBAR_EXPAND_SM;
      }
    }
    return BootstrapClass.NAVBAR_EXPAND;
  }

  private BootstrapClass getNavbarColorScheme(final Markup markup) {
    if (markup != null) {
      if (markup.contains(Markup.DARK)) {
        return BootstrapClass.NAVBAR_DARK;
      } else if (markup.contains(Markup.LIGHT)) {
        return BootstrapClass.NAVBAR_LIGHT;
      }
    }
    return null;
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final UIComponent after = ComponentUtils.getFacet(component, Facets.after);

    if (after != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.MY_LG_0, BootstrapClass.MS_AUTO);
      insideBegin(facesContext, Facets.after);
      after.encodeAll(facesContext);
      insideEnd(facesContext, Facets.after);
      writer.endElement(HtmlElements.DIV);
    }
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_BAR);

    insideEnd(facesContext, HtmlElements.TOBAGO_BAR);
  }

  private void encodeOpener(
      final FacesContext facesContext, final AbstractUIBar bar, final TobagoResponseWriter writer,
      final String navbarId)
      throws IOException {
    final boolean togglerLeft = bar.getMarkup() != null && bar.getMarkup().contains(Markup.TOGGLER_LEFT);
    final UIComponent brand = ComponentUtils.getFacet(bar, Facets.brand);

    if (brand != null && !togglerLeft) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute(BootstrapClass.NAVBAR_BRAND);
      brand.encodeAll(facesContext);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_TOGGLER);
    writer.writeAttribute(DataAttributes.TOGGLE, "collapse", false);
    writer.writeAttribute(DataAttributes.TARGET, "#" + navbarId, true);
    writer.writeAttribute(Arias.EXPANDED, Boolean.FALSE.toString(), false);
    writer.writeAttribute(Arias.CONTROLS, navbarId, false);
    writer.writeAttribute(Arias.LABEL, "Toggle navigation", false);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_TOGGLER_ICON);
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.BUTTON);

    if (brand != null && togglerLeft) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute(BootstrapClass.NAVBAR_BRAND);
      brand.encodeAll(facesContext);
      writer.endElement(HtmlElements.SPAN);
    }
  }
}
