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
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConfirmation;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFragment;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLink;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOutcome;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTarget;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDefaultCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabledBySecurity;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsOmit;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsTransition;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UICommand;

/**
 * Renders a button element.
 */
@Tag(name = "button")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIButton",
    uiComponentFacesClass = "jakarta.faces.component.UICommand",
    interfaces = {
        "org.apache.myfaces.tobago.component.SupportsAccessKey"
    },
    componentFamily = UICommand.COMPONENT_FAMILY,
    rendererType = RendererTypes.BUTTON,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(
            name = Facets.CONFIRMATION,
            description = "Contains a UIOutput instance with the confirmation message.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Out")
    },
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CLICK,
            description = "Behavior of a click event.",
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.DBLCLICK),
        @Behavior(
            name = ClientBehaviors.FOCUS),
        @Behavior(
            name = ClientBehaviors.BLUR)
    },
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_PRIMARY,
            description = "Set button color to primary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SECONDARY,
            description = "Set button color to secondary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SUCCESS,
            description = "Set button color to success color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DANGER,
            description = "Set button color to danger color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_WARNING,
            description = "Set button color to warning color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_INFO,
            description = "Set button color to info color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LIGHT,
            description = "Set button color to light color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DARK,
            description = "Set button color to dark color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_NONE,
            description = "Colorless button."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_BADGE,
            description = "Display the button as a badge."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_PILL,
            description = "Make badges more rounded if button is displayed as a badge."
        )
    })
public interface ButtonTagDeclaration
    extends HasIdBindingAndRendered, HasAction, HasActionListener, IsImmediateCommand, HasConfirmation,
    HasLink, HasOutcome, HasFragment, IsTransition, HasTarget, IsDisabledBySecurity,
    IsOmit, IsVisual, HasLabel, HasAccessKey, HasTip, HasImage,
    IsDefaultCommand, HasTabIndex, HasAutoSpacing {
}
