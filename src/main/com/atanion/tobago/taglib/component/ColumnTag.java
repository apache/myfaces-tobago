/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UIData component.
 */
@Tag(name="column", bodyContent="JSP")
public class ColumnTag extends TobagoTag
    implements HasIdBindingAndRendered, HasLabel {
  // ----------------------------------------------------------------- attributes

  protected String sortable;
  private String align;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIColumn.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected void provideLabel(UIComponent component) {
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
  }

  public void release() {
    super.release();
    sortable = null;
    align = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setBooleanProperty(component, ATTR_SORTABLE, sortable, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_ALIGN, align, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getAlign() {
    return align;
  }


  /**
   *  <![CDATA[
   *  Alignment of this column.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class)
  public void setAlign(String align) {
    this.align = align;
  }

  public String getSortable() {
    return sortable;
  }

   /**
   *  <![CDATA[
   *  Flag indicating whether or not this column is sortable.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Boolean.class, defaultValue="false")
  public void setSortable(String sortable) {
    this.sortable = sortable;
  }
}

