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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIBox;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxRenderer extends PanelRendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIBox box = (AbstractUIBox) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final UIComponent label = ComponentUtils.getFacet(box, Facets.label);
    final String labelString = box.getLabel();
    final UIComponent bar = ComponentUtils.getFacet(box, Facets.bar);

    writer.startElement(HtmlElements.DIV);
    final boolean collapsed = box.isCollapsed();

    // TODO: optimize class attribute writing
    final List<CssItem> classAttributes = new ArrayList<CssItem>();
    classAttributes.add(TobagoClass.BOX);
    classAttributes.addAll(Arrays.asList(
        TobagoClass.BOX.createMarkup(ComponentUtils.updateMarkup(box, box.getMarkup()))));
    classAttributes.add(collapsed ? TobagoClass.COLLAPSED : null);
    classAttributes.add(BootstrapClass.CARD);
    classAttributes.add(box.getCustomClass());
    writer.writeClassAttribute(null, null, classAttributes.toArray(new CssItem[classAttributes.size()]));
    final String clientId = box.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, box);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, box);
    writer.writeStyleAttribute(box.getStyle());

    if (box.getCollapsedMode() != CollapseMode.none) {
      encodeHidden(writer, clientId, collapsed);
    }

    if (label != null || labelString != null || bar != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.CARD_HEADER, TobagoClass.BOX__HEADER);

      writer.startElement(HtmlElements.H3);
      if (labelString != null) {
        writer.writeText(labelString);
      }
      if (label != null) {
        RenderUtils.encode(facesContext, label);
      }
      writer.endElement(HtmlElements.H3);
      if (bar != null) {
        RenderUtils.encode(facesContext, bar);
      }

      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.CARD_BLOCK);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }
}
