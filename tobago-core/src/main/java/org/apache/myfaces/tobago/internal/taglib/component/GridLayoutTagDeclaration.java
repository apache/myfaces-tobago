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
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasColumnLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRowLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

/**
 * Renders a grid-layout. The grid is defined by the columns and rows attributes. You can adjust the
 * widths of the columns and rows by value of the tokens. The number of tokens describe the number of
 * columns and rows.
 * <pre>
 * columns/rows ::= LAYOUT
 * LAYOUT       ::= TOKEN [" " TOKEN]+
 * TOKEN        ::= "auto" | MEASURE | FRACTION
 * MEASURE      ::= [NUMBER] ("px" | "em" | "ex" | "rem" | ...)
 * FRACTION     ::= [POSITIVE_INTEGER] "fr"
 * </pre>
 */
@Preliminary
@Tag(name = "gridLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIGridLayout",
    componentFamily = AbstractUIGridLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.GRID_LAYOUT,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    allowedChildComponenents = "NONE",
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SPREAD,
            description = "Use the full height for the HTML content."
        )
    })
public interface GridLayoutTagDeclaration extends HasId, HasSpacing,
    HasColumnLayout, HasRowLayout, HasBinding, IsVisual {

}
