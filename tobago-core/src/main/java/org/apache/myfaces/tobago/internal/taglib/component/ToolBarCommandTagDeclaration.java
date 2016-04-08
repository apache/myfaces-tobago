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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLink;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderedPartially;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasResource;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTarget;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsOmit;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsTransition;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UICommand;

/**
 * Renders a command button within a toolbar.
 */
@Tag(name = "toolBarCommand")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIToolBarCommand",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIToolBarCommand",
    uiComponentFacesClass = "javax.faces.component.UICommand",
    componentFamily = UICommand.COMPONENT_FAMILY,
    rendererType = RendererTypes.BUTTON,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(name = Facets.CONFIRMATION, description = "Contains a UIOutput instance with the confirmation message.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Out"),
        @Facet(name = Facets.POPUP, description = "Contains a UIPopup instance.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Popup"),
        @Facet(name = Facets.DROP_DOWN_MENU, description = "Contains a UIMenu instance to render a drop down menu.",
            allowedChildComponenents = "org.apache.myfaces.tobago.Menu")
    })
public interface ToolBarCommandTagDeclaration
    extends HasAction, HasActionListener, IsImmediateCommand,
    HasLink, HasResource, IsTransition, HasTarget, HasRenderedPartially, IsOmit, HasIdBindingAndRendered,
    HasLabelAndAccessKey, HasImage, IsDisabled, HasTip, IsVisual {
}
