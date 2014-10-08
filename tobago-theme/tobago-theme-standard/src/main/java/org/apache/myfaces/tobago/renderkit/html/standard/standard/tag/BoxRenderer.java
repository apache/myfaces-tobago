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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIBox box = (UIBox) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final UIComponent label = box.getFacet(Facets.LABEL);
    final String labelString = box.getLabel();
    final UIPanel toolbar = (UIPanel) box.getFacet(Facets.TOOL_BAR);
    final Style style = new Style(facesContext, box);
    if (toolbar != null) {
      final Measure padding = getResourceManager().getThemeMeasure(facesContext, box, "paddingTopWhenToolbar");
      style.setPaddingTop(padding);
      style.setPaddingBottom(Measure.ZERO);
    }

    writer.startElement(HtmlElements.FIELDSET, box);
    writer.writeClassAttribute(Classes.create(box));
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, box);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, box);
    writer.writeStyleAttribute(style);

    if (label != null || labelString != null || toolbar != null) {
      writer.startElement(HtmlElements.LEGEND, box);
      writer.writeClassAttribute(Classes.create(box, "legend"));

      if (label != null) {
        RenderUtils.encode(facesContext, label);
      } else {
        writer.writeText(labelString);
      }

      if (toolbar != null) {
        writer.startElement(HtmlElements.DIV, null);
        writer.writeClassAttribute(Classes.create(box, "toolbarOuter"));
        writer.startElement(HtmlElements.DIV, null);
        writer.writeClassAttribute(Classes.create(box, "toolbarInner"));
        toolbar.setRendererType(RendererTypes.BOX_TOOL_BAR);
        RenderUtils.encode(facesContext, toolbar);
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.DIV);
      }

      writer.endElement(HtmlElements.LEGEND);
    }

    final UIMenuBar menuBar = ComponentUtils.findFacetDescendant(box, Facets.MENUBAR, UIMenuBar.class);
    if (menuBar != null) {
      RenderUtils.encode(facesContext, menuBar);
    }
    
    writer.startElement(HtmlElements.DIV, box);
    writer.writeClassAttribute(Classes.create(box, "content")); // needed to be scrollable inside of the box
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.FIELDSET);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }
}
