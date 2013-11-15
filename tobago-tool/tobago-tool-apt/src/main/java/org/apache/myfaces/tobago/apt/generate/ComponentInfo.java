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

import org.apache.myfaces.tobago.apt.AnnotationUtils;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;

import javax.lang.model.element.TypeElement;
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

  public ComponentInfo(final TypeElement declaration, final UIComponentTag componentTag) {
    super(declaration.getQualifiedName().toString(), componentTag.uiComponent(), componentTag.rendererType());

    setComponentType(AnnotationUtils.componentType(componentTag));
    setComponentFamily(componentTag.componentFamily());
    setComponentClassName(componentTag.uiComponent());
  }

  @Deprecated
  public ComponentInfo(final String sourceClass, final String qualifiedName, final String rendererType) {
    super(sourceClass, qualifiedName, rendererType);
  }

  public boolean isInvokeOnComponent() {
    return invokeOnComponent;
  }

  public void setInvokeOnComponent(final boolean invokeOnComponent) {
    this.invokeOnComponent = invokeOnComponent;
  }

  public void addPropertyInfo(final ComponentPropertyInfo propertyInfo) {
    getProperties().add(propertyInfo);
    propertyInfo.setIndex(index);
    index++;
    if (!propertyInfo.isTransient()) {
      nonTransientProperties.add(propertyInfo);
      propertyInfo.setNonTransientIndex(nonTransientIndex);
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

  public void setMessages(final boolean messages) {
    this.messages = messages;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setDeprecated(final boolean deprecated) {
    this.deprecated = deprecated;
  }

  public boolean isDeprecated() {
    return deprecated;
  }
}
