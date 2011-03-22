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
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderRange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsInline;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRendered;

/**
 * Render a set of radio buttons.
 */
@Tag(name = "selectOneRadio")
@BodyContentDescription(anyTagOf = "(<f:selectItems>|<f:selectItem>|<tc:selectItem>)+ <f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectOneRadio",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio",
    rendererType = RendererTypes.SELECT_ONE_RADIO,
    allowedChildComponenents = {
        "javax.faces.SelectItem",
        "javax.faces.SelectItems"
        },
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
public interface SelectOneRadioTagDeclaration extends SelectOneTagDeclaration, IsDisabled, IsReadonly, HasId,
    HasTip, IsInline, HasRenderRange, IsRendered, HasBinding, HasConverter {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was chosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setRequired(String required);
}
