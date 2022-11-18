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
import org.apache.myfaces.tobago.internal.component.AbstractUIFigure;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class FigureRenderer<T extends AbstractUIFigure> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.FIGURE);

    writer.writeClassAttribute(
        BootstrapClass.FIGURE,
        component.getCustomClass());
    final String tip = component.getTip();
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.FIGURE_IMG);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final UIComponent label = ComponentUtils.getFacet(component, Facets.label);
    final String labelString = component.getLabel();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);

    if (labelString != null || label != null) {
      writer.startElement(HtmlElements.FIGCAPTION);
      writer.writeClassAttribute(BootstrapClass.FIGURE_CAPTION);
      if (labelString != null) {
        writer.writeText(labelString);
      }
      if (label != null) {
        insideBegin(facesContext, Facets.label);
        label.encodeAll(facesContext);
        insideEnd(facesContext, Facets.label);
      }
      writer.endElement(HtmlElements.FIGCAPTION);
    }
    writer.endElement(HtmlElements.FIGURE);
  }
}
