/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:10.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;

public class TabTag extends TobagoBodyTag {

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void provideLabel(UIComponent component) {
    setStringProperty(component, ATTR_LABEL, label);
  }
}

