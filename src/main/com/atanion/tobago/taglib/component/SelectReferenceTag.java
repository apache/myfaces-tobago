/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.03.2004 at 15:41:39.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

@Tag(name="selectReference")
public class SelectReferenceTag extends TobagoTag
    implements HasIdBindingAndRendered {
// ----------------------------------------------------------------- attributes

  private String _for;

  private String renderRange;

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
    renderRange = null;
  }


  /**
   *  Id of the component, this is related to.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setFor(String _for) {
    this._for = _for;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getRenderRange() {
    return renderRange;
  }


  /**
   *  Range of items to render.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}