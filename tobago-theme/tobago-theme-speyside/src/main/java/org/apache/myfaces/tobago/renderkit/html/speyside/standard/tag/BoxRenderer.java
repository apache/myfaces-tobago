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

package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {

  /*
XXX TOBAGO3 todo: rewrite the doc here

with shadow

<div class="tobago-box" style="width: 100px; height: 100px">
  <div class="tobago-box-header">Label</div>

  <div class="tobago-box-content"
      style="position: absolute; top: 26px; left: 6px; width: 87px; height: 67px; background-color: blue;">
    Content
  </div>
</div>

without shadow

<div class="tobago-box" style="width: 100px; height: 100px">
  <span class="tobago-box-header">Label</span>

  <div style="position: absolute; top: 26px; left: 6px; width: 87px; height: 67px; background-color: blue;">
    Content
  </div>
</div>

   */
  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final UIBox box = (UIBox) component;

    final String clientId = box.getClientId(facesContext);
    writer.startElement(HtmlElements.DIV, box);
    writer.writeClassAttribute(Classes.create(box));
    writer.writeIdAttribute(clientId);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, box);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, box);
    writer.writeStyleAttribute(new Style(facesContext, box));
    encodeBox(facesContext, writer, box);
  }

  private void encodeBox(final FacesContext facesContext, final TobagoResponseWriter writer, final UIBox box)
      throws IOException {

    final UIComponent label = box.getFacet(Facets.LABEL);
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(box, "header"));
    final String labelString = (String) box.getAttributes().get(Attributes.LABEL);
    if (label != null) {
      RenderUtils.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString);
    }
    writer.endElement(HtmlElements.DIV);

    final UIMenuBar menuBar = getMenuBarFacet(box);
    if (menuBar != null) {
      RenderUtils.encode(facesContext, menuBar);
    }

    final UIPanel toolbar = (UIPanel) box.getFacet(Facets.TOOL_BAR);
    if (toolbar != null) {
      renderToolbar(facesContext, writer, box, toolbar);
    }

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(box, "content")); // needed to be scrollable inside of the box
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }

  protected void renderToolbar(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UIBox box, final UIPanel toolbar)
      throws IOException {
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(box, "headerToolBar"));
    toolbar.setRendererType(RendererTypes.BOX_TOOL_BAR);
    RenderUtils.encode(facesContext, toolbar);
    writer.endElement(HtmlElements.DIV);
  }
}
