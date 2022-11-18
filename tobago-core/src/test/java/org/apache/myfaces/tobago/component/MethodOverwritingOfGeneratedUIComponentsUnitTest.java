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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.component.UIComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodOverwritingOfGeneratedUIComponentsUnitTest extends AbstractGeneratedUIComponentsUnitTest {

  private static final List<String> IGNORED_METHODS
      = Arrays.asList("getFamily", "saveState", "restoreState", "getEventNames", "getDefaultEventName");
  private static final MethodOfComponentList IGNORED_METHODS_PER_COMPONENT = new MethodOfComponentList();

  static {
    IGNORED_METHODS_PER_COMPONENT.add("isResizable", UIColumn.class);
    IGNORED_METHODS_PER_COMPONENT.add("isResizable", UIColumnNode.class);
    IGNORED_METHODS_PER_COMPONENT.add("isShowRootJunction", UISheet.class);
    IGNORED_METHODS_PER_COMPONENT.add("isShowRootJunction", UITree.class);
    IGNORED_METHODS_PER_COMPONENT.add("isPlain", UIOut.class);
    IGNORED_METHODS_PER_COMPONENT.add("isPlain", UIForm.class);
  }

  @Test
  public void test() {

    for (final Class<? extends UIComponent> uiComponent : getUiComponents()) {
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
            Assertions.fail(method.getName() + " of " + uiComponent.getName()
                + " hides a concrete super method (but is not in the ignore list).");
            break;
          }
        }
      }
    }
  }

  private static class MethodOfComponentList {

    private List<String> list = new ArrayList<>();

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
