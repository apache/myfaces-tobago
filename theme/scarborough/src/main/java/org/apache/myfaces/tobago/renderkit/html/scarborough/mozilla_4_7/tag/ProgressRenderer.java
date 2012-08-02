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

package org.apache.myfaces.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

/*
 * Created 07.02.2003 16:00:00.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIProgress;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.IOException;

public class ProgressRenderer extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(ProgressRenderer.class);
       
  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

    UIProgress component = (UIProgress) uiComponent;

    BoundedRangeModel model = (BoundedRangeModel) component.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(4, 1, 0, 10);
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(model.getMaximum() - model.getValue());

    Integer width = LayoutUtil.getLayoutWidth(component);

    String width1 = value1;
    String width2 = value2;

    if (width != null) {
      int value = (width -1) * model.getValue()
          / (model.getMaximum() - model.getMinimum());
      width1 = Integer.toString(value);
      width2 = Integer.toString((width - 2) - value);
    }


    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);

    writer.startElement(HtmlConstants.TR, null);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeStyleAttribute("background-color: #aabbcc;");
    writer.writeAttribute(HtmlAttributes.WIDTH, width1, false);
    writer.writeText(TobagoConstants.CHAR_NON_BEAKING_SPACE);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeStyleAttribute("background-color: #ddeeff;");
    writer.writeAttribute(HtmlAttributes.WIDTH, width2, false);
    writer.writeText(TobagoConstants.CHAR_NON_BEAKING_SPACE);
    writer.endElement(HtmlConstants.TD);

    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);
    UIComponent facet = component.getFacet("complete");
    if (model.getValue() == model.getMaximum() && facet != null
        && facet instanceof UICommand) {
      UICommand command = (UICommand) facet;
      writer.writeJavascript("Tobago.submitAction2(this, '" + command.getClientId(facesContext) + "', null, null);");
    }
  }
}
