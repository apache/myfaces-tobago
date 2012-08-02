/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;

/*
 * Date: 20.02.2006
 * Time: 22:10:07
 */

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UIData component.
 */
@Tag(name = "column")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIColumn")
//rendererType = "Column")
public interface ColumnTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered, HasLabel, HasTip,
    HasWidth {
  /**
   * Alignment of this column.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setAlign(String align);

  /**
   * Flag indicating whether or not this column is sortable.
   * To make a column sortable the data of the sheet must be one of
   * <code>java.util.List</code> or <code>Object[]</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Boolean"},
      defaultValue = "false")
  void setSortable(String sortable);

  /**
   * Flag indicating whether or not the width of this column is resizable.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Boolean"},
      defaultValue = "true")
  void setResizable(String resizable);

  /**
   * Indicate markup of this component.
   * Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  void setMarkup(String markup);
}
