/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 4:01:42 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class RenderResponsePhase extends Phase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {

    try {
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      viewHandler.renderView(facesContext, facesContext.getViewRoot());
    } catch (IOException e) {
      throw new FacesException(e.getMessage(), e);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
