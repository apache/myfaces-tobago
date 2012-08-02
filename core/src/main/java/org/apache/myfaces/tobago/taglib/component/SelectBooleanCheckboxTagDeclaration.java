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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWithAccessKey;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;

/*
 * Created: Aug 5, 2005 5:18:50 PM
 * User: bommel
 * $Id$
 */
/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@BodyContentDescription(anyTagOf = "<f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectBoolean",
    rendererType = "SelectBooleanCheckbox",
    facets = {
    @Facet(name = "click",
        description =
            "This facet can contain a UICommand that is invoked in case of a click event from the component"),
    @Facet(name = "change",
        description =
            "This facet can contain a UICommand that is invoked in case of a change event from the component")
        })

public interface SelectBooleanCheckboxTagDeclaration
    extends BeanTagDeclaration, HasValidator, HasOnchange, HasValueChangeListener, HasIdBindingAndRendered, HasLabel,
    HasLabelWithAccessKey, HasBooleanValue, IsDisabled, IsInline, HasTip, IsReadonly, HasMarkup, HasTabIndex {
}
