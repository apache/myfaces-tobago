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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIMessages;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class MessagesRenderer<T extends AbstractUIMessages> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    if (component.isConfirmation()) {
      LOG.warn("'confirmation' is currently not supported for tc:messages!");
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    final List<AbstractUIMessages.Item> messageList = component.createMessageList(facesContext);

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

    writer.startElement(HtmlElements.TOBAGO_MESSAGES);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(component.getCustomClass());

    FacesMessage.Severity lastSeverity = null;
    boolean first = true;

    for (final AbstractUIMessages.Item item : messageList) {
      final FacesMessage message = item.getFacesMessage();
      final FacesMessage.Severity severity = message.getSeverity();

      if (!first && lastSeverity != severity) {
        writer.endElement(HtmlElements.DIV);
      }

      if (first || lastSeverity != severity) {
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(
            BootstrapClass.ALERT, BootstrapClass.ALERT_DISMISSIBLE, BootstrapClass.alert(severity));
        HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
        writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.ALERT.toString(), false);

        writer.startElement(HtmlElements.BUTTON);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
        writer.writeClassAttribute(BootstrapClass.BTN_CLOSE);
        writer.writeAttribute(DataAttributes.DISMISS, "alert", false);
        writer.writeAttribute(Arias.ACTIVEDESCENDANT, "Close", false); // todo: i18n
        writer.endElement(HtmlElements.BUTTON);
      }

      encodeMessage(writer, component, message, item.getForId());

      lastSeverity = severity;
      first = false;
    }
    if (messageList.size() > 0) {
      writer.endElement(HtmlElements.DIV); // close open tag from for-loop
    }
    if (component.getFor() == null) {
      final String id = component.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "messagesExists";
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.VALUE, Boolean.TRUE.toString(), false);
      writer.writeAttribute(HtmlAttributes.ID, id, false);
      writer.writeAttribute(HtmlAttributes.NAME, id, false);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      writer.endElement(HtmlElements.INPUT);
    }
    writer.endElement(HtmlElements.TOBAGO_MESSAGES);
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
  }

  private void encodeMessage(
      final TobagoResponseWriter writer, final AbstractUIMessages messages, final FacesMessage message,
      final String forId)
      throws IOException {

    final String summary = message.getSummary();
    final String detail = message.getDetail();
    final boolean showSummary = summary != null && messages.isShowSummary() && summary.length() > 0;
    final boolean showDetails = detail != null && messages.isShowDetail() && detail.length() > 0;
    writer.startElement(HtmlElements.LABEL);
    writer.writeAttribute(HtmlAttributes.FOR, forId, false);
    writer.writeAttribute(HtmlAttributes.TITLE, detail, true);

    if (showSummary && showDetails && !summary.equals(detail)) {
      writer.startElement(HtmlElements.STRONG);
      writer.writeText(summary);
      writer.endElement(HtmlElements.STRONG);
      writer.writeText(detail);
    } else if (showSummary) {
      writer.writeText(summary);
    } else if (showDetails) {
      writer.writeText(detail);
    } else {
      writer.writeText(message.getSeverity().toString());
    }
    writer.endElement(HtmlElements.LABEL);

    message.rendered();
  }
}
