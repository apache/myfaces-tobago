/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created Jan 20, 2003.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;

/**
 * Renders error/validation messages.
 */
@Tag(name="messages", bodyContent=BodyContent.EMPTY)
public class MessagesTag extends TobagoTag
    implements HasIdBindingAndRendered, HasFor {


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
