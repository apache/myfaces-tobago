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

import jakarta.faces.component.UIColumn;
import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.model.Selectable;

/**
 * Renders a column with checkboxes to mark selected rows.
 */
@Tag(name = "columnSelector")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIColumnSelector",
    uiComponentFacesClass = "jakarta.faces.component.UIColumn",
    componentFamily = UIColumn.COMPONENT_FAMILY,
    rendererType = RendererTypes.COLUMN_SELECTOR,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    behaviors = {
        @Behavior(name = ClientBehaviors.CHANGE, isDefault = true)
    },
    allowedChildComponents = "NONE")
public interface ColumnSelectorTagDeclaration
    extends HasIdBindingAndRendered, IsVisual, IsDisabled {

  /**
   * Indicating the selection mode of the columnSelector. Only effective if sheet selection mode is none.
   * Use case: Show details with tc:row event and columnSelector for action on selected rows.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.Selectable",
      allowedValues = {
          Selectable.SINGLE, Selectable.SINGLE_OR_NONE, Selectable.MULTI
      })
  void setSelectable(String selectable);
}
