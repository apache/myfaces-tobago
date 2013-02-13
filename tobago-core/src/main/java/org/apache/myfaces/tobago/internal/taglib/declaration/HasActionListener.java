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

public interface HasActionListener {
  /**
   * MethodBinding representing an action listener method that will be
   * notified when this component is activated by the user.
   * The expression must evaluate to a public method that takes an ActionEvent
   * parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ActionEvent")
  void setActionListener(String actionListener);
}
