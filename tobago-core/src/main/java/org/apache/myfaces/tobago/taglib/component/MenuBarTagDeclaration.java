package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 14.03.2006
 * Time: 17:14:12
 * To change this template use File | Settings | File Templates.
 */
/**
 * Renders a menu bar.<br>
 * Add menu bar as facet name="menuBar" to page tag or use it anywhere
 * on page.<br>
 */
@Tag(name = "menuBar")
@BodyContentDescription(
    anyClassOf = { "org.apache.myfaces.tobago.taglib.component.MenuTag",
        "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
        "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })
@UIComponentTag(
    uiComponent = "javax.faces.component.UIPanel",
    rendererType = "MenuBar")
public interface MenuBarTagDeclaration extends TobagoBodyTagDeclaration, HasIdBindingAndRendered, HasWidth {
}
