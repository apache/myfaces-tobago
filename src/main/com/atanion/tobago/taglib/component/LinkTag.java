/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public class LinkTag extends CommandTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String target;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_TARGET, target);
  }

  public void release() {
    super.release();
    target = null;
  }

// /////////////////////////////////////////// bean getter + setter

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }
}
