package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class MessagesRenderer extends MessageRendererBase {

  private static final Log LOG = LogFactory.getLog(MessagesRenderer.class);


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

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContect is " + facesContext.getClass().getName());
    }
    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement("span", component);
      writer.writeClassAttribute("tobago-validation-message");
      writer.writeAttribute("style", null, ATTR_STYLE);

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
    writer.writeAttribute("title", message.getDetail(), null);
    writer.writeText(message.getSummary(), null);
    writer.endElement("label");
    writer.startElement("br", null);
    writer.endElement("br");
  }

// ///////////////////////////////////////////// bean getter + setter

}

