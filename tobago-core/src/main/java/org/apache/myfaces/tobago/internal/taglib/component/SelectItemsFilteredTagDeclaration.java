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

import jakarta.faces.component.UISelectItems;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasItemLabel;

/**
 * The selectItemsFiltered component is based on UISelectItems but with a query attribute for server-side filtering.
 * When used in tc:selectOneList or tc:selectManyList, the
 * {@link SelectOneListTagDeclaration#setFilter}/{@link SelectManyListTagDeclaration#setFilter} attribute is ignored.
 *
 * @since 6.8.0
 */
@Tag(name = "selectItemsFiltered")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectItemsFiltered",
    uiComponentFacesClass = "jakarta.faces.component.UISelectItems",
    componentFamily = UISelectItems.COMPONENT_FAMILY,
    rendererType = RendererTypes.SELECT_ITEMS_FILTERED,
    allowedChildComponents = "NONE")
public interface SelectItemsFilteredTagDeclaration extends HasId, HasBinding, HasItemLabel {

  /**
   * Value binding expression pointing at a List or array of SelectItem instances containing
   * the information for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {"jakarta.faces.model.SelectItem[]", "java.lang.Object[]", "java.util.Collection"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * Name of a variable under which the iterated data will be exposed.
   * It may be referred to in EL of other attributes.
   */
  @TagAttribute
  @UIComponentTagAttribute(expression = DynamicExpression.PROHIBITED)
  void setVar(String var);

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
  @UIComponentTagAttribute(type = {"java.lang.Object"})
  void setItemValue(String itemValue);

  /**
   * The query is the string typed by the user. It is intended to filter the result list.
   */
  @TagAttribute
  @UIComponentTagAttribute(generate = false, isTransient = true)
  void setQuery(String query);

  /**
   * Time in milliseconds before the list will be requested (by AJAX).
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "200")
  void setDelay(String delay);

  /**
   * Minimum number of characters to type before the list will be requested.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setMinimumCharacters(String minimumCharacters);
}
