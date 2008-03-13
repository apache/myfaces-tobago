package org.apache.myfaces.tobago.apt.annotation;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created: Apr 27, 2005 5:08:45 PM
 * User: bommel
 * $Id$
 */
public enum DynamicExpression {

  VALUE_BINDING(false, true), VALUE_BINDING_REQUIRED(true, true),
  METHOD_BINDING(false, false), METHOD_BINDING_REQUIRED(true, false),
  PROHIBITED(false, false);

  private boolean required;
  private boolean valueExpression;

  DynamicExpression(boolean required, boolean valueExpression) {
    this.required = required;
    this.valueExpression = valueExpression;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isValueExpression() {
    return valueExpression;
  }

  public String toMetaDataString() {
    switch (this) {
      case VALUE_BINDING:
        return "ALLOWED";
      case METHOD_BINDING:
        return "ALLOWED";
      case VALUE_BINDING_REQUIRED:
        return "REQUIRED";
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
      case VALUE_BINDING:
        return "VB";
      case VALUE_BINDING_REQUIRED:
        return "VB";
      case METHOD_BINDING:
        return "MB";
      case METHOD_BINDING_REQUIRED:
        return "MB";
      case PROHIBITED:
        return "NONE";
      default:
        throw new IllegalStateException("Unexpected DynamicExpression " + name());
    }
  }
}
