/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 19:33:37
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class LabelTag extends BeanTag {

// ----------------------------------------------------------------- attributes

  private String _for;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public String getFor() {
    return _for;
  }

  public void release() {
    super.release();
    _for = null;
  }

  public void setFor(String _for) {
    this._for = _for;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for, getIterationHelper());
  }
}

