/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIColumnSelector;
import com.atanion.tobago.component.ComponentUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

public class ColumnSelectorTag extends ColumnTag {

  private static final Log LOG = LogFactory.getLog(ColumnSelectorTag.class);

  private String disabled ;

  public String getComponentType() {
    return UIColumnSelector.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_DISABLED, disabled, getIterationHelper());
  }

  public void release() {
    super.release();
    disabled = null;
  }

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }
}
