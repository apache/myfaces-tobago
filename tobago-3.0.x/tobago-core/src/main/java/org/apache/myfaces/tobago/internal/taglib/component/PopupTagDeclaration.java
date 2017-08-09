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
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.model.CollapseMode;

/**
 * Renders a popup panel.
 */
@Tag(name = "popup")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPopup",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIPopup",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    componentFamily = AbstractUIPopup.COMPONENT_FAMILY,
    rendererType = RendererTypes.POPUP
)
public interface PopupTagDeclaration
    extends HasIdBindingAndRendered, IsVisual, HasTip {

  /**
   * Indicating the collapsed state of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setCollapsed(String collapsed);

  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.CollapseMode",
      defaultValue = CollapseMode.ABSENT,
      defaultCode = "org.apache.myfaces.tobago.model.CollapseMode.absent")
  void setCollapsedMode(String collapsed);
}
