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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUIToolBar;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandRendererBase extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(CommandRendererBase.class);
  private CssItem cssItems;

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("sourceId = '" + sourceId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (clientId.equals(sourceId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("queueEvent = '" + clientId + "'");
      }
      commandActivated(component);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand command = (AbstractUICommand) component;
    final String clientId = command.getClientId(facesContext);
    final boolean disabled = command.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(command);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    encodeBeginElement(facesContext, command);
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    if (!disabled) {
      String commands = RenderUtils.getBehaviorCommands(facesContext, command);
      if (commands == null) { // old way
        final CommandMap map = new CommandMap(new Command(facesContext, command));
        commands = JsonUtils.encode(map);
      }
      writer.writeAttribute(DataAttributes.COMMANDS, commands, true);

      writer.writeAttribute(HtmlAttributes.HREF, "#", false);

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

    if (command.isParentOfCommands()) {
      // XXX BootstrapClass.NAV_LINK should only be shown inside of UINav or UIButtonGroup
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_TOGGLE, BootstrapClass.NAV_LINK); // todo: CSS classes
      writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
    } else {
      final List<CssItem> cssItems = new ArrayList<CssItem>();
      cssItems.add(command.getCustomClass());
      addCssItems(facesContext, command, cssItems);
      writer.writeClassAttribute(Classes.create(command), cssItems.toArray(new CssItem[cssItems.size()]));
    }
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, command);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    final boolean defaultCommand = ComponentUtils.getBooleanAttribute(command, Attributes.defaultCommand);
    // TODO this might be too expensive:
    // TODO please put a flag in the ToolBar-handler and Button-handler (facelets-handler)
    final boolean insideToolbar = ComponentUtils.findAncestor(command, AbstractUIToolBar.class) != null;
    writer.writeClassAttribute(
        Classes.create(command),
        BootstrapClass.BTN,
        defaultCommand ? BootstrapClass.BTN_PRIMARY : BootstrapClass.BTN_SECONDARY,
        insideToolbar ? BootstrapClass.NAVBAR_BTN : null,
        command.getCustomClass());

    if (defaultCommand) {
      final AbstractUIForm form = ComponentUtils.findAncestor(command, AbstractUIForm.class);
      writer.writeAttribute(DataAttributes.DEFAULT, form.getClientId(facesContext), false);
    }

/*
//  image

    String image = command instanceof AbstractUILink ? ((AbstractUILink) command).getImage() : null;
    if (image != null) {
      if (ResourceManagerUtils.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = HtmlRendererUtils.getImageWithPath(facesContext, image, disabled);
      }
      HtmlRendererUtils.encodeIconWithLabel(writer, facesContext, image, label, disabled);
    }

//  label
    if (label.getLabel() != null) {
      if (image != null) {
        writer.write(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
*/

    final String image = ComponentUtils.getStringAttribute(command, Attributes.image);
    HtmlRendererUtils.encodeIconWithLabel(writer, facesContext, image, label, disabled);

    encodeEndElement(facesContext, command);

    if (command.isParentOfCommands()) {
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

    final AbstractUICommand link = (AbstractUICommand) component;

    if (link.isParentOfCommands()) {
      final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

      for (UIComponent child : component.getChildren()) {
        if (child.isRendered()) {
          writer.startElement(HtmlElements.LI);
          CssItem submenu = child instanceof AbstractUICommand && ((AbstractUICommand) child).isParentOfCommands()
              ? TobagoClass.DROPDOWN_SUBMENU : null;
          // fixme: this name comes not from bootstrap, using prefix? tobago-command-dropdown-submenu
          writer.writeClassAttribute(BootstrapClass.DROPDOWN_ITEM, submenu);
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

    final AbstractUICommand link = (AbstractUICommand) component;

    if (link.isParentOfCommands()) {
      final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlElements.UL);
    }

  }

  protected void encodeBeginElement(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.A);
  }

  protected void encodeEndElement(final FacesContext facesContext, final AbstractUICommand command)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.A);
  }

  protected void commandActivated(final UIComponent component) {
    component.queueEvent(new ActionEvent(component));
  }

  protected void addCssItems(
      final FacesContext facesContext, final AbstractUICommand command, final List<CssItem> collected) {
  }
}
