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

import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessageForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSize;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequiredForSelect;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UISelectMany;

@Tag(name = "selectManyShuttle")
@BodyContentDescription(anyTagOf = "(<f:selectItems>|<f:selectItem>|<tc:selectItem>)+ <f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectManyShuttle",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISelectManyShuttle",
    uiComponentFacesClass = "javax.faces.component.UISelectMany",
    componentFamily = UISelectMany.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_MANY_SHUTTLE,
    allowedChildComponenents = {
        "javax.faces.SelectItem",
        "javax.faces.SelectItems"},
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CHANGE,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.CLICK),
        @Behavior(
            name = ClientBehaviors.DBLCLICK)
    })
public interface SelectManyShuttleTagDeclaration extends
    IsDisabled, HasId, HasTip, IsRendered, IsRequiredForSelect, HasBinding, IsReadonly, HasConverter,
    HasLabel, HasValidator, HasValueChangeListener, HasLabelLayout,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessageForSelect, HasTabIndex, IsFocus, IsVisual, HasSize {

  /**
   * The value of the multi select.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List"})
  void setValue(String value);

  /**
   * A localized user presentable label for the left select box.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setUnselectedLabel(String label);


  /**
   * A localized user presentable label for the right select box.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setSelectedLabel(String label);

}
