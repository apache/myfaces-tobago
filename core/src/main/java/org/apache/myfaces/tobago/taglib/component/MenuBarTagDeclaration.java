package org.apache.myfaces.tobago.taglib.component;

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
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedWidth;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UIMenuSeparator;

/*
 * Date: 14.03.2006
 * Time: 17:14:12
 */

/**
* Renders a menu bar.<br />
* Add menu bar as facet name="menuBar" to page tag or use it anywhere
* on page.<br />
*/
@Tag(name = "menuBar")
@BodyContentDescription(
    anyClassOf = {"org.apache.myfaces.tobago.taglib.component.MenuTag",
        "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag"})
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIMenuBar",
    uiComponentBaseClass = "javax.faces.component.UIPanel",
    rendererType = "MenuBar", isComponentAlreadyDefined = false,
    allowedChildComponenents = {
        UIMenu.COMPONENT_TYPE,
        UIMenuCommand.COMPONENT_TYPE,
        UISelectBooleanCommand.COMPONENT_TYPE,
        UIMenuSelectOne.COMPONENT_TYPE,
        UIMenuSeparator.COMPONENT_TYPE
        })
public interface MenuBarTagDeclaration extends TobagoBodyTagDeclaration, HasIdBindingAndRendered, HasDeprecatedWidth {
}
