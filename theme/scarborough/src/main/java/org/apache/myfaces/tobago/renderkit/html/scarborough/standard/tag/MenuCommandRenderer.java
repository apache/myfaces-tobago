package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.JQueryUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

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

  private static final Log LOG = LogFactory.getLog(MenuCommandRenderer.class);

  private static final String MENU_ACCELERATOR_KEYS = "menuAcceleratorKeys";

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    UIMenuCommand menu = (UIMenuCommand) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    boolean disabled = menu.isDisabled();
    boolean firstLevel = RendererTypes.MENU_BAR.equals(menu.getParent().getRendererType());
    LabelWithAccessKey label = new LabelWithAccessKey(menu);
    String clientId = menu.getClientId(facesContext);
    String submit = "Tobago.submitAction(this, '" + clientId +"');";

    if (menu.getFacet(Facets.CHECKBOX) != null) {
      // checkbox menu
      UISelectBooleanCheckbox checkbox = (UISelectBooleanCheckbox) menu.getFacet(Facets.CHECKBOX);
      boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
      String image = checked ? "image/MenuCheckmark.gif" : null;
      String hiddenId = checkbox.getClientId(facesContext);
      // the function toggles true <-> false
      String setValue = JQueryUtils.selectId(hiddenId) 
          + ".each(function(){$(this).val($(this).val() == 'true' ? 'false' : 'true')}); ";
      encodeItem(facesContext, writer, menu, label, setValue + submit, disabled, firstLevel, image);
      encodeHidden(writer, hiddenId, checked);
    } else if (menu.getFacet(Facets.RADIO) != null) {
      // radio menu
      UISelectOne radio = (UISelectOne) menu.getFacet(Facets.RADIO);
      List<SelectItem> items = RenderUtil.getSelectItems(radio);
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
        String formattedValue = RenderUtil.getFormattedValue(facesContext, radio, item.getValue());
        String setValue = JQueryUtils.selectId(hiddenId) + ".val('" + JQueryUtils.escapeValue(formattedValue) + "'); ";
        encodeItem(facesContext, writer, null, label, setValue + submit, disabled, firstLevel, image);
      }
      encodeHidden(writer, hiddenId, radio.getValue());
    } else {
      // normal menu command
      CommandRendererHelper helper = new CommandRendererHelper(facesContext, menu);
      String onclick = helper.getOnclick();
      encodeItem(facesContext, writer, menu, label, onclick != null ? onclick : submit, disabled, firstLevel, null);
    }
  }

  private void encodeHidden(TobagoResponseWriter writer, String hiddenId, Object value) throws IOException {
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeIdAttribute(hiddenId);
    writer.writeNameAttribute(hiddenId);
    if (value != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, value.toString(), true);
    }
    writer.endElement(HtmlConstants.INPUT);
  }

  private void encodeItem(
      FacesContext facesContext, TobagoResponseWriter writer, UIComponent component, LabelWithAccessKey label,
      String onclick, boolean disabled, boolean firstLevel, String image) throws IOException {

    writer.startElement(HtmlConstants.LI, null);
    String clazz = (firstLevel ? "tobago-menu-top " : "") + "tobago-menu-parent";
    writer.writeClassAttribute(clazz);
    writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, true);

    if (image != null) {
      Style style = new Style();
      style.setBackgroundImage("url(" 
          + ResourceManagerUtil.getImageWithPath(facesContext, image) 
          + ")");
      writer.writeStyleAttribute(style);
    }

    writer.startElement(HtmlConstants.A, null);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
//    writer.writeIdAttribute(clientId);

    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("duplicated accessKey : " + label.getAccessKey());
        }
        if (!disabled && component != null) {
          addAcceleratorKey(facesContext, component, label.getAccessKey());
        }
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.A);
    writer.endElement(HtmlConstants.LI);
  }

  private void addAcceleratorKey(
      FacesContext facesContext, UIComponent component, Character accessKey) {
    String clientId = component.getClientId(facesContext);
    while (component != null && !component.getAttributes().containsKey(MENU_ACCELERATOR_KEYS)) {
      component = component.getParent();
    }
    if (component != null) {
      List<String> keys
          = (List<String>) component.getAttributes().get(MENU_ACCELERATOR_KEYS);
      String jsStatement = HtmlRendererUtils.createOnclickAcceleratorKeyJsStatement(clientId, accessKey, null);
      keys.add(jsStatement);
    } else {
      LOG.warn("Can't find menu root component!");
    }
  }
  
  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }
  

}
