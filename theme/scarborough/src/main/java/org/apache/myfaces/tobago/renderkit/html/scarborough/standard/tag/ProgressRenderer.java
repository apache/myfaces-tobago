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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.IOException;

public class ProgressRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(ProgressRenderer.class);

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIOutput component = (UIOutput) uiComponent;

    BoundedRangeModel model = (BoundedRangeModel) component.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(40, 1, 0, 100);
    }

    String image = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(
        model.getMaximum() - model.getValue());


    String title = (String) component.getAttributes().get(ATTR_TIP);
    if (title == null) {
      title = Integer.toString(100 * model.getValue()
          / (model.getMaximum() - model.getMinimum()))
          + " %";
    }

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.SPAN, null);
    // TODO: use tobago standard class names
    writer.writeClassAttribute("tobago-progress");
    writer.writeAttribute(HtmlAttributes.TITLE, title, null);

    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-progress-color1");
    writer.writeAttribute(HtmlAttributes.SRC, image, null);
    writer.writeAttribute(HtmlAttributes.ALT, title, null);
    writer.writeAttribute(HtmlAttributes.WIDTH, value1, null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.endElement(HtmlConstants.IMG);

    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-progress-color2");
    writer.writeAttribute(HtmlAttributes.SRC, image, null);
    writer.writeAttribute(HtmlAttributes.ALT, title, null);
    writer.writeAttribute(HtmlAttributes.WIDTH, value2, null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.endElement(HtmlConstants.IMG);

    writer.endElement(HtmlConstants.SPAN);
    UIComponent facet = component.getFacet("complete");
    if (model.getValue() == model.getMaximum() && facet != null
        && facet instanceof UICommand) {
      UICommand command = (UICommand) facet;
      HtmlRendererUtil.writeJavascript(writer, "Tobago.submitAction('" + command.getClientId(facesContext) + "');");
    }

  }

}

