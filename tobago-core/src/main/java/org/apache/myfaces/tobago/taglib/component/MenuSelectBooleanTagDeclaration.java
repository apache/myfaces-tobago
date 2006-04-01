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
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 31.03.2006
 * Time: 21:46:55
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a checkable menuitem.
 */

@Tag(name = "menucheck")
@UIComponentTag(
    UIComponent = "org.apache.myfaces.tobago.component.UISelectBooleanCommand",
    RendererType = "MenuCommand")

public interface MenuSelectBooleanTagDeclaration extends TobagoTagDeclaration, CommandTagDeclaration,
    HasIdBindingAndRendered, IsDisabled, HasAction, HasCommandType, HasBooleanValue, HasLabelAndAccessKey,
    IsImmediateCommand {
}
