/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class OutRenderer extends RendererBase {

// ------------------------------------------------------------------ constants

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    boolean escape
        = ComponentUtil.getBooleanAttribute(component, ATTR_ESCAPE);
    boolean createSpan = ComponentUtil.getBooleanAttribute(
        component, ATTR_CREATE_SPAN);

    if (createSpan) {
      writer.startElement("span", component);
      writer.writeAttribute("style", null, ATTR_STYLE);
      writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
      writer.writeAttribute("title", null, ATTR_TIP);
    }
    if (escape) {
      StringTokenizer tokenizer = new StringTokenizer(text, "\n\r");
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        writer.writeText(token, null);
        if (tokenizer.hasMoreTokens()) {
          writer.write("<br>");
        }
      }
    } else {
      writer.write(text);
    }
    if (createSpan) {
      writer.endElement("span");
    }
  }
}
