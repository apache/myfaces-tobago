/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 19:33:37
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class LabelTag extends BeanTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String _for;

// ///////////////////////////////////////////// constructor

  public LabelTag() {
    setI18n(Boolean.TRUE.toString()); // overwrite default
  }

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_FOR, _for);
  }

  public void release() {
    super.release();
    _for = null;
    setI18n(Boolean.TRUE.toString()); // overwrite default
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getFor() {
    return _for;
  }

  public void setFor(String _for) {
    this._for = _for;
  }
}
