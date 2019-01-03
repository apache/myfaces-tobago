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

import org.apache.myfaces.tobago.component.LabelLayout;
import org.apache.myfaces.tobago.component.SupportsHelp;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public abstract class MessageLayoutRendererBase extends LabelLayoutRendererBase {

  @Override
  public void encodeBeginMessageField(final FacesContext facesContext, final UIComponent component) throws IOException {

    final LabelLayout labelLayout = ((SupportsLabelLayout) component).getLabelLayout();
    final LabelLayout segment = LabelLayout.getSegment(facesContext);

    if (labelLayout == LabelLayout.segmentLeft && segment != LabelLayout.segmentRight
        || labelLayout == LabelLayout.segmentRight && segment != LabelLayout.segmentLeft) {
      return; // skip, because this component is the label
    }

    encodeBeginMessagesContainer(facesContext, component);
    encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEndMessageField(final FacesContext facesContext, final UIComponent component) throws IOException {

    final LabelLayout labelLayout = ((SupportsLabelLayout) component).getLabelLayout();
    final LabelLayout segment = LabelLayout.getSegment(facesContext);

    if (labelLayout == LabelLayout.segmentLeft && segment != LabelLayout.segmentRight
        || labelLayout == LabelLayout.segmentRight && segment != LabelLayout.segmentLeft) {
      return; // skip, because this component is the label
    }

    encodeEndField(facesContext, component);
    encodeEndMessagesContainer(facesContext, component);
  }

  private void encodeBeginMessagesContainer(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final String help = component instanceof SupportsHelp ? ((SupportsHelp) component).getHelp() : null;
    final boolean hasMessage = !messages.isEmpty();
    final boolean hasHelp = !StringUtils.isEmpty(help);

    if (hasMessage || hasHelp) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.MESSAGES__CONTAINER, TobagoClass.FLEX_LAYOUT, BootstrapClass.D_FLEX);
    }
  }

  private void encodeEndMessagesContainer(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final String help = component instanceof SupportsHelp ? ((SupportsHelp) component).getHelp() : null;
    final boolean hasMessage = !messages.isEmpty();
    final boolean hasHelp = !StringUtils.isEmpty(help);

    if (hasMessage || hasHelp) {
      if (hasMessage) {
        encodeFacesMessagesButton(writer, messages);
      }
      if (hasHelp) {
        encodeHelpButton(facesContext, writer, help);
      }
      writer.endElement(HtmlElements.DIV);
    }
  }

  protected abstract void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException;

  protected abstract void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException;

  private void encodeFacesMessagesButton(
      final TobagoResponseWriter writer, final List<FacesMessage> messages) throws IOException {
    writer.startElement(HtmlElements.A);
    writer.writeAttribute(HtmlAttributes.TABINDEX, "0", false);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(
        TobagoClass.MESSAGES__BUTTON,
        BootstrapClass.BTN,
        BootstrapClass.buttonColor(ComponentUtils.getMaximumSeverity(messages)));
    writer.writeAttribute(DataAttributes.TOGGLE, "popover", false);
    writer.writeAttribute(DataAttributes.TITLE, getTitle(messages), true);
    writer.writeAttribute(DataAttributes.CONTENT, getMessage(messages), true);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FA, Icons.EXCLAMATION);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.A);
  }

  private String getTitle(final List<FacesMessage> messages) {
    int fatalCount = 0;
    int errorCount = 0;
    int warningCount = 0;
    int informationCount = 0;

    for (final FacesMessage message : messages) {
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

    final StringBuilder stringBuilder = new StringBuilder();

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
    final StringBuilder stringBuilder = new StringBuilder();
    boolean firstMessage = true;
    for (final FacesMessage message : messages) {
      if (firstMessage) {
        firstMessage = false;
      } else {
        stringBuilder.append("\n\n");
      }
      stringBuilder.append(message.getDetail());
    }
    return stringBuilder.toString();
  }

  private void encodeHelpButton(final FacesContext facesContext, final TobagoResponseWriter writer, final String help)
      throws IOException {
    writer.startElement(HtmlElements.A);
    writer.writeAttribute(HtmlAttributes.TABINDEX, "0", false);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(
        TobagoClass.HELP__BUTTON,
        BootstrapClass.BTN,
        BootstrapClass.BTN_OUTLINE_INFO);
    writer.writeAttribute(DataAttributes.TOGGLE, "popover", false);
    writer.writeAttribute(DataAttributes.TITLE, ResourceUtils.getString(facesContext, "help.title"), true);
    writer.writeAttribute(DataAttributes.CONTENT, help, true);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FA, Icons.QUESTION);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.A);
  }
}
