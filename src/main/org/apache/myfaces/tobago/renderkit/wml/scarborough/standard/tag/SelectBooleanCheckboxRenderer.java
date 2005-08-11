/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.wml.scarborough.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    boolean value = ComponentUtil.getBooleanAttribute(component, ATTR_VALUE);

    writer.startElement("select", component);
    writer.writeAttribute("name", component.getClientId(facesContext), null);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    writer.writeAttribute("multiple", true);
    writer.startElement("option", null);
    writer.writeAttribute("value", value ? "on" : "off", null);

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    writer.endElement("option");
    writer.endElement("select");
  }
}
