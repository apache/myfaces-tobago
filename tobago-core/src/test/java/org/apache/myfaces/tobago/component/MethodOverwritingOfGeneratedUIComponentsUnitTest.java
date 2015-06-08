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

import junit.framework.Assert;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.faces.component.UIComponent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class MethodOverwritingOfGeneratedUIComponentsUnitTest {

  private static final List<String> IGNORED_METHODS = Arrays.asList("getFamily", "saveState", "restoreState");
  private static final List<String> IGNORED_COMPONENTS = Arrays.asList(
      UIExtensionPanel.class.getSimpleName(),
      UIMenuSelectOne.class.getSimpleName());
  private static final MethodOfComponentList IGNORED_METHODS_PER_COMPONENT = new MethodOfComponentList();

  static {
    IGNORED_METHODS_PER_COMPONENT.add("isResizable", UIColumn.class);
    IGNORED_METHODS_PER_COMPONENT.add("isResizable", UIColumnNode.class);
    IGNORED_METHODS_PER_COMPONENT.add("isShowRootJunction", UISheet.class);
    IGNORED_METHODS_PER_COMPONENT.add("isShowRootJunction", UITree.class);
  }

  private List<Class<? extends UIComponent>> uiComponents;

  @Before
  public void setup() throws IOException, ClassNotFoundException {
    uiComponents = findUIComponents();
  }

  @Test
  public void test() {

    for (final Class<? extends UIComponent> uiComponent : uiComponents) {
      final Method[] methods = uiComponent.getMethods();
      for (final Method method : methods) {

        if (!method.getDeclaringClass().equals(uiComponent)) {
          // check only the method, that have an implementation in the generated class
          continue;
        }

        if (IGNORED_METHODS.contains(method.getName())) {
          continue;
        }

        if (IGNORED_METHODS_PER_COMPONENT.contains(method.getName(), uiComponent)) {
          continue;
        }

        for (Class<?> superClass = uiComponent.getSuperclass();
             superClass != null;
             superClass = superClass.getSuperclass()) {
          final Method superMethod;
          try {
            superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
          } catch (final NoSuchMethodException e) {
            // only looking for super methods
            continue;
          }

          if (!Modifier.isAbstract(superMethod.getModifiers())) {
            Assert.fail(method.getName() + " of " + uiComponent.getName()
                + " hides a concrete super method (but is not in the ignore list).");
            break;
          }
        }
      }
    }
  }

  /**
   * Find all classes in a package
   */
  private List<Class<? extends UIComponent>> findUIComponents() throws ClassNotFoundException, IOException {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final String packageName = this.getClass().getPackage().getName();
    final String path = packageName.replace('.', '/');
    final Enumeration<URL> resources = classLoader.getResources(path);
    final List<File> directories = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      final URL resource = resources.nextElement();
      directories.add(new File(resource.getFile()));
    }
    final ArrayList<Class<? extends UIComponent>> result = new ArrayList<Class<? extends UIComponent>>();
    for (final File directory : directories) {
      final File[] files = directory.listFiles();
      if (files != null) {
        for (final File file : files) {
          final String name = file.getName();
          if (!StringUtils.endsWith(name, ".class")) {
            continue;
          }
          final String nameWithoutSuffix = name.substring(0, name.length() - 6);
          if (IGNORED_COMPONENTS.contains(nameWithoutSuffix)) {
            continue;
          }
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

  private static class MethodOfComponentList {

    private List<String> list = new ArrayList<String>();

    public void add(final String method, final Class<? extends UIComponent> component) {
      list.add(concatenate(method, component));
    }

    public boolean contains(final String method, final Class<? extends UIComponent> component) {
      return list.contains(concatenate(method, component));
    }

    private String concatenate(final String method, final Class<? extends UIComponent> component) {
      return method + '@' + component.getName();
    }

  }
}
