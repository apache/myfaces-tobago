/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIPanel;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class MenuseparatorTag extends TobagoTag {

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(null);
    setStringProperty(component, ATTR_MENU_TYPE, "separator");
  }

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }
}