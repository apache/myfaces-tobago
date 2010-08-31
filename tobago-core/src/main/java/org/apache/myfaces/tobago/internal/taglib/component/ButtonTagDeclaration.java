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
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCommandType;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDefaultCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsInline;

/**
 * Renders a button element.
 */
@Tag(name = "button", tagExtraInfoClassName = "org.apache.myfaces.tobago.internal.taglib.component.CommandTagExtraInfo")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIButton",
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.UICommand",
    rendererType = RendererTypes.BUTTON,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(name = Facets.CONFIRMATION, description = "Contains a UIOutput instance with the confirmation message.",
            allowedChildComponenents = ComponentTypes.OUT),
        @Facet(name = Facets.POPUP, description = "Contains a UIPopup instance.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Popup")})
public interface ButtonTagDeclaration
    extends AbstractCommandTagDeclaration, HasIdBindingAndRendered, HasLabelAndAccessKey, HasImage,
    IsDisabled, HasCommandType, IsDefaultCommand, HasTip,
    IsInline, HasMarkup, HasCurrentMarkup, HasTabIndex, IsGridLayoutComponent {
}
