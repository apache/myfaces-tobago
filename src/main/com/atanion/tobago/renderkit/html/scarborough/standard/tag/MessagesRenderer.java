/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.MessageRendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class MessagesRenderer extends MessageRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MessagesRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int count = 0;
    for (Iterator i = facesContext.getMessages(); i.hasNext(); i.next()) {
      count++;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
      LOG.debug("here are " + count + " messages");
    }
    return (count > 0)
        ? count * getConfiguredValue(facesContext, component, "messageHeight")
        : getConfiguredValue(facesContext, component, "fixedHeight");
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContect is " + facesContext.getClass().getName());
    }
    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement("span", component);
      writer.writeAttribute("class", "tobago-validation-message", null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

      // with id
      String focusId = null;
      Iterator clientIds = facesContext.getClientIdsWithMessages();
      while(clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        encodeMessagesForId(facesContext, writer, clientId);
        if (focusId == null) {
          focusId = clientId;
      }
      }
      if (focusId != null) {
        ComponentUtil.findPage(component).setFocusId(focusId);
      }

      writer.endElement("span");
    }
  }

  private void encodeMessagesForId(FacesContext facesContext,
      ResponseWriter writer, String clientId) throws IOException {
    Iterator iterator = facesContext.getMessages(clientId);
    while (iterator.hasNext()) {
      FacesMessage message = (FacesMessage) iterator.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("message = " + message.getSummary());
      }
      encodeMessage(writer, message, clientId);
    }
  }

  private void encodeMessage(ResponseWriter writer, FacesMessage message,
      String clientId)
      throws IOException {
    writer.startElement("label", null);
    if (clientId != null) {
      writer.writeAttribute("for", clientId, null);
    }
    writer.writeAttribute("title", message.getSummary(), null);
    writer.writeText(message.getSummary(), null);
    writer.endElement("label");
    writer.startElement("br", null);
    writer.endElement("br");
  }

// ///////////////////////////////////////////// bean getter + setter

}

