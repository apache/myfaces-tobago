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
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUILabel;
import org.apache.myfaces.tobago.internal.component.AbstractUISeparator;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SeparatorRenderer extends RendererBase {

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUISeparator separator = (AbstractUISeparator) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = separator.getClientId(facesContext);
    final String label = getLabel(separator);
    final Markup markup = separator.getMarkup();

    if (label != null) {
      writer.startElement(HtmlElements.P);
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
      writer.writeClassAttribute(
          TobagoClass.SEPARATOR,
          TobagoClass.SEPARATOR.createMarkup(separator.getMarkup()),
          separator.getCustomClass());
      writer.writeText(label);
      writer.endElement(HtmlElements.P);
    } else {
      writer.startElement(HtmlElements.HR);
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
      writer.writeClassAttribute(
          TobagoClass.SEPARATOR,
          TobagoClass.SEPARATOR.createMarkup(separator.getMarkup()),
          separator.getCustomClass());
      writer.endElement(HtmlElements.HR);
    }
  }

  private String getLabel(final AbstractUISeparator separator) {
    String label = separator.getLabel();
    final UIComponent facet = ComponentUtils.getFacet(separator, Facets.label);
    if (label == null && facet != null) {
      label = String.valueOf(((AbstractUILabel) facet).getValue());
    }
    return label;
  }
}
