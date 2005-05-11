/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a progressbar.
 */
@Tag(name="progress", bodyContent=BodyContent.EMPTY)
public class ProgressTag extends BeanTag
    implements HasIdBindingAndRendered, HasTip {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String tip;


// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip,
        getIterationHelper());
  }

  public void release() {
    super.release();
    tip = null;
  }

  /**
   * The current value of this component. 
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"javax.swing.BoundedRangeModel"})
  public void setValue(String value) {
    super.setValue(value);
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
  // /////////////////////////////////////////// bean getter + setter

}
