/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created Jan 20, 2003.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;

public class MessagesTag extends TobagoTag {


// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String _for;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIMessages.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for);
  }

  public void release() {
    super.release();
    _for = null;
  }
  
// ///////////////////////////////////////////// bean getter + setter

  public String getFor() {
    return _for;
  }

  public void setFor(String _for) {
    this._for = _for;
  }
}
