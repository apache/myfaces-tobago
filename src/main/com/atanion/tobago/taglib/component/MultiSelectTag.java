/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;

public class MultiSelectTag extends InputTag{

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String rows;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    setIntegerProperty(component, ATTR_ROWS, rows);
  }

  public void release() {
    super.release();
    rows = null;
  }
  
// /////////////////////////////////////////// bean getter + setter

  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }
}
