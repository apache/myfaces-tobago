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
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UIPanel;

/**
 * Renders a tab within a tab group.
 */
@Tag(name = "tab")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITab",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITab",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    interfaces = {
        "javax.faces.component.NamingContainer",
        "org.apache.myfaces.tobago.component.SupportsAccessKey"
    },
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.TAB,
    facets = {
        @Facet(name = Facets.BAR,
            description = "Contains some code to be placed in the header at the bar position.")})
public interface TabTagDeclaration
    extends HasIdBindingAndRendered, IsVisual, HasLabel, HasAccessKey, HasTip, IsDisabled, HasImage {
}
