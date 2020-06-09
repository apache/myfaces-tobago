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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIBadge;
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUIButtons;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonsRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIButtons buttons = (AbstractUIButtons) component;
    final Markup markup = buttons.getMarkup();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(buttons.getClientId(facesContext));

    writer.writeClassAttribute(
        TobagoClass.BUTTONS,
        TobagoClass.BUTTONS.createMarkup(markup),
        Orientation.vertical.equals(buttons.getOrientation())
            ? BootstrapClass.BTN_GROUP_VERTICAL : BootstrapClass.BTN_GROUP,
        buttons.getCustomClass());
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.GROUP.toString(), false);
    if (buttons.isDisabled()) {
      writer.writeAttribute(HtmlAttributes.DISABLED, buttons.isDisabled());
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, buttons);
    final String tip = buttons.getTip();
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    for (final UIComponent child : component.getChildren()) {
      if (child.isRendered()) {
        if (child instanceof AbstractUIButton) {
          child.setRendererType(RendererTypes.ButtonInsideButtons.name());
          child.encodeAll(facesContext);
        } else if (child instanceof AbstractUIBadge) {
          child.setRendererType(RendererTypes.BadgeInsideButtons.name());
          child.encodeAll(facesContext);
        } else {
          child.encodeAll(facesContext);
        }
      }
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}
