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
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BadgeRenderer<T extends AbstractUIBadge> extends DecorationPositionRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_BADGE;
  }

  @Override
  protected CssItem[] getComponentCss(final FacesContext facesContext, final T component) {
    if (!component.hasLabel()) {
      return getBadgeCss(facesContext, component);
    } else {
      return null;
    }
  }

  @Override
  protected String getFieldId(FacesContext facesContext, T component) {
    return component.getClientId(facesContext);
  }

  @Override
  protected boolean isOutputOnly(T component) {
    return true;
  }

  @Override
  protected void encodeBeginField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String icon = component.getIcon();
    final String image = component.getImage();
    final String tip = component.getTip();
    final String value = getCurrentValue(facesContext, component);

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
    HtmlRendererUtils.encodeIcon(writer, icon);
    HtmlRendererUtils.encodeIconOrImage(writer, image);
    if (value != null) {
      writer.startElement(HtmlElements.SPAN);
      if (component.hasLabel()) {
        writer.writeClassAttribute(null, getBadgeCss(facesContext, component));
      }
      writer.writeText(value);
      writer.endElement(HtmlElements.SPAN);
    }
  }

  @Override
  protected void encodeEndField(FacesContext facesContext, T component) throws IOException {
  }

  private CssItem[] getBadgeCss(final FacesContext facesContext, final T component) {
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;

    List<CssItem> cssItems = new ArrayList<>();
    cssItems.add(BootstrapClass.BADGE);
    cssItems.add(getBadgeColor(markup));
    cssItems.add(markup.contains(Markup.PILL) ? BootstrapClass.ROUNDED_PILL : null);
    cssItems.add(isInside(facesContext, HtmlElements.TOBAGO_BUTTONS) ? BootstrapClass.BTN : null);
    cssItems.add(component.getCustomClass());
    return cssItems.toArray(new CssItem[0]);
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
