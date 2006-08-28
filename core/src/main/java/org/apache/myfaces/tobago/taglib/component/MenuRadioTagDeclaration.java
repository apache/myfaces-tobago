package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;

/*
 * $Id: MenuSelectOneTag.java 390886 2006-04-02 19:32:32Z bommel $
 */

/**
 * Renders a submenu with select one items (like a radio button).
 */
@Tag(name = "menuRadio", tagExtraInfoClassName = "org.apache.myfaces.tobago.taglib.component.CommandTagExtraInfo")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectOneCommand",
    rendererType = "MenuCommand")
public interface MenuRadioTagDeclaration extends TobagoTagDeclaration, AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, HasLabel, IsDisabled, HasCommandType, HasValue {
}
