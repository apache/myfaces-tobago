/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;

public class MenucheckTag extends MenuradioTag {
// ----------------------------------------------------------------- attributes

  private String checked;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    checked = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setBooleanProperty(component, ATTR_CHECKED, checked);
    setStringProperty(component, ATTR_MENU_TYPE, "menuCheck");
  }

// ------------------------------------------------------------ getter + setter

  public String getChecked() {
    return checked;
  }

  public void setChecked(String checked) {
    this.checked = checked;
  }
}