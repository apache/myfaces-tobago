/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Sep 9, 2002 4:28:29 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public class CheckBoxGroupTag extends MultiSelectTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  String renderRange;

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

