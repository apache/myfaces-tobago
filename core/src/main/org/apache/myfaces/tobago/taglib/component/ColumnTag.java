/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
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

