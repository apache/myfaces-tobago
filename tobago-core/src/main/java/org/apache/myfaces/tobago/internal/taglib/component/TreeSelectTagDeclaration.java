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

import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessageForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequiredForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UISelectBoolean;

/**
 * Makes a tree node selectable.
 */
@Tag(name = "treeSelect")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITreeSelect",
    uiComponentFacesClass = "jakarta.faces.component.UISelectBoolean",
    componentFamily = UISelectBoolean.COMPONENT_FAMILY,
    rendererType = RendererTypes.TREE_SELECT,
    allowedChildComponents = "NONE",
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CHANGE,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.INPUT),
        @Behavior(
            name = ClientBehaviors.CLICK),
        @Behavior(
            name = ClientBehaviors.DBLCLICK),
        @Behavior(
            name = ClientBehaviors.FOCUS),
        @Behavior(
            name = ClientBehaviors.BLUR)
    })
public interface TreeSelectTagDeclaration
    extends HasIdBindingAndRendered, HasLabel, HasTip, IsVisual, HasValue, HasValueChangeListener,
    IsDisabled, IsRequiredForSelect, HasConverter,
    HasValidatorMessage, HasRequiredMessageForSelect, HasConverterMessage {

  /**
   * Show a checkbox to visualize the selection state.
   * This is useful to hide the checkbox, e.g. dependent of the type of the node.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setShowCheckbox(String showCheckbox);
}
