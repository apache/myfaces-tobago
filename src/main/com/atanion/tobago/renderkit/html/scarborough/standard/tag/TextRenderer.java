/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class TextRenderer extends RendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TextRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectEnd(
      FacesContext facesContext,
      UIComponent component) throws IOException {

    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    boolean escape = ComponentUtil.getBooleanAttribute(
        component, TobagoConstants.ATTR_ESCAPE);
    boolean createSpan = ComponentUtil.getBooleanAttribute(
        component, TobagoConstants.ATTR_CREATE_SPAN);

/* fixme: how to check this?
    if (verbatim) {
      if (component.getAttributes().get(TobagoConstants.ATTR_STYLE) != null) {
        LOG.warn(
            "Attribute " + TobagoConstants.ATTR_STYLE + "='"
            + component.getAttributes().get(TobagoConstants.ATTR_STYLE)
            + "' ignored!");
      }
      if (component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS)
          != null) {
        LOG.warn(
            "Attribute " + TobagoConstants.ATTR_STYLE_CLASS + "='"
            + component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS)
            + "' ignored!");
      }
    }
*/

    if (createSpan) {
      writer.startElement("span", component);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    }
    if (escape) {
      writer.writeText(text, null);
    } else {
      writer.write(text);
    }
    if (createSpan) {
      writer.endElement("span");
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

