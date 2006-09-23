package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String clientId = component.getClientId(facesContext);
    String buttonType = createButtonType(component);

    String onclick;

    boolean disabled = ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED);
    if (disabled) {
      onclick = "";
    } else {
      onclick = createOnClick(facesContext, component);
      onclick = CommandRendererBase.appendConfirmationScript(onclick, component,
          facesContext);
    }

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    writer.startElement(HtmlConstants.BUTTON, component);
    writer.writeAttribute(HtmlAttributes.TYPE, buttonType, null);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    if (onclick != null) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, null);
    }
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.writeComponentClass();
    writer.writeText("", null); // force closing the start tag

//  image
    String imageName = (String) component.getAttributes().get(ATTR_IMAGE);
    if (imageName != null) {
      String image = null;
      if (disabled) {
        image = ResourceManagerUtil.getDisabledImageWithPath(facesContext, imageName);
      }
      if (image == null) {
        image = ResourceManagerUtil.getImageWithPath(facesContext, imageName);
      }
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, image, null);
      writer.writeAttribute(HtmlAttributes.ALT, "", null);
      writer.endElement(HtmlConstants.IMG);
    }

//  label
    if (label.getText() != null) {
      if (imageName != null) {
        writer.writeText(" ", null); // separator: e.g. &nbsp;
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
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlConstants.BUTTON);
  }

  private String createButtonType(UIComponent component) {
    String buttonType;
    //String type = (String) component.getAttributes().get(ATTR_TYPE);

    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        ATTR_DEFAULT_COMMAND);
    //if (COMMAND_TYPE_RESET.equals(type)) {
    //  buttonType = "reset";
    //} else { // default: Action.TYPE_SUBMIT
      buttonType = defaultCommand ? "submit" : "button";
    //}
    return buttonType;
  }

  public static String createOnClick(FacesContext facesContext,
      UIComponent component) {
    //String type = (String) component.getAttributes().get(ATTR_TYPE);
    //String command = (String) component.getAttributes().get(ATTR_ACTION_STRING);
    String clientId = component.getClientId(facesContext);
    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        ATTR_DEFAULT_COMMAND);
    String onclick;

    if (component.getAttributes().get(ATTR_ACTION_LINK)!=null) {
      onclick = "Tobago.navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, (String) component.getAttributes().get(ATTR_ACTION_LINK)) + "');";
      // FIXME !!
      //} else if (COMMAND_TYPE_RESET.equals(type)) {
    //  onclick = null;
    } else if (component.getAttributes().get(ATTR_ACTION_ONCLICK)!=null) {
      onclick = (String) component.getAttributes().get(ATTR_ACTION_ONCLICK);
    } else if (defaultCommand) {
      ComponentUtil.findPage(component).setDefaultActionId(clientId);
//      onclick = "Tobago.setAction('" + clientId + "');";
      onclick = null;
    } else {
      onclick = "Tobago.submitAction('" + clientId + "');";
    }
    return onclick;
  }
}
