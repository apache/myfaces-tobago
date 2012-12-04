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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIProgress;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;

public class ProgressRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(ProgressRenderer.class);

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIProgress component = (UIProgress) uiComponent;

    BoundedRangeModel model = (BoundedRangeModel) component.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(40, 1, 0, 100);
    }

    String image = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(model.getMaximum() - model.getValue());

    final int diff = model.getMaximum() - model.getMinimum();
    Object title = component.getAttributes().get(ATTR_TIP);
    if (title == null && diff > 0) {
      title = Integer.toString(100 * model.getValue() / diff) + " %";
    }

    Integer width = LayoutUtil.getLayoutWidth(component);

    String width1 = value1;
    String width2 = value2;

    if (width != null) {
      int value = diff > 0 ? (width - 1) * model.getValue() / diff : width;
      width1 = Integer.toString(value);
      width2 = Integer.toString((width - 2) - value);
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlConstants.SPAN, component);
    writer.writeClassAttribute();
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, String.valueOf(title), true);
    }

    writer.startElement(HtmlConstants.IMG, null);
    StyleClasses color1Classes = new StyleClasses();
    color1Classes.addClass("progress", "color1");
    color1Classes.addMarkupClass(component, "progress", "color1");
    writer.writeClassAttribute(color1Classes);
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.ALT, String.valueOf(title), true);
    }
    writer.writeAttribute(HtmlAttributes.STYLE, "width:" + width1 + "px", false);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.endElement(HtmlConstants.IMG);

    writer.startElement(HtmlConstants.IMG, null);
    StyleClasses color2Classes = new StyleClasses();
    color2Classes.addClass("progress", "color2");
    color2Classes.addMarkupClass(component, "progress", "color2");
    writer.writeClassAttribute(color2Classes);
    writer.writeAttribute(HtmlAttributes.SRC, image, false);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.ALT, String.valueOf(title), true);
    }
    writer.writeAttribute(HtmlAttributes.STYLE, "width:" + width2 + "px", false);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.endElement(HtmlConstants.IMG);

    writer.endElement(HtmlConstants.SPAN);
    UIComponent facet = component.getFacet("complete");
    if (model.getValue() == model.getMaximum() && facet != null
        && facet instanceof UICommand) {
      UICommand command = (UICommand) facet;
      writer.writeJavascript("Tobago.submitAction2(this, '" + command.getClientId(facesContext) + "', null, null);");
    }
  }
}
