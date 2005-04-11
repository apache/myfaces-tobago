/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;

public class ProgressTag extends BeanTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String tip;


// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip,
        getIterationHelper());
  }

  public void release() {
    super.release();
    tip = null;    
  }

  // /////////////////////////////////////////// bean getter + setter

}
