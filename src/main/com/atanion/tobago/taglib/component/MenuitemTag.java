/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;

public class MenuitemTag extends MenuradioTag {
// ----------------------------------------------------------------- attributes

  private String image;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    image = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_IMAGE, image);
    setStringProperty(component, ATTR_MENU_TYPE, "menuItem");
  }

// ------------------------------------------------------------ getter + setter

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}