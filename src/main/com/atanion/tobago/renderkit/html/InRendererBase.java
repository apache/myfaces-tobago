/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package com.atanion.tobago.renderkit.html;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.RenderUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Iterator;

public class InRendererBase extends InputRendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(InRendererBase.class);

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

    String tip = (String) input.getAttributes().get(ATTR_TIP);
    if (tip != null) {
      if (title != null) {
        title += " :: ";
      }
      else {
        title = "";
      }
      title += tip;
    }

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
    writer.writeAttribute("name", id, null);
    writer.writeAttribute("id", id, null);
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
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
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
          writer.writeAttribute("id", id + ":converterPattern", null);
          writer.writeAttribute("value", pattern, null);
          writer.endElement("input");
        }
      }
    }
  }

  protected void renderPicker(FacesContext facesContext, UIComponent input,
      UIComponent picker) throws IOException {
    if (picker != null) {
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      String url
          = viewHandler.getActionURL(facesContext, ResourceManagerUtil.getJsp(facesContext, "datePicker.jsp"))
          + "?tobago.date.inputId="
          + input.getClientId(facesContext);
      String command = "calendarWindow('" + url + "');";
      picker.getAttributes().put(ATTR_ACTION_STRING, command);
      HtmlRendererUtil.createCssClass(facesContext, picker);
      RenderUtil.encode(facesContext, picker);
    }
  }

}

