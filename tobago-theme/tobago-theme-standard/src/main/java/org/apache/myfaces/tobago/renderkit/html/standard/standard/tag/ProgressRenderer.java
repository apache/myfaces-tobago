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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIProgress;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
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

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIProgress progress = (UIProgress) component;

    BoundedRangeModel model = (BoundedRangeModel) progress.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(0, 1, 0, 100);
    }

    final int diff = model.getMaximum() - model.getMinimum();
    Object title = progress.getAttributes().get(Attributes.TIP);
    final double percent = 100.0 * model.getValue() / diff;
    if (title == null && diff > 0) {
      title = Integer.toString((int)percent) + " %";
    }

    final Style style = new Style(facesContext, progress);
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, progress);
    writer.writeClassAttribute(Classes.create(progress));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, progress);
    writer.writeStyleAttribute(style);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, String.valueOf(title), true);
    }
    final UIComponent facet = progress.getFacet("complete");
    if (model.getValue() == model.getMaximum() && facet instanceof UICommand) {
      HtmlRendererUtils.renderCommandFacet(progress, facesContext, writer);
    }
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(progress, "value"));
    writer.writeStyleAttribute("width: " + percent + "%");
    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);

  }
}
