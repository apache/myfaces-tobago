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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase{

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String onclick = null;
    String href;

    String type = (String) component.getAttributes().get(ATTR_TYPE);
    String action = (String) component.getAttributes().get(ATTR_COMMAND_NAME);

    if (COMMAND_TYPE_NAVIGATE.equals(type)) {
      href = HtmlUtils.generateUrl(facesContext, action);
    } else if (COMMAND_TYPE_RESET.equals(type)) {
      href = "javascript:resetForm('" +
          ComponentUtil.findPage(component).getFormId(facesContext) +
          "')";
    } else if (COMMAND_TYPE_SCRIPT.equals(type)) {
      href = "#";
      onclick = action;
    } else { // default: Action.TYPE_SUBMIT
      href = "javascript:submitAction('" +
          ComponentUtil.findPage(component).getFormId(facesContext)
          + "','" + component.getClientId(facesContext) + "')";
    }

    onclick =
        CommandRendererBase.appendConfirmationScript(onclick, component,
            facesContext);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    LabelWithAccessKey label = new LabelWithAccessKey(component);

    if (!ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.startElement("a", component);
      writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
      writer.writeAttribute("name", component.getClientId(facesContext), null);
      writer.writeAttribute("href", href, null);
      if (onclick != null) {
        writer.writeAttribute("onclick", onclick, null);
      }
      writer.writeAttribute("title", null, ATTR_TITLE);
      writer.writeAttribute("target", null, ATTR_TARGET);
      if (label.getAccessKey() != null) {
        writer.writeAttribute("accesskey", label.getAccessKey(), null);
      }
      writer.writeText("", null); // force closing the start tag
    }

//  image
    String image = (String) component.getAttributes().get(ATTR_IMAGE);
    if (image != null) {
      image = ResourceManagerUtil.getImage(facesContext, image);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("alt", "", null);
      writer.writeAttribute("border", "0", null); // todo: is border=0 setting via style possible?
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

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {
    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(ATTR_BODY_CONTENT);

    ResponseWriter writer = facesContext.getResponseWriter();

    if (bodyContentHandler != null) {
      writer.writeText(bodyContentHandler.getBodyContent(), null);
    }

    if (!ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.endElement("a");
    }
  }
}

