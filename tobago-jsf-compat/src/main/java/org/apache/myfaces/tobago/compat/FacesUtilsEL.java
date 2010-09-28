package org.apache.myfaces.tobago.compat;

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

import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.ValueExpressionPopupActionListener;
import org.apache.myfaces.tobago.event.ValueExpressionTabChangeListener;
import org.apache.myfaces.tobago.util.ValueExpressionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.validator.MethodExpressionValidator;
import java.util.Comparator;

public class FacesUtilsEL {

  private static final Logger LOG = LoggerFactory.getLogger(FacesUtilsEL.class);

  public static boolean invokeOnComponent(
      FacesContext context, UIComponent component, String clientId, ContextCallback callback) {
    String thisClientId = component.getClientId(context);

    if (clientId.equals(thisClientId)) {
      callback.invokeContextCallback(context, component);
      return true;
    } else if (component instanceof NamingContainer) {
      // This component is a naming container. If the client id shows it's inside this naming container,
      // then process further.
      // Otherwise we know the client id we're looking for is not in this naming container,
      // so for improved performance short circuit and return false.
      if (clientId.startsWith(thisClientId)
          && (clientId.charAt(thisClientId.length()) == NamingContainer.SEPARATOR_CHAR)) {
        if (invokeOnComponentFacetsAndChildren(context, component, clientId, callback)) {
          return true;
        }
      }
    } else {
      if (invokeOnComponentFacetsAndChildren(context, component, clientId, callback)) {
        return true;
      }
    }

    return false;
  }

  private static boolean invokeOnComponentFacetsAndChildren(
      FacesContext context, UIComponent component, String clientId, ContextCallback callback) {
    for (java.util.Iterator<UIComponent> it = component.getFacetsAndChildren(); it.hasNext();) {
      UIComponent child = it.next();

      if (child.invokeOnComponent(context, clientId, callback)) {
        return true;
      }
    }
    return false;
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

  public static void invokeMethodBinding(
      FacesContext facesContext, MethodExpression methodExpression, FacesEvent event) {

    if (methodExpression != null && event != null) {
      try {
        methodExpression.invoke(facesContext.getELContext(), new Object[]{event});
      } catch (Exception e) {
        throw new AbortProcessingException(e);
      }
    }
  }

  public static Object getValueFromValueBindingOrValueExpression(
      FacesContext context, UIComponent component, String name) {
    return component.getValueExpression(name).getValue(context.getELContext());
  }


  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
    return component.getValueExpression(name) != null;
  }

  public static boolean isReadonlyValueBindingOrValueExpression(
      FacesContext context, UIComponent component, String name) {
    return component.getValueExpression(name).isReadOnly(context.getELContext());
  }


  public static String getExpressionString(UIComponent component, String name) {
    return component.getValueExpression(name).getExpressionString();
  }

  public static void setValueOfBindingOrExpression(
      FacesContext context, Object value, UIComponent component, String bindingName) {
    ValueExpression ve = component.getValueExpression(bindingName);
    if (ve != null) {
      ve.setValue(context.getELContext(), value);
    }
  }

  public static void setValueOfBindingOrExpression(
      FacesContext context, Object value, Object bindingOrExpression) {
    if (bindingOrExpression instanceof ValueExpression) {
      ValueExpression ve = (ValueExpression) bindingOrExpression;
      ve.setValue(context.getELContext(), value);
    }
  }

  public static void copyValueBindingOrValueExpression(
      UIComponent fromComponent, String fromName, UIComponent toComponent, String toName) {
    ValueExpression ve = fromComponent.getValueExpression(fromName);
    if (ve != null) {
      toComponent.setValueExpression(toName, ve);
    }
  }

  public static Object getValueFromBindingOrExpression(Object obj) {
    if (obj instanceof ValueExpression) {
      ValueExpression expression = (ValueExpression) obj;
      return expression.getValue(FacesContext.getCurrentInstance().getELContext());
    }
    return null;
  }

  public static void setValidator(EditableValueHolder editableValueHolder, Object validator) {
    if (validator instanceof MethodExpression) {
      editableValueHolder.addValidator(new MethodExpressionValidator((MethodExpression) validator));
    } else {
      LOG.error("Unknow instance for validator: " + (validator != null ? validator.getClass().getName() : validator));
    }
  }

  public static void setConverter(ValueHolder valueHolder, Object converterExpression) {
    if (converterExpression instanceof ValueExpression) {
      ValueExpression expression = (ValueExpression) converterExpression;
      if (!expression.isLiteralText()) {
        ((UIComponent) valueHolder).setValueExpression("converter", expression);
      } else {
        valueHolder.setConverter(FacesContext.getCurrentInstance()
            .getApplication().createConverter(expression.getExpressionString()));
      }
    }
  }

  public static void setBindingOrExpression(UIComponent component, String name, Object valueBindingOrExpression) {
    component.setValueExpression(name, (ValueExpression) valueBindingOrExpression);
  }

  public static void addBindingOrExpressionTabChangeListener(
      TabChangeSource source, String type, Object bindingOrExpression) {
    if (bindingOrExpression instanceof ValueExpression) {
      source.addTabChangeListener(new ValueExpressionTabChangeListener(type, (ValueExpression) bindingOrExpression));
    }
  }

  public static Comparator getBindingOrExpressionComparator(
      FacesContext facesContext, UIComponent child, String var, boolean descending, Comparator comparator) {
    ValueExpression valueBinding = child.getValueExpression("value");
    return new ValueExpressionComparator(facesContext, var, valueBinding, descending, comparator);
  }

  public static void addBindingOrExpressionPopupActionListener(ActionSource actionSource, Object bindingOrExpression) {
    actionSource.addActionListener(new ValueExpressionPopupActionListener((ValueExpression) bindingOrExpression));
  }
}
