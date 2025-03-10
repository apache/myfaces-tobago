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

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasItemImage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasItemLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

/**
 * Add a child UISelectItem component to the UIComponent
 * associated with the closed parent UIComponent custom
 * action.
 */
@Tag(name = "selectItem")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectItem",
    uiComponentFacesClass = "jakarta.faces.component.UISelectItem",
    allowedChildComponents = "NONE")
public interface SelectItemTagDeclaration extends HasBinding, HasId, IsVisual, HasItemLabel, HasItemImage, HasTip {

  /**
   * Description of an item, might be rendered as a tool tip.
   *
   * @deprecated Please use itemTip.
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.String"})
  void setItemDescription(String itemDescription);

  /**
   * Flag indicating whether the option created
   * by this component is disabled.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"boolean"}, defaultValue = "false")
  void setItemDisabled(String itemDisabled);

  /**
   * Value to be returned to the server if this option is selected by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setItemValue(String itemValue);

  /**
   * Value binding expression pointing at a SelectItem instance containing
   * the information for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "jakarta.faces.model.SelectItem",
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * Flag indicating whether the option created
   * by this component is a noSelectionOption.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"boolean"}, defaultValue = "false")
  void setNoSelectionOption(String itemDisabled);

}
