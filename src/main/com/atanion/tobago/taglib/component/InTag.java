/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.taglib.UIComponentTagAttribute;

import javax.faces.component.UIComponent;

public class InTag extends InputTag {

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
    ComponentUtil.setBooleanProperty(component, ATTR_PASSWORD, password,
        getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getPassword() {
    return password;
  }

  /**
   * 
   * @param password
   */
  @UIComponentTagAttribute(
      internalType = Boolean.class,
      description = "Is rendered as password, so you will not see the typed charakters.")
  public void setPassword(String password) {
    this.password = password;
  }
}
