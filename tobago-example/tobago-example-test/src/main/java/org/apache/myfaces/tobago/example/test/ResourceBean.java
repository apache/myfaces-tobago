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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;

import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.List;

public class ResourceBean {

  private List<ResourceEntry> resources;
  private int fails;

  public ResourceBean() {
    if (resources == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ResourceManager resourceManager
          = ResourceManagerFactory.getResourceManager(facesContext);

      resources = Arrays.asList(
          new ResourceEntry(
              "data/sun-behind-mountains.jpg",
              ResourceType.IMAGE,
              "comes from JAR (tobago-example-data): META-INF/resources"),
          new ResourceEntry(
              "non-existing.jpg",
              ResourceType.IMAGE,
              "should fail"),
          new ResourceEntry(
              "image/date.png",
              ResourceType.IMAGE,
              "comes from JAR (tobago-theme-speyside)"),
          new ResourceEntry(
              "image/wait/cat.jpg",
              ResourceType.IMAGE,
              "comes from the WAR directly"),
          new ResourceEntry(
              "tobago",
              "configTheme",
              ResourceType.PROPERTY,
              "comes from the theme (tobago-theme-scarborough)"),
          new ResourceEntry(
              "tobago",
              "not-existing",
              ResourceType.PROPERTY,
              "should fail")
      );

      for (final ResourceEntry resource : resources) {
        if (!resource.check(facesContext, resourceManager)) {
          fails++;
        }
      }
    }
  }

  public List<ResourceEntry> getResultList() {
    return resources;
  }

  public int getFails() {
    return fails;
  }
}
