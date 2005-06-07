/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.renderkit.html.InRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class TextAreaRenderer extends InRendererBase {



// ----------------------------------------------------------- business methods



  protected void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException {
    Iterator messages = facesContext.getMessages(
        input.getClientId(facesContext));
    StringBuffer stringBuffer = new StringBuffer();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      stringBuffer.append(message.getDetail());
    }

    String title = null;
    if (stringBuffer.length() > 0) {
      title = stringBuffer.toString();
    }


    title = HtmlRendererUtil.addTip(
            title, (String) input.getAttributes().get(ATTR_TIP));

    String clientId = input.getClientId(facesContext);
    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    writer.startElement("textarea", input);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("rows", null, ATTR_ROWS);
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("readonly", 
        ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    if (onchange != null) {
      writer.writeAttribute("onchange", onchange, null);
    }
    String currentValue = ComponentUtil.currentValue(input);
    if (currentValue != null) {
      // this is because browsers eat the first CR+LF of <textarea>
      if (currentValue.startsWith("\r\n")) {
        currentValue = "\r\n" + currentValue;
      } else if (currentValue.startsWith("\n")) {
        currentValue = "\n" + currentValue;
      } else if (currentValue.startsWith("\r")) {
        currentValue = "\r" + currentValue;
      }
      writer.writeText(currentValue, null);
    }
    writer.endElement("textarea");
  }
}

