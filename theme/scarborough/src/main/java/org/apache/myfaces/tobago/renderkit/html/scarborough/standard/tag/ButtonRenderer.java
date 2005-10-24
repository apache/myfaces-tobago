/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String clientId = component.getClientId(facesContext);
    String buttonType = createButtonType(component);

    String onclick = createOnClick(facesContext, component);
    onclick = CommandRendererBase.appendConfirmationScript(
        onclick, component, facesContext);

    boolean disabled
        = ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED);
    if (disabled) {
      onclick = "";
    }

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    writer.startElement("button", component);
    writer.writeAttribute("type", buttonType, null);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.writeAttribute("disabled", disabled);
    if (onclick != null) {
      writer.writeAttribute("onclick", onclick, null);
    }
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass();
    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && ! AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }
      writer.writeAttribute("accesskey", label.getAccessKey(), null);
    }
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
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("alt", "", null);
      writer.endElement("img");
    }

//  label
    if (label.getText() != null) {
      if (imageName != null) {
        writer.writeText(" ", null); // separator: e.g. &nbsp;
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    //BodyContentHandler bodyContentHandler = (BodyContentHandler)
    //    component.getAttributes().get(ATTR_BODY_CONTENT);

    //if (bodyContentHandler != null) {
    //  writer.writeText(bodyContentHandler.getBodyContent(), null);
    //}
    writer.endElement("button");
  }

// ----------------------------------------------------------- business methods

  private String createButtonType(UIComponent component) {
    String buttonType;
    String type = (String) component.getAttributes().get(ATTR_TYPE);

    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        ATTR_DEFAULT_COMMAND);
    if (COMMAND_TYPE_RESET.equals(type)) {
      buttonType = "reset";
    } else { // default: Action.TYPE_SUBMIT
      buttonType = defaultCommand ? "submit" : "button";
    }
    return buttonType;
  }

  public static String createOnClick(FacesContext facesContext,
      UIComponent component) {
    String type = (String) component.getAttributes().get(ATTR_TYPE);
    String command = (String) component.getAttributes().get(ATTR_ACTION_STRING);
    String clientId = component.getClientId(facesContext);
    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        ATTR_DEFAULT_COMMAND);
    String onclick;

    if (COMMAND_TYPE_NAVIGATE.equals(type)) {
      onclick = "navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, command) + "')";
    } else if (COMMAND_TYPE_RESET.equals(type)) {
      onclick = null;
    } else if (COMMAND_TYPE_SCRIPT.equals(type)) {
      onclick = command;
    } else if (defaultCommand) {
      onclick = "setAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
    } else {
      onclick = "submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
    }
    return onclick;
  }
}

