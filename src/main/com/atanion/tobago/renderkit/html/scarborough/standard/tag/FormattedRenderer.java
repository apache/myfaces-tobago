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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class FormattedRenderer extends RendererBase
    implements DirectRenderer {
// ------------------------------------------------------------------ constants

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface DirectRenderer

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    String text = ComponentUtil.currentValue(component);

    ResponseWriter writer = facesContext.getResponseWriter();

    boolean createSpan = ComponentUtil.getBooleanAttribute(
        component, TobagoConstants.ATTR_CREATE_SPAN);

    if (createSpan) {
      writer.startElement("span", component);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE); // todo: not supported
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS); // todo: not supported
      writer.writeAttribute("title", null, TobagoConstants.ATTR_TITLE); // todo: not supported
    }

    if (text != null) {
      writer.write(text);
    }

    if (createSpan) {
      writer.endElement("span");
    }
  }
}

