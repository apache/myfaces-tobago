/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;

public class TextAreaTag extends TextBoxTag {
// ----------------------------------------------------------------- attributes

  private String rows;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    rows = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_ROWS, rows);
  }

// ------------------------------------------------------------ getter + setter
  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }
}

