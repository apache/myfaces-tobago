/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.context.ResourceManagerUtil;
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
      model = new DefaultBoundedRangeModel(40,1,0,100);
    }

    String image = ResourceManagerUtil.getImage(facesContext, "1x1.gif");

    String value1 = Integer.toString(model.getValue());
    String value2 = Integer.toString(
        model.getMaximum() - model.getValue());
    String title = Integer.toString(100 * model.getValue() /
        (model.getMaximum() - model.getMinimum()))
        + " %";

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("span", null);
    // todo: use tobago standard class names
    writer.writeAttribute("class", "tobago-progress", null);
    writer.writeAttribute("title", title, null);

    writer.startElement("img", null);
    writer.writeAttribute("class", "tobago-progress-color1", null);
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("alt", title, null);
    writer.writeAttribute("width", value1, null);
    writer.writeAttribute("border", "0", null);
    writer.endElement("img");

    writer.startElement("img", null);
    writer.writeAttribute("class", "tobago-progress-color2", null);
    writer.writeAttribute("src", image, null);
    writer.writeAttribute("alt", title, null);
    writer.writeAttribute("width", value2, null);
    writer.writeAttribute("border", "0", null);
    writer.endElement("img");

    writer.endElement("span");
  }
  
// ///////////////////////////////////////////// bean getter + setter

}

