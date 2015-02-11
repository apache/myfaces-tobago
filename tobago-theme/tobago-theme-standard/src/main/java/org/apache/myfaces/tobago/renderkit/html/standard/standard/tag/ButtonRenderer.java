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
import org.apache.myfaces.tobago.component.SupportsCss;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUIToolBar;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ButtonRenderer.class);

  @Override
  public void prepareRender(
      final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    if (component instanceof SupportsCss) {
      SupportsCss css = (SupportsCss) component;
      css.getCurrentCss().add("btn");
      if (ComponentUtils.getBooleanAttribute(component, Attributes.DEFAULT_COMMAND)) {
        css.getCurrentCss().add("btn-primary");
      } else {
        css.getCurrentCss().add("btn-default");
      }

      // TODO this might be too expensive: please put a flag in the ToolBar-handler and Button-handler (facelets-handler)
      if (ComponentUtils.findAncestor(component, AbstractUIToolBar.class) != null) {
        css.getCurrentCss().add("navbar-btn");
      }
    }
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUICommand button = (AbstractUICommand) component;
    final String clientId = button.getClientId(facesContext);
    final boolean disabled = button.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(button);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.BUTTON, button);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON, false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, button);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, button);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    if (!disabled) {
      final CommandMap map = new CommandMap(new Command(facesContext, button));
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);

      writer.writeAttribute(HtmlAttributes.HREF, "#", false);

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
      }

      if (button instanceof UIButton) {
        final Integer tabIndex = ((UIButton)button).getTabIndex();
        if (tabIndex != null) {
          writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
        }
      }
    }

    final Style style = new Style(facesContext, button);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(button));
    if (button instanceof UIButton && ((UIButton) component).isDefaultCommand()) {
      final AbstractUIForm form = ComponentUtils.findAncestor(component, AbstractUIForm.class);
      writer.writeAttribute(DataAttributes.DEFAULT, form.getClientId(facesContext), false);
    }
    writer.flush(); // force closing the start tag

    String image = (String) button.getAttributes().get(Attributes.IMAGE);
    if (image != null) {
      if (ResourceManagerUtils.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = getImageWithPath(facesContext, image, disabled);
      }
      writer.startElement(HtmlElements.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      final String tip = button.getTip();
      writer.writeAttribute(HtmlAttributes.ALT, tip != null ? tip : "", true);
      writer.endElement(HtmlElements.IMG);
    }

    if (label.getLabel() != null) {
      writer.startElement(HtmlElements.SPAN, null);
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.BUTTON);
  }
}
