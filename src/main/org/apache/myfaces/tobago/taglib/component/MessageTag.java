/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created Jan 20, 2003.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasFor;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;

/**
 * Renders error/validation message.
 */
@Tag(name="message", bodyContent=BodyContent.EMPTY)
public class MessageTag extends TobagoTag
    implements HasIdBindingAndRendered, HasFor {


// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String _for;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIMessage.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for, getIterationHelper());
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
