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
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class FigureRenderer extends RendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final AbstractUIFigure figure = (AbstractUIFigure) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.FIGURE);
    writer.writeClassAttribute(Classes.create(figure), BootstrapClass.FIGURE, figure.getCustomClass());
    writer.writeStyleAttribute(figure.getStyle());
    final String tip = figure.getTip();
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.FIGURE_IMG);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final AbstractUIFigure figure = (AbstractUIFigure) component;
    final UIComponent label = ComponentUtils.getFacet(figure, Facets.label);
    final String labelString = figure.getLabel();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);

    if (labelString != null || label != null) {
      writer.startElement(HtmlElements.FIGCAPTION);
      writer.writeClassAttribute(BootstrapClass.FIGURE_CAPTION);
      if (labelString != null) {
        writer.writeText(labelString);
      }
      if (label != null) {
        RenderUtils.encode(facesContext, label);
      }
      writer.endElement(HtmlElements.FIGCAPTION);
    }
    writer.endElement(HtmlElements.FIGURE);
  }
}
