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

package org.apache.myfaces.tobago.apt.generate;

import org.apache.commons.lang3.StringUtils;

public class PropertyInfo {
  private String name;
  private String type;
  private String[] allowedValues;
  private String[] methodSignature;
  private String defaultValue;
  private String defaultCode;
  private boolean valueExpressionRequired;
  private boolean methodExpressionRequired;
  private boolean literalOnly;
  private boolean deprecated;
  private boolean bodyContent;
  private boolean tagAttribute;
  private String description;
  private boolean transientValue;
  private boolean generate;

  public PropertyInfo() {
  }

  public PropertyInfo(final String name) {
    this.name = name;
  }

  public boolean isBodyContent() {
    return bodyContent;
  }

  public void setBodyContent(final boolean bodyContent) {
    this.bodyContent = bodyContent;
  }

  public boolean isLiteralOnly() {
    return literalOnly;
  }

  public void setLiteralOnly(final boolean literalOnly) {
    this.literalOnly = literalOnly;
  }

  public String getTemplate() {
    if (valueExpressionRequired) {
      return "ValueExpression";
    }
    if (isMethodExpression()) {
      return getUpperCamelCaseName();
    }
    return getShortTypeProperty();
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getType() {
    return type.replace("$", ".");
  }

  public String getUnmodifiedType() {
    return type;
  }

  public String getInternalType() {
    return "boolean".equals(type) ? Boolean.class.getName() : getType();
  }

  public boolean isWidthOrHeight() {
    return "width".equals(name) || "height".equals(name);
  }

  public void setType(final String type) {
    this.type = type;
  }

  public boolean isMethodExpression() {
    return "javax.el.MethodExpression".equals(type);
  }

  public String getShortType() {
    final String shortType = type.substring(type.lastIndexOf('.') + 1, type.length());
    return shortType.replace("[]", "Array").replace("$", ".");
  }

  public String getShortTypeProperty() {
    final String shortType = getShortType();
    final int index = shortType.lastIndexOf('.');
    if (index != -1) {
      return shortType.substring(shortType.lastIndexOf('.') + 1, shortType.length());
    }
    return shortType;
  }

  public String getUpperCamelCaseName() {
    return StringUtils.capitalize(name);
  }

  public String getPropertyName() {
    if (name.equals("for")) {
      return "forComponent";
    }
    return name;
  }

  public String[] getAllowedValues() {
    return allowedValues;
  }

  public void setAllowedValues(final String[] allowdValues) {
    this.allowedValues = allowdValues;
  }

  public String[] getMethodSignature() {
    return methodSignature;
  }

  public void setMethodSignature(final String[] methodSignature) {
    this.methodSignature = methodSignature;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setDeprecated(final boolean deprecated) {
    this.deprecated = deprecated;
  }

  public boolean isDeprecated() {
    return deprecated;
  }

  public String getDefaultCode() {
    if (defaultCode == null && defaultValue != null) {
      if (String.class.getName().equals(type)) {
        return "\"" + defaultValue + "\"";
      } else if (Character.class.getName().equals(type)) {
        return "'" + defaultValue + "'";
      } else {
        return defaultValue;
      }
    }
    return defaultCode;
  }

  public void setDefaultCode(final String defaultCode) {
    this.defaultCode = defaultCode;
  }

  public boolean isValueExpressionRequired() {
    return valueExpressionRequired;
  }

  public void setValueExpressionRequired(final boolean valueExpressionRequired) {
    this.valueExpressionRequired = valueExpressionRequired;
  }

  public boolean isMethodExpressionRequired() {
    return methodExpressionRequired;
  }

  public void setMethodExpressionRequired(final boolean methodExpressionRequired) {
    this.methodExpressionRequired = methodExpressionRequired;
  }

  public boolean isTagAttribute() {
    return tagAttribute;
  }

  public void setTagAttribute(final boolean tagAttribute) {
    this.tagAttribute = tagAttribute;
  }

  public PropertyInfo fill(final PropertyInfo info) {
    info.setName(name);
    info.setType(type);
    info.setAllowedValues(allowedValues);
    info.setDefaultValue(defaultValue);
    info.setDeprecated(deprecated);
    info.setMethodSignature(methodSignature);
    info.setDefaultCode(defaultCode);
    info.setValueExpressionRequired(valueExpressionRequired);
    info.setLiteralOnly(literalOnly);
    info.setMethodExpressionRequired(methodExpressionRequired);
    info.setTagAttribute(tagAttribute);
    info.setDescription(description);
    info.setTransient(transientValue);
    return info;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PropertyInfo)) {
      return false;
    }

    final PropertyInfo that = (PropertyInfo) o;

    return name.equals(that.name);

  }

  public int hashCode() {
    return name.hashCode();
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public boolean isTransient() {
    return transientValue;
  }

  public void setTransient(final boolean transientValueParameter) {
    this.transientValue = transientValueParameter;
  }

  public boolean isGenerate() {
    return generate;
  }

  public void setGenerate(final boolean generate) {
    this.generate = generate;
  }
}
