/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class RadioGroupTag extends SingleSelectTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String renderRange;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange);

  }

  public void release() {
    super.release();
    renderRange = null;
  }

// /////////////////////////////////////////// bean getter + setter
  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}

