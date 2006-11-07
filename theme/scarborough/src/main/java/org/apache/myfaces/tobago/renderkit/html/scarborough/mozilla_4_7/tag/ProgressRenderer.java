package org.apache.myfaces.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
 * : $
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
      model = new DefaultBoundedRangeModel(4, 1, 0, 10);
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);

    writer.startElement(HtmlConstants.TR, null);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.STYLE, "background-color: #aabbcc;", null);
    writer.writeAttribute(HtmlAttributes.WIDTH, Integer.toString(model.getValue()), null);
    writer.write("&nbsp;");
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.STYLE, "background-color: #ddeeff;", null);
    writer.writeAttribute(HtmlAttributes.WIDTH,
        Integer.toString(model.getMaximum() - model.getValue()), null);
    writer.write("&nbsp;");
    writer.endElement(HtmlConstants.TD);

    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);
  }

}

