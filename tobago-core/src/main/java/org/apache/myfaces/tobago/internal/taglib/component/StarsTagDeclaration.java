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
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasHelp;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIInput;

/**
 * Renders a star rating component.
 */
@Tag(name = "stars")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIStars",
    uiComponentFacesClass = "jakarta.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.STARS,
    allowedChildComponenents = "NONE",
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CHANGE,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.INPUT),
        @Behavior(
            name = ClientBehaviors.CLICK),
        @Behavior(
            name = ClientBehaviors.DBLCLICK)
    }
)

public interface StarsTagDeclaration extends HasIdBindingAndRendered, HasConverter, HasConverterMessage, IsDisabled,
    IsFocus, HasTabIndex, HasLabel, HasLabelLayout, IsReadonly, IsRequired, HasRequiredMessage, HasTip,
    HasValidator, HasValidatorMessage, HasValue, HasValueChangeListener, IsVisual, HasAccessKey, HasHelp,
    HasAutoSpacing {

  /**
   * The current value of this component. May be a java.lang.Number or a javax.swing.BoundedRangeModel
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"javax.swing.BoundedRangeModel", "java.lang.Integer"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * The maximum value must be &gt; '0'. Default is '5'.
   * To split stars, set a higher maximum value.
   * E.g. set '10' for half stars, set '20' for quarter stars.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.lang.Integer"})
  void setMax(String max);

  /**
   * The placeholder value is displayed if no star is set by the user.
   * Value must be between '0' and '5'.
   * The placeholder can show values which are not selectable by the user.
   * E.g. if only full stars are available for the user, the placeholder can show a 4.6 star rating.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"java.lang.Double"}
  )
  void setPlaceholder(String placeholder);
}
