/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;

public class MultiSelectTag extends SelectTag{

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  Integer rows;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_ROWS, rows);
  }

// /////////////////////////////////////////// bean getter + setter

  public void setRows(Integer rows) {
    this.rows = rows;
  }
}
