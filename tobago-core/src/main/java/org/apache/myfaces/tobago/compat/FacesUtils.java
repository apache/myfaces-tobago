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

@SuppressWarnings("deprecation")
public class FacesUtils {

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};

  /**
   * @deprecated since 1.6.0. Please use ComponentUtils.invokeOnComponent(context, this, clientId, callback)
   */
  @Deprecated
  public static boolean invokeOnComponent(
      FacesContext context, UIComponent component, String clientId, ContextCallback callback) {
    return FacesInvokeOnComponent12.invokeOnComponent(context, component, clientId, callback);
  }

  public static void invokeMethodBinding(FacesContext facesContext, MethodBinding methodBinding, FacesEvent event) {
    if (methodBinding != null && event != null) {
      try {
        methodBinding.invoke(facesContext, new Object[]{event});
      } catch (EvaluationException e) {
        Throwable cause = e.getCause();
        if (cause instanceof AbortProcessingException) {
          throw (AbortProcessingException) cause;
        } else {
          throw e;
        }
      }
    }
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static Object getValueFromValueBindingOrValueExpression(
      FacesContext context, UIComponent component, String name) {
      return FacesUtilsEL.getValueFromValueBindingOrValueExpression(context, component, name);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
      return FacesUtilsEL.hasValueBindingOrValueExpression(component, name);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static boolean isReadonlyValueBindingOrValueExpression(
      FacesContext context, UIComponent component, String name) {
      return FacesUtilsEL.isReadonlyValueBindingOrValueExpression(context, component, name);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static String getExpressionString(UIComponent component, String name) {
      return FacesUtilsEL.getExpressionString(component, name);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setValueOfBindingOrExpression(
      FacesContext context, Object value, UIComponent component, String bindingName) {
      FacesUtilsEL.setValueOfBindingOrExpression(context, value, component, bindingName);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setValueOfBindingOrExpression(
      FacesContext context, Object value, Object bindingOrExpression) {
      FacesUtilsEL.setValueOfBindingOrExpression(context, value, bindingOrExpression);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void copyValueBindingOrValueExpression(
      UIComponent fromComponent, String fromName, UIComponent toComponent, String toName) {
      FacesUtilsEL.copyValueBindingOrValueExpression(fromComponent, fromName, toComponent, toName);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static Object getValueFromBindingOrExpression(Object obj) {
      return FacesUtilsEL.getValueFromBindingOrExpression(obj);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static Object createExpressionOrBinding(String string) {
      return FacesUtilsEL.createExpressionOrBinding(string);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setValidator(EditableValueHolder editableValueHolder, Object validator) {
      FacesUtilsEL.setValidator(editableValueHolder, validator);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setConverter(ValueHolder valueHolder, Object converterExpression) {
      FacesUtilsEL.setConverter(valueHolder, converterExpression);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setBindingOrExpression(UIComponent component, String name, Object valueBindingOrExpression) {
      FacesUtilsEL.setBindingOrExpression(component, name, valueBindingOrExpression);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void setBindingOrExpression(UIComponent component, String name, String valueBindingOrExpression) {
    setBindingOrExpression(component, name, createExpressionOrBinding(valueBindingOrExpression));
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void addBindingOrExpressionTabChangeListener(TabChangeSource source, String type,
      Object bindingOrExpression) {
      FacesUtilsEL.addBindingOrExpressionTabChangeListener(source, type, bindingOrExpression);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static Comparator getBindingOrExpressionComparator(
      FacesContext facesContext, UIComponent child, String var, boolean descending, Comparator comparator) {
      return FacesUtilsEL.getBindingOrExpressionComparator(facesContext, child, var, descending, comparator);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void addBindingOrExpressionPopupActionListener(ActionSource actionSource, Object bindingOrExpression) {
      FacesUtilsEL.addBindingOrExpressionPopupActionListener(actionSource, bindingOrExpression);
  }

  /**
   * @deprecated since 1.6.0
   */
  @Deprecated
  public static void addBindingOrExpressionResetActionListener(ActionSource actionSource, Object bindingOrExpression) {
      FacesUtilsEL.addBindingOrExpressionResetActionListener(actionSource, bindingOrExpression);
  }

  /**
   * @deprecated since 1.6.0 Please call facesContext.getAttributes() directly.
   */
  @Deprecated
  public static Map getFacesContextAttributes(FacesContext facesContext) {
    return facesContext.getAttributes();
  }

  /**
   * @deprecated since 1.6.0. Always true.
   */
  @Deprecated
  public static boolean supportsEL() {
    return true;
  }
}
