/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import java.io.IOException;

public class ProgressRenderer extends RendererBase implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ProgressRenderer.class);
       
// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectEnd(FacesContext facesContext,
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

