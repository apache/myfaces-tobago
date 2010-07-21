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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class MenuRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MenuRenderer.class);

  private static final String MENU_ACCELERATOR_KEYS = "menuAcceleratorKeys";

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    
    UIMenu menu = (UIMenu) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    boolean disabled = menu.isDisabled();
    boolean firstLevel = !RendererTypes.MENU.equals(menu.getParent().getRendererType());
    boolean isParentMenu = menu.getChildCount() > 0; // todo: may be not correct
    String clientId = menu.getClientId(facesContext);

    writer.startElement(HtmlConstants.LI, menu);
    writer.writeClassAttribute(Classes.create(menu, firstLevel ? Markup.TOP : null));
    if (menu.getImage() != null) {
      Style style = new Style();
      style.setBackgroundImage("url(" + menu.getImage() + ")");
      writer.writeStyleAttribute(style);
    }
    writer.startElement(HtmlConstants.A, menu);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeIdAttribute(clientId);

    LabelWithAccessKey label = new LabelWithAccessKey(menu);
    if (label.getText() != null) {
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("duplicated accessKey : " + label.getAccessKey());
        }
        if (!disabled) {
          addAcceleratorKey(facesContext, menu, label.getAccessKey());
        }
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }
    writer.endElement(HtmlConstants.A);
    if (isParentMenu) {
      if (firstLevel) {
        ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, MenuBarRenderer.DIVIDER);
        divider.activateBranch(facesContext);
        writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      }
      writer.startElement(HtmlConstants.OL, menu);
      writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "menu");
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIMenu menu = (UIMenu) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    boolean firstLevel = !RendererTypes.MENU.equals(menu.getParent().getRendererType());

    boolean isParentMenu = menu.getChildCount() > 0; // todo: may be not correct
    if (isParentMenu) {
      writer.endElement(HtmlConstants.OL);
      if (firstLevel) {
        ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, MenuBarRenderer.DIVIDER);
        divider.passivateBranch(facesContext);
        writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      }
    }
    writer.endElement(HtmlConstants.LI);
  }

  private void addAcceleratorKey(FacesContext facesContext, UIComponent component, Character accessKey) {
    String clientId = component.getClientId(facesContext);
    while (component != null && !component.getAttributes().containsKey(MENU_ACCELERATOR_KEYS)) {
      component = component.getParent();
    }
    if (component != null) {
      List<String> keys = (List<String>) component.getAttributes().get(MENU_ACCELERATOR_KEYS);
      String jsStatement = HtmlRendererUtils.createOnclickAcceleratorKeyJsStatement(clientId, accessKey, null);
      keys.add(jsStatement);
    } else {
      LOG.warn("Can't find menu root component!");
    }
  }
  
}
