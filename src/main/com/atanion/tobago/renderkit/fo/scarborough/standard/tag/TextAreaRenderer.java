package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:43:42 PM
 * User: bommel
 * $Id$
 */
public class TextAreaRenderer extends InputRendererBase {

  public void encodeEndTobago(FacesContext facesContext,
        UIComponent component) throws IOException {
      String text = ComponentUtil.currentValue(component);
      if (text == null) {
        text = "";
      }
      Layout layout = Layout.getLayout(component.getParent());
      layout.addMargin(200, 0, 0, 0);
      ResponseWriter writer = facesContext.getResponseWriter();
      writer.startElement("fo:block", component);
      writer.writeAttribute("font-size", "16pt", null);
      writer.writeAttribute("font-family", "sans-serif", null);
      //writer.writeAttribute("line-height", "24pt", null);
      //writer.writeAttribute("space-after.optimum", "15pt", null);
      writer.writeAttribute("background-color", "red", null);
      writer.writeAttribute("color", "white", null);
      writer.writeAttribute("text-align", "left", null);
      //writer.writeAttribute("padding-top", "3pt", null);
      writer.writeText("TextArea", null);
      writer.endElement("fo:block");

    }


}
