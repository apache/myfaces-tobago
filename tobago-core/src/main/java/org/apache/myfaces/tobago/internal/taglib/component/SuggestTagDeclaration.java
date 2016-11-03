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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.model.SuggestFilter;

import javax.faces.component.UIInput;

/**
 * Renders a list of suggested texts for a given input field.
 *
 * Basic features:
 * <ul>
 *   <li>provide a list directly while rendering (not AJAX needed) [todo]</li>
 *   <li>update by typing (AJAX)</li>
 *   <li>minimum number of typed characters (to avoid useless requests)</li>
 *   <li>update delay (useful for optimization)</li>
 *   <li>filter on client side (useful for optimization) [todo]</li>
 * </ul>
 *
 * @since 2.0.0
 */
@Tag(name = "suggest")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISuggest",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISuggest",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.SUGGEST,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.SelectItems",
        "org.apache.myfaces.tobago.SelectItem"
    }/* todo ,
    behaviors = {
        @Behavior(
            name = ClientBehaviors.SUGGEST,
            isDefault = true)
    }*/)
public interface SuggestTagDeclaration extends HasIdBindingAndRendered {

  /**
   * MethodBinding which generates a list of suggested input values based on
   * the currently entered text, which could be retrieved via getSubmittedValue() on the UIIn.
   * The expression has to evaluate to a public method which has a javax.faces.component.UIInput parameter
   * and returns a List&lt;String&gt;, a List&lt;org.apache.myfaces.tobago.model.AutoSuggestItem&gt;
   * or a org.apache.myfaces.tobago.model.AutoSuggestItems.
   *
   * @deprecated Please use an <code>&lt;tc:selectItems&gt;</code> tag or a list of <code>&lt;tc:selectItem&gt;</code>
   * tags (TODO: not implemented yet).
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.component.UIInput",
      methodReturnType = "java.lang.Object")
  void setSuggestMethod(String suggestMethod);

  /**
   * Minimum number of characters to type before the list will be requested.
   * If the value is 0, there will be send an initial list to the client.
   * So, if you set <pre>update="false"</pre> this value should be 0.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "1")
  void setMinimumCharacters(String minimumCharacters);

  /**
   * Time in milli seconds before the list will be requested (by AJAX).
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "200")
  void setDelay(String delay);

  /**
   * The maximum number of item to display in the drop down list.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "10")
  void setMaximumItems(String maximumItems);

  /**
   * The real size of the result list.
   * Typically the result list will be cropped (in the backend) to save memory.
   * This value can be set, to show the user there are more results for the given string.
   * If the value is -1, no hint will be displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "-1")
  void setTotalCount(String totalCount);

  /**
   * TODO: not implemented yet
   * <p>
   * Additional client side filtering of the result list.
   * This is useful when sending the full list initially to the client and
   * setting <code>update=false</code>.
   * </p>
   * <p>
   * Possible values are:
   * </p>
   * <dl>
   *   <dt>all</dt>
   *   <dd>no filtering</dd>
   *   <dt>prefix</dt>
   *   <dd>checks if the suggested string starts with the typed text</dd>
   *   <dt>contains</dt>
   *   <dd>checks if the typed text is inside of the suggested string</dd>
   * </dl>
   * <p>
   * The filter will only applied on the client side and
   * only if server updated (by AJAX) are turned off (<code>update=false</code>);
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.SuggestFilter",
      defaultValue = "SuggestFilter.ALL",
      allowedValues = {
          SuggestFilter.STRING_ALL,
          SuggestFilter.STRING_PREFIX,
          SuggestFilter.STRING_CONTAINS})
  void setFilter(String filter);

  /**
   * TODO: not implemented yet
   * <p>
   * Should the list be updated while typing (via AJAX). This is the default behaviour.
   * If you set this value to <code>false</code>, please set the <code>minimumCharacters="0"</code>.
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setUpdate(String update);

  /**
   * The query is the string typed by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute(generate = false, isTransient = true)
  void setQuery(String query);
}
