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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ButtonRenderer extends CommandRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ButtonRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectBegin(FacesContext facesContext,
      UIComponent component) throws IOException {

    String onclick;
    String buttonType;

    String type = (String) component.getAttributes().get(
        TobagoConstants.ATTR_TYPE);
    String action = (String) component.getAttributes().get(
        TobagoConstants.ATTR_COMMAND_NAME);
    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        TobagoConstants.ATTR_DEFAULT_COMMAND);

    String clientId = component.getClientId(facesContext);

    if (TobagoConstants.COMMAND_TYPE_NAVIGATE.equals(type)) {
      onclick = "navigateToUrl('" + HtmlUtils.generateUrl(facesContext, action) +
          "')";
      buttonType = defaultCommand ? "submit" : "button";
    } else if (TobagoConstants.COMMAND_TYPE_RESET.equals(type)) {
      onclick = null;
      buttonType = "reset";
    } else if (TobagoConstants.COMMAND_TYPE_SCRIPT.equals(type)) {
      onclick = action;
      buttonType = defaultCommand ? "submit" : "button";
    } else { // default: Action.TYPE_SUBMIT
      onclick = "submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "','" + clientId + "')";
      buttonType = defaultCommand ? "submit" : "button";
    }

    onclick =
        CommandRendererBase.appendConfirmationScript(onclick, component,
            facesContext);

    if (ComponentUtil.isDisabled(component)) {
      onclick = "";
    }

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("button", component);
    writer.writeAttribute("type", buttonType, null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("disabled", ComponentUtil.isDisabled(component));
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

// ///////////////////////////////////////////// bean getter + setter

}

