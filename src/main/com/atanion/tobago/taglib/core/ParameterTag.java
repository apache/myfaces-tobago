/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 13.05.2003 11:28:34.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.TobagoTag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class ParameterTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ParameterTag.class);

// ///////////////////////////////////////////// attribute

  protected String name;
  protected String value;

// ///////////////////////////////////////////// constructor

  public ParameterTag() {
  }

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIParameter.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    UIParameter parameter = (UIParameter) component;

    setProperty(parameter, TobagoConstants.ATTR_NAME, name);

    if (value != null) {
      if (isValueReference(value)) {
        ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
        parameter.setValueBinding("value", valueBinding);
      } else {
        parameter.setValue(value);
      }
    }

  }

// ///////////////////////////////////////////// bean getter + setter

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
