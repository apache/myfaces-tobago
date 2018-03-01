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
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BadgeRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUIBadge badge = (AbstractUIBadge) component;
    final Markup markup = badge.getMarkup() != null ? badge.getMarkup() : Markup.NULL;
    final String value = RenderUtils.currentValue(badge);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(badge.getClientId(facesContext));
    writer.writeClassAttribute(
        TobagoClass.BADGE,
        TobagoClass.BADGE.createMarkup(markup),
        BootstrapClass.BADGE,
        getBadgeColor(markup),
        markup.contains(Markup.PILL) ? BootstrapClass.BADGE_PILL : null,
        getAdditionalCssItem(),
        badge.getCustomClass());

    if (value != null) {
      writer.writeText(value);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.SPAN);
  }

  protected CssItem getAdditionalCssItem() {
    return null;
  }

  private BootstrapClass getBadgeColor(final Markup markup) {
    if (markup.contains(Markup.NONE)) {
      return null;
    } else if (markup.contains(Markup.PRIMARY)) {
      return BootstrapClass.BADGE_PRIMARY;
    } else if (markup.contains(Markup.SECONDARY)) {
      return BootstrapClass.BADGE_SECONDARY;
    } else if (markup.contains(Markup.SUCCESS)) {
      return BootstrapClass.BADGE_SUCCESS;
    } else if (markup.contains(Markup.DANGER)) {
      return BootstrapClass.BADGE_DANGER;
    } else if (markup.contains(Markup.WARNING)) {
      return BootstrapClass.BADGE_WARNING;
    } else if (markup.contains(Markup.INFO)) {
      return BootstrapClass.BADGE_INFO;
    } else if (markup.contains(Markup.LIGHT)) {
      return BootstrapClass.BADGE_LIGHT;
    } else if (markup.contains(Markup.DARK)) {
      return BootstrapClass.BADGE_DARK;
    } else {
      return BootstrapClass.BADGE_SECONDARY;
    }
  }
}
