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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;

import jakarta.faces.component.UIComponent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AbstractGeneratedUIComponentsUnitTest {

  private List<Class<? extends UIComponent>> uiComponents;

  protected List<Class<? extends UIComponent>> getUiComponents() {
    return uiComponents;
  }

  @BeforeEach
  public void setup() throws IOException, ClassNotFoundException {
    uiComponents = findUIComponents();
  }

  /**
   * Find all classes in a package
   */
  private List<Class<? extends UIComponent>> findUIComponents() throws ClassNotFoundException, IOException {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final String packageName = this.getClass().getPackage().getName();
    final String path = packageName.replace('.', '/');
    final Enumeration<URL> resources = classLoader.getResources(path);
    final List<File> directories = new ArrayList<>();
    while (resources.hasMoreElements()) {
      final URL resource = resources.nextElement();
      directories.add(new File(resource.getFile()));
    }
    final ArrayList<Class<? extends UIComponent>> result = new ArrayList<>();
    for (final File directory : directories) {
      final File[] files = directory.listFiles();
      if (files != null) {
        for (final File file : files) {
          final String name = file.getName();
          if (!StringUtils.endsWith(name, ".class")) {
            continue;
          }
          final String nameWithoutSuffix = name.substring(0, name.length() - 6);
          final String className = packageName + '.' + nameWithoutSuffix;
          final Class<? extends UIComponent> clazz = (Class<? extends UIComponent>) Class.forName(className);
          if (UIComponent.class.isAssignableFrom(clazz)) {
            result.add(clazz);
          }
        }
      }
    }
    return result;
  }

}
