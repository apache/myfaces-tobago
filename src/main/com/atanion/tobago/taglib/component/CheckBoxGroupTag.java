/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Sep 9, 2002 4:28:29 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class CheckBoxGroupTag extends MultiSelectTag {
// ----------------------------------------------------------------- attributes

  private String renderRange;

// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange);
  }

  public void release() {
    super.release();
    renderRange = null;
  }
// ------------------------------------------------------------ getter + setter

  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}

