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

import org.apache.myfaces.tobago.component.UINav;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandGroup;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.Aria;
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

    final UINav nav = (UINav) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = nav.getClientId(facesContext);
    final String navbarId = clientId + "::navbar";

    writer.startElement(HtmlElements.NAV);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CONTAINER_FLUID);

    encodeOpener(facesContext, nav, writer, navbarId);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(navbarId);
    writer.writeClassAttribute(BootstrapClass.COLLAPSE, BootstrapClass.NAVBAR_COLLAPSE);
// XXX writer.writeClassAttribute(BootstrapClass.COLLAPSE, BootstrapClass.NAVBAR_COLLAPSE, BootstrapClass.NAVBAR_TEXT);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final DivHelper helper = new DivHelper(writer);

    for (UIComponent child : component.getChildren()) {
      if (child.isRendered()) {
        if (child instanceof AbstractUIForm) {
          helper.mayEnd();
          encodeChildren(facesContext, child);
        } else if (child instanceof AbstractUICommandGroup) {
          helper.mayEnd();
          child.encodeAll(facesContext);
        } else {
          helper.mayStart();
          child.encodeAll(facesContext);
        }
      }
    }

    helper.mayEnd();
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NAV);
  }

  private void encodeOpener(
      FacesContext facesContext, UINav nav, TobagoResponseWriter writer, String navbarId) throws IOException {

    // todo: consolidate this rendering with ToolBarRenderer

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_HEADER);

    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_TOGGLE, BootstrapClass.COLLAPSED);
    writer.writeAttribute(DataAttributes.TOGGLE, "collapse", false);
    writer.writeAttribute(DataAttributes.TARGET, JQueryUtils.escapeIdForHtml(navbarId), true);
    writer.writeAttribute(Aria.EXPANDED, Boolean.FALSE.toString(), false);
    writer.writeAttribute(Aria.CONTROLS, navbarId, false);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.SR_ONLY);
    writer.writeText("Toggle navigation"); // todo: i18n
    writer.endElement(HtmlElements.SPAN);

    for (int i = 0; i < 3; i++) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute(BootstrapClass.ICON_BAR);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.BUTTON);

    final String image = nav.getImage();
    if (image != null) {
      final String src = ResourceManagerUtils.getImageWithPath(facesContext, image);
      if (src != null) {
        writer.startElement(HtmlElements.IMG);
        writer.writeClassAttribute(BootstrapClass.NAVBAR_BRAND);
        writer.writeAttribute(HtmlAttributes.SRC, src, true);
        writer.writeAttribute(HtmlAttributes.ALT, "", false);
        writer.endElement(HtmlElements.IMG);
      }
    }

    final String label = nav.getLabel();
    if (label != null) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute(BootstrapClass.NAVBAR_BRAND);
      writer.writeText(label);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.DIV);
  }

  /**
   * This class helps to put some tags of specific type into one DIV.
   */
  public static class DivHelper {

    private TobagoResponseWriter writer;

    private boolean isInDiv = false;


    public DivHelper(final TobagoResponseWriter writer) {
      this.writer = writer;
    }

    public void mayStart() throws IOException {
      if (!isInDiv) {
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(BootstrapClass.NAVBAR_FORM);
        isInDiv = true;
      }
    }

    public void mayEnd() throws IOException {
      if(isInDiv) {
        writer.endElement(HtmlElements.DIV);
      }
    }

  }

}
