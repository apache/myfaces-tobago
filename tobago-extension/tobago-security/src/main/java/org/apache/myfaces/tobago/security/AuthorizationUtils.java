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

package org.apache.myfaces.tobago.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.faces.context.FacesContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorizationUtils {
  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationUtils.class);

  private static final Object NULL_VALUE = new Object();
  private static final Map<String, Object> AUTHORISATION_CACHE = new ConcurrentHashMap<String, Object>();

  public static boolean isAuthorized(FacesContext facesContext, String expression) {

    Annotation securityAnnotation = getSecurityAnnotation(facesContext, expression);
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

  private static Annotation getSecurityAnnotations(AnnotatedElement annotatedElement) {
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

  private static Annotation getSecurityAnnotation(FacesContext facesContext, String expression) {
    if (AUTHORISATION_CACHE.containsKey(expression)) {
      Object obj = AUTHORISATION_CACHE.get(expression);
      if (obj instanceof Annotation) {
        return (Annotation) obj;
      }
      return null;
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
      if (securityAnnotation != null) {
        AUTHORISATION_CACHE.put(expression, securityAnnotation);
      } else {
        AUTHORISATION_CACHE.put(expression, NULL_VALUE);
      }
      return securityAnnotation;
    }
  }
}

