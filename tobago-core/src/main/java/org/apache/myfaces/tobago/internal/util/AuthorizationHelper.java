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

package org.apache.myfaces.tobago.internal.util;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.el.ELContext;
import jakarta.el.MethodExpression;
import jakarta.el.ValueExpression;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@ApplicationScoped
public class AuthorizationHelper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String AUTHORIZATION_HELPER = "authorizationHelper";

  private static final Pattern PATTERN = Pattern.compile("#\\{(\\w+(?:\\.\\w+)*)\\.(\\w+)(?:\\(.*\\))?}");

  private static final String CC_ATTRS = "cc.attrs.";

  private static final Annotation NULL_VALUE = new Annotation() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return null;
    }

    @Override
    public String toString() {
      return "(NULL)";
    }
  };

  private final Map<String, Object> cache = new ConcurrentHashMap<>();

  public static AuthorizationHelper getInstance(final FacesContext facesContext) {
    final ELContext elContext = facesContext.getELContext();
    return (AuthorizationHelper)
        elContext.getELResolver().getValue(elContext, null, AUTHORIZATION_HELPER);
  }

  public boolean isAuthorized(final FacesContext facesContext, final UIComponent component, final String expression) {

    final Annotation securityAnnotation = getSecurityAnnotation(facesContext, component, expression);
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
      final String[] roles = ((RolesAllowed) securityAnnotation).value();
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

  private Annotation getSecurityAnnotation(
      final FacesContext facesContext, final UIComponent component, final String expressionFull) {

    final String expression = skipParameterPart(expressionFull);

    Annotation securityAnnotation = null;

    if (cache.containsKey(expression)) {
      final Object obj = cache.get(expression);
      if (obj instanceof Annotation) {
        securityAnnotation = (Annotation) obj;
      }
    } else {
      final Matcher matcher = PATTERN.matcher(expression);
      if (matcher.matches()) {
        final String beanString = matcher.group(1);
        final String methodString = matcher.group(2);

        final Object bean = getBean(facesContext, beanString);
        if (bean != null) {
          // try first from method
          final List<Method> methods = findMethods(bean, methodString);
          switch (methods.size()) {
            case 0:
              LOG.error("No Method '" + methodString + "' in class " + bean.getClass());
              break;
            case 1:
              securityAnnotation = getSecurityAnnotations(methods.get(0));
              break;
            default:
              LOG.warn("Method name ambiguous '" + methodString + "' in class " + bean.getClass()
                  + ". Found " + methods.size() + " but only 1 is supported, yet.");
          }
          // if not set, try from class
          if (securityAnnotation == null) {
            securityAnnotation = getSecurityAnnotations(bean.getClass());
          }
        }
      }
      if (securityAnnotation == null) {
        securityAnnotation = NULL_VALUE;
      }

      cache.put(expression, securityAnnotation);
      if (LOG.isInfoEnabled()) {
        LOG.info("Security annotation '{}' saved for expression '{}'", securityAnnotation, expression);
      }
    }

    if (securityAnnotation == NULL_VALUE && expression.contains(CC_ATTRS)) {

      UIComponent compositeComponent = getParentCompositeComponent(component);
      if (compositeComponent != null) {
        final int attrNameStart = expression.indexOf(CC_ATTRS) + CC_ATTRS.length();
        final int attrNameEnd = expression.substring(attrNameStart).contains(".")
            ? attrNameStart + expression.substring(attrNameStart).indexOf(".")
            : attrNameStart + expression.substring(attrNameStart).indexOf("}");
        final String attrName = expression.substring(attrNameStart, attrNameEnd);

        ValueExpression valueExpression = compositeComponent.getValueExpression(attrName);
        if (valueExpression != null) {
          final String ccExpression = valueExpression.getExpressionString();
          final int bracketStart = ccExpression.indexOf('{');
          final int bracketEnd = ccExpression.indexOf("}");
          final String trimmedCcExpression = ccExpression.substring(bracketStart + 1, bracketEnd).trim();

          return getSecurityAnnotation(facesContext, component,
              expression.replace(CC_ATTRS + attrName, trimmedCcExpression));
        }

        MethodExpression methodExpression = (MethodExpression) compositeComponent.getAttributes().get(attrName);
        if (methodExpression != null) {
          return getSecurityAnnotation(facesContext, component,
              methodExpression.getExpressionString().replaceAll(" ", ""));
        }

        return securityAnnotation;
      } else {
        return securityAnnotation;
      }
    } else {
      return securityAnnotation;
    }
  }

  /**
   * Problems with brackets are not processed!
   */
  protected static String skipParameterPart(String expressionFull) {
    final int open = expressionFull.indexOf('(');
    final int close = expressionFull.lastIndexOf(')');
    if (open == -1 && close == -1) {
      return expressionFull;
    } else if (open > -1 && close == -1 || open == -1 /*&& close > -1 (always true) */) {
      LOG.warn("Invalid brackets in expression (unbalanced): '{}'", expressionFull);
      return expressionFull;
    } else if (close < open) {
      LOG.warn("Invalid brackets in expression (disordered): '{}'", expressionFull);
      return expressionFull;
    } else {
      return expressionFull.substring(0, open) + expressionFull.substring(close + 1);
    }
  }

  private Object getBean(final FacesContext facesContext, final String beanName) {

    Object bean = null;

    try {
      BeanManager beanManager = (BeanManager) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap()
          .get(BeanManager.class.getName());

      if (beanManager != null) {
        for (final Bean<?> entry : beanManager.getBeans(beanName)) {
          if (bean == null) {
            bean = entry;
          } else {
            LOG.warn("Bean name ambiguous: '{}'", beanName);
          }
        }
      }
    } catch (NoClassDefFoundError e) {
      // ignore - if there is no class BeanManager is in class path
    } catch (Exception e) {
      LOG.warn("Problem with getting bean from BeanManager", e);
    }

    if (bean == null) {
      final ELContext elContext = facesContext.getELContext();
      bean = elContext.getELResolver().getValue(elContext, null, beanName);
    }

    return bean;
  }

  private UIComponent getParentCompositeComponent(final UIComponent component) {
    if (component == null) {
      return null;
    } else if (UIComponent.isCompositeComponent(component)) {
      return component;
    } else {
      return getParentCompositeComponent(component.getParent());
    }
  }

  private Annotation getSecurityAnnotations(final AnnotatedElement annotatedElement) {
    Annotation annotation = annotatedElement.getAnnotation(RolesAllowed.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = annotatedElement.getAnnotation(DenyAll.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = annotatedElement.getAnnotation(PermitAll.class);
    return annotation;
  }

  private List<Method> findMethods(final Object bean, final String name) {
    Class clazz = null;
    try {
      if (bean instanceof Bean) {
        clazz = ((Bean) bean).getBeanClass();
      }
    } catch (NoClassDefFoundError e) {
      // ignore - if there is no class Bean is in class path
    } catch (Exception e) {
      LOG.warn("Problem with getting bean from BeanManager", e);
    }
    // XXX check if this works correctly with spring.
    if (clazz == null) {
      clazz = bean.getClass();
    }
    final Method[] methods = clazz.getMethods();
    final List<Method> result = new ArrayList<>();
    for (final Method method : methods) {
      if (method.getName().equals(name)) {
        result.add(method);
      }
    }
    return result;
  }

}
