package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 29, 2004 7:02:04 PM
 * User: bommel
 * $Id$
 */
public class TextRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.startElement("fo:block", component);
    writer.writeAttribute("font-size", "18pt", null);
    writer.writeAttribute("font-family", "sans-serif", null);
    writer.writeAttribute("line-height", "24pt", null);
    writer.writeAttribute("space-after.optimum", "15pt", null);
    writer.writeAttribute("background-color", "blue", null);
    writer.writeAttribute("color", "white", null);
    writer.writeAttribute("text-align", "center", null);
    writer.writeAttribute("padding-top", "3pt", null);
    writer.writeText(text, null);
    writer.endElement("fo:block");

  }

}
