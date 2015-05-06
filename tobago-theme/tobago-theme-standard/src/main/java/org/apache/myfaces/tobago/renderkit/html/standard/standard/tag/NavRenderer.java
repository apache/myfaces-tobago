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

import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.JQueryUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class NavRenderer extends RendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String navbarId = clientId + "::navbar";

    writer.startElement(HtmlElements.NAV, null);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(BootstrapClass.CONTAINER_FLUID);

    encodeOpener(writer, navbarId);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(navbarId);
    writer.writeClassAttribute(BootstrapClass.COLLAPSE, BootstrapClass.NAVBAR_COLLAPSE);
// XXX writer.writeClassAttribute(BootstrapClass.COLLAPSE, BootstrapClass.NAVBAR_COLLAPSE, BootstrapClass.NAVBAR_TEXT);

    writer.startElement(HtmlElements.UL, null);
    writer.writeClassAttribute(BootstrapClass.NAV, BootstrapClass.NAVBAR_NAV);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    for (UIComponent child : component.getChildren()) {
      if (child.isRendered()) {
        if (child instanceof AbstractUIForm) { // XXX hack! TBD: How to walk through the children, or do that in JS?
          encodeChildren(facesContext, child);
        } else {
          writer.startElement(HtmlElements.LI, null);
          child.encodeAll(facesContext);
          writer.endElement(HtmlElements.LI);
        }
      }
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.UL);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NAV);
  }

  private void encodeOpener(TobagoResponseWriter writer, String navbarId) throws IOException {

    // todo: consolidate this rendering with ToolBarRenderer

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_HEADER);

    writer.startElement(HtmlElements.BUTTON, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON, false);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_TOGGLE, BootstrapClass.COLLAPSED);
    writer.writeAttribute(DataAttributes.TOGGLE, "collapse", false);
    writer.writeAttribute(DataAttributes.TARGET, JQueryUtils.escapeIdForHtml(navbarId), true);
    writer.writeAttribute("aria-expanded", "false", false);
    writer.writeAttribute("aria-controls", navbarId, false);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(BootstrapClass.SR_ONLY);
    writer.writeText("Toggle navigation"); // todo: i18n
    writer.endElement(HtmlElements.SPAN);

    for (int i = 0; i < 3; i++) {
      writer.startElement(HtmlElements.SPAN, null);
      writer.writeClassAttribute(BootstrapClass.ICON_BAR);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.BUTTON);

    writer.endElement(HtmlElements.DIV);
  }

}
