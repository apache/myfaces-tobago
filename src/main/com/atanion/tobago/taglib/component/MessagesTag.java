/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created Jan 20, 2003.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;

public class MessagesTag extends TobagoTag {


// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String forValue;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIMessages.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_FOR, forValue);
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setFor(String forValue) {
    this.forValue = forValue;
  }
}
