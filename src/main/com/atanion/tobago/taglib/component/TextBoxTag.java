/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

public class TextBoxTag extends InputTag {
// ----------------------------------------------------------------- attributes

  private String password;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    password = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setBooleanProperty(component, ATTR_PASSWORD, password);
  }

// ------------------------------------------------------------ getter + setter

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

