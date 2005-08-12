/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.BodyContentHandler;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {


  public void encodeBeginTobago(FacesContext facesContext, UIComponent component)
      throws IOException {
    // <card> ?
  }

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UIPanel panel = (UIPanel) component;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        panel.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);

    writer.write(bodyContentHandler.getBodyContent());
    // </card> ?
  }
}

