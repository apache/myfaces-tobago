/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 20, 2002 at 11:39:23 AM.
 * $Id$
 */
package com.atanion.tobago.component;

import javax.faces.context.FacesContext;

public class UIInput extends javax.faces.component.UIInput {

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Input";

  public void updateModel(FacesContext facesContext) {
    if (ComponentUtil.mayUpdateModel(this)) {
      super.updateModel(facesContext);
    }
  }

}
