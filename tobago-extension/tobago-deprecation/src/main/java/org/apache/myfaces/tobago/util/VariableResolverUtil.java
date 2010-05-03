package org.apache.myfaces.tobago.util;

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

import org.apache.myfaces.tobago.context.ClientProperties;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

/**
 * @deprecated Please use VariableResolverUtils
 */
@Deprecated
public final class VariableResolverUtil {

  private VariableResolverUtil() {
    // utils class
  }

  /**
   * Returns the requested object configured in the faces-config or from library.
   */
  public static Object resolveVariable(FacesContext context, String variable) {
    Application application = context.getApplication();
    VariableResolver variableResolver = application.getVariableResolver();
    return variableResolver.resolveVariable(context, variable);
  }

  /**
   * Returns the ClientProperties object configured in the faces-config.
   */
  public static ClientProperties resolveClientProperties(FacesContext facesContext) {
    return (ClientProperties) resolveVariable(facesContext, ClientProperties.MANAGED_BEAN_NAME);
  }
  
  /**
   * Clears the value of the variable.
   * Useful for cleaning up e.g. a session or application variable
   * to save memory without the knowledge of the scope.
   * Also useful to enforce a new creation of a managed-bean.
   */
  public static void clearVariable(FacesContext context, String variable) {
    Application application = context.getApplication();
    ValueBinding valueBinding = application.createValueBinding("#{" + variable + "}");
    valueBinding.setValue(context, null);
  }
}
