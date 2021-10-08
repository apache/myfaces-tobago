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
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIPanel;

/**
 * Defines a container for navigation elements, branding, ...
 */
@Tag(name = "bar")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIBar",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.BAR,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    facets = {
        @Facet(
            name = Facets.BRAND,
            description = "Contains an element which will get a 'navbar-brand' style, e.g. use <tc:link> "),
        @Facet(
            name = Facets.AFTER,
            description = "Content will be rendered usually at the right end of the bar.")},
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DARK,
            description = "Theming for dark backgrounds"
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LIGHT,
            description = "Theming for light backgrounds"
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SMALL,
            description = "Bar collapse at a small size."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_MEDIUM,
            description = "Bar collapse at a medium size."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_LARGE,
            description = "Bar collapse at a large size."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_EXTRA_LARGE,
            description = "Bar collapse at a extra large size."
        )
    })
public interface BarTagDeclaration
    extends HasIdBindingAndRendered, HasTip, IsVisual {
}
