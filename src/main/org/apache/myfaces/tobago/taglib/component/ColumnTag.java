/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UIData component.
 */
@Tag(name="column")
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
   *  
   *  Alignment of this column.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setAlign(String align) {
    this.align = align;
  }

  public String getSortable() {
    return sortable;
  }

  /**
   *
   * Flag indicating whether or not this column is sortable.
   * To make a column sortable the data of the sheet must be one of
   * <code>java.util.List</code>, <code>Object[]</code> or instance of
   * <code>org.apache.myfaces.tobago.model.SortableByApplication</code>.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.lang.Boolean"}, defaultValue="false")
  public void setSortable(String sortable) {
    this.sortable = sortable;
  }
}

