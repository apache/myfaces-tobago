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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIProgress;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.IOException;

public class ProgressRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ProgressRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIProgress progress = (UIProgress) component;

    BoundedRangeModel model = (BoundedRangeModel) progress.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(40, 1, 0, 100);
    }

    String image = ResourceManagerUtils.getImageWithPath(facesContext, "image/1x1.gif");

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(model.getMaximum() - model.getValue());

    Object title = progress.getAttributes().get(Attributes.TIP);
    if (title == null) {
      title = Integer.toString(100 * model.getValue()
          / (model.getMaximum() - model.getMinimum())) + " %";
    }

    Integer width = 100; // FIXME: make dynamic (was removed by changing the layouting
    LOG.error("100; // FIXME: make dynamic (was removed by changing the layouting");

    String width1 = value1;
    String width2 = value2;

    if (width != null) {
      int value = (width - 1) * model.getValue()
          / (model.getMaximum() - model.getMinimum());
      width1 = Integer.toString(value);
      width2 = Integer.toString((width - 2) - value);
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN, progress);
    writer.writeClassAttribute(Classes.create(progress));
    writer.writeAttribute(HtmlAttributes.TITLE, String.valueOf(title), true);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(progress, "color1"));
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    writer.writeAttribute(HtmlAttributes.ALT, String.valueOf(title), true);
    writer.writeAttribute(HtmlAttributes.WIDTH, width1, false);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(progress, "color2"));
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    writer.writeAttribute(HtmlAttributes.ALT, String.valueOf(title), true);
    writer.writeAttribute(HtmlAttributes.WIDTH, width2, false);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.endElement(HtmlElements.IMG);

    writer.endElement(HtmlElements.SPAN);
    UIComponent facet = progress.getFacet("complete");
    if (model.getValue() == model.getMaximum() && facet != null
        && facet instanceof UICommand) {
      UICommand command = (UICommand) facet;
      writer.writeJavascript("Tobago.submitAction(this, '" + command.getClientId(facesContext) + "');");
    }

  }

}
