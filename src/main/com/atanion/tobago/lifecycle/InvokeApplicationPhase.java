/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 4:00:40 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

public class InvokeApplicationPhase extends Phase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext context) {
    UIViewRoot viewRoot = context.getViewRoot();
    viewRoot.processApplication(context);
  }

// ///////////////////////////////////////////// bean getter + setter

}
