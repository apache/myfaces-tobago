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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConfirmation;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFragment;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLink;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOutcome;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTarget;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabledBySecurity;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsOmit;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsTransition;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UICommand;

/**
 * Add an event behavior to the component.
 * It can contain f:ajax and tc:operation tags.
 */
@Tag(name = "event")
@UIComponentTag(uiComponent = "org.apache.myfaces.tobago.component.UIEvent",
    uiComponentFacesClass = "jakarta.faces.component.UICommand",
    componentFamily = UICommand.COMPONENT_FAMILY,
    rendererType = RendererTypes.EVENT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    behaviors = {
        @Behavior(name = ClientBehaviors.CHANGE),
        @Behavior(
            name = ClientBehaviors.CLICK,
            description = "Behavior of a click event.",
            isDefault = true),
        @Behavior(name = ClientBehaviors.INPUT),
        @Behavior(name = ClientBehaviors.DBLCLICK),
        @Behavior(name = ClientBehaviors.FOCUS),
        @Behavior(name = ClientBehaviors.BLUR),
        @Behavior(name = ClientBehaviors.MOUSEOUT),
        @Behavior(name = ClientBehaviors.MOUSEOVER),
        @Behavior(name = ClientBehaviors.COMPLETE),
        @Behavior(name = ClientBehaviors.LOAD),
        @Behavior(name = ClientBehaviors.RELOAD),
        @Behavior(name = ClientBehaviors.RESIZE)
    },
    faceletHandler = "org.apache.myfaces.tobago.facelets.EventHandler")
public interface EventTagDeclaration
    extends HasIdBindingAndRendered, HasAction, HasActionListener, IsImmediateCommand, HasConfirmation,
    HasLink, HasOutcome, HasFragment, IsTransition, HasTarget, IsDisabledBySecurity, IsOmit {

  /**
   * The name of the event as an instance of {@link org.apache.myfaces.tobago.component.ClientBehaviors}
   * This will be also overwrite events of possible f:ajax children.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.component.ClientBehaviors")
  void setEvent(ValueExpression event);

}
