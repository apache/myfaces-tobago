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

import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public abstract class MessageLayoutRendererBase extends LabelLayoutRendererBase {

  @Override
  public void encodeBeginMessageField(final FacesContext facesContext, final UIComponent component) throws IOException {
    encodeBeginSurroundingMessage(facesContext, component);
    encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEndMessageField(final FacesContext facesContext, final UIComponent component) throws IOException {
    encodeEndField(facesContext, component);
    encodeEndSurroundingMessage(facesContext, component);
  }

  protected void encodeBeginSurroundingMessage(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (!messages.isEmpty()) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.MESSAGES__CONTAINER, TobagoClass.FLEX_LAYOUT);
    }
  }

  protected void encodeEndSurroundingMessage(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (!messages.isEmpty()) {
      encodeMessages(writer, messages);
      writer.endElement(HtmlElements.DIV);
    }
  }

  protected abstract void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException;

  protected abstract void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException;

  private void encodeMessages(
          final TobagoResponseWriter writer, final List<FacesMessage> messages) throws IOException {
    writer.startElement(HtmlElements.A);
    writer.writeAttribute(HtmlAttributes.TABINDEX, "0", false);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(
        TobagoClass.MESSAGES__BUTTON,
        BootstrapClass.BTN,
        BootstrapClass.BTN_SECONDARY,
        getHighestSeverity(messages));
    writer.writeAttribute(DataAttributes.TOGGLE, "popover", false);
    writer.writeAttribute(DataAttributes.TITLE, getTitle(messages), true);
    writer.writeAttribute(DataAttributes.CONTENT, getMessage(messages), true);
    writer.writeIcon(null, FontAwesomeIconEncoder.generateClass("fa-exclamation"));
    writer.endElement(HtmlElements.A);
  }

  private BootstrapClass getHighestSeverity(final List<FacesMessage> messages) {
    FacesMessage.Severity highestSeverity = FacesMessage.SEVERITY_INFO;
    for (FacesMessage message : messages) {
      if (highestSeverity == null || message.getSeverity().getOrdinal() > highestSeverity.getOrdinal()) {
        highestSeverity = message.getSeverity();
      }
    }

    switch (highestSeverity.getOrdinal()) {
      case 1:
        return BootstrapClass.BTN_INFO;
      case 2:
        return BootstrapClass.BTN_WARNING;
      default:
        return BootstrapClass.BTN_DANGER;
    }
  }

  private String getTitle(final List<FacesMessage> messages) {
    int fatalCount = 0;
    int errorCount = 0;
    int warningCount = 0;
    int informationCount = 0;

    for (FacesMessage message : messages) {
      if (FacesMessage.SEVERITY_FATAL.equals(message.getSeverity())) {
        fatalCount++;
      } else if (FacesMessage.SEVERITY_ERROR.equals(message.getSeverity())) {
        errorCount++;
      } else if (FacesMessage.SEVERITY_WARN.equals(message.getSeverity())) {
        warningCount++;
      } else if (FacesMessage.SEVERITY_INFO.equals(message.getSeverity())) {
        informationCount++;
      }
    }

    StringBuilder stringBuilder = new StringBuilder();

    if (messages.size() > 1) {
      if (fatalCount > 0) {
        stringBuilder.append(fatalCount);
        stringBuilder.append(" Fatal");

        if (errorCount + warningCount + informationCount > 0) {
          stringBuilder.append(", ");
        }
      }

      if (errorCount > 0) {
        stringBuilder.append(errorCount);
        stringBuilder.append(" Error");
        if (errorCount > 1) {
          stringBuilder.append("s");
        }

        if (warningCount + informationCount > 0) {
          stringBuilder.append(", ");
        }
      }

      if (warningCount > 0) {
        stringBuilder.append(warningCount);
        stringBuilder.append(" Warning");
        if (warningCount > 1) {
          stringBuilder.append("s");
        }

        if (informationCount > 0) {
          stringBuilder.append(", ");
        }
      }

      if (informationCount > 0) {
        stringBuilder.append(informationCount);
        stringBuilder.append(" Information");
      }
    } else {
      if (fatalCount == 1) {
        stringBuilder.append("Fatal");
      } else if (errorCount == 1) {
        stringBuilder.append("Error");
      } else if (warningCount == 1) {
        stringBuilder.append("Warning");
      } else if (informationCount == 1) {
        stringBuilder.append("Information");
      }
    }
    return stringBuilder.toString();
  }

  private String getMessage(final List<FacesMessage> messages) {
    StringBuilder stringBuilder = new StringBuilder();
    for (FacesMessage message : messages) {
      stringBuilder.append(message.getDetail());
      stringBuilder.append("\n\n");
    }
    return stringBuilder.toString();
  }
}
