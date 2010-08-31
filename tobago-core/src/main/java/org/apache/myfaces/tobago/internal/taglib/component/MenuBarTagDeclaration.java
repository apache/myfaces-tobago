package org.apache.myfaces.tobago.internal.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;

/**
* Renders a menu bar.<br />
* Add menu bar as facet name="menuBar" to page tag or use it anywhere
* on page.<br />
*/
@Tag(name = "menuBar")
@BodyContentDescription(
    anyClassOf = {"org.apache.myfaces.tobago.internal.taglib.MenuTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuCommandTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSelectOneTag",
        "org.apache.myfaces.tobago.internal.taglib.MenuSeparatorTag"})
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIMenuBar",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIMenuBar",
    rendererType = RendererTypes.MENU_BAR, isComponentAlreadyDefined = false,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.Menu",
        "org.apache.myfaces.tobago.MenuCommand",
        "org.apache.myfaces.tobago.SelectBooleanCommand",
        "org.apache.myfaces.tobago.MenuSelectOne",
        "org.apache.myfaces.tobago.MenuSeparator"
        })
public interface MenuBarTagDeclaration extends HasIdBindingAndRendered, HasMarkup, HasCurrentMarkup {
}
