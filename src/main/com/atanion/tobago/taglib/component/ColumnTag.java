/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

public class ColumnTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ColumnTag.class);

// ///////////////////////////////////////////// attribute

  protected String sortable;
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
   ComponentUtil.setBooleanProperty(component, ATTR_SORTABLE, sortable, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_ALIGN, align, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_STYLE_CLASS, cssClass, getIterationHelper());
  }

  protected void provideLabel(UIComponent component) {
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
  }

  public void release() {
    super.release();
    sortable = null;
    align = null;
    cssClass = null;
  }
  
// ///////////////////////////////////////////// bean getter + setter

  public String getSortable() {
    return sortable;
  }

  public void setSortable(String sortable) {
    this.sortable = sortable;
  }

  public String getAlign() {
    return align;
  }

  public void setAlign(String align) {
    this.align = align;
  }

  public String getCssClass() {
    return cssClass;
  }

  public void setCssClass(String cssClass) {
    this.cssClass = cssClass;
  }
}
