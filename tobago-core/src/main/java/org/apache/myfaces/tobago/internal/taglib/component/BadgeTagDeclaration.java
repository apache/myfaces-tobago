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

import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIOutput;

/**
 * Renders a badge element.
 */
@Tag(name = "badge")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIBadge",
    uiComponentFacesClass = "jakarta.faces.component.UIOutput",
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    componentFamily = UIOutput.COMPONENT_FAMILY,
    rendererType = RendererTypes.BADGE,
    allowedChildComponents = "NONE",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_PILL,
            description = "Make badges more rounded."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_PRIMARY,
            description = "Set badge color to primary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SECONDARY,
            description = "Set badge color to secondary color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DANGER,
            description = "Set badge color to danger color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_WARNING,
            description = "Set badge color to warning color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SUCCESS,
            description = "Set badge color to success color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_INFO,
            description = "Set badge color to info color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LIGHT,
            description = "Set badge color to light color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DARK,
            description = "Set badge color to dark color of the theme."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_NONE,
            description = "Colorless badge."
        )
    })
public interface BadgeTagDeclaration extends HasIdBindingAndRendered, HasTip, IsVisual, HasConverter, HasValue, HasImage {
}
