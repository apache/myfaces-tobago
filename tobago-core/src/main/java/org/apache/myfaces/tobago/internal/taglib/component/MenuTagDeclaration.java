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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasStyle;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;

import javax.faces.component.UIPanel;

/**
 * Container component to hold submenus and items.
 */
@Tag(name = "menu")
@BodyContentDescription(
    anyClassOf = {"org.apache.myfaces.tobago.internal.taglib.MenuTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuCommandTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSelectOneTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSeparatorTag"})
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIMenu",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIMenu",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    interfaces = "org.apache.myfaces.tobago.component.SupportsAccessKey",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.MENU,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.Menu",
        "org.apache.myfaces.tobago.MenuCommand",
        "org.apache.myfaces.tobago.SelectBooleanCommand",
        "org.apache.myfaces.tobago.MenuSelectOne",
        "org.apache.myfaces.tobago.MenuSeparator"})
public interface MenuTagDeclaration extends HasIdBindingAndRendered, HasLabelAndAccessKey,
    IsDisabled, HasImage, IsGridLayoutComponent, HasStyle, HasMarkup, HasCurrentMarkup {
}
