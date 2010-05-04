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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;

/**
 * Use this tag only as a facet for click, change in selectOneRadio,
 * selectBooleanCheckbox, selectManyCheckbox and selectOneChoice
 */
@Tag(name = "command", tagExtraInfoClassName = "org.apache.myfaces.tobago.internal.taglib.component.CommandTagExtraInfo")
@UIComponentTag(uiComponent = "org.apache.myfaces.tobago.component.UICommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUICommand",
    rendererType = RendererTypes.COMMAND,
    allowedChildComponenents = "NONE")
public interface CommandTagDeclaration
    extends AbstractCommandTagDeclaration, HasId, HasValue, IsDisabled, IsGridLayoutComponent, HasMarkup {



}
