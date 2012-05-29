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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
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

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIButton button = (UIButton) component;
    String clientId = button.getClientId(facesContext);

    CommandRendererHelper helper = new CommandRendererHelper(facesContext, button, CommandRendererHelper.Tag.BUTTON);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(button);

    writer.startElement(HtmlElements.BUTTON, button);
    writer.writeAttribute(HtmlAttributes.TYPE, createButtonType(button), false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.renderTip(button, writer);
    writer.writeAttribute(HtmlAttributes.DISABLED, helper.isDisabled());
    Integer tabIndex = button.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    if (helper.getOnclick() != null) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true);
    }
    Style style = new Style(facesContext, button);
    writer.writeStyleAttribute(style);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute(Classes.create(button));
    writer.flush(); // force closing the start tag

    String image = (String) button.getAttributes().get(Attributes.IMAGE);
    if (image != null) {
      if (ResourceManagerUtils.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = getImageWithPath(facesContext, image, helper.isDisabled());
      }
      writer.startElement(HtmlElements.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      String tip = button.getTip();
      writer.writeAttribute(HtmlAttributes.ALT, tip != null ? tip : "", true);
      writer.endElement(HtmlElements.IMG);
    }

    if (label.getText() != null) {
      writer.startElement(HtmlElements.SPAN, null);
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.BUTTON);
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("duplicated accessKey : " + label.getAccessKey());
      }
      HtmlRendererUtils.addClickAcceleratorKey(
          facesContext, button.getClientId(facesContext), label.getAccessKey());
    }

    if (ComponentUtils.getBooleanAttribute(component, Attributes.DEFAULT_COMMAND)) {
      boolean transition = ComponentUtils.getBooleanAttribute(button, Attributes.TRANSITION);
      HtmlRendererUtils.setDefaultTransition(facesContext, transition);

      HtmlRendererUtils.writeScriptLoader(facesContext, null, new String[]{
          "Tobago.registerListener(\n"
              + "function() { \n"
              + "Tobago.setDefaultAction('" + button.getClientId(facesContext) + "') \n"
              + "}, \n"
              + "Tobago.Phase.DOCUMENT_READY);"
              });
    }
  }

  private String createButtonType(UIComponent component) {
    boolean defaultCommand = ComponentUtils.getBooleanAttribute(component, Attributes.DEFAULT_COMMAND);
    return defaultCommand ? HtmlButtonTypes.SUBMIT : HtmlButtonTypes.BUTTON;
  }

  @Override
  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {

    UIButton button = (UIButton) component;
    Measure width = Measure.ZERO;
    boolean image = button.getImage() != null;
    if (image) {
      width = getResourceManager().getThemeMeasure(facesContext, button, "imageWidth");
    }
    LabelWithAccessKey label = new LabelWithAccessKey(button);

    width = width.add(RenderUtils.calculateStringWidth(facesContext, button, label.getText()));
    Measure padding = getResourceManager().getThemeMeasure(facesContext, button, "paddingWidth");
    // left padding, right padding and when an image and an text then a middle padding.
    width = width.add(padding.multiply(image && label.getText() != null ? 3 : 2));

    return width;
  }
}
