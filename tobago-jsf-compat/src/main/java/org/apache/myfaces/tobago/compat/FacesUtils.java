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

import javax.faces.application.Application;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.el.ValueExpression;


public class FacesUtils {

  static {
    try {
      Application.class.getMethod("getExpressionFactory", null);
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

  private static boolean invokeOnComponentFacetsAndChildren(FacesContext context, UIComponent component, String clientId,
      ContextCallback callback) {
    for (java.util.Iterator<UIComponent> it = component.getFacetsAndChildren(); it.hasNext();) {
      UIComponent child = it.next();

      if (facesVersion == 11) {
        if (child instanceof InvokeOnComponent) {
          System.err.println("Found InvokeOnComponent with clientId " + child.getClientId(context));
          if (((InvokeOnComponent) child).invokeOnComponent(context, clientId, callback)) {
            return true;
          }
        } else {
          System.err.println("Did not found InvokeOnComponent " + child.getClass().getName() + " "
              + child.getClientId(context) + " "
              + child.getRendererType() + (child.getParent()!=null?child.getParent().getClass().getName():"null"));
        }
      } else {
        if (child.invokeOnComponent(context, clientId, callback)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name) != null;
    } else {
      return component.getValueExpression(name) != null;
    }
  }

  public static boolean isReadonlyValueBindingOrValueExpression(FacesContext context, UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name).isReadOnly(context);
    } else {
      return component.getValueExpression(name).isReadOnly(context.getELContext());
    }
  }


  public static String getExpressionString(UIComponent component, String name) {
    if (facesVersion == 11) {
      return component.getValueBinding(name).getExpressionString();
    } else {
      return component.getValueExpression(name).getExpressionString();
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
      ValueExpression ve = component.getValueExpression(bindingName);
      if (ve != null) {
        ve.setValue(context.getELContext(), value);
      }
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
      ValueExpression ve = fromComponent.getValueExpression(fromName);
      if (ve != null) {
        toComponent.setValueExpression(toName, ve);
      }
    }
  }
}
