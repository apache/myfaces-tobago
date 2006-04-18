package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 20.02.2006
 * Time: 22:10:07
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UIData component.
 */
@Tag(name = "column")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIColumn")
public interface ColumnTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered, HasLabel {
  /**
   * Alignment of this column.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setAlign(String align);

  /**
   * Flag indicating whether or not this column is sortable.
   * To make a column sortable the data of the sheet must be one of
   * <code>java.util.List</code>, <code>Object[]</code> or instance of
   * <code>org.apache.myfaces.tobago.model.SortableByApplication</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = { "java.lang.Boolean" },
      defaultValue = "false")
  void setSortable(String sortable);
}
