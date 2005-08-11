/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.03.2004 at 15:41:39.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a set of option related to and same type as the <strong>for</strong>
 * component.
 */
@Tag(name="selectReference", bodyContent=BodyContent.EMPTY)
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
  @UIComponentTagAttribute()
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
  @UIComponentTagAttribute()
  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}