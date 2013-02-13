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

package org.apache.myfaces.tobago.apt.annotation;

public enum DynamicExpression {

  VALUE_EXPRESSION(false, true, false),
  VALUE_EXPRESSION_REQUIRED(true, true, false),
  METHOD_EXPRESSION(false, false, true),
  METHOD_EXPRESSION_REQUIRED(true, false, true),
  PROHIBITED(false, false, false),
  /** @deprecated since 1.6.0 */
  @Deprecated
  VALUE_BINDING(false, true, false),
  /** @deprecated since 1.6.0 */
  @Deprecated
  VALUE_BINDING_REQUIRED(true, true, false),
  /** @deprecated since 1.6.0 */
  @Deprecated
  METHOD_BINDING(false, false, true),
  /** @deprecated since 1.6.0 */
  @Deprecated
  METHOD_BINDING_REQUIRED(true, false, true);

  private boolean required;
  private boolean valueExpression;
  private boolean methodExpression;

  DynamicExpression(boolean required, boolean valueExpression, boolean methodExpression) {
    this.required = required;
    this.valueExpression = valueExpression;
    this.methodExpression = methodExpression;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isValueExpression() {
    return valueExpression;
  }

  public boolean isMethodExpression() {
    return methodExpression;
  }

  public String toMetaDataString() {
    switch (this) {
      case VALUE_EXPRESSION:
      case VALUE_BINDING:
        return "ALLOWED";
      case METHOD_EXPRESSION:
      case METHOD_BINDING:
        return "ALLOWED";
      case VALUE_EXPRESSION_REQUIRED:
      case VALUE_BINDING_REQUIRED:
        return "REQUIRED";
      case METHOD_EXPRESSION_REQUIRED:
      case METHOD_BINDING_REQUIRED:
        return "REQUIRED";
      case PROHIBITED:
        return "PROHIBITED";
      default:
        throw new IllegalStateException("Unexpected DynamicExpression " + name());      
    }
  }

  public String toString() {
    switch (this) {
      case VALUE_EXPRESSION:
      case VALUE_BINDING:
        return "VB";
      case VALUE_EXPRESSION_REQUIRED:
      case VALUE_BINDING_REQUIRED:
        return "VB";
      case METHOD_EXPRESSION:
      case METHOD_BINDING:
        return "MB";
      case METHOD_EXPRESSION_REQUIRED:
      case METHOD_BINDING_REQUIRED:
        return "MB";
      case PROHIBITED:
        return "NONE";
      default:
        throw new IllegalStateException("Unexpected DynamicExpression " + name());
    }
  }
}
