package org.apache.myfaces.tobago.apt.generate;

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

import java.util.Locale;

public class PropertyInfo {
  private String name;
  private String type;
  private String[] allowdValues;
  private String[] methodSignature;
  private String defaultValue;
  private boolean deprecated;

  public PropertyInfo() {
  }

  public PropertyInfo(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isMethodBinding() {
    return "javax.faces.el.MethodBinding".equals(type);
  }

  public String getShortType() {
    String shortType = type.substring(type.lastIndexOf('.')+1, type.length());
    return shortType.replace("[]", "Array");
  }

  public String getUpperCamelCaseName() {
    return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
  }

  public String getPropertyName() {
    if (name.equals("for")) {
      return "forComponent";
    }
    return name;
  }

  public String[] getAllowdValues() {
    return allowdValues;
  }

  public void setAllowdValues(String[] allowdValues) {
    this.allowdValues = allowdValues;
  }

  public String[] getMethodSignature() {
    return methodSignature;
  }

  public void setMethodSignature(String[] methodSignature) {
    this.methodSignature = methodSignature;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public PropertyInfo fill(PropertyInfo info) {
    info.setName(name);
    info.setType(type);
    info.setAllowdValues(allowdValues);
    info.setDefaultValue(defaultValue);
    info.setDeprecated(deprecated);
    info.setMethodSignature(methodSignature);
    return info;
  }
}
