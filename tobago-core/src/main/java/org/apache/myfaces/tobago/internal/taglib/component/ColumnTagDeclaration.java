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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UISheet component.
 */
@Tag(name = "column")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIColumn",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIColumn",
    rendererType = RendererTypes.COLUMN,
    facets = {
        @Facet(name = Facets.DROP_DOWN_MENU, description = "Contains a UIMenu instance to render a drop down menu."
            + " (not implemented yet, work in progress)", // XXX
            allowedChildComponenents = "org.apache.myfaces.tobago.Menu")
    })
public interface ColumnTagDeclaration
    extends HasIdBindingAndRendered, HasLabel, HasTip, HasMarkup, HasCurrentMarkup, IsGridLayoutContainer {
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
  @UIComponentTagAttribute(type = {"boolean"}, defaultValue = "false")
  void setSortable(String sortable);

  /**
   * Flag indicating whether or not the width of this column in a sheet is resizable, by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"boolean"}, defaultValue = "true")
  void setResizable(String resizable);

  /**
   * The layout token for this column.
   * Allowd layout tokens ('*', '&lt;x>*', '&lt;x>px' or '&lt;x>%').
   * Where '*' is equivalent to '1*'.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setWidth(String width);
}
