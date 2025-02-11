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

import jakarta.faces.component.UIData;
import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.Placement;

/**
 * Render toasts elements.
 */
@Preliminary
@Tag(name = "toasts")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIToasts",
    uiComponentFacesClass = "jakarta.faces.component.UIData",
    componentFamily = UIData.COMPONENT_FAMILY,
    rendererType = RendererTypes.TOASTS,
    facets = {
        @Facet(name = Facets.RELOAD, description = "Contains an instance of UIReload",
            allowedChildComponents = "org.apache.myfaces.tobago.Reload"),
        @Facet(name = Facets.HEADER, description = "Contains code to be placed in the header of the toast.")},
    behaviors = {
        @Behavior(name = ClientBehaviors.RELOAD, isDefault = true)
    })
public interface ToastsTagDeclaration extends HasIdBindingAndRendered, HasVar, IsVisual {

  /**
   * Must be a collection of #{@link org.apache.myfaces.tobago.application.Toast} objects.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.util.Collection<org.apache.myfaces.tobago.application.Toast>"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * Defines the placement of the toasts. Default is "bottomRight".
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.Placement",
      allowedValues = {
          Placement.TOP_LEFT, Placement.TOP_CENTER, Placement.TOP_RIGHT,
          Placement.MIDDLE_LEFT, Placement.MIDDLE_CENTER, Placement.MIDDLE_RIGHT,
          Placement.BOTTOM_LEFT, Placement.BOTTOM_CENTER, Placement.BOTTOM_RIGHT
      },
      defaultValue = Placement.BOTTOM_RIGHT,
      defaultCode = "org.apache.myfaces.tobago.layout.Placement.bottomRight")
  void setPlacement(String placement);

  /**
   * Delay in milliseconds before hiding the toast. Default is "5000".
   * Set to "-1" to never automatically hide the toast.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "5000")
  void setDisposeDelay(String disposeDelay);
}
