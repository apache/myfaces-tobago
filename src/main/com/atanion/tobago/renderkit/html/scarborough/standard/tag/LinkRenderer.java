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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(LinkRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectBegin(FacesContext facesContext,
      UIComponent component) throws IOException {

    String onclick = null;
    String href;

    String type = (String) component.getAttributes().get(ATTR_TYPE);
    String action = (String) component.getAttributes().get(ATTR_COMMAND_NAME);

    if (COMMAND_TYPE_NAVIGATE.equals(type)) {
      href = HtmlUtils.generateUrl(facesContext, action);
    } else if (COMMAND_TYPE_RESET.equals(type)) {
      href = "javascript:resetForm('" + ComponentUtil.findPage(component).getFormId(facesContext) + "')";
    } else if (COMMAND_TYPE_SCRIPT.equals(type)) {
      href = "#";
      onclick = action;
    } else { // default: Action.TYPE_SUBMIT
      href = "javascript:submitAction('" + ComponentUtil.findPage(component).getFormId(facesContext)
          + "','" + component.getClientId(facesContext) + "')";
    }

    onclick = CommandRendererBase.appendConfirmationScript(onclick, component, facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    if (! ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.startElement("a", component);
      writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
      writer.writeAttribute("name", component.getClientId(facesContext), null);
      writer.writeAttribute("href", href, null);
      if (onclick != null) {
        writer.writeAttribute("onclick", onclick, null);
      }
      writer.writeAttribute("title", null, ATTR_TITLE);
      writer.writeAttribute("target", null, ATTR_TARGET);
      writer.writeText("", null); // force closing the start tag
    }
  }

  public void encodeDirectEnd(FacesContext facesContext, UIComponent component)
      throws IOException {

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(ATTR_BODY_CONTENT);

    ResponseWriter writer = facesContext.getResponseWriter();

    if (bodyContentHandler != null) {
      writer.writeText(bodyContentHandler.getBodyContent(), null);
    }

    if (! ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.endElement("a");
    }

  }

// ///////////////////////////////////////////// bean getter + setter

}

