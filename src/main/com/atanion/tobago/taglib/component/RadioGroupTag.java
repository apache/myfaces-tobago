/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public class RadioGroupTag extends SingleSelectTag{

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String renderRange;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_RENDER_RANGE, renderRange);

  }

// /////////////////////////////////////////// bean getter + setter
  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}

