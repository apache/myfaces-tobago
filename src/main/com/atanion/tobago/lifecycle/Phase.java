/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 3:34:04 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

public abstract class Phase {

  public abstract void execute(FacesContext facesContext) throws FacesException;

}
