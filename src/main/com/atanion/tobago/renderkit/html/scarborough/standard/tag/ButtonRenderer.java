/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.CommandRendererBase;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase
    implements DirectRenderer {
// ----------------------------------------------------------------- interfaces


// ---------------------------- interface DirectRenderer

  public void encodeDirectBegin(FacesContext facesContext,
      UIComponent component) throws IOException {
    String clientId = component.getClientId(facesContext);
    String buttonType = createButtonType(component);

    String onclick = createOnClick(facesContext, component);
    onclick =
        CommandRendererBase.appendConfirmationScript(onclick, component,
            facesContext);

    boolean disabled = ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED);
    if (disabled) {
      onclick = "";
    }

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("button", component);
    writer.writeAttribute("type", buttonType, null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("disabled", disabled);
    writer.writeAttribute("onclick", onclick, null);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeText("", null); // force closing the start tag
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);

    if (bodyContentHandler != null) {
      writer.writeText(bodyContentHandler.getBodyContent(), null);
    }
    writer.endElement("button");
  }

// ----------------------------------------------------------- business methods

  private String createButtonType(UIComponent component) {
    String buttonType;
    String type = (String) component.getAttributes().get(
        TobagoConstants.ATTR_TYPE);

    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        TobagoConstants.ATTR_DEFAULT_COMMAND);
    if (TobagoConstants.COMMAND_TYPE_RESET.equals(type)) {
      buttonType = "reset";
    } else { // default: Action.TYPE_SUBMIT
      buttonType = defaultCommand ? "submit" : "button";
    }
    return buttonType;
  }

  public static String createOnClick(FacesContext facesContext,
      UIComponent component) {
    String type = (String) component.getAttributes().get(
        TobagoConstants.ATTR_TYPE);
    String commandName = (String) component.getAttributes().get(
        TobagoConstants.ATTR_COMMAND_NAME);
    String clientId = component.getClientId(facesContext);
    String onclick;

    if (TobagoConstants.COMMAND_TYPE_NAVIGATE.equals(type)) {
      onclick = "navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext, commandName) + "')";
    } else if (TobagoConstants.COMMAND_TYPE_RESET.equals(type)) {
      onclick = null;
    } else if (TobagoConstants.COMMAND_TYPE_SCRIPT.equals(type)) {
      onclick = commandName;
    } else { // default: Action.TYPE_SUBMIT
      onclick = "submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
    }
    return onclick;
  }
}

