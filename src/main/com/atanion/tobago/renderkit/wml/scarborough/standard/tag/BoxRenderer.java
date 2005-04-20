/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.wml.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.renderkit.BoxRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

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

