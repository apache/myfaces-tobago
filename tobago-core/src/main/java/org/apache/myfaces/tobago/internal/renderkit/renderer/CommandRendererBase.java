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
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio;
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
    final boolean link = command.getLink() != null && !disabled;
    final String target = command.getTarget();
    final boolean parentOfCommands = command.isParentOfCommands();
    final boolean dropdownSubmenu = this instanceof LinkInsideCommandRenderer;

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    encodeBeginOuter(facesContext, command);

    if (link) {
      writer.startElement(HtmlElements.A);
    } else {
      writer.startElement(HtmlElements.BUTTON);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    }
    writer.writeIdAttribute(command.getFieldId(facesContext));
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    if (!disabled) {
      if (link) {
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
    writer.writeStyleAttribute(command.getStyle());

    final List<CssItem> cssItems = new ArrayList<CssItem>();
    if (parentOfCommands) {
      cssItems.add(dropdownSubmenu ? null : BootstrapClass.DROPDOWN_TOGGLE);
      writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
    } else {
      addOuterCssItems(facesContext, command, cssItems);
    }
    addCssItems(facesContext, command, cssItems);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, command);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    cssItems.add(command.getCustomClass());

    writer.writeClassAttribute(
        getRendererCssClass(),
        null,
        cssItems.toArray(new CssItem[cssItems.size()]));

    final boolean defaultCommand = ComponentUtils.getBooleanAttribute(command, Attributes.defaultCommand);
    if (defaultCommand) {
      final AbstractUIFormBase form = ComponentUtils.findAncestor(command, AbstractUIFormBase.class);
      writer.writeAttribute(DataAttributes.DEFAULT, form.getClientId(facesContext), false);
    }

    final String image = ComponentUtils.getStringAttribute(command, Attributes.image);
    HtmlRendererUtils.encodeIconWithLabel(writer, facesContext, image, label, disabled);

    if (link) {
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
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_MENU);
      writer.writeAttribute(Arias.LABELLEDBY, "dropdownMenuButton", false);

      for (final UIComponent child : component.getChildren()) {
        if (!(child instanceof UIParameter) && child.isRendered()) {
          if (child instanceof AbstractUILink) {
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
          } else {
            writer.startElement(HtmlElements.DIV);
            writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM);
            child.encodeAll(facesContext);
            writer.endElement(HtmlElements.DIV);
          }
        }
      }
      writer.endElement(HtmlElements.DIV);
    } else {
      super.encodeChildren(facesContext, component);
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

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeIdAttribute(clientId);

      final List<CssItem> cssItemsForSpan = new ArrayList<CssItem>();
      addOuterCssItems(facesContext, command, cssItemsForSpan);
      writer.writeClassAttribute(
          dropdownSubmenu ? TobagoClass.DROPDOWN__SUBMENU : BootstrapClass.DROPDOWN,
          null,
          cssItemsForSpan.toArray(new CssItem[cssItemsForSpan.size()]));
    }
  }

  protected void encodeEndOuter(final FacesContext facesContext, final AbstractUICommand command) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    if (command.isParentOfCommands()) {
      writer.endElement(HtmlElements.SPAN);
    }
  }

  protected void addOuterCssItems(final FacesContext facesContext, final AbstractUICommand command,
                                  final List<CssItem> collected) {
  }

  abstract TobagoClass getRendererCssClass();

  protected void addCssItems(final FacesContext facesContext, final AbstractUICommand command,
                             final List<CssItem> collected) {
  }
}
