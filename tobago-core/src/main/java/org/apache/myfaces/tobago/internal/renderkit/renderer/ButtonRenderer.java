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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIBadge;
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class ButtonRenderer<T extends AbstractUIButton> extends CommandRendererBase<T> {

  @Override
  protected CssItem getRendererCssClass() {
    return TobagoClass.BUTTON;
  }

  @Override
  protected CssItem[] getOuterCssItems(final FacesContext facesContext, final T command) {
    return new CssItem[]{isInside(facesContext, HtmlElements.TOBAGO_BUTTONS) ? BootstrapClass.BTN_GROUP : null};
  }

  @Override
  protected CssItem[] getCssItems(final FacesContext facesContext, final T command) {
    final boolean defaultCommand = ComponentUtils.getBooleanAttribute(command, Attributes.defaultCommand);
    final Markup markup = command.getMarkup() != null ? command.getMarkup() : Markup.NULL;

    return new CssItem[]{
        BootstrapClass.BTN,
        getButtonColor(command.getMarkup(), defaultCommand),
        markup.contains(Markup.BADGE) ? BootstrapClass.BADGE : null,
        markup.contains(Markup.BADGE) && markup.contains(Markup.PILL) ? BootstrapClass.ROUNDED_PILL : null,
    };
  }

  private BootstrapClass getButtonColor(final Markup markup, final boolean defaultCommand) {
    if (markup != null) {
      if (markup.contains(Markup.NONE)) {
        return null;
      } else if (markup.contains(Markup.PRIMARY)) {
        return BootstrapClass.BTN_PRIMARY;
      } else if (markup.contains(Markup.SECONDARY)) {
        return BootstrapClass.BTN_SECONDARY;
      } else if (markup.contains(Markup.SUCCESS)) {
        return BootstrapClass.BTN_SUCCESS;
      } else if (markup.contains(Markup.DANGER)) {
        return BootstrapClass.BTN_DANGER;
      } else if (markup.contains(Markup.WARNING)) {
        return BootstrapClass.BTN_WARNING;
      } else if (markup.contains(Markup.INFO)) {
        return BootstrapClass.BTN_INFO;
      } else if (markup.contains(Markup.LIGHT)) {
        return BootstrapClass.BTN_LIGHT;
      } else if (markup.contains(Markup.DARK)) {
        return BootstrapClass.BTN_DARK;
      } else if (markup.contains(Markup.OUTLINE_PRIMARY)) {
        return BootstrapClass.BTN_OUTLINE_PRIMARY;
      } else if (markup.contains(Markup.OUTLINE_SECONDARY)) {
        return BootstrapClass.BTN_OUTLINE_SECONDARY;
      } else if (markup.contains(Markup.OUTLINE_SUCCESS)) {
        return BootstrapClass.BTN_OUTLINE_SUCCESS;
      } else if (markup.contains(Markup.OUTLINE_DANGER)) {
        return BootstrapClass.BTN_OUTLINE_DANGER;
      } else if (markup.contains(Markup.OUTLINE_WARNING)) {
        return BootstrapClass.BTN_OUTLINE_WARNING;
      } else if (markup.contains(Markup.OUTLINE_INFO)) {
        return BootstrapClass.BTN_OUTLINE_INFO;
      } else if (markup.contains(Markup.OUTLINE_LIGHT)) {
        return BootstrapClass.BTN_OUTLINE_LIGHT;
      } else if (markup.contains(Markup.OUTLINE_DARK)) {
        return BootstrapClass.BTN_OUTLINE_DARK;
      }
    }
    return defaultCommand ? BootstrapClass.BTN_PRIMARY : BootstrapClass.BTN_SECONDARY;
  }

  @Override
  protected void encodeBadge(FacesContext facesContext, T component) throws IOException {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof AbstractUIBadge) {
        child.encodeAll(facesContext);
      }
    }
  }
}
