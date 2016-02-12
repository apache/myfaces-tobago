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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import java.io.IOException;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class PopupRenderer extends RendererBase implements ComponentSystemEventListener {

  @Override
  public void processEvent(ComponentSystemEvent event) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIPopup popup = (UIPopup) event.getComponent();
    FacesContextUtils.addPopup(facesContext, popup);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final UIPopup popup = (UIPopup) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.POPUP, BootstrapClass.MODAL, BootstrapClass.FADE);
    writer.writeIdAttribute(popup.getClientId(facesContext));
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DIALOG.toString(), false);
    // todo: aria-labelledby
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.MODAL_DIALOG);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.DOCUMENT.toString(), false);
/*
    final Style style = new Style();
    style.setWidth(popup.getWidth());
    style.setHeight(popup.getHeight());
    writer.writeStyleAttribute(style);
*/
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.MODAL_CONTENT);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);

  }
}
