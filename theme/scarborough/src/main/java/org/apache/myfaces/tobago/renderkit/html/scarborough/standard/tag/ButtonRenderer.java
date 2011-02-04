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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIButtonCommand;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;

public class ButtonRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UICommand)) {
      LOG.error("Wrong type: Need " + UICommand.class.getName() + ", but was " + component.getClass().getName());
      return;
    }
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    if (!(component instanceof UICommand)) {
      return;
    }
    UICommand command = (UICommand) component;
    String clientId = command.getClientId(facesContext);

    CommandRendererHelper helper = new CommandRendererHelper(facesContext, command, CommandRendererHelper.Tag.BUTTON);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(command);

    writer.startElement(HtmlConstants.BUTTON, command);
    writer.writeAttribute(HtmlAttributes.TYPE, createButtonType(command), false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtil.renderTip(command, writer);
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


    String image = (String) command.getAttributes().get(ATTR_IMAGE);
    if (image != null) {
      if (ResourceManagerUtil.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = ResourceManagerUtil.getImageWithPath(facesContext, image, helper);
      }
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      HtmlRendererUtil.renderImageTip(component, writer);
      writer.endElement(HtmlConstants.IMG);
    }

    if (label.getText() != null) {
      if (image != null) {
        writer.writeText(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }


    writer.endElement(HtmlConstants.BUTTON);
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("duplicated accessKey : " + label.getAccessKey());
      }
      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, command.getClientId(facesContext), label.getAccessKey());
    }

    if (ComponentUtil.getBooleanAttribute(component, ATTR_DEFAULT_COMMAND)) {
      boolean transition = ComponentUtil.getBooleanAttribute(command, ATTR_TRANSITION);
      HtmlRendererUtil.setDefaultTransition(facesContext, transition);

      HtmlRendererUtil.writeScriptLoader(facesContext, null, new String[]{
          "Tobago.setDefaultAction('" + command.getClientId(facesContext) + "')"});      
    }
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
      width += RenderUtil.calculateStringWidth(facesContext, component, label.getText());
    }
    int padding = getConfiguredValue(facesContext, component, "paddingWidth");
    width += 2 * padding;
    if (imageName != null && label.getText() != null) {
      width += padding;
    }

    return width;
  }

}
