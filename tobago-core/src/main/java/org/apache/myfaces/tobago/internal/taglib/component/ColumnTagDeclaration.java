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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.layout.VerticalAlign;

import jakarta.faces.component.UIColumn;

/**
 * Renders a UIComponent that represents a single column of data within a
 * parent UISheet component.
 */
@Tag(name = "column")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIColumn",
    uiComponentFacesClass = "jakarta.faces.component.UIColumn",
    componentFamily = UIColumn.COMPONENT_FAMILY,
    rendererType = RendererTypes.COLUMN,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    facets = {
        @Facet(name = Facets.LABEL,
            description = "Contains some code to be placed in the header at the label position."),
        @Facet(name = Facets.BAR,
            description = "Contains some code to be placed in the header at the bar position.")
    }
)
public interface ColumnTagDeclaration
    extends HasIdBindingAndRendered, HasLabel, HasTip, IsVisual {

  /**
   * Horizontal alignment of this column.
   * Possible values:
   * {@link org.apache.myfaces.tobago.layout.TextAlign#left} (default),
   * {@link org.apache.myfaces.tobago.layout.TextAlign#right},
   * {@link org.apache.myfaces.tobago.layout.TextAlign#center},
   * {@link org.apache.myfaces.tobago.layout.TextAlign#justify}
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"org.apache.myfaces.tobago.layout.TextAlign"},
      allowedValues = {
          TextAlign.LEFT, TextAlign.RIGHT, TextAlign.CENTER, TextAlign.JUSTIFY
      })
  void setAlign(String align);

  /**
   * Vertical alignment of this column.
   * Possible values:
   * {@link org.apache.myfaces.tobago.layout.VerticalAlign#top} (default),
   * {@link org.apache.myfaces.tobago.layout.VerticalAlign#bottom},
   * {@link org.apache.myfaces.tobago.layout.VerticalAlign#middle}
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"org.apache.myfaces.tobago.layout.VerticalAlign"},
      allowedValues = {
          VerticalAlign.TOP, VerticalAlign.BOTTOM, VerticalAlign.MIDDLE
      })
  void setVerticalAlign(String verticalAlign);

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
   * Allowed layout tokens ('*', '&lt;x>*', '&lt;x>px' or '&lt;x>%').
   * Where '*' is equivalent to '1*'.
   */
/* TBD
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.layout.Measure")
  void setWidth(String width);
*/
}
