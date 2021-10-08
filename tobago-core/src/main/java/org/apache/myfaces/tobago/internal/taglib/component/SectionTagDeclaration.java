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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCollapsedMode;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsCollapsed;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

/**
 * Renders a section or subsection.
 */
@Tag(name = "section")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISection",
    uiComponentFacesClass = "jakarta.faces.component.UIPanel",
    componentFamily = "org.apache.myfaces.tobago.Section",
    rendererType = RendererTypes.SECTION,
    interfaces = {
        // As long as no behavior event names are defined, ClientBehaviorHolder must be implemented for Mojarra.
        "jakarta.faces.component.behavior.ClientBehaviorHolder"
    },
    facets = {
        @Facet(name = Facets.LABEL,
            description = "Contains some code to be placed in the header at the label position."),
        @Facet(name = Facets.BAR,
            description = "Contains some code to be placed in the header at the bar position.")
    })

public interface SectionTagDeclaration
    extends HasIdBindingAndRendered, HasLabel, IsVisual, HasImage, IsCollapsed, HasCollapsedMode, HasTip,
    HasAutoSpacing {

  /**
   * Level of the title. Lower value means bigger title, greater value means smaller title.
   * Value goes from 1 to 6.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"})
  void setLevel(String level);
}
