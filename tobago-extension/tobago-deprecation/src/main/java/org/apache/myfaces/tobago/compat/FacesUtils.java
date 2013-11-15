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

package org.apache.myfaces.tobago.compat;

import org.apache.myfaces.tobago.event.TabChangeSource;

import javax.faces.component.ActionSource;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.Comparator;
import java.util.Map;

/**
 * @deprecated since 2.0.0
 */
@Deprecated
@SuppressWarnings("deprecation")
public class FacesUtils {

  private FacesUtils() {
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};

  /**
   * @deprecated since 2.0.0. Please use ComponentUtils.invokeOnComponent(context, this, clientId, callback)
   */
  @Deprecated
  public static boolean invokeOnComponent(
      final FacesContext context, final UIComponent component, final String clientId, final ContextCallback callback) {
    return FacesInvokeOnComponent12.invokeOnComponent(context, component, clientId, callback);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void invokeMethodBinding(
      final FacesContext facesContext, final MethodBinding methodBinding, final FacesEvent event) {
    if (methodBinding != null && event != null) {
      try {
        methodBinding.invoke(facesContext, new Object[]{event});
      } catch (final EvaluationException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof AbortProcessingException) {
          throw (AbortProcessingException) cause;
        } else {
          throw e;
        }
      }
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static Object getValueFromValueBindingOrValueExpression(
      final FacesContext context, final UIComponent component, final String name) {
      return FacesUtilsEL.getValueFromValueBindingOrValueExpression(context, component, name);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static boolean hasValueBindingOrValueExpression(final UIComponent component, final String name) {
      return FacesUtilsEL.hasValueBindingOrValueExpression(component, name);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static boolean isReadonlyValueBindingOrValueExpression(
      final FacesContext context, final UIComponent component, final String name) {
      return FacesUtilsEL.isReadonlyValueBindingOrValueExpression(context, component, name);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String getExpressionString(final UIComponent component, final String name) {
      return FacesUtilsEL.getExpressionString(component, name);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValueOfBindingOrExpression(
      final FacesContext context, final Object value, final UIComponent component, final String bindingName) {
      FacesUtilsEL.setValueOfBindingOrExpression(context, value, component, bindingName);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValueOfBindingOrExpression(
      final FacesContext context, final Object value, final Object bindingOrExpression) {
      FacesUtilsEL.setValueOfBindingOrExpression(context, value, bindingOrExpression);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void copyValueBindingOrValueExpression(
      final UIComponent fromComponent, final String fromName, final UIComponent toComponent, final String toName) {
      FacesUtilsEL.copyValueBindingOrValueExpression(fromComponent, fromName, toComponent, toName);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static Object getValueFromBindingOrExpression(final Object obj) {
      return FacesUtilsEL.getValueFromBindingOrExpression(obj);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static Object createExpressionOrBinding(final String string) {
      return FacesUtilsEL.createExpressionOrBinding(string);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValidator(final EditableValueHolder editableValueHolder, final Object validator) {
      FacesUtilsEL.setValidator(editableValueHolder, validator);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setConverter(final ValueHolder valueHolder, final Object converterExpression) {
      FacesUtilsEL.setConverter(valueHolder, converterExpression);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setBindingOrExpression(
      final UIComponent component, final String name, final Object valueBindingOrExpression) {
    FacesUtilsEL.setBindingOrExpression(component, name, valueBindingOrExpression);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setBindingOrExpression(
      final UIComponent component, final String name, final String valueBindingOrExpression) {
    setBindingOrExpression(component, name, createExpressionOrBinding(valueBindingOrExpression));
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void addBindingOrExpressionTabChangeListener(final TabChangeSource source, final String type,
      final Object bindingOrExpression) {
      FacesUtilsEL.addBindingOrExpressionTabChangeListener(source, type, bindingOrExpression);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static Comparator getBindingOrExpressionComparator(
      final FacesContext facesContext, final UIComponent child, final String var, final boolean descending,
      final Comparator comparator) {
    return FacesUtilsEL.getBindingOrExpressionComparator(facesContext, child, var, descending, comparator);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void addBindingOrExpressionPopupActionListener(
      final ActionSource actionSource, final Object bindingOrExpression) {
    FacesUtilsEL.addBindingOrExpressionPopupActionListener(actionSource, bindingOrExpression);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void addBindingOrExpressionResetActionListener(
      final ActionSource actionSource, final Object bindingOrExpression) {
    FacesUtilsEL.addBindingOrExpressionResetActionListener(actionSource, bindingOrExpression);
  }

  /**
   * @deprecated since 2.0.0 Please call facesContext.getAttributes() directly.
   */
  @Deprecated
  public static Map getFacesContextAttributes(final FacesContext facesContext) {
    return facesContext.getAttributes();
  }

  /**
   * @deprecated since 2.0.0. Always true.
   */
  @Deprecated
  public static boolean supportsEL() {
    return true;
  }
}
