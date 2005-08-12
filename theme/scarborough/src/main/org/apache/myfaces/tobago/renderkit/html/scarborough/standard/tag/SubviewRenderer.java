/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.BodyContentHandler;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class SubviewRenderer extends RendererBase {

// ///////////////////////////////////////////// constant
    private static final Log LOG = LogFactory.getLog(SubviewRenderer.class);
// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
    LOG.info("SSSSSSSSSSSSSSSSSSS Subview component = " + component.getClass().getName());
    super.encodeBegin(facesContext, component);
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
    if (bodyContentHandler != null) {
      writer.write(bodyContentHandler.getBodyContent());
    }

  }
// ///////////////////////////////////////////// bean getter + setter

}

