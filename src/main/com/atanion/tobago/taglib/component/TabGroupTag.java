/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:05.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

public class TabGroupTag extends TobagoTag {
// ----------------------------------------------------------------- attributes

  private String serverside;
  private String stateBinding;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UITabGroup.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setBooleanProperty(component, ATTR_SERVER_SIDE_TABS, serverside, getIterationHelper());

    // todo: check, if it is an writeable object
    if (stateBinding != null && isValueReference(stateBinding)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(stateBinding, getIterationHelper());
      component.setValueBinding(ATTR_STATE_BINDING, valueBinding);
    }
  }

  public void release() {
    super.release();
    serverside = null;
    stateBinding = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getServerside() {
    return serverside;
  }

  public void setServerside(String serverside) {
    this.serverside = serverside;
  }

  public void setStateBinding(String stateBinding) {
    this.stateBinding = stateBinding;
  }
}

