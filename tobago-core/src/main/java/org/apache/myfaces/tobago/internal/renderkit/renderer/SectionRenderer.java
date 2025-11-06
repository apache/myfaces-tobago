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

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISection;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import java.io.IOException;

public class SectionRenderer<T extends AbstractUISection> extends CollapsiblePanelRendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final boolean collapsed = component.isCollapsed();
    final Markup markup = component.getMarkup();
    final boolean autoSpacing = component.getAutoSpacing(facesContext);
    final Integer level = component.getLevel();

    writer.startElement(HtmlElements.TOBAGO_SECTION);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(
        collapsed ? TobagoClass.COLLAPSED : null,
        autoSpacing ? TobagoClass.AUTO__SPACING : null,
        component.getCustomClass());
    writer.writeAttribute(DataAttributes.LEVEL, level);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    final HtmlElements tag;
    switch (level) {
      case 1:
        tag = HtmlElements.H1;
        break;
      case 2:
        tag = HtmlElements.H2;
        break;
      case 3:
        tag = HtmlElements.H3;
        break;
      case 4:
        tag = HtmlElements.H4;
        break;
      case 5:
        tag = HtmlElements.H5;
        break;
      default:
        tag = HtmlElements.H6;
    }

    if (component.getCollapsedMode() != CollapseMode.none) {
      encodeHidden(writer, clientId, collapsed);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.HEADER);
    writer.startElement(tag);

    final String image = component.getImage();
    HtmlRendererUtils.encodeIconOrImage(writer, image);

    final UIComponent labelFacet = ComponentUtils.getFacet(component, Facets.label);
    final String labelString = component.getLabel();
    if (labelFacet != null) {
      insideBegin(facesContext, Facets.label);
      for (final UIComponent child : RenderUtils.getFacetChildren(labelFacet)) {
        child.encodeAll(facesContext);
      }
      insideEnd(facesContext, Facets.label);
    } else if (labelString != null) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeText(labelString);
      writer.endElement(HtmlElements.SPAN);
    }
    writer.endElement(tag);

    final UIComponent bar = ComponentUtils.getFacet(component, Facets.bar);
    if (bar != null) {
      insideBegin(facesContext, Facets.bar);
      bar.encodeAll(facesContext);
      insideEnd(facesContext, Facets.bar);
    }

    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.SECTION__CONTENT);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_SECTION);
  }
}
