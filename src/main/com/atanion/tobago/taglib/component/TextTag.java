/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;

public class TextTag extends BeanTag {
// ----------------------------------------------------------------- attributes

  private String escape = "true";

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    escape = "true";
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setBooleanProperty(component, ATTR_ESCAPE, escape, getIterationHelper());
   ComponentUtil.setBooleanProperty(component, ATTR_CREATE_SPAN, "true", getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getEscape() {
    return escape;
  }

  public void setEscape(String escape) {
    this.escape = escape;
  }
}
