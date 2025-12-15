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

import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;

public class LinkRenderer<T extends AbstractUILink> extends CommandRendererBase<T> {

  @Override
  protected CssItem getRendererCssClass() {
    return TobagoClass.LINK;
  }

  @Override
  protected CssItem[] getCssItems(final FacesContext facesContext, final T command) {
    final boolean disabled = command.isDisabled();
    final boolean anchor = (command.getLink() != null || command.getOutcome() != null) && !disabled;
    final boolean dropdownSubmenu = isInside(facesContext, HtmlElements.COMMAND);
    final boolean insideTobagoLinks = isInside(facesContext, HtmlElements.TOBAGO_LINKS);
    final Markup markup = command.getMarkup() != null ? command.getMarkup() : Markup.NULL;

    return new CssItem[]{
        !anchor && !dropdownSubmenu & !insideTobagoLinks ? BootstrapClass.BTN : null,
        !anchor && !dropdownSubmenu & !insideTobagoLinks ? BootstrapClass.BTN_LINK : null,
        BootstrapClass.textColor(markup),
        BootstrapClass.fontStyle(markup)
    };
  }
}
