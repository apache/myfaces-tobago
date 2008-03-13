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

import java.util.List;
import java.util.ArrayList;


public class TagInfo extends RendererInfo {
  private List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
  private String componentClassName;
  private String componentType;
  private String componentFamily;
  private boolean ajaxComponent;
  private boolean namingContainer;
  private boolean markup;

  public TagInfo(String qualifiedName, String rendererName) {
    super(qualifiedName, rendererName);
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

  public void setComponentClassName(String componentClass) {
    addImport(componentClass);
    this.componentClassName = ClassUtils.getSimpleName(componentClass);
  }

  public String getComponentClassName() {
    return componentClassName;
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    if (componentType != null && componentType.length() > 0) {
      this.componentType = componentType;
    }
  }

  public String getComponentFamily() {
    return componentFamily;
  }

  public void setComponentFamily(String componentFamily) {
    if (componentFamily != null && componentFamily.length() > 0) {
      this.componentFamily = componentFamily;
    }
  }

  public boolean isAjaxComponent() {
    return ajaxComponent;
  }

  public void setAjaxComponent(boolean ajaxComponent) {
    this.ajaxComponent = ajaxComponent;
  }

  public boolean isNamingContainer() {
    return namingContainer;
  }

  public void setNamingContainer(boolean namingContainer) {
    this.namingContainer = namingContainer;
  }

  public boolean hasMarkup() {
    return markup;
  }

  public void setMarkup(boolean markup) {
    this.markup = markup;
  }
}
