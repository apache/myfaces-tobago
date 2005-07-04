/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.html.InRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Iterator;

public class InRenderer extends InRendererBase{
  private static final Log LOG = LogFactory.getLog(InRenderer.class);

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

    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    String type = ComponentUtil.getBooleanAttribute(input,
        ATTR_PASSWORD) ? "password" : "text";

    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    String id = input.getClientId(facesContext);

    writer.startElement("input", input);
    writer.writeAttribute("type", type, null);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    if (currentValue != null) {
      writer.writeAttribute("value", currentValue, null);
    }
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("readonly",
        ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass( ATTR_STYLE_CLASS);
    if (onchange != null) {
      // todo: create and use utility method to write attributes without quoting
//      writer.writeAttribute("onchange", onchange, null);
    }
    writer.endElement("input");

    if (input.getConverter() != null) {
      Converter converter = input.getConverter();
      if (converter instanceof DateTimeConverter) {
        String pattern
            = ((DateTimeConverter) converter).getPattern();
        if (pattern != null) {
          writer.startElement("input", input);
          writer.writeAttribute("type", "hidden", null);
          writer.writeIdAttribute(id + ":converterPattern");
          writer.writeAttribute("value", pattern, null);
          writer.endElement("input");
        }
      }
    }
  }
}

