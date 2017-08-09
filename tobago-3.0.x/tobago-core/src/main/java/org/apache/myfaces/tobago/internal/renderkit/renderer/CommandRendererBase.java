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
import org.apache.myfaces.tobago.internal.component.AbstractUIButton;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
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

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (parentOfCommands) { // extra span
      writer.startElement(HtmlElements.SPAN);
      writer.writeIdAttribute(clientId);
      if (component instanceof AbstractUIButton) { // todo: check if we should differ here
        writer.writeClassAttribute(BootstrapClass.BTN_GROUP);
      } else {
        writer.writeClassAttribute(BootstrapClass.DROPDOWN);
      }
    }

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
      // XXX BootstrapClass.NAV_LINK should only be shown inside of UICommands or UIButtons
      cssItems.add(BootstrapClass.DROPDOWN_TOGGLE);
      writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
    }
    addCssItems(facesContext, command, cssItems);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, command);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    cssItems.add(command.getCustomClass());

    writer.writeClassAttribute(
        Classes.create(command),
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

    if (parentOfCommands) {
      writer.startElement(HtmlElements.UL);
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_MENU);
      writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.MENU.toString(), false);
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand command = (AbstractUICommand) component;

    if (command.isParentOfCommands()) {
      final TobagoResponseWriter writer = getResponseWriter(facesContext);

      for (final UIComponent child : component.getChildren()) {
        if (child.isRendered()) {
          writer.startElement(HtmlElements.LI);
          final CssItem submenu;
          final CssItem disabled;
          if (child instanceof AbstractUICommand) {
            final AbstractUICommand c = (AbstractUICommand) child;
            submenu = c.isParentOfCommands() ? TobagoClass.DROPDOWN__SUBMENU : null;
            disabled = c.isDisabled() ? BootstrapClass.DISABLED : null;
          } else {
            submenu = null;
            disabled = null;
          }
          writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM, submenu, disabled);
          child.encodeAll(facesContext);
          writer.endElement(HtmlElements.LI);
        }
      }
    } else {
      super.encodeChildren(facesContext, component);
    }

  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand command = (AbstractUICommand) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (command.isParentOfCommands()) {
      writer.endElement(HtmlElements.UL);
      writer.endElement(HtmlElements.SPAN);
    }
  }

  protected void addCssItems(
      final FacesContext facesContext, final AbstractUICommand command, final List<CssItem> collected) {
  }

}
