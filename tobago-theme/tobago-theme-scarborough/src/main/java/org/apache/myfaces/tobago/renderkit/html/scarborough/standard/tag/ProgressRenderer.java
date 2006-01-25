/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
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
      model = new DefaultBoundedRangeModel(40,1,0,100);
    }

    String image = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(
        model.getMaximum() - model.getValue());


    String title = (String) component.getAttributes().get(ATTR_TIP);
    if (title == null) {
      title = Integer.toString(100 * model.getValue() /
          (model.getMaximum() - model.getMinimum()))
          + " %";
    }

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("span", null);
    // TODO: use tobago standard class names
    writer.writeClassAttribute("tobago-progress");
    writer.writeAttribute("title", title, null);

    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-progress-color1");
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("alt", title, null);
    writer.writeAttribute("width", value1, null);
    writer.writeAttribute("border", "0", null);
    writer.endElement("img");

    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-progress-color2");
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("alt", title, null);
    writer.writeAttribute("width", value2, null);
    writer.writeAttribute("border", "0", null);
    writer.endElement("img");

    writer.endElement("span");
  }
  
// ///////////////////////////////////////////// bean getter + setter

}

