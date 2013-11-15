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

package org.apache.myfaces.tobago.util;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * XXX remove when JUnit implemented this feature
 * This class will be obsolete if this feature request for JUnit is resolved:
 * http://github.com/KentBeck/junit/issues/#issue/44
 */
public class Parameterized extends Suite {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public static @interface Parameters {
  }

  public Parameterized(final Class<?> clazz) throws Exception {
    super(clazz, new ArrayList<Runner>());

    final FrameworkMethod method = findMethod(getTestClass());
    List<Object[]> parametersList = null;
    try {
      parametersList = (List<Object[]>) method.invokeExplosively(null);
    } catch (final Throwable throwable) {
      throw new Exception(throwable);
    }
    for (final Object[] aParametersList : parametersList) {
      getChildren().add(new ClassRunnerForParameters(getTestClass().getJavaClass(), aParametersList));
    }
  }

  private FrameworkMethod findMethod(final TestClass clazz) throws Exception {
    final List<FrameworkMethod> methods = clazz.getAnnotatedMethods(Parameters.class);
    for (final FrameworkMethod method : methods) {
      final int modifiers = method.getMethod().getModifiers();
      if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
        return method;
      }
    }
    throw new Exception(
        "Need a public static method with @Parameters annotation in class " + getName());
  }

  public static class ClassRunnerForParameters extends BlockJUnit4ClassRunner {

    private Object[] parameters;

    ClassRunnerForParameters(final Class<?> clazz, final Object[] parameters) throws InitializationError {
      super(clazz);
      this.parameters = parameters;
    }

    @Override
    protected Object createTest() throws Exception {
      return getTestClass().getOnlyConstructor().newInstance(parameters);
    }

    @Override
    protected String getName() {
      return "[" + parameters[0].toString() + "]";
    }

    @Override
    protected String testName(final FrameworkMethod method) {
      return method.getName() + getName();
    }

    @Override
    protected Statement classBlock(final RunNotifier notifier) {
      return childrenInvoker(notifier);
    }

    @Override
    protected void validateZeroArgConstructor(final List<Throwable> errors) {
      // In this case there should be a constructor with arguments.
    }
  }

}
