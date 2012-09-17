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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.JQueryUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

/**
 * Does the decoding with parent class CommandRendererBase.
 *
 * @see CommandRendererBase
 */
public class MenuCommandRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MenuCommandRenderer.class);

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UIMenuCommand menu = (UIMenuCommand) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    boolean disabled = menu.isDisabled();
    boolean firstLevel = RendererTypes.MENU_BAR.equals(menu.getParent().getRendererType());
    LabelWithAccessKey label = new LabelWithAccessKey(menu);
    String clientId = menu.getClientId(facesContext);
    String submit = HtmlRendererUtils.createSubmitAction(clientId, true, null, null);

    if (menu.getFacet(Facets.CHECKBOX) != null) {
      // checkbox menu
      UISelectBooleanCheckbox checkbox = (UISelectBooleanCheckbox) menu.getFacet(Facets.CHECKBOX);
      boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
      String image = checked ? "image/MenuCheckmark.gif" : null;
      String hiddenId = checkbox.getClientId(facesContext);
      // the function toggles true <-> false
      String setValue = JQueryUtils.selectId(hiddenId) 
          + ".each(function(){jQuery(this).val(jQuery(this).val() == 'true' ? 'false' : 'true')}); ";
      encodeItem(facesContext, writer, menu, label, setValue + submit, disabled, firstLevel, image);
      encodeHidden(writer, hiddenId, checked);
    } else if (menu.getFacet(Facets.RADIO) != null) {
      // radio menu
      UISelectOne radio = (UISelectOne) menu.getFacet(Facets.RADIO);
      List<SelectItem> items = RenderUtils.getSelectItems(radio);
      String hiddenId = radio.getClientId(facesContext);
      for (SelectItem item : items) {
        boolean checked = item.getValue().equals(radio.getValue());
        String image = checked ? "image/MenuRadioChecked.gif" : null;
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
        String formattedValue = RenderUtils.getFormattedValue(facesContext, radio, item.getValue());
        String setValue = JQueryUtils.selectId(hiddenId) + ".val('" + JQueryUtils.escapeValue(formattedValue) + "'); ";
        encodeItem(facesContext, writer, null, label, setValue + submit, disabled, firstLevel, image);
      }
      encodeHidden(writer, hiddenId, getCurrentValue(facesContext, radio));
    } else {
      // normal menu command
      CommandRendererHelper helper = new CommandRendererHelper(facesContext, menu);
      String onclick = helper.getOnclick();
      String image = menu.getImage();
      encodeItem(facesContext, writer, menu, label, onclick != null ? onclick : submit, disabled, firstLevel, image);
    }
  }

  private void encodeHidden(TobagoResponseWriter writer, String hiddenId, Object value) throws IOException {
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeIdAttribute(hiddenId);
    writer.writeNameAttribute(hiddenId);
    if (value != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, value.toString(), true);
    }
    writer.endElement(HtmlElements.INPUT);
  }

  private void encodeItem(
      FacesContext facesContext, TobagoResponseWriter writer, UIMenuCommand component, LabelWithAccessKey label,
      String onclick, boolean disabled, boolean firstLevel, String image) throws IOException {

    writer.startElement(HtmlElements.LI, null);
    if (component != null) {
        writer.writeIdAttribute(component.getClientId(facesContext));
    }
    Markup markup = null;
    if (component != null) {
      markup = component.getCurrentMarkup();
      if (firstLevel) {
        markup = Markup.TOP.add(markup);
      }
    }
    writer.writeClassAttribute(Classes.createWorkaround("menu", markup)); // todo: solve workaround
    writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, true);
    if (component != null) {
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    }

    if (image != null) {
      if (firstLevel) {
        Style iconStyle = new Style();
        iconStyle.setLeft(Measure.valueOf(0));
        iconStyle.setTop(Measure.valueOf(0));
        iconStyle.setHeight(Measure.valueOf(16));
        iconStyle.setWidth(Measure.valueOf(16));

        writer.startElement(HtmlElements.IMG, null);
        String imageWithPath = ResourceManagerUtils.getImageWithPath(facesContext, image, true);
        writer.writeAttribute(HtmlAttributes.SRC, imageWithPath, false);
        String imageHover
            = ResourceManagerUtils.getImageWithPath(facesContext, HtmlRendererUtils.createSrc(image, "Hover"), true);
        if (imageHover != null) {
          writer.writeAttribute(DataAttributes.SRCDEFAULT, imageWithPath, false);
          writer.writeAttribute(DataAttributes.SRCHOVER, imageHover, false);
        }

        writer.writeAttribute(HtmlAttributes.ALT, label.getText(), true);
        writer.writeStyleAttribute(iconStyle);
        writer.endElement(HtmlElements.IMG);
      } else {
        Style style = new Style();
        style.setBackgroundImage("url("
            + ResourceManagerUtils.getImageWithPath(facesContext, image)
            + ")");
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

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }
  

}
