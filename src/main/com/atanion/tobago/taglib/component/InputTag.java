/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public abstract class InputTag extends BeanTag {

  private String onchange;

  private boolean focus;

  public InputTag() {
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_ONCHANGE, onchange);
    setProperty(component, TobagoConstants.ATTR_FOCUS, focus);
  }

  public String getOnchange() {
    return onchange;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public boolean isFocus() {
    return focus;
  }

  public void setFocus(boolean focus) {
    this.focus = focus;
  }

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = false;
  }
}
