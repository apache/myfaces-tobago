/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class ColumnTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ColumnTag.class);

// ///////////////////////////////////////////// attribute

  private boolean sortable;
  private String align;
  private String cssClass;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIColumn.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_SORTABLE, sortable);
    setProperty(component, TobagoConstants.ATTR_ALIGN, align);
    LOG.debug("class = " + cssClass);
    setProperty(component, TobagoConstants.ATTR_STYLE_CLASS, cssClass);
  }

  protected void provideLabel(UIComponent component) {
    provideAttribute(component, label, TobagoConstants.ATTR_LABEL);
  }


// ///////////////////////////////////////////// bean getter + setter

  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public void setCssClass(String cssClass) {
    LOG.debug("class = " + cssClass);
    this.cssClass = cssClass;
  }
}
