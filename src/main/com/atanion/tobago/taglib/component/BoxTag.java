/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPanel;

import javax.faces.component.UIComponent;

public class BoxTag extends TobagoBodyTag {

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void provideLabel(UIComponent component) {
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
  }
}

