/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


public abstract class BeanTag extends TobagoTag {

// ///////////////////////////////////////////// constants

  private static final Log LOG = LogFactory.getLog(BeanTag.class);

// ///////////////////////////////////////////// attributes

//  private String scope;
  private String converter;
  private String value;
  private boolean required;

// ///////////////////////////////////////////// constructors

// ///////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ValueHolder valueHolder = (ValueHolder) component;
      if (converter != null && valueHolder.getConverter() == null) {
      valueHolder.setConverter(
          FacesContext.getCurrentInstance().getApplication().createConverter(converter));
      }

    if (required) {
      ((EditableValueHolder) valueHolder).setRequired(true);
    }

    if (value != null) {
      if (isValueReference(value)) {
        ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
        component.setValueBinding("value", valueBinding);
      } else {
        valueHolder.setValue(value);
      }
    }

  }

  public void release() {
    super.release();
//    this.scope = null;
    this.converter = null;
    this.value = null;
  }

// ///////////////////////////////////////////// bean getter + setter

//  public void setScope(String scope) {
//    this.scope = scope;
//  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }
}
