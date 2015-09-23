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
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasItemLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessageForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequiredForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UISelectBoolean;

/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@BodyContentDescription(anyTagOf = "<f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectBooleanCheckbox",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox",
    uiComponentFacesClass = "javax.faces.component.UISelectBoolean",
    interfaces = "org.apache.myfaces.tobago.component.SupportsAccessKey",
    componentFamily = UISelectBoolean.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_BOOLEAN_CHECKBOX,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(name = Facets.CLICK,
            description =
                "This facet can contain a UICommand that is invoked in case of a click event from the component",
            allowedChildComponenents = "org.apache.myfaces.tobago.Command"),
        @Facet(name = Facets.CHANGE,
            description =
                "This facet can contain a UICommand that is invoked in case of a change event from the component",
            allowedChildComponenents = "org.apache.myfaces.tobago.Command")
    })

public interface SelectBooleanCheckboxTagDeclaration extends HasValidator,
    HasValueChangeListener, HasIdBindingAndRendered, HasLabelAndAccessKey, HasValue, IsDisabled,
    HasTip, IsReadonly, HasTabIndex, IsRequiredForSelect, HasConverter, IsFocus,
    HasValidatorMessage, HasRequiredMessageForSelect, HasConverterMessage, IsVisual, HasItemLabel {
}
