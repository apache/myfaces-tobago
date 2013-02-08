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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;

/**
 * Add a child UISelectItems component to the UIComponent
 * associated with the closed parent UIComponent custom
 * action.
 */
@Tag(name = "selectItems", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectItems",
    uiComponentBaseClass = "javax.faces.component.UISelectItems",
    uiComponentFacesClass = "javax.faces.component.UISelectItems",
    isComponentAlreadyDefined = false,
    allowedChildComponenents = "NONE")
public interface SelectItemsTagDeclaration extends HasId, HasBinding {

  /**
   * Value binding expression pointing at a List or array of SelectItem instances containing
   * the information for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "javax.faces.model.SelectItem[]",
      expression = DynamicExpression.VALUE_BINDING_REQUIRED)
  void setValue(String value);

}
