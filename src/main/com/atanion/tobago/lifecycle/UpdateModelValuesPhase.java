/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 25, 2002 at 5:41:11 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class UpdateModelValuesPhase extends Phase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {

    UIViewRoot root = facesContext.getViewRoot();

    if (root != null) {
      root.processUpdates(facesContext);
    }

    if (facesContext.getMessages().hasNext()) {
      facesContext.renderResponse();
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
