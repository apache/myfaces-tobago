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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIBadge;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class BadgeRenderer<T extends AbstractUIBadge> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;
    final String tip = component.getTip();
    final String value = getCurrentValue(facesContext, component);

    writer.startElement(HtmlElements.TOBAGO_BADGE);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
      BootstrapClass.BADGE,
      getBadgeColor(markup),
      markup.contains(Markup.PILL) ? BootstrapClass.ROUNDED_PILL : null,
      isInside(facesContext, HtmlElements.TOBAGO_BUTTONS) ? BootstrapClass.BTN : null,
      component.getCustomClass());

    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
    if (value != null) {
      writer.writeText(value);
    }
  }

  @Override
  public void encodeEndInternal(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_BADGE);
  }

  private BootstrapClass getBadgeColor(final Markup markup) {
    if (markup.contains(Markup.NONE)) {
      return null;
    } else if (markup.contains(Markup.PRIMARY)) {
      return BootstrapClass.TEXT_BG_PRIMARY;
    } else if (markup.contains(Markup.SECONDARY)) {
      return BootstrapClass.TEXT_BG_SECONDARY;
    } else if (markup.contains(Markup.SUCCESS)) {
      return BootstrapClass.TEXT_BG_SUCCESS;
    } else if (markup.contains(Markup.DANGER)) {
      return BootstrapClass.TEXT_BG_DANGER;
    } else if (markup.contains(Markup.WARNING)) {
      return BootstrapClass.TEXT_BG_WARNING;
    } else if (markup.contains(Markup.INFO)) {
      return BootstrapClass.TEXT_BG_INFO;
    } else if (markup.contains(Markup.LIGHT)) {
      return BootstrapClass.TEXT_BG_LIGHT;
    } else if (markup.contains(Markup.DARK)) {
      return BootstrapClass.TEXT_BG_DARK;
    } else {
      return null;
    }
  }
}
