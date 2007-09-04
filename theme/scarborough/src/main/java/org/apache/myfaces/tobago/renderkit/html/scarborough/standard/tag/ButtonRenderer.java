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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIButtonCommand;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UICommand)) {
      LOG.error("Wrong type: Need " + UICommand.class.getName() + ", but was " + component.getClass().getName());
      return;
    }
    
    UICommand command = (UICommand) component;
    String clientId = command.getClientId(facesContext);
    String buttonType = createButtonType(command);

    CommandRendererHelper helper = new CommandRendererHelper(facesContext, command, CommandRendererHelper.Tag.BUTTON);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(command);

    writer.startElement(HtmlConstants.BUTTON, command);
    writer.writeAttribute(HtmlAttributes.TYPE, buttonType, false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttributeFromComponent(HtmlAttributes.TITLE, ATTR_TIP);
    writer.writeAttribute(HtmlAttributes.DISABLED, helper.isDisabled());
    Integer tabIndex = null;
    if (command instanceof UIButtonCommand) {
      tabIndex = ((UIButtonCommand) command).getTabIndex();
    }
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    if (helper.getOnclick() != null) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true);
    }
    writer.writeStyleAttribute();
    writer.writeClassAttribute();
    writer.flush(); // force closing the start tag

//  image
    String imageName = (String) command.getAttributes().get(ATTR_IMAGE);
    if (imageName != null) {
      String image = null;
      if (imageName.startsWith("HTTP:") || imageName.startsWith("FTP:")
                || imageName.startsWith("/")) {
        image = imageName;
        // absolute Path to image : nothing to do
      } else {
        image = ResourceManagerUtil.getImageWithPath(facesContext, imageName, helper);
      }
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.endElement(HtmlConstants.IMG);
    }

//  label
    if (label.getText() != null) {
      if (imageName != null) {
        writer.writeText(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }

// AcceleratorKey

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }
      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, clientId, label.getAccessKey());
    }
    if ("submit".equals(buttonType)) {
      boolean transition = ComponentUtil.getBooleanAttribute(command, ATTR_TRANSITION);
      HtmlRendererUtil.setDefaultTransition(facesContext, transition);
    }

  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    if (!(component instanceof UICommand)) {
      return;
    }    
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlConstants.BUTTON);
  }

  private String createButtonType(UIComponent component) {
    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component, ATTR_DEFAULT_COMMAND);
    return defaultCommand ? "submit" : "button";
  }

  public int getFixedWidth(FacesContext facesContext, UIComponent component) {
    int width = 0;
    String imageName = (String) component.getAttributes().get(ATTR_IMAGE);
    if (imageName != null) {
      width = getConfiguredValue(facesContext, component, "imageWidth");
    }
    LabelWithAccessKey label = new LabelWithAccessKey(component);

    if (label.getText() != null) {
      width += calculateStringWidth(facesContext, component, label.getText());
    }
    int padding = getConfiguredValue(facesContext, component, "paddingWidth");
    width += 2 * padding;
    if (imageName != null && label.getText() != null){
      width += padding;
    }

    return width;

  }

  private int calculateStringWidth(FacesContext facesContext, UIComponent component, String text) {
    int width = 0;
    int defaultCharWidth = getConfiguredValue(facesContext, component, "fontWidth");

    String fontWidths = ResourceManagerUtil.getProperty(facesContext, "tobago", "tobago.font.widths");

    for (char c : text.toCharArray()) {
      int charWidth;
      if (c >= 32 && c < 128) {
        int begin = (c - 32) * 2;
        charWidth = Integer.parseInt(fontWidths.substring(begin, begin + 2), 16);
      } else {
        charWidth = defaultCharWidth;
      }
      width += charWidth;
    }

    return width;
  }

}
