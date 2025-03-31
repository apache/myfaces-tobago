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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;

import javax.faces.component.UIInput;

/**
 * Filter the results for tc:selectOneList and tc:selectManyList.
 * When used, the filter attribute of tc:selectOneList/tc:selectManyList is ignored.
 *
 * @since 6.8.0
 */
@Tag(name = "filter")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIFilter",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.FILTER,
    allowedChildComponents = "org.apache.myfaces.tobago.SelectItems")
public interface FilterTagDeclaration extends HasIdBindingAndRendered {

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

  /**
   * The query is the string typed by the user. It is intended to filter the result list.
   */
  @TagAttribute
  @UIComponentTagAttribute(generate = false, isTransient = true)
  void setQuery(String query);
}
