package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    UIBox box = (UIBox) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    UIComponent label = box.getFacet(Facets.LABEL);
    String labelString = box.getLabel();
    UIPanel toolbar = (UIPanel) box.getFacet(Facets.TOOL_BAR);
    Style style = new Style(facesContext, box);
    if (toolbar != null) {
      Measure padding = getResourceManager().getThemeMeasure(facesContext, box, "paddingTopWhenToolbar");
      style.setPaddingTop(padding);
      style.setPaddingBottom(Measure.ZERO);
    }

    writer.startElement(HtmlElements.FIELDSET, box);
    writer.writeClassAttribute(Classes.create(box));
    writer.writeStyleAttribute(style);

    if (label != null || labelString != null) {
      writer.startElement(HtmlElements.LEGEND, box);
      writer.writeClassAttribute(Classes.create(box, "legend"));

      if (label != null) {
        RenderUtils.encode(facesContext, label);
      } else {
        writer.writeText(labelString);
      }
      writer.endElement(HtmlElements.LEGEND);
    }

    Style contentStyle = new Style(facesContext, box);
    if (toolbar != null) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(box, "toolbarOuter"));
      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(box, "toolbarInner"));
      toolbar.setRendererType(RendererTypes.BOX_TOOL_BAR);
      RenderUtils.encode(facesContext, toolbar);
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.DIV);
      if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
// XXX check for what is this, and delete or comment it
        contentStyle.setTop(Measure.valueOf(-10));
      }
    }
    writer.startElement(HtmlElements.DIV, box);
    writer.writeClassAttribute(Classes.create(box, "content")); // needed to be scrollable inside of the box
    final Measure offsetLeft = getOffsetLeft(facesContext, box);
    final Measure offsetRight = getOffsetRight(facesContext, box);
    final Measure offsetTop = getOffsetTop(facesContext, box);
    final Measure offsetBottom = getOffsetBottom(facesContext, box);
    contentStyle.setWidth(contentStyle.getWidth().subtract(offsetLeft).subtract(offsetRight));
    contentStyle.setHeight(contentStyle.getHeight().subtract(offsetTop).subtract(offsetBottom));
    contentStyle.setLeft(offsetLeft);
    contentStyle.setTop(offsetTop);
    writer.writeStyleAttribute(contentStyle);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.FIELDSET);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }
}
