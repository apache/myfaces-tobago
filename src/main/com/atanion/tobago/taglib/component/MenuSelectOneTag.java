/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class MenuSelectOneTag extends CommandTag {

  public static final String COMMAND_TYPE = "menuSelectOne";


// ----------------------------------------------------------------- attributes

  private String value;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    value = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter



  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}