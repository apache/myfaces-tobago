/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
public class InTag extends TextInputTag
    implements com.atanion.tobago.taglib.decl.InTag {

  private String password;


  public void release() {
    super.release();
    password = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_PASSWORD, password,
        getIterationHelper());
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
