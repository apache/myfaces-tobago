/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

public class TextAreaTag extends TextBoxTag {

// /////////////////////////////////////////// attributes

  private int rows = 3;

// /////////////////////////////////////////// constructors

  public TextAreaTag() {
    super();
  }

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    if (rows > 0 && null == component.getAttributes().get(TobagoConstants.ATTR_ROWS)) {
      component.getAttributes().put(TobagoConstants.ATTR_ROWS, new Integer(rows));
    }
  }

  public void release() {
    super.release();
    this.rows = 3;
  }

// /////////////////////////////////////////// bean getter + setter
  
  public int getRows() {
    return this.rows;
  }

  public void setRows(int argRows) {
    this.rows = argRows;
  }

}
