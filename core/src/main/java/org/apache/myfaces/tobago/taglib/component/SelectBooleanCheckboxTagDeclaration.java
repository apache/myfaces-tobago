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
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasConverterMessage;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasRequiredMessage;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValidatorMessage;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsGridLayoutComponent;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@BodyContentDescription(anyTagOf = "<f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectBooleanCheckbox",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox",
    rendererType = RendererTypes.SELECT_BOOLEAN_CHECKBOX,
    allowedChildComponenents = "NONE",
    facets = {
        @Facet(name= Facets.CLICK,
            description =
                "This facet can contain a UICommand that is invoked in case of a click event from the component",
            allowedChildComponenents = "org.apache.myfaces.tobago.Command"),
        @Facet(name=Facets.CHANGE,
            description =
                "This facet can contain a UICommand that is invoked in case of a change event from the component",
                allowedChildComponenents = "org.apache.myfaces.tobago.Command")
        })

public interface SelectBooleanCheckboxTagDeclaration extends HasValidator,
    HasOnchange, HasValueChangeListener, HasIdBindingAndRendered, HasLabelAndAccessKey, HasBooleanValue, IsDisabled,
    IsInline, HasTip, IsReadonly, HasMarkup, HasTabIndex, IsRequired, HasConverter, IsFocus,
    HasValidatorMessage, HasRequiredMessage, HasConverterMessage, IsGridLayoutComponent {
    // IsImmediateComponent
}
