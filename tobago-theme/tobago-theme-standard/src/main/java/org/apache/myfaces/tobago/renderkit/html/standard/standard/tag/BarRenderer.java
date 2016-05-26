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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIBar;
import org.apache.myfaces.tobago.internal.component.AbstractUICommands;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.JQueryUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BarRenderer extends RendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final UIBar bar = (UIBar) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = bar.getClientId(facesContext);
    final String navbarId = clientId + "::navbar";

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, bar);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CONTAINER_FLUID);

    encodeOpener(facesContext, bar, writer, navbarId);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(navbarId);
    writer.writeClassAttribute(BootstrapClass.COLLAPSE, BootstrapClass.NAVBAR_TOGGLEABLE_XS);
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
        } else if (child instanceof AbstractUICommands) {
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
    writer.endElement(HtmlElements.DIV);
  }

  private void encodeOpener(
      FacesContext facesContext, UIBar bar, TobagoResponseWriter writer, String navbarId) throws IOException {

    // todo: consolidate this rendering with ToolBarRenderer

    writer.startElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.NAVBAR_TOGGLER, BootstrapClass.HIDDEN_SM_UP);
    writer.writeAttribute(DataAttributes.TOGGLE, "collapse", false);
    writer.writeAttribute(DataAttributes.TARGET, JQueryUtils.escapeIdForHtml(navbarId), true);
    writer.writeAttribute(Arias.EXPANDED, Boolean.FALSE.toString(), false);
    writer.writeAttribute(Arias.CONTROLS, navbarId, false);

    writer.writeText("☰");

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.SR_ONLY);
    writer.writeText("Toggle navigation"); // todo: i18n
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.BUTTON);

    final UIComponent brand = ComponentUtils.getFacet(bar, Facets.brand);
    if (brand != null) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute(BootstrapClass.NAVBAR_BRAND);
      RenderUtils.encode(facesContext, brand);
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
        writer.writeClassAttribute(BootstrapClass.FORM_INLINE);
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
