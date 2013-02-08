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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;

import javax.faces.component.UIPanel;

/**
 * Renders a panel-like layout element with the ability to span over more than
 * one layout cell. A cell may only contain one child.
 * @deprecated The Cell is deprecated since Tobago 1.5.0
 */
@Deprecated
@Tag(name = "cell")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UICell",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUICell",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.CELL)
public interface CellTagDeclaration 
    extends HasIdBindingAndRendered, IsGridLayoutComponent, IsGridLayoutContainer, HasMarkup, HasCurrentMarkup {

  @UIComponentTagAttribute(
      type = {"java.lang.Integer"},
      defaultValue = "1")
  void setColumnSpan(String columnSpan);

  @UIComponentTagAttribute(
      type = {"java.lang.Integer"},
      defaultValue = "1")
  void setRowSpan(String rowSpan);

  /**
   * Count of layout columns to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.lang.Integer"},
      defaultValue = "1",
      generate = false)
  void setSpanX(String spanX);

  /**
   * Count of layout rows to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.lang.Integer"},
      defaultValue = "1",
  generate = false)
  void setSpanY(String spanY);

  /**
   * possible values are:
   * <ul>
   * <li>'false' : no scrollbars should be rendered</li>
   * <li>'true'  : scrollbars should always be rendered</li>
   * <li>'auto'  : scrollbars should be rendered when needed</li>
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "false",
      allowedValues = {"false", "true", "auto"})
  void setScrollbars(String scrollbars);
}
