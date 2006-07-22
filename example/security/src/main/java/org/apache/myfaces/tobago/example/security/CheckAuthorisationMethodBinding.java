package org.apache.myfaces.tobago.example.security;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 19.07.2006
 * Time: 16:11:43
 * To change this template use File | Settings | File Templates.
 */
public class CheckAuthorisationMethodBinding extends MethodBinding implements StateHolder {
  private static final Log LOG = LogFactory.getLog(CheckAuthorisationMethodBinding.class);
  private MethodBinding methodBinding;

  public CheckAuthorisationMethodBinding() {
  }

  public CheckAuthorisationMethodBinding(MethodBinding methodBinding) {
    this.methodBinding = methodBinding;
  }

  public String getExpressionString() {
    return methodBinding.getExpressionString();
  }

  public Class getType(FacesContext facesContext) throws MethodNotFoundException {
    return methodBinding.getType(facesContext);
  }

  public Object invoke(FacesContext facesContext, Object[] objects)
      throws EvaluationException, MethodNotFoundException {
    // TODO check Authorisation and don't invoke method binding if user is not authorised
    // TODO add FacesMessage if user is not authorised
    if (LOG.isDebugEnabled()) {
      LOG.debug("MethodBinding invoke " + getExpressionString());
    }
    String expression = getExpressionString();

    // TODO find a better way

    if (expression.startsWith("#{") && expression.endsWith("}")) {
      expression = expression.substring(2, expression.length()-1);
      int index = expression.lastIndexOf('.');
      if (index != -1) {
        String methodExpression = expression.substring(index+1, expression.length());
        String beanExpression = expression.substring(0, index);
        // TODO find a better way
        Object bean = facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, beanExpression);
        if (bean != null) {
          // TODO cache the security constraints for the expression
          logRolesAllowed(bean.getClass().getAnnotation(RolesAllowed.class));
          logDenyAll(bean.getClass().getAnnotation(DenyAll.class));
          logPermitAll(bean.getClass().getAnnotation(PermitAll.class));
          try {
            Method method = bean.getClass().getMethod(methodExpression);
            logRolesAllowed(method.getAnnotation(RolesAllowed.class));
            logDenyAll(method.getAnnotation(DenyAll.class));
            logPermitAll(method.getAnnotation(PermitAll.class));

          } catch (NoSuchMethodException e) {
            LOG.error("No Method " + methodExpression + " in class " + bean.getClass(), e);
          }
        }

      }
    }
    return methodBinding.invoke(facesContext, objects);
  }
  private void logRolesAllowed(RolesAllowed rolesAllowed) {
    if (rolesAllowed != null) {
      LOG.debug("RolesAllowed " + Arrays.asList(rolesAllowed.value()));
    }
  }

  private void logPermitAll(PermitAll permitAll) {
    if (permitAll != null) {
      LOG.debug("PermitAll");
    }
  }

  private void logDenyAll(DenyAll denyAll) {
    if (denyAll != null) {
      LOG.debug("DenyAll");
    }
  }

  public Object saveState(FacesContext facesContext) {
    Object[] saveState = new Object[1];
    saveState[0] = UIComponentBase.saveAttachedState(facesContext, methodBinding);
    return saveState;
  }

  public void restoreState(FacesContext facesContext, Object savedState) {
    Object[] values = (Object[]) savedState;
    methodBinding = (MethodBinding) UIComponentBase.restoreAttachedState(facesContext, values[0]);
  }

  public boolean isTransient() {
    if (methodBinding instanceof StateHolder) {
      return ((StateHolder)methodBinding).isTransient();
    }
    return false;
  }

  public void setTransient(boolean bool) {
    if (methodBinding instanceof StateHolder) {
      ((StateHolder)methodBinding).setTransient(bool);
    }
  }
}
