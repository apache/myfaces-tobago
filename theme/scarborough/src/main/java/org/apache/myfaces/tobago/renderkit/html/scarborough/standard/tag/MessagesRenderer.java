package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_GLOBAL_ONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DETAIL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_SUMMARY;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
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

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContect is " + facesContext.getClass().getName());
    }
    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement(HtmlConstants.SPAN, component);
      writer.writeClassAttribute("tobago-validation-message");
      writer.writeStyleAttribute();

      // with id
      String focusId = null;
      Iterator clientIds;
      if (ComponentUtil.getBooleanAttribute(component, ATTR_GLOBAL_ONLY)) {
        ArrayList<String> list = new ArrayList<String>(1);
        list.add(null);
        clientIds = list.iterator();
      } else {
        clientIds = facesContext.getClientIdsWithMessages();
      }
      boolean showSummary = ComponentUtil.getBooleanAttribute(component, ATTR_SHOW_SUMMARY);
      boolean showDetail = ComponentUtil.getBooleanAttribute(component, ATTR_SHOW_DETAIL);
      while(clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        encodeMessagesForId(facesContext, writer, clientId, showSummary, showDetail);
        if (focusId == null) {
          focusId = clientId;
        }
      }
      if (focusId != null) {
        ComponentUtil.findPage(facesContext, component).setFocusId(focusId);
      }

      writer.endElement(HtmlConstants.SPAN);
    }
  }

  private void encodeMessagesForId(FacesContext facesContext,
      TobagoResponseWriter writer, String clientId, boolean showSummary, boolean showDetail) throws IOException {
    Iterator iterator = facesContext.getMessages(clientId);
    while (iterator.hasNext()) {
      FacesMessage message = (FacesMessage) iterator.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("message = " + message.getSummary());
      }
      encodeMessage(writer, message, clientId, showSummary, showDetail);
    }
  }

  private void encodeMessage(TobagoResponseWriter writer, FacesMessage message,
      String clientId, boolean showSummary, boolean showDetail)
      throws IOException {

    String summary = message.getSummary();
    String detail = message.getDetail();
    writer.startElement(HtmlConstants.LABEL, null);
    if (clientId != null) {
      writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, detail, true);
    boolean writeEmptyText = true;
    if (summary != null && showSummary) {
      writer.writeText(summary);
      writeEmptyText = false;
      if (detail != null && showDetail) {
        writer.writeText(" ");
      }
    }
    if (detail != null && showDetail) {
      writeEmptyText = false;
      writer.writeText(detail);
    }
    if (writeEmptyText) {
      writer.writeText("");
    }
    writer.endElement(HtmlConstants.LABEL);
    writer.startElement(HtmlConstants.BR, null);
    writer.endElement(HtmlConstants.BR);
  }

}

