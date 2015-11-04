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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class CommandRenderer extends CommandRendererBase {

/*
  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
  }
*/

  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand link = (AbstractUICommand) component;
    final String clientId = link.getClientId(facesContext);
    final boolean disabled = link.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(link);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (disabled) {
      writer.startElement(HtmlElements.SPAN);
    } else {
      writer.startElement(HtmlElements.A);

      final CommandMap map = new CommandMap(new Command(facesContext, link));
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);

      writer.writeAttribute(HtmlAttributes.HREF, "#", false);

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
      }

      final Integer tabIndex = link instanceof AbstractUILink ? ((AbstractUILink) link).getTabIndex() : null;
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, link);
    writer.writeStyleAttribute(link.getStyle());
    if (link.isParentOfCommands()) {
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_TOGGLE); // todo: CSS classes
      writer.writeAttribute(DataAttributes.TOGGLE, "dropdown", false);
    } else {
      writer.writeClassAttribute(Classes.create(link), link.getCustomClass());
    }
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, link);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.flush();

//  image
    String image = link instanceof AbstractUILink ? ((AbstractUILink) link).getImage() : null;
    if (image != null) {
      if (ResourceManagerUtils.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = HtmlRendererUtils.getImageWithPath(facesContext, image, disabled);
      }
      writer.startElement(HtmlElements.IMG);
      writer.writeClassAttribute(Classes.create(link, "image"));
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      writer.writeAttribute(HtmlAttributes.BORDER, 0); // TODO: is border=0 setting via style possible?
      final String tip = link.getTip();
      writer.writeAttribute(HtmlAttributes.ALT, tip != null ? tip : "", true);
      if (tip != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
      }
      writer.endElement(HtmlElements.IMG);
    }

//  label
    if (label.getLabel() != null) {
      if (image != null) {
        writer.write(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }

    if (disabled) {
      writer.endElement(HtmlElements.SPAN);
    } else {
      writer.endElement(HtmlElements.A);
    }

    if (link.isParentOfCommands()) {
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
          if (child instanceof AbstractUICommand) {
            AbstractUICommand command = (AbstractUICommand) child;
            if (command.isParentOfCommands()) {
              // fixme: this name comes not from bootstrap, using prefix? tobago-command-dropdown-submenu
              writer.writeClassAttribute(TobagoClass.DROPDOWN_SUBMENU);
            }
          }
          child.encodeAll(facesContext);
          writer.endElement(HtmlElements.LI);
        }
      }
    } else {
      super.encodeChildren(facesContext, component);
    }

  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand link = (AbstractUICommand) component;

    if (link.isParentOfCommands()) {
      final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.endElement(HtmlElements.UL);
    }

  }
}
