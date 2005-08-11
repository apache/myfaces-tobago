/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class VerbatimRenderer extends RendererBase  {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(VerbatimRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    LOG.info("HI! " + component.getClientId(facesContext), new Exception());

    ResponseWriter writer = facesContext.getResponseWriter();

    String value = ComponentUtil.currentValue(component);
    if (value == null) {
      return;
    }

    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_ESCAPE)) {
      writer.writeText(value, null);
    }
    else {
      writer.write(value);
    }

  }

// ///////////////////////////////////////////// bean getter + setter

}
