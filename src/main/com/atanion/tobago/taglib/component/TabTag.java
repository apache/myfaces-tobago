/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:10.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIPanel;
import javax.faces.component.UIComponent;

public class TabTag extends TobagoBodyTag {

// /////////////////////////////////////////////// constructors

// /////////////////////////////////////////////// code

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void provideLabel(UIComponent component) {
    provideAttribute(component, label, TobagoConstants.ATTR_LABEL);
  }
// /////////////////////////////////////////////// bean getter + setter

}
