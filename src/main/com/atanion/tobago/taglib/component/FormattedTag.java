/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;


public class FormattedTag extends TobagoTag {
// ----------------------------------------------------------------- attributes

  private String createSpan = "true";
  private String value;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    createSpan = "true";
    value = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setBooleanProperty(component, ATTR_CREATE_SPAN, createSpan);
    setBooleanProperty(component, ATTR_VALUE, value);
  }

// ------------------------------------------------------------ getter + setter

  public String getCreateSpan() {
    return createSpan;
  }

  public void setCreateSpan(String createSpan) {
    this.createSpan = createSpan;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

