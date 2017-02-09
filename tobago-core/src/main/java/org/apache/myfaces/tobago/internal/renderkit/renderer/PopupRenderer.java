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
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class PopupRenderer extends PanelRendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final AbstractUIPopup popup = (AbstractUIPopup) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = popup.getClientId(facesContext);
    final boolean collapsed = popup.isCollapsed();
    Markup popupMarkup = popup.getMarkup() != null ? popup.getMarkup() : Markup.NULL;

    ComponentUtils.putDataAttribute(popup, "backdrop", "static");

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        Classes.create(popup),
        BootstrapClass.MODAL,
        BootstrapClass.FADE,
        popup.getCustomClass());
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DIALOG.toString(), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, popup);
    // todo: aria-labelledby
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        BootstrapClass.MODAL_DIALOG,
        popupMarkup.contains(Markup.LARGE) ? BootstrapClass.MODAL_LG : null,
        popupMarkup.contains(Markup.SMALL) ? BootstrapClass.MODAL_SM : null);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DOCUMENT.toString(), false);
/*
    final Style style = new Style();
    style.setWidth(popup.getWidth());
    style.setHeight(popup.getHeight());
    writer.writeStyleAttribute(style);
*/
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.MODAL_CONTENT);

    if (popup.getCollapsedMode() != CollapseMode.none) {
      encodeHidden(writer, clientId, collapsed);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);

  }
}
