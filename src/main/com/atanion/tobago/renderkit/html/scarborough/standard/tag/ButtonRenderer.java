/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.CommandRendererBase;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.LabelWithAccessKey;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("disabled", disabled);
    if (onclick != null) {
      writer.writeAttribute("onclick", onclick, null);
    }
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    if (label.getAccessKey() != null) {
      writer.writeAttribute("accesskey", label.getAccessKey(), null);
    }
    writer.writeText("", null); // force closing the start tag

//  image
    String image = (String) component.getAttributes().get(ATTR_IMAGE);
    if (image != null) {
      image = ResourceManagerUtil.getImage(facesContext, image);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("alt", "", null);
      writer.endElement("img");
    }

//  label
    if (label.getText() != null) {
      if (image != null) {
        writer.writeText(" ", null); // separator: e.g. &nbsp;
      }
      RenderUtil.writeLabelWithAccessKey(writer, label);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(ATTR_BODY_CONTENT);

    if (bodyContentHandler != null) {
      writer.writeText(bodyContentHandler.getBodyContent(), null);
    }
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
    String command = (String) component.getAttributes().get(ATTR_COMMAND_NAME);
    String clientId = component.getClientId(facesContext);
    String onclick;

    if (COMMAND_TYPE_NAVIGATE.equals(type)) {
      onclick = "navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, command) + "')";
    } else if (COMMAND_TYPE_RESET.equals(type)) {
      onclick = null;
    } else if (COMMAND_TYPE_SCRIPT.equals(type)) {
      onclick = command;
    } else { // default: Action.TYPE_SUBMIT
      onclick = "submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
    }
    return onclick;
  }
}

