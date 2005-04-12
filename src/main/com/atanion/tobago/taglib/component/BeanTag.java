/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasConverter;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsRequired;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;


public abstract class BeanTag extends TobagoTag {

// ///////////////////////////////////////////// constants

// ///////////////////////////////////////////// attributes

  private String converter;
  private String value;
  private String required;

// ///////////////////////////////////////////// constructors

// ///////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ValueHolder valueHolder = (ValueHolder) component;
    if (converter != null && valueHolder.getConverter() == null) {
      valueHolder.setConverter(
          FacesContext.getCurrentInstance().getApplication().createConverter(
              converter));
    }

    ComponentUtil.setBooleanProperty(component, ATTR_REQUIRED, required, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
  }

  public void release() {
    super.release();
    this.converter = null;
    this.value = null;
    this.required = null;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getConverter() {
    return converter;
  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }
}
