/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.InputRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class HiddenRenderer extends InputRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    String clientId = component.getClientId(facesContext);
    
    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("input", component);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", ComponentUtil.currentValue(component), null);
    writer.endElement("input");
  }

// ///////////////////////////////////////////// bean getter + setter

}

