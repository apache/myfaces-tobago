/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:05.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public class TabGroupTag extends TobagoTag {

  private boolean serverside;

// ///////////////////////////////////////////// constructors

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UITabGroup.COMPONENT_TYPE;
  }


  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_SERVER_SIDE_TABS, serverside);
  }
// ///////////////////////////////////////////// bean getter and setter

  public void setServerside(boolean serverside) {
    this.serverside = serverside;
  }
}
