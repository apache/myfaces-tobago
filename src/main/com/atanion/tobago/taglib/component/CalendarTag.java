/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 11:30:31 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a calendar.
 */
@Tag(name="calendar", bodyContent=BodyContent.EMPTY)
public class CalendarTag extends TobagoTag
    implements HasIdBindingAndRendered, HasValue {

// ----------------------------------------------------------------- attributes

  private String value;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    value = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public void setValue(String value) {
    this.value = value;
  }
}
