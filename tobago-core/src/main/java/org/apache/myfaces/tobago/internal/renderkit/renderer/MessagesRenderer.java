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
import org.apache.myfaces.tobago.layout.MessageType;
import org.apache.myfaces.tobago.layout.Placement;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    final MessageType messageType = component.getType();
    final Placement placement = component.getPlacement();

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    final List<AbstractUIMessages.Item> messageList = component.createMessageList(facesContext);

    writer.startElement(HtmlElements.TOBAGO_MESSAGES);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
        component.getCustomClass(),
        BootstrapClass.toastPlacement(placement),
        MessageType.toast.equals(messageType) ? BootstrapClass.TOAST_CONTAINER : null,
        MessageType.toast.equals(messageType) ? BootstrapClass.POSITION_FIXED : null,
        MessageType.toast.equals(messageType) ? BootstrapClass.P_3 : null
    );

    if (component.getFor() == null) {
      final String id = component.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "messagesExists";
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.VALUE, Boolean.TRUE.toString(), false);
      writer.writeAttribute(HtmlAttributes.ID, id, false);
      writer.writeAttribute(HtmlAttributes.NAME, id, false);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      writer.endElement(HtmlElements.INPUT);
    }

    for (final AbstractUIMessages.Item item : messageList) {
      if (MessageType.toast.equals(messageType)) {
        renderToast(facesContext, component, writer, item);
      } else {
        renderAlert(facesContext, component, writer, item);
      }
    }

    writer.endElement(HtmlElements.TOBAGO_MESSAGES);
  }

  private void renderAlert(
      final FacesContext facesContext, final T component, final TobagoResponseWriter writer,
      final AbstractUIMessages.Item item
  ) throws IOException {
    final FacesMessage message = item.getFacesMessage();
    final FacesMessage.Severity severity = message.getSeverity();

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        BootstrapClass.ALERT,
        BootstrapClass.ALERT_DISMISSIBLE,
        BootstrapClass.alert(severity),
        BootstrapClass.FADE,
        BootstrapClass.SHOW);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.ALERT.toString(), false);

    encodeMessage(writer, component, message, item.getForId());

    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN_CLOSE);
    writer.writeAttribute(DataAttributes.BS_DISMISS, "alert", false);
    writer.writeAttribute(Arias.LABEL, "Close", false); // todo: i18n
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.DIV);
  }

  private void renderToast(
      final FacesContext facesContext, final T component, final TobagoResponseWriter writer,
      final AbstractUIMessages.Item item
  ) throws IOException {
    final FacesMessage message = item.getFacesMessage();
    final FacesMessage.Severity severity = message.getSeverity();

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.TOAST);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.ALERT.toString(), false);
    writer.writeAttribute(Arias.LIVE, "assertive", false);
    writer.writeAttribute(Arias.ATOMIC, true);
    writer.writeAttribute(DataAttributes.DISPOSE_DELAY, component.getDisposeDelay());

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.TOAST_HEADER);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.SQUARE,
        BootstrapClass.ROUNDED,
        BootstrapClass.ME_2,
        BootstrapClass.toast(severity));
    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.STRONG);
    writer.writeClassAttribute(BootstrapClass.ME_2);
    writer.write(getTitle(severity));
    writer.endElement(HtmlElements.STRONG);
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN_CLOSE, BootstrapClass.MS_AUTO);
    writer.writeAttribute(DataAttributes.BS_DISMISS, "toast", false);
    writer.writeAttribute(Arias.LABEL, "Close", false); //todo: i18n
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.TOAST_BODY);
    encodeMessage(writer, component, message, item.getForId());
    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);
  }

  private void encodeMessage(
      final TobagoResponseWriter writer, final AbstractUIMessages component, final FacesMessage message,
      final String forId) throws IOException {
    final String summary = message.getSummary();
    final String detail = message.getDetail();
    final boolean showSummary = summary != null && component.isShowSummary() && summary.length() > 0;
    final boolean showDetails = detail != null && component.isShowDetail() && detail.length() > 0;
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

  private String getTitle(final FacesMessage.Severity severity) {
    if (FacesMessage.SEVERITY_FATAL.equals(severity)) {
      return "Fatal";
    } else if (FacesMessage.SEVERITY_ERROR.equals(severity)) {
      return "Error";
    } else if (FacesMessage.SEVERITY_WARN.equals(severity)) {
      return "Warning";
    } else if (FacesMessage.SEVERITY_INFO.equals(severity)) {
      return "Information";
    } else {
      return null;
    }
  }
}
