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
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.layout.Measure;
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
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ButtonRenderer.class);

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIButton button = (UIButton) component;
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

      final Integer tabIndex = button.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }

    final Style style = new Style(facesContext, button);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(button));
    if (((UIButton) component).isDefaultCommand()) {
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

  @Override
  public Measure getPreferredWidth(final FacesContext facesContext, final Configurable component) {

    final UIButton button = (UIButton) component;
    Measure width = Measure.ZERO;
    final boolean image = button.getImage() != null;
    if (image) {
      final Measure imageWidth = getResourceManager().getThemeMeasure(facesContext, button, "imageWidth");
      if (imageWidth != null) {
        width = imageWidth;
      }
    }
    final LabelWithAccessKey label = new LabelWithAccessKey(button);

    width = width.add(RenderUtils.calculateStringWidth(facesContext, button, label.getLabel()));
    final Measure padding = getResourceManager().getThemeMeasure(facesContext, button, "paddingWidth");
    if (padding != null) {
      // left padding, right padding and when an image and an text then a middle padding.
      width = width.add(padding.multiply(image && label.getLabel() != null ? 3 : 2));
    }

    return width;
  }
}
