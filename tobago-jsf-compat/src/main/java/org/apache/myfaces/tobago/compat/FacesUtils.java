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
import org.apache.myfaces.tobago.event.ValueBindingTabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;

import javax.faces.application.Application;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.convert.Converter;

@SuppressWarnings("deprecation")
public class FacesUtils {

  private static final Log LOG = LogFactory.getLog(FacesUtils.class);

  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};


  static {
    try {
      Application.class.getMethod("getExpressionFactory");
      facesVersion = 12;
    } catch (NoSuchMethodException e) {
      facesVersion = 11;
    }
  }

  private static int facesVersion;

  public static boolean invokeOnComponent(FacesContext context, UIComponent component,
      String clientId, ContextCallback callback) {
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

  private static boolean invokeOnComponentFacetsAndChildren(FacesContext context, UIComponent component,
      String clientId, ContextCallback callback) {
    for (java.util.Iterator<UIComponent> it = component.getFacetsAndChildren(); it.hasNext();) {
      UIComponent child = it.next();

      if (facesVersion == 11) {
        if (child instanceof InvokeOnComponent) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Found InvokeOnComponent with clientId " + child.getClientId(context));
          }
          if (((InvokeOnComponent) child).invokeOnComponent(context, clientId, callback)) {
            return true;
          }
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Did not found InvokeOnComponent " + child.getClass().getName() + " "
                + child.getClientId(context) + " "
                + child.getRendererType() +
                (child.getParent() != null ? child.getParent().getClass().getName() : "null"));
          }
        }
      } else {
        if (child.invokeOnComponent(context, clientId, callback)) {
          return true;
        }
      }
    }
    return false;
  }


  public static Object getValueFromValueBindingOrValueExpression(FacesContext context, UIComponent component,
      String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name).getValue(context);
    } else {
      return FacesUtils12.getValueFromValueBindingOrValueExpression(context, component, name);
    }
  }


  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name) != null;
    } else {
      return FacesUtils12.hasValueBindingOrValueExpression(component, name);
    }
  }

  public static boolean isReadonlyValueBindingOrValueExpression(FacesContext context,
      UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name).isReadOnly(context);
    } else {
      return FacesUtils12.isReadonlyValueBindingOrValueExpression(context, component, name);
    }
  }


  public static String getExpressionString(UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name).getExpressionString();
    } else {
      return FacesUtils12.getExpressionString(component, name);
    }
  }

  public static void setValueOfBindingOrExpression(FacesContext context, Object value,
      UIComponent component, String bindingName) {
    if (facesVersion == 11) {
      ValueBinding vb = component.getValueBinding(bindingName);
      if (vb != null) {
        vb.setValue(context, value);
      }
    } else {
      FacesUtils12.setValueOfBindingOrExpression(context, value, component, bindingName);
    }
  }

   public static void setValueOfBindingOrExpression(FacesContext context, Object value,
      Object bindingOrExpression) {
    if (facesVersion == 11) {
      if (bindingOrExpression instanceof ValueBinding) {
        ValueBinding vb = (ValueBinding) bindingOrExpression;
        vb.setValue(context, value);
      }
    } else {
      FacesUtils12.setValueOfBindingOrExpression(context, value, bindingOrExpression);
    }
  }

  public static void copyValueBindingOrValueExpression(UIComponent fromComponent, String fromName,
      UIComponent toComponent, String toName) {
    if (facesVersion == 11) {
      ValueBinding vb = fromComponent.getValueBinding(fromName);
      if (vb != null) {
        toComponent.setValueBinding(toName, vb);
      }
    } else {
      FacesUtils12.copyValueBindingOrValueExpression(fromComponent, fromName, toComponent, toName);
    }
  }

  public static Object getValueFromBindingOrExpression(Object obj) {
    if (facesVersion == 11) {
      if (obj instanceof ValueBinding) {
        return ((ValueBinding) obj).getValue(FacesContext.getCurrentInstance());
      }
    } else {
      return FacesUtils12.getValueFromBindingOrExpression(obj);
    }
    return null;
  }

  public static void setValidator(EditableValueHolder editableValueHolder, Object validator) {
    if (facesVersion == 11) {
      MethodBinding methodBinding =
          FacesContext.getCurrentInstance().getApplication().createMethodBinding(validator.toString(), VALIDATOR_ARGS);
      editableValueHolder.setValidator(methodBinding);
    } else {
      FacesUtils12.setValidator(editableValueHolder, validator);
    }
  }

  public static void setConverter(ValueHolder valueHolder, Object converterExpression) {
    if (facesVersion == 11) {
      if (converterExpression != null && converterExpression instanceof String) {
        String converterExpressionStr = (String) converterExpression;
        FacesContext context = FacesContext.getCurrentInstance();
        if (UIComponentTag.isValueReference(converterExpressionStr)) {
          ValueBinding valueBinding = context.getApplication().createValueBinding(converterExpressionStr);
          if (valueHolder instanceof UIComponent) {
            ((UIComponent) valueHolder).setValueBinding("converter", valueBinding);
          }
        } else {
          Converter converter = context.getApplication().createConverter(converterExpressionStr);
          valueHolder.setConverter(converter);
        }
      }
    } else {
      FacesUtils12.setConverter(valueHolder, converterExpression);
    }
  }

  public static void setBindingOrExpression(UIComponent component, String name, Object valueBindingOrExpression) {
    if (facesVersion == 11) {
      component.setValueBinding(name, (ValueBinding) valueBindingOrExpression);
    } else {
      FacesUtils12.setBindingOrExpression(component, name, valueBindingOrExpression);
    }
  }

  public static void addBindingOrExpressionTabChangeListener(TabChangeSource source, String type,
      Object bindingOrExpression) {
    if (facesVersion == 11) {
      source.addTabChangeListener(new ValueBindingTabChangeListener(type, (ValueBinding) bindingOrExpression));
    } else {
      FacesUtils12.addBindingOrExpressionTabChangeListener(source, type, bindingOrExpression);
    }
  }
}
