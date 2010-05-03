package org.apache.myfaces.tobago.security;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.application.FacesMessage;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;
import java.lang.annotation.Annotation;

/*
 * Date: 19.07.2006
 * Time: 16:11:43
 */
public class CheckAuthorisationMethodBinding extends MethodBinding implements StateHolder {
  private static final Logger LOG = LoggerFactory.getLogger(CheckAuthorisationMethodBinding.class);
  private static final Map<String, Annotation> AUTHORISATION_CACHE = new HashMap<String, Annotation>();

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
      throws EvaluationException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MethodBinding invoke " + getExpressionString());
    }
    // Methodbindings with a argument list would not be checked for authorisation
    if ((objects!=null && objects.length > 0) || isAuthorized(facesContext)) {
      return methodBinding.invoke(facesContext, objects);
    } else {
      // TODO better message
      facesContext.addMessage(null, new FacesMessage("Not authorised"));
      return null;
    }
  }

  public boolean isAuthorized(FacesContext facesContext) {

    Annotation securityAnnotation = getSecurityAnnotation(facesContext);
    if (securityAnnotation == null) {
      return true;
    }

    if (securityAnnotation instanceof DenyAll) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("DenyAll");
      }
      return false;
    }
    if (securityAnnotation instanceof RolesAllowed) {
      String [] roles = ((RolesAllowed) securityAnnotation).value();
      if (LOG.isDebugEnabled()) {
        LOG.debug("RolesAllowed " + Arrays.asList(((RolesAllowed) securityAnnotation).value()));
      }
      for (String role : roles) {
        boolean authorised = facesContext.getExternalContext().isUserInRole(role);
        if (authorised) {
          return true;
        }
      }
      return false;
    }
    if (securityAnnotation instanceof PermitAll) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("PermitAll");
      }
      return true;
    }
    return true;
  }
  private Annotation getSecurityAnnotation(FacesContext facesContext) {
    String expression = getExpressionString();
    if (AUTHORISATION_CACHE.containsKey(expression)) {
      return AUTHORISATION_CACHE.get(expression);
    } else {
      Annotation securityAnnotation = null;
      if (expression.startsWith("#{") && expression.endsWith("}")) {
        expression = expression.substring(2, expression.length()-1);
        int index = expression.lastIndexOf('.');
        if (index != -1) {
          String methodExpression = expression.substring(index+1, expression.length());
          String beanExpression = expression.substring(0, index);
          // TODO find a better way
          Object bean =
              facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, beanExpression);
          if (bean != null) {
            try {
              Method method = bean.getClass().getMethod(methodExpression);
              securityAnnotation = getSecurityAnnotations(method);
              if (securityAnnotation == null) {
                securityAnnotation = getSecurityAnnotations(bean.getClass());
              }
            } catch (NoSuchMethodException e) {
              LOG.error("No Method " + methodExpression + " in class " + bean.getClass(), e);
            }
          }
        }
      }
      AUTHORISATION_CACHE.put(expression, securityAnnotation);
      return securityAnnotation;
    }
  }
  private Annotation getSecurityAnnotations(AnnotatedElement annotatedElement) {
    Annotation annotation = annotatedElement.getAnnotation(RolesAllowed.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = annotatedElement.getAnnotation(DenyAll.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = annotatedElement.getAnnotation(PermitAll.class);
    if (annotation != null) {
      return annotation;
    }
    return null;
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
    return methodBinding instanceof StateHolder && ((StateHolder) methodBinding).isTransient();
  }

  public void setTransient(boolean bool) {
    if (methodBinding instanceof StateHolder) {
      ((StateHolder) methodBinding).setTransient(bool);
    }
  }
}
