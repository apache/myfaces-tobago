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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasPlaceholder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.InputTagDeclaration;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.faces.component.UIInput;

/**
 * Renders a date input field.
 *
 * For a time input field set you'll need to set the &lt;f:convertDateTime type="time"> inside the &lt;tc:date>.
 */
@Tag(name = "date")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIDate",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIDate",
    uiComponentFacesClass = "javax.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.DATE,
    allowedChildComponenents = "NONE",
    facets = {
    @Facet(name = Facets.CHANGE,
        description =
            "This facet can contain a UICommand that is invoked in a case of a change event from the component")
        })
public interface DateTagDeclaration
    extends InputTagDeclaration, HasIdBindingAndRendered, IsReadonly,
    IsDisabled, HasConverter, HasLabel, HasLabelLayout,
    HasTip, IsRequired, HasMarkup, HasCurrentMarkup, HasPlaceholder {

}
