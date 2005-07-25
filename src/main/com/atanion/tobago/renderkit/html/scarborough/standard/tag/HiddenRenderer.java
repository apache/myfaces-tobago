/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class HiddenRenderer extends InputRendererBase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    final String clientId = component.getClientId(facesContext);
    final String value = ComponentUtil.currentValue(component);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("input", component);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("value", value != null ? value : "", null);
    writer.endElement("input");
  }

// ///////////////////////////////////////////// bean getter + setter

}

