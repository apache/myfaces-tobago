/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class TextTag extends BeanTag {
// ----------------------------------------------------------------- attributes

  private String escape;
  private String createSpan;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    escape = null;
    createSpan = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setBooleanProperty(component, ATTR_ESCAPE, escape);
    setBooleanProperty(component, ATTR_CREATE_SPAN, createSpan);
  }

// ------------------------------------------------------------ getter + setter

  public String getEscape() {
    return escape;
  }

  public void setEscape(String escape) {
    this.escape = escape;
  }

  public String getCreateSpan() {
    return createSpan;
  }

  public void setCreateSpan(String createSpan) {
    this.createSpan = createSpan;
  }
}

