/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.MessageRendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class MessageRenderer extends MessageRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MessageRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
    }
    String clientId = null;
    if (component instanceof UIMessage) {
      clientId = ((UIMessage)component).getFor();
    } else if (component instanceof UIMessage) {
      clientId = ((UIMessage)component).getFor();
    }
    int count = 0;
    for (Iterator i = facesContext.getMessages(clientId); i.hasNext(); ) {
      count++;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("here are " + count + " messages");
    }
    return count * getConfiguredValue(facesContext, component, "messageHeight");
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {


    // fixme: must be refactored! Bitte daran denken die msie version auch umzubauen!!!

    UIMessage component = (UIMessage) uiComponent;

    String clientId = ComponentUtil.findClientIdFor(component, facesContext);

    Iterator iterator = facesContext.getMessages(clientId);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("span", component);
    writer.writeClassAttribute("tobago-validation-message");
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

    while (iterator.hasNext()) {
      FacesMessage message = (FacesMessage) iterator.next();
//      MessageFormat detail = new MessageFormat(formatString, tobagoContext.getLocale());
      writer.startElement("label", null);
      writer.writeAttribute("for", clientId, null);
      writer.writeAttribute("title", message.getDetail(), null);
      writer.writeText(message.getSummary(), null);
      writer.endElement("label");

      writer.startElement("br", null);
      writer.endElement("br");
    }
    writer.endElement("span");

  }
// ///////////////////////////////////////////// bean getter + setter

}

