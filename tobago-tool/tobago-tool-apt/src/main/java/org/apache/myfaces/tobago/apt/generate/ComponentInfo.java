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

import java.util.ArrayList;
import java.util.List;

public class ComponentInfo extends TagInfo {
  private List<PropertyInfo> nonTransientProperties = new ArrayList<PropertyInfo>();
  private List<PropertyInfo> transientProperties = new ArrayList<PropertyInfo>();
  private boolean invokeOnComponent;
  private boolean messages;
  private String description;
  private boolean deprecated;
  private int index = 0;
  private int nonTransientIndex = 0;

  public ComponentInfo(String sourceClass, String qualifiedName, String rendererType) {
    super(sourceClass, qualifiedName, rendererType);
  }

  public boolean isInvokeOnComponent() {
    return invokeOnComponent;
  }

  public void setInvokeOnComponent(boolean invokeOnComponent) {
    this.invokeOnComponent = invokeOnComponent;
  }

  public void addPropertyInfo(ComponentPropertyInfo propertyInfo, ComponentPropertyInfo elAlternative) {
    getProperties().add(propertyInfo);
    propertyInfo.setIndex(index);
    if (elAlternative != null) {
      getProperties().add(elAlternative);
      elAlternative.setIndex(index);
    }
    index++;
    if (!propertyInfo.isTransient()) {
      nonTransientProperties.add(propertyInfo);
      propertyInfo.setNonTransientIndex(nonTransientIndex);
      if (elAlternative != null) {
        nonTransientProperties.add(elAlternative);
        elAlternative.setNonTransientIndex(nonTransientIndex);
      }
      nonTransientIndex++;
    } else {
      transientProperties.add(propertyInfo);
    }
  }

  public List<PropertyInfo> getNonTransientProperties() {
    return nonTransientProperties;
  }

  public List<PropertyInfo> getTransientProperties() {
    return transientProperties;
  }

  public int getPropertiesSize() {
    return index;
  }

  public int getPropertiesSizePlusOne() {
    return index + 1;
  }

  public int getNonTransientPropertiesSize() {
    return nonTransientIndex;
  }

  public int getNonTransientPropertiesSizePlusOne() {
    return nonTransientIndex + 1;
  }

  public boolean isMessages() {
    return messages;
  }

  public void setMessages(boolean messages) {
    this.messages = messages;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setDeprecated(boolean deprecated) {
    this.deprecated = deprecated;
  }

  public boolean isDeprecated() {
    return deprecated;
  }
}
