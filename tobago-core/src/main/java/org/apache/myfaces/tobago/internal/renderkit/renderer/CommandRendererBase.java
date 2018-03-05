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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIBadge;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio;
import org.apache.myfaces.tobago.internal.component.AbstractUISeparator;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandRendererBase extends DecodingCommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(CommandRendererBase.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand command = (AbstractUICommand) component;
    final String clientId = command.getClientId(facesContext);
    final boolean disabled = command.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final boolean anchor = (command.getLink() != null || command.getOutcome() != null) && !disabled;
    final String target = command.getTarget();
    final boolean parentOfCommands = command.isParentOfCommands();
    final boolean dropdownSubmenu = this instanceof LinkInsideCommandRenderer;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    encodeBeginOuter(facesContext, command);

    if (anchor) {
      writer.startElement(HtmlElements.A);
    } else {
      writer.startElement(HtmlElements.BUTTON);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    }
    writer.writeIdAttribute(command.getFieldId(facesContext));
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    if (!disabled) {
      if (anchor) {
        final String href = RenderUtils.generateUrl(facesContext, command);
        writer.writeAttribute(HtmlAttributes.HREF, href, false);
        writer.writeAttribute(HtmlAttributes.TARGET, target, false);

        command.setOmit(true);
      }

      writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, command)));

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
      }

      final int tabIndex = ComponentUtils.getIntAttribute(command, Attributes.tabIndex);
      if (tabIndex != 0) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }

    HtmlRendererUtils.writeDataAttributes(facesContext, writer, command);

    if (parentOfCommands) {
      writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
    }
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, command);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.writeClassAttribute(
        getRendererCssClass(),
        getRendererCssClass().createMarkup(command.getMarkup()),
        parentOfCommands ? null : getOuterCssItems(facesContext, command),
        getCssItems(facesContext, command),
        parentOfCommands && !dropdownSubmenu ? BootstrapClass.DROPDOWN_TOGGLE : null,
        command.getCustomClass());

    final boolean defaultCommand = ComponentUtils.getBooleanAttribute(command, Attributes.defaultCommand);
    if (defaultCommand) {
      final AbstractUIFormBase form = ComponentUtils.findAncestor(command, AbstractUIFormBase.class);
      writer.writeAttribute(DataAttributes.DEFAULT, form.getClientId(facesContext), false);
    }

    final String image = ComponentUtils.getStringAttribute(command, Attributes.image);
    HtmlRendererUtils.encodeIconOrImage(writer, image);

    if (label.getLabel() != null) {
      writer.startElement(HtmlElements.SPAN);
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      writer.endElement(HtmlElements.SPAN);
      encodeBadge(facesContext, command);
    }

    if (anchor) {
      writer.endElement(HtmlElements.A);
    } else {
      writer.endElement(HtmlElements.BUTTON);
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUICommand command = (AbstractUICommand) component;
    final boolean parentOfCommands = command.isParentOfCommands();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {
      List<UIComponent> renderLater = null;

      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(
          BootstrapClass.DROPDOWN_MENU,
          getDropdownCssItems(facesContext, command));
      writer.writeAttribute(Arias.LABELLEDBY, "dropdownMenuButton", false);

      for (final UIComponent child : component.getChildren()) {
        if (child.isRendered()
            && !(child instanceof UIParameter)
            && !(child instanceof AbstractUIBadge)) {
          if (child instanceof AbstractUIStyle) {
            if (renderLater == null) {
              renderLater = new ArrayList<>();
            }
            renderLater.add(child);
          } else if (child instanceof AbstractUILink) {
            child.setRendererType(RendererTypes.LinkInsideCommand.name());
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUISelectBooleanCheckbox) {
            child.setRendererType(RendererTypes.SelectBooleanCheckboxInsideCommand.name());
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUISelectManyCheckbox) {
            child.setRendererType(RendererTypes.SelectManyCheckboxInsideCommand.name());
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUISelectOneRadio) {
            child.setRendererType(RendererTypes.SelectOneRadioInsideCommand.name());
            child.encodeAll(facesContext);
          } else if (child instanceof AbstractUISeparator) {
            child.setRendererType(RendererTypes.SeparatorInsideCommand.name());
            child.encodeAll(facesContext);
          } else {
            writer.startElement(HtmlElements.DIV);
            writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
            child.encodeAll(facesContext);
            writer.endElement(HtmlElements.DIV);
          }
        }
      }
      writer.endElement(HtmlElements.DIV);

      if (renderLater != null) {
        for (UIComponent child : renderLater) {
          child.encodeAll(facesContext);
        }
      }
    } else {
      for (final UIComponent child : component.getChildren()) {
        if (!(child instanceof AbstractUIBadge)) {
          child.encodeAll(facesContext);
        }
      }
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUICommand command = (AbstractUICommand) component;
    encodeEndOuter(facesContext, command);
  }

  protected void encodeBeginOuter(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
    final String clientId = command.getClientId(facesContext);
    final boolean parentOfCommands = command.isParentOfCommands();
    final boolean dropdownSubmenu = this instanceof LinkInsideCommandRenderer;
    final boolean childOfButtonGroup = this instanceof ButtonInsideButtonsRenderer;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(command.getMarkup()), false);

      writer.writeClassAttribute(
          childOfButtonGroup ? null : dropdownSubmenu ? TobagoClass.DROPDOWN__SUBMENU : BootstrapClass.DROPDOWN,
          getOuterCssItems(facesContext, command));
    }
  }

  protected void encodeEndOuter(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    if (command.isParentOfCommands()) {
      writer.endElement(HtmlElements.SPAN);
    }
  }

  protected CssItem[] getOuterCssItems(final FacesContext facesContext, final AbstractUICommand command) {
    return null;
  }

  abstract TobagoClass getRendererCssClass();

  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUICommand command) {
    return null;
  }

  protected CssItem[] getDropdownCssItems(final FacesContext facesContext, final AbstractUICommand command) {
    return null;
  }

  protected void encodeBadge(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
  }
}
