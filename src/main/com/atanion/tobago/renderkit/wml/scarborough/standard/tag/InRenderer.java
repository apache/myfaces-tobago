/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.wml.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.TobagoConstants;
import com.atanion.util.KeyValuePair;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class InRenderer extends RendererBase {
// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    String clientId = component.getClientId(facesContext);

    UIPage uiPage = ComponentUtil.findPage(component);

    if (uiPage != null){
      uiPage.getPostfields().add(new KeyValuePair(clientId, clientId));
    }

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    String currentValue = ComponentUtil.currentValue(component);

    String type = ComponentUtil.getBooleanAttribute(
        component, TobagoConstants.ATTR_PASSWORD) ? "password" : "text";

    writer.startElement("input", component);
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("value", currentValue, null);
    writer.writeAttribute("type", type, null);
    writer.endElement("input");


  }
}

