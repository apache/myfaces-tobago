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
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SeparatorRenderer extends LayoutComponentRendererBase {

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UISeparator separator = (UISeparator) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String label = getLabel(separator);

    if (label != null) {
      if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
        label = StringUtils.replace(label, " ", HtmlUtils.CHAR_NON_BEAKING_SPACE);
      }

      writer.startElement(HtmlElements.TABLE, component);
      writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
      writer.writeIdAttribute(separator.getClientId(facesContext));
      writer.writeClassAttribute(Classes.create(component));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
      final Style style = new Style(facesContext, separator);
      writer.writeStyleAttribute(style);

      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", false);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", false);
      writer.startElement(HtmlElements.TR, component);

      writer.startElement(HtmlElements.TD, component);
      writer.writeClassAttribute(Classes.create(component, "start"));
      writer.startElement(HtmlElements.HR , component);
      writer.endElement(HtmlElements.HR);
      writer.endElement(HtmlElements.TD);

      writer.startElement(HtmlElements.TD, component);
      writer.writeClassAttribute(Classes.create(component, "label"));
      writer.writeText(label);
      writer.endElement(HtmlElements.TD);

      writer.startElement(HtmlElements.TD, component);
      writer.writeClassAttribute(Classes.create(component, "end"));
      writer.startElement(HtmlElements.HR , component);
      writer.endElement(HtmlElements.HR);
      writer.endElement(HtmlElements.TD);

      writer.endElement(HtmlElements.TR);
      writer.endElement(HtmlElements.TABLE);
    } else {
      writer.startElement(HtmlElements.HR , component);
      writer.writeIdAttribute(separator.getClientId(facesContext));
      writer.writeClassAttribute(Classes.create(component));
      final Style style = new Style(facesContext, separator);
      style.setHeight(Measure.ZERO); // not nice
      writer.writeStyleAttribute(style);
      writer.endElement(HtmlElements.HR);
    }
  }

  private String getLabel(final UISeparator separator) {
    String label = separator.getLabel();
    if (label == null && separator.getFacet(Facets.LABEL) != null) {
      // deprecated
      Deprecation.LOG.warn("label facet in tc:separator is deprecated, use label attribute instead, please.");
      label = String.valueOf(((UILabel) separator.getFacet(Facets.LABEL)).getValue());
    }
    return label;
  }

  @Override
  public Measure getHeight(final FacesContext facesContext, final Configurable component) {
    final String label = getLabel((UISeparator) component);
    if (label == null) {
      return getResourceManager().getThemeMeasure(facesContext, component, "withoutLabelHeight");
    } else {
      return super.getHeight(facesContext, component);
    }
  }
}
