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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationUtils {
  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationUtils.class);

  private static final Object NULL_VALUE = new Object();
  private static final Map<String, Object> AUTHORISATION_CACHE = new ConcurrentHashMap<String, Object>();

  private static final Pattern PATTERN = Pattern.compile("#\\{(\\w+(?:\\.\\w+)*)\\.(\\w+)(?:\\(.*\\))?\\}");

  private AuthorizationUtils() {
  }

  public static boolean isAuthorized(final FacesContext facesContext, final String expression) {

    final Annotation securityAnnotation = getSecurityAnnotation(facesContext, expression);
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
      final String [] roles = ((RolesAllowed) securityAnnotation).value();
      if (LOG.isDebugEnabled()) {
        LOG.debug("RolesAllowed " + Arrays.asList(((RolesAllowed) securityAnnotation).value()));
      }
      for (final String role : roles) {
        final boolean authorised = facesContext.getExternalContext().isUserInRole(role);
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

  private static Annotation getSecurityAnnotations(final AnnotatedElement annotatedElement) {
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

  private static Annotation getSecurityAnnotation(final FacesContext facesContext, String expression) {
    if (AUTHORISATION_CACHE.containsKey(expression)) {
      final Object obj = AUTHORISATION_CACHE.get(expression);
      if (obj instanceof Annotation) {
        return (Annotation) obj;
      }
      return null;
    } else {
      Annotation securityAnnotation = null;
      final Matcher matcher = PATTERN.matcher(expression);
      if (matcher.matches()) {
        String beanString = matcher.group(1);
        String methodString = matcher.group(2);
        final Object bean =
            facesContext.getELContext().getELResolver().getValue(facesContext.getELContext(), null, beanString);
        if (bean != null) {
          final List<Method> methods = findMethods(bean, methodString);
          switch (methods.size()) {
            case 0:
              LOG.error("No Method '" + methodString + "' in class " + bean.getClass());
              break;
            case 1:
              securityAnnotation = getSecurityAnnotations(methods.get(0));
              if (securityAnnotation == null) {
                securityAnnotation = getSecurityAnnotations(bean.getClass());
              }
              break;
            default:
              LOG.warn("Method name ambiguous '" + methodString + "' in class " + bean.getClass()
                  + ". Found " + methods.size() + " but only 1 is supported, yet.");
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

  private static List<Method> findMethods(Object bean, String name) {
    final Method[] methods = bean.getClass().getMethods();
    final List<Method> result = new ArrayList<Method>();
    for (Method method : methods) {
      if (method.getName().equals(name)) {
        result.add(method);
      }
    }
    return result;
  }
}
