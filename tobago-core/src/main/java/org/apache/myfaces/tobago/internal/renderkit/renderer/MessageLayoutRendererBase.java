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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.SupportsHelp;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneChoice;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.util.List;

public abstract class MessageLayoutRendererBase<T extends UIComponent & SupportsLabelLayout & SupportsAutoSpacing>
    extends LabelLayoutRendererBase<T> {

  @Override
  public void encodeBeginInternal(FacesContext facesContext, T component) throws IOException {
    if (isInside(facesContext, HtmlElements.COMMAND)) {
      encodeBeginField(facesContext, component);
    } else {
      super.encodeBeginInternal(facesContext, component);
    }
  }

  @Override
  public void encodeEndInternal(FacesContext facesContext, T component) throws IOException {
    if (isInside(facesContext, HtmlElements.COMMAND)) {
      encodeEndField(facesContext, component);
    } else {
      super.encodeEndInternal(facesContext, component);
    }
  }

  @Override
  public void encodeBeginMessageField(final FacesContext facesContext, final T component) throws IOException {
    encodeBeginMessagesContainer(facesContext, component);
    encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEndMessageField(final FacesContext facesContext, final T component) throws IOException {
    encodeEndField(facesContext, component);
    encodeEndMessagesContainer(facesContext, component);
  }

  private void encodeBeginMessagesContainer(final FacesContext facesContext, final T component)
      throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final String help = component instanceof SupportsHelp ? ((SupportsHelp) component).getHelp() : null;
    final boolean hasMessage = !messages.isEmpty();
    final boolean hasHelp = !StringUtils.isEmpty(help);

    if (hasMessage || hasHelp) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.MESSAGES__CONTAINER);
    }
  }

  private void encodeEndMessagesContainer(final FacesContext facesContext, final T component)
      throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId();
    final List<FacesMessage> messages = facesContext.getMessageList(clientId);
    final String help = component instanceof SupportsHelp ? ((SupportsHelp) component).getHelp() : null;
    final boolean hasMessage = !messages.isEmpty();
    final boolean hasHelp = !StringUtils.isEmpty(help);

    if (hasMessage || hasHelp) {
      Integer tabIndex = (component instanceof AbstractUIInput) ? ((AbstractUIInput) component).getTabIndex() : null;
      if (hasMessage) {
        encodePopover(writer,
            BootstrapClass.buttonColor(ComponentUtils.getMaximumSeverity(messages)),
            Icons.EXCLAMATION_LG, getTitle(messages), getMessage(messages), tabIndex);
      }
      if (hasHelp) {
        encodePopover(writer,
            BootstrapClass.BTN_OUTLINE_INFO, Icons.QUESTION_LG,
            ResourceUtils.getString(facesContext, "help.title"), help, tabIndex);
      }
      writer.endElement(HtmlElements.DIV);
    }
  }

  protected abstract void encodeBeginField(FacesContext facesContext, T component) throws IOException;

  protected abstract void encodeEndField(FacesContext facesContext, T component) throws IOException;

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

  private void encodePopover(
      final TobagoResponseWriter writer, final CssItem buttonColor, final Icons icon, final String title,
      final String content, final Integer tabIndex)
      throws IOException {
    writer.startElement(HtmlElements.TOBAGO_POPOVER);
    writer.writeAttribute(DataAttributes.BS_TOGGLE, "popover", false);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(DataAttributes.BS_CONTENT, content, true);
    writer.writeAttribute(DataAttributes.BS_TRIGGER, "focus", false);

    writer.startElement(HtmlElements.A);
    writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex != null ? tabIndex : 0);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlButtonTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN, buttonColor);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(icon);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.A);

    writer.endElement(HtmlElements.TOBAGO_POPOVER);
  }

  protected void encodeGroupAddon(
      final FacesContext facesContext, final TobagoResponseWriter writer,
      final UIComponent addon, final boolean isAfterFacet) throws IOException {
    if (addon != null) {
      for (final UIComponent child : RenderUtils.getFacetChildren(addon)) {
        insideBegin(facesContext, isAfterFacet ? Facets.after : Facets.before);
        if (child instanceof AbstractUIButton) {
          child.encodeAll(facesContext);
        } else if (child instanceof AbstractUIOut) {
          child.encodeAll(facesContext);
        } else if (child instanceof AbstractUISelectOneChoice) {
          child.encodeAll(facesContext);
        } else {
          writer.startElement(HtmlElements.SPAN);
          writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_TEXT);
          child.encodeAll(facesContext);
          writer.endElement(HtmlElements.SPAN);
        }
        insideEnd(facesContext, isAfterFacet ? Facets.after : Facets.before);
      }
    }
  }

}
