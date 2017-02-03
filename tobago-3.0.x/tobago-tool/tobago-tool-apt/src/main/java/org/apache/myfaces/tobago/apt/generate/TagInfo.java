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


public class TagInfo extends RendererInfo {
  private List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
  private String componentClassName;
  private String componentType;
  private String componentFamily;
  private PropertyInfo bodyContent;
  private boolean checkBodyContent;

  public TagInfo(final String sourceClass, final String qualifiedName, final String rendererType) {
    super(sourceClass, qualifiedName, rendererType);
  }

  public TagInfo(final String sourceClass, final String qualifiedName) {
    super(sourceClass, qualifiedName);
  }

  public PropertyInfo getBodyContent() {
    if (!checkBodyContent) {
      checkBodyContent = true;
      for (final PropertyInfo info : properties) {
        if (info.isBodyContent()) {
          bodyContent = info;
          break;
        }
      }
    }
    return bodyContent;
  }

  public void setBodyContent(final PropertyInfo bodyContent) {
    this.bodyContent = bodyContent;
  }

  public List<PropertyInfo> getProperties() {
    return properties;
  }

  public int getPropertiesSize() {
    return properties.size();
  }

  public int getPropertiesSizePlusOne() {
    return properties.size() + 1;
  }

  public void setComponentClassName(final String componentClass) {
    addImport(componentClass);
    this.componentClassName = ClassUtils.getSimpleName(componentClass);
  }

  public String getComponentClassName() {
    return componentClassName;
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(final String componentType) {
    this.componentType = componentType;
  }

  public String getComponentFamily() {
    return componentFamily;
  }

  public void setComponentFamily(final String componentFamily) {
    this.componentFamily = componentFamily;
  }
}
