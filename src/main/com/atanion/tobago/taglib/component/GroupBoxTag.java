/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIPanel;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class GroupBoxTag extends TobagoBodyTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void provideLabel(UIComponent component) {
    setStringProperty(component, ATTR_LABEL, label);
  }

// /////////////////////////////////////////// bean getter + setter

}
