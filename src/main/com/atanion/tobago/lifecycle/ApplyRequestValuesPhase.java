/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 3:59:26 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class ApplyRequestValuesPhase extends Phase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {

    UIViewRoot root = facesContext.getViewRoot();

    if (root != null) {
      root.processDecodes(facesContext);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
