/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 14:50:02.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPopup;

import javax.faces.component.UIComponent;

public class PopupTag extends TobagoBodyTag {

  private String width;
  private String height;
  private String left;
  private String top;

  public String getComponentType() {
    return UIPopup.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    width = null;
    height = null;
    left = null;
    top = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LEFT, left, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TOP, top, getIterationHelper());
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public void setTop(String top) {
    this.top = top;
  }

}

