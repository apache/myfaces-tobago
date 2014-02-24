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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

/**
 * Does the decoding with parent class CommandRendererBase.
 *
 * @see CommandRendererBase
 */
public class MenuCommandRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MenuCommandRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIMenuCommand menu = (UIMenuCommand) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final boolean disabled = menu.isDisabled();
    final boolean firstLevel = RendererTypes.MENU_BAR.equals(menu.getParent().getRendererType());
    final LabelWithAccessKey label = new LabelWithAccessKey(menu);

    if (menu.getFacet(Facets.CHECKBOX) != null) {
      // checkbox menu
      final UISelectBooleanCheckbox checkbox = (UISelectBooleanCheckbox) menu.getFacet(Facets.CHECKBOX);
      final boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
      final String image = checked ? "image/MenuCheckmark.gif" : null;
      final String hiddenId = checkbox.getClientId(facesContext);
      final CommandMap map = new CommandMap(new Command());
      encodeItem(facesContext, writer, menu, label, map, disabled, firstLevel, image, null, "selectBoolean");
      encodeHidden(writer, hiddenId, checked);
    } else if (menu.getFacet(Facets.RADIO) != null) {
      // radio menu
      final String clientId = menu.getClientId(facesContext);
      final UISelectOne radio = (UISelectOne) menu.getFacet(Facets.RADIO);
      final String hiddenId = radio.getClientId(facesContext);
      for (final SelectItem item : SelectItemUtils.iterator(facesContext, radio)) {
        final boolean checked = item.getValue().equals(radio.getValue());
        final String image = checked ? "image/MenuRadioChecked.gif" : null;
        final String labelText = item.getLabel();
        label.reset();

        if (labelText != null) {
          if (labelText.indexOf(LabelWithAccessKey.INDICATOR) > -1) {
            label.setup(labelText);
          } else {
            label.setText(labelText);
          }
        } else {
          LOG.warn("Menu item has label=null where clientId=" + clientId);
        }
        final String formattedValue = RenderUtils.getFormattedValue(facesContext, radio, item.getValue());
        final CommandMap map = new CommandMap(
            new Command(clientId, null, null, null, null, null, null, null, null, null));
        encodeItem(facesContext, writer, null, label, map, disabled, firstLevel, image, formattedValue, "selectOne");
      }
      encodeHidden(writer, hiddenId, getCurrentValue(facesContext, radio));
    } else {
      // normal menu command
      final String image = menu.getImage();
      final CommandMap map = new CommandMap(new Command(facesContext, menu));
      encodeItem(facesContext, writer, menu, label, map, disabled, firstLevel, image, null, null);
    }
  }

  private void encodeHidden(final TobagoResponseWriter writer, final String hiddenId, final Object value)
      throws IOException {
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(hiddenId);
    if (value != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, value.toString(), true);
    }
    writer.endElement(HtmlElements.INPUT);
  }

  private void encodeItem(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UIMenuCommand component,
      final LabelWithAccessKey label,
      final CommandMap map, final boolean disabled, final boolean firstLevel, final String image, final String value,
      final String sub)
      throws IOException {

    writer.startElement(HtmlElements.LI, null);
    if (component != null && !component.isTransient()) {
      writer.writeIdAttribute(component.getClientId(facesContext));
    }
    Markup markup = null;
    if (component != null) {
      markup = component.getCurrentMarkup();
      if (firstLevel) {
        markup = Markup.TOP.add(markup);
      }
    }
     // todo: solve workaround
    String css = Classes.createWorkaround("menu", markup).getStringValue();
    if (sub != null) {
      css += " tobago-menu-" + sub;
    }
    writer.writeClassAttribute(css);
    if (!disabled) {
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);
    }
    writer.writeAttribute(DataAttributes.VALUE, value, true);

    if (component != null) {
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    }

    if (image != null) {
      if (firstLevel) {
        final Style iconStyle = new Style();
        iconStyle.setLeft(Measure.valueOf(0));
        iconStyle.setTop(Measure.valueOf(0));
        iconStyle.setHeight(Measure.valueOf(16));
        iconStyle.setWidth(Measure.valueOf(16));

        writer.startElement(HtmlElements.IMG, null);
        final String imageWithPath
            = ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, image, disabled);
        writer.writeAttribute(HtmlAttributes.SRC, imageWithPath, false);
        final String imageHover = ResourceManagerUtils
            .getImageOrDisabledImageWithPath(facesContext, HtmlRendererUtils.createSrc(image, "Hover"), disabled, true);
        if (imageHover != null) {
          writer.writeAttribute(DataAttributes.SRC_DEFAULT, imageWithPath, false);
          writer.writeAttribute(DataAttributes.SRC_HOVER, imageHover, false);
        }

        writer.writeAttribute(HtmlAttributes.ALT, label.getText(), true);
        writer.writeStyleAttribute(iconStyle);
        writer.endElement(HtmlElements.IMG);
      } else {
        final Style style = new Style();
        style.setBackgroundImage("url('"
            + ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, image, disabled)
            + "')");
        writer.writeStyleAttribute(style);
      }
    }

    writer.startElement(HtmlElements.A, null);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    if (image != null && firstLevel) {
      writer.writeStyleAttribute("vertical-align:top");
    }
//    writer.writeIdAttribute(clientId);

    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("duplicated accessKey : " + label.getAccessKey());
        }
        if (!disabled && component != null && label.getAccessKey() != null) {
          writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        }
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlElements.A);
    writer.endElement(HtmlElements.LI);
  }

  public void encodeChildren(final FacesContext facesContext, final UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }
  

}
