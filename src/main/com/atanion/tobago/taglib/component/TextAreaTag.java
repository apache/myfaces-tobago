/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasRows;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

@Tag(name="textarea", bodyContent="empty")
public class TextAreaTag extends InTag implements HasHeight, HasRows {
// ----------------------------------------------------------------- attributes

  private String rows;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    rows = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_ROWS, rows, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter
  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }
}

