/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.*;
import java.io.IOException;

public class ProgressRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ProgressRenderer.class);
       
// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIOutput component = (UIOutput) uiComponent;

    BoundedRangeModel model = (BoundedRangeModel) component.getValue();

    if (model == null) {
      LOG.warn("'null' value found! Using dummy Model instead!");
      model = new DefaultBoundedRangeModel(4,1,0,10);
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("table", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);

    writer.startElement("tr", null);

    writer.startElement("td", null);
    writer.writeAttribute("style", "background-color: #aabbcc;", null);
    writer.writeAttribute("width", Integer.toString(model.getValue()), null);
    writer.write("&nbsp;");
    writer.endElement("td");

    writer.startElement("td", null);
    writer.writeAttribute("style", "background-color: #ddeeff;", null);
    writer.writeAttribute("width",
        Integer.toString(model.getMaximum() - model.getValue()), null);
    writer.write("&nbsp;");
    writer.endElement("td");

    writer.endElement("tr");
    writer.endElement("table");            
  }

// ///////////////////////////////////////////// bean getter + setter

}

