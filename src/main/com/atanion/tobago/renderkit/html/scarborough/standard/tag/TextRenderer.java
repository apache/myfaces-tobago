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
import javax.faces.component.ValueHolder;
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

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component)
      throws IOException {

    Object text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("span", component);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeText(text, null);
    writer.endElement("span");
  }

// ///////////////////////////////////////////// bean getter + setter

}

