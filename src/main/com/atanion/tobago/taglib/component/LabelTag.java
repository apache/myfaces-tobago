/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 19:33:37
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class LabelTag extends BeanTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String forValue;

// ///////////////////////////////////////////// constructor

  public LabelTag() {
    i18n = true; // overwrite default
  }

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
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
