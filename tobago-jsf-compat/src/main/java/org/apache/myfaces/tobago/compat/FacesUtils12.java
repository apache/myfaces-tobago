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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.ValueExpressionTabChangeListener;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.validator.MethodExpressionValidator;
import javax.el.ValueExpression;
import javax.el.MethodExpression;


public class FacesUtils12 {
  private static final Log LOG = LogFactory.getLog(FacesUtils12.class);


  public static Object getValueFromValueBindingOrValueExpression(FacesContext context, UIComponent component,
      String name) {
    return component.getValueExpression(name).getValue(context.getELContext());
  }

  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
    return component.getValueExpression(name) != null;
  }

  public static boolean isReadonlyValueBindingOrValueExpression(FacesContext context,
      UIComponent component, String name) {
    return component.getValueExpression(name).isReadOnly(context.getELContext());
  }

  public static String getExpressionString(UIComponent component, String name) {
    return component.getValueExpression(name).getExpressionString();
  }

  public static void setValueOfBindingOrExpression(FacesContext context, Object value,
      UIComponent component, String bindingName) {
    ValueExpression ve = component.getValueExpression(bindingName);
    if (ve != null) {
      ve.setValue(context.getELContext(), value);
    }
  }

  public static void copyValueBindingOrValueExpression(UIComponent fromComponent, String fromName,
      UIComponent toComponent, String toName) {
    ValueExpression ve = fromComponent.getValueExpression(fromName);
    if (ve != null) {
      toComponent.setValueExpression(toName, ve);
    }
  }

  public static void setValidator(EditableValueHolder editableValueHolder, Object validator) {
    if (validator instanceof MethodExpression) {
      editableValueHolder.addValidator(new MethodExpressionValidator((MethodExpression) validator));
    } else {
      LOG.error("Unknow instance for validator: " + (validator!=null ? validator.getClass().getName() : validator));
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

  public static Object getValueFromBindingOrExpression(Object valueBindingOrExpression) {
    if (valueBindingOrExpression instanceof ValueExpression) {
     ValueExpression expression = (ValueExpression) valueBindingOrExpression;
      return expression.getValue(FacesContext.getCurrentInstance().getELContext());
    }
    return null; 
  }

  public static void setBindingOrExpression(UIComponent component, String name, Object valueBindingOrExpression) {
    component.setValueExpression(name, (ValueExpression) valueBindingOrExpression);
  }

  public static void setValueOfBindingOrExpression(FacesContext context, Object value, Object bindingOrExpression) {
    if (bindingOrExpression instanceof ValueExpression) {
      ValueExpression ve = (ValueExpression) bindingOrExpression;
      ve.setValue(context.getELContext(), value);
    }
  }

  public static void addBindingOrExpressionTabChangeListener(TabChangeSource source, String type,
      Object bindingOrExpression) {
    if (bindingOrExpression instanceof ValueExpression) {
      source.addTabChangeListener(new ValueExpressionTabChangeListener(type, (ValueExpression) bindingOrExpression));
    }
  }
}
