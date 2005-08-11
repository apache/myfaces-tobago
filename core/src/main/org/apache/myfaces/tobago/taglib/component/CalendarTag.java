/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 11:30:31 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;


public class CalendarTag extends TobagoTag
    implements org.apache.myfaces.tobago.taglib.decl.CalendarTag {

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

  public void setValue(String value) {
    this.value = value;
  }
}
