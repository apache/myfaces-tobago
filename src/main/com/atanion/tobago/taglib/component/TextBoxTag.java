/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

public class TextBoxTag extends TextInputTag {

// /////////////////////////////////////////// attributes

  private int size = -1;
  private String style;
  private boolean password;

// /////////////////////////////////////////// constructors

  public TextBoxTag() {
    super();
  }

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_SIZE, new Integer(getSize()));
    setProperty(component, TobagoConstants.ATTR_STYLE, getStyle());
    setProperty(component, TobagoConstants.ATTR_PASSWORD, password ? Boolean.TRUE : null);
  }

  public void release() {
    super.release();
    this.size = -1;
  }

// /////////////////////////////////////////// bean getter + setter

  public int getSize() {
    return this.size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public boolean isPassword() {
    return password;
  }

  public void setPassword(boolean password) {
    this.password = password;
  }
}
