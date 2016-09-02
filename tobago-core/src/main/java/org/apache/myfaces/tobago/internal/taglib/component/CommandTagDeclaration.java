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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConfirmation;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLink;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasResource;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTarget;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDefaultCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabledBySecurity;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsOmit;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsTransition;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UICommand;

/**
 * Use this tag only as a facet for the client events "click", "change"
 * in selectOneRadio, selectBooleanCheckbox, selectManyCheckbox, selectOneChoice, etc.
 * TODO: may add some events and components. See {@link org.apache.myfaces.tobago.component.Facets#isEvent(String)}
 */
@Tag(name = "command")
@UIComponentTag(uiComponent = "org.apache.myfaces.tobago.component.UICommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUICommand",
    uiComponentFacesClass = "javax.faces.component.UICommand",
    componentFamily = UICommand.COMPONENT_FAMILY,
    rendererType = RendererTypes.COMMAND,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(
            name = Facets.CONFIRMATION,
            description = "Contains a UIOutput instance with the confirmation message.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Out")})
public interface CommandTagDeclaration
    extends HasIdBindingAndRendered, HasAction, HasActionListener, IsImmediateCommand, HasConfirmation,
    HasLink, HasResource, IsTransition, HasTarget, IsDisabledBySecurity,
    IsOmit, HasValue, IsVisual, HasLabelAndAccessKey, HasTip, HasImage,
    IsDefaultCommand {
}
