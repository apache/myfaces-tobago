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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;

import javax.faces.component.UIPanel;

/**
 * Intended for use in situations when only one UIComponent child can be nested, such as in the case of facets.
 */
@Tag(name = "panel")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIPanel",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIPanel",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.PANEL,
    interfaces = "org.apache.myfaces.tobago.component.SupportsMarkup",
    facets = {
        @Facet(name = Facets.RELOAD, description = "Contains an instance of UIReload",
            allowedChildComponenents = "org.apache.myfaces.tobago.Reload"),
        @Facet(name = Facets.LAYOUT, description = "Contains an instance of AbstractUILayoutBase",
            allowedChildComponenents = "org.apache.myfaces.tobago.GridLayout")})
public interface PanelTagDeclaration
    extends HasIdBindingAndRendered, IsGridLayoutComponent, IsGridLayoutContainer, HasMarkup, HasCurrentMarkup, HasTip {
}
