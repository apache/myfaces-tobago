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

package org.apache.myfaces.tobago.internal.taglib.declaration;

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * @deprecated since 2.0.0, please use tc:suggest
 */
@Deprecated
public interface HasSuggestMethod {

  /**
   * MethodBinding which generates a list of suggested input values based on
   * the currently entered text, which could be retrieved via getSubmittedValue() on the UIIn.
   * The expression has to evaluate to a public method which has a javax.faces.component.UIInput parameter
   * and returns a List&lt;String>(deprecated), a List&lt;org.apache.myfaces.tobago.model.AutoSuggestItem>
   * or a org.apache.myfaces.tobago.model.AutoSuggestItems.
   *
   * @since 1.5.9
   * @deprecated since 2.0.0, please use tc:suggest
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.component.UIInput",
      methodReturnType = "java.lang.Object")
  void setSuggestMethod(String suggestMethod);

  /**
   * Minimum number of chars to type before the list will be requested.
   *
   * @since 1.5.9
   * @deprecated since 2.0.0, please use tc:suggest
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "1")
  void setSuggestMinChars(String suggestMinChars);

  /**
   * Time in milli seconds before the list will be requested.
   *
   * @since 1.5.9
   * @deprecated since 2.0.0, please use tc:suggest
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "300")
  void setSuggestDelay(String suggestDelay);
}
