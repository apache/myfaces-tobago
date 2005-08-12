/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
      uiPage.getPostfields().add(new DefaultKeyValue(clientId, clientId));
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

