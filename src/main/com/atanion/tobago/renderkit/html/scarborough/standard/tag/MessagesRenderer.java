/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.MessageRendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class MessagesRenderer extends MessageRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MessagesRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
    }
    int count = 0;
    for (Iterator i = facesContext.getMessages(); i.hasNext(); i.next()) {
      count++;
    }
    LOG.debug("here are " + count + " messages");
    return count > 0 ? count * 20: 1; // ie can't have td with 0px height
//    return count * 20; // fixme: depends on theme
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement("span", component);
      writer.writeAttribute("class", "tobago-validation-message", null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

      // with id
      Iterator clientIds = facesContext.getClientIdsWithMessages();
      while(clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        encodeMessagesForId(facesContext, writer, clientId);
      }

      // without id
      encodeMessagesForId(facesContext, writer, null);

      writer.endElement("span");
    }
  }

  private void encodeMessagesForId(FacesContext facesContext,
      ResponseWriter writer, String clientId) throws IOException {
    Iterator iterator = facesContext.getMessages(clientId);
    while (iterator.hasNext()) {
      FacesMessage message = (FacesMessage) iterator.next();
      encodeMessage(facesContext, writer, message, clientId);
    }
  }

  private void encodeMessage(FacesContext facesContext,
      ResponseWriter writer, FacesMessage message, String clientId)
      throws IOException {
    String formatString = TobagoResource.getProperty(facesContext, "tobago", message.getSummary());
    if (formatString.length() == 0) {
      formatString = message.getSummary();
    }
    writer.startElement("label", null);
    if (clientId != null) {
      writer.writeAttribute("for", clientId, null);
    }
    writer.writeText(formatString, null);
    writer.endElement("label");
    writer.startElement("br", null);
    writer.endElement("br");
  }

// ///////////////////////////////////////////// bean getter + setter

}

