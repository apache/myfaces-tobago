/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public abstract class InputTag extends BeanTag {
// ----------------------------------------------------------------- attributes

  private String onchange;
  private String focus;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_ONCHANGE, onchange, getIterationHelper());
   ComponentUtil.setBooleanProperty(component, ATTR_FOCUS, focus, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getOnchange() {
    return onchange;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public String getFocus() {
    return focus;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }
}

