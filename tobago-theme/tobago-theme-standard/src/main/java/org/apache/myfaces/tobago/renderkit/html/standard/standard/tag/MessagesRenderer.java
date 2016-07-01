/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.UIMessages;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class MessagesRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MessagesRenderer.class);

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIMessages messages = (UIMessages) component;

    if (messages.isConfirmation()) {
      LOG.warn("'confirmation' is currently not supported for tc:messages!");
    }

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    final List<UIMessages.Item> messageList = messages.createMessageList(facesContext);

    if (messageList.size() > 0) { // in ie empty span gets a height
      writer.writeStyleAttribute(messages.getStyle());

      // with id
      /*String focusId = null;
      Iterator clientIds;
      if (ComponentUtils.getBooleanAttribute(messages, Attributes.globalOnly)) {
        ArrayList<String> list = new ArrayList<String>(1);
        list.add(null);
        clientIds = list.iterator();
      } else {
        clientIds = facesContext.getClientIdsWithMessages();
      }*/

      writer.startElement(HtmlElements.DIV);
      writer.writeIdAttribute(messages.getClientId(facesContext));
      writer.writeClassAttribute(Classes.create(messages), messages.getCustomClass());

      FacesMessage.Severity lastSeverity = null;
      boolean first = true;

      for (final UIMessages.Item item : messageList) {
        final FacesMessage message = item.getFacesMessage();
        final FacesMessage.Severity severity = message.getSeverity();

        if (!first && lastSeverity != severity) {
          writer.endElement(HtmlElements.DIV);
        }

        if (first || lastSeverity != severity) {
          writer.startElement(HtmlElements.DIV);
          writer.writeClassAttribute(
              BootstrapClass.ALERT, BootstrapClass.ALERT_DISMISSIBLE, BootstrapClass.alert(severity));
          HtmlRendererUtils.writeDataAttributes(facesContext, writer, messages);
          writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.ALERT.toString(), false);

          writer.startElement(HtmlElements.BUTTON);
          writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
          writer.writeClassAttribute(BootstrapClass.CLOSE);
          writer.writeAttribute(DataAttributes.DISMISS, "alert", false);
          writer.writeAttribute(Arias.ACTIVEDESCENDANT, "Close", false); // todo: i18n
          writer.startElement(HtmlElements.SPAN);
          writer.writeAttribute(Arias.HIDDEN, Boolean.TRUE.toString(), false);
          writer.writeText("×"); // times
          writer.endElement(HtmlElements.SPAN);
          writer.endElement(HtmlElements.BUTTON);

        }

        encodeMessage(writer, messages, message, item.getClientId());

        lastSeverity = severity;
        first = false;
      }
      writer.endElement(HtmlElements.DIV); // close open tag from for-loop

      writer.endElement(HtmlElements.DIV);
/*
      while(clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        encodeMessagesForId(facesContext, writer, clientId, showSummary, showDetail);
        if (focusId == null) {
          focusId = clientId;
        }
      }
  todo: don't forget: focus
      if (focusId != null) {
        ComponentUtils.findPage(facesContext, messages).setFocusId(focusId);
      }
*/
      if (messages.getFor() == null) {
        final String id = messages.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "messagesExists";
        writer.startElement(HtmlElements.INPUT);
        writer.writeAttribute(HtmlAttributes.VALUE, Boolean.TRUE.toString(), false);
        writer.writeAttribute(HtmlAttributes.ID, id, false);
        writer.writeAttribute(HtmlAttributes.NAME, id, false);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
        writer.endElement(HtmlElements.INPUT);
      }
    }
  }

  private void encodeMessage(
      final TobagoResponseWriter writer, final UIMessages messages, final FacesMessage message, final String clientId)
      throws IOException {

    final String summary = message.getSummary();
    final String detail = message.getDetail();
    writer.startElement(HtmlElements.LABEL);
    if (clientId != null) {
      writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, detail, true);
    boolean writeEmptyText = true;
    if (summary != null && messages.isShowSummary()) {
      writer.writeText(summary);
      writeEmptyText = false;
      if (detail != null && messages.isShowDetail()) {
        writer.writeText(" ");
      }
    }
    if (detail != null && messages.isShowDetail()) {
      writeEmptyText = false;
      writer.writeText(detail);
    }
    if (writeEmptyText) {
      writer.writeText("");
    }
    writer.endElement(HtmlElements.LABEL);

    message.rendered();
  }
}
