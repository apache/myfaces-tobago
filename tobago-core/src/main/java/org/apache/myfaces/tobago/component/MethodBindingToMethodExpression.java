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

package org.apache.myfaces.tobago.component;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;

/**
 * @deprecated since 1.6.0
 */
@Deprecated
public class MethodBindingToMethodExpression extends MethodExpression implements StateHolder {
  private MethodBinding methodBinding;

  private boolean transientFlag;

  private transient MethodInfo methodInfo;

  /**
   * No-arg constructor used during restoreState
   */
  public MethodBindingToMethodExpression() {
  }

  /**
   * Creates a new instance of MethodBindingToMethodExpression
   * @param methodBinding The MethodBinding to wrap.
   */
  public MethodBindingToMethodExpression(MethodBinding methodBinding) {
    checkNullArgument(methodBinding, "methodBinding");
    this.methodBinding = methodBinding;
  }

  /**
   * Return the wrapped MethodBinding.
   * @return the wrapped MethodBinding
   */
  public MethodBinding getMethodBinding() {
    return methodBinding;
  }

  void setMethodBinding(MethodBinding methodBinding) {
    this.methodBinding = methodBinding;
  }

  /**
   * Note: MethodInfo.getParamTypes() may incorrectly return an empty class array if invoke() has not been called.
   *
   * @throws IllegalStateException if expected params types have not been determined.
   */
  public MethodInfo getMethodInfo(ELContext context) throws ELException {
    checkNullArgument(context, "elcontext");
    checkNullState(methodBinding, "methodBinding");

    if (methodInfo == null) {
      final FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
      if (facesContext != null) {
        methodInfo = invoke(new Invoker<MethodInfo>() {
          public MethodInfo invoke() {
            return new MethodInfo(null, methodBinding.getType(facesContext), null);
          }
        });
      }
    }
    return methodInfo;
  }

  public Object invoke(ELContext context, final Object[] params) throws ELException {
    checkNullArgument(context, "elcontext");
    checkNullState(methodBinding, "methodBinding");
    final FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
    if (facesContext != null) {
      return invoke(new Invoker<Object>() {
        public Object invoke() {
          return methodBinding.invoke(facesContext, params);
        }
      });
    }
    return null;
  }

  public boolean isLiteralText() {
    if (methodBinding == null) {
      throw new IllegalStateException("methodBinding is null");
    }
    String expr = methodBinding.getExpressionString();
    return !(expr.startsWith("#{") && expr.endsWith("}"));
  }

  public String getExpressionString() {
    return methodBinding.getExpressionString();
  }

  public Object saveState(FacesContext context) {
    if (!isTransient()) {
      if (methodBinding instanceof StateHolder) {
        Object[] state = new Object[2];
        state[0] = methodBinding.getClass().getName();
        state[1] = ((StateHolder) methodBinding).saveState(context);
        return state;
      } else {
        return methodBinding;
      }
    }
    return null;
  }

  public void restoreState(FacesContext context, Object state) {
    if (state instanceof MethodBinding) {
      methodBinding = (MethodBinding) state;
      methodInfo = null;
    } else if (state != null) {
      Object[] values = (Object[]) state;
      methodBinding = (MethodBinding) newInstance(values[0].toString());
      ((StateHolder) methodBinding).restoreState(context, values[1]);
      methodInfo = null;
    }
  }

  public void setTransient(boolean transientFlag) {
    this.transientFlag = transientFlag;
  }

  public boolean isTransient() {
    return transientFlag;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((methodBinding == null) ? 0 : methodBinding.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MethodBindingToMethodExpression other = (MethodBindingToMethodExpression) obj;
    if (methodBinding == null) {
      if (other.methodBinding != null) {
        return false;
      }
    } else if (!methodBinding.equals(other.methodBinding)) {
      return false;
    }
    return true;
  }

  private void checkNullState(Object notNullInstance, String instanceName) {
    if (notNullInstance == null) {
      throw new IllegalStateException(instanceName + " is null");
    }
  }

  private void checkNullArgument(Object notNullInstance, String instanceName) {
    if (notNullInstance == null) {
      throw new IllegalArgumentException(instanceName + " is null");
    }
  }

  private <T> T invoke(Invoker<T> invoker) {
    try {
      return invoker.invoke();
    } catch (javax.faces.el.MethodNotFoundException e) {
      throw new MethodNotFoundException(e.getMessage(), e);
    } catch (EvaluationException e) {
      throw new ELException(e.getMessage(), e);
    }
  }

  private interface Invoker<T> {
    T invoke();
  }

  private static Object newInstance(String type) {
    if (type == null) {
      throw new NullPointerException("type");
    }
    try {
      try {
        return Class.forName(type, false, Thread.currentThread().getContextClassLoader()).newInstance();
      } catch (ClassNotFoundException e) {
        // ignore
        return Class.forName(type, false, MethodBindingToMethodExpression.class.getClassLoader()).newInstance();
      }
    } catch (ClassNotFoundException e) {
      throw new FacesException(e);
    } catch (NoClassDefFoundError e) {
      throw new FacesException(e);
    } catch (InstantiationException e) {
      throw new FacesException(e);
    } catch (IllegalAccessException e) {
      throw new FacesException(e);
    }
  }

}
