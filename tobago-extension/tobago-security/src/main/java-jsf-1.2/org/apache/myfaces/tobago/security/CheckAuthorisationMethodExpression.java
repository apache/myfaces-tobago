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

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class CheckAuthorisationMethodExpression extends MethodExpression implements StateHolder {
  private static final Logger LOG = LoggerFactory.getLogger(CheckAuthorisationMethodExpression.class);

  private MethodExpression methodExpression;

  public CheckAuthorisationMethodExpression() {
  }

  public CheckAuthorisationMethodExpression(MethodExpression methodExpression) {
    this.methodExpression = methodExpression;
  }

  @Override
  public MethodInfo getMethodInfo(ELContext context) {
    return methodExpression.getMethodInfo(context);
  }

  @Override
  public Object invoke(ELContext context, Object[] objects) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MethodBinding invoke " + getExpressionString());
    }
    // MethodExpression with a argument list would not be checked for authorisation
    if ((objects!=null && objects.length > 0) || AuthorizationUtils.isAuthorized(FacesContext.getCurrentInstance(), getExpressionString())) {
      return methodExpression.invoke(context, objects);
    } else {
      // TODO better message
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Not authorised"));
      return null;
    }
  }

  @Override
  public String getExpressionString() {
    return methodExpression.getExpressionString();
  }

  @Override
  public boolean equals(Object obj) {
    return methodExpression.equals(obj);
  }

  @Override
  public int hashCode() {
    return methodExpression.hashCode();
  }

  @Override
  public boolean isLiteralText() {
    return methodExpression.isLiteralText();
  }

  public Object saveState(FacesContext facesContext) {
  Object[] saveState = new Object[1];
    saveState[0] = UIComponentBase.saveAttachedState(facesContext, methodExpression);
    return saveState;
  }

  public void restoreState(FacesContext facesContext, Object savedState) {
    Object[] values = (Object[]) savedState;
    methodExpression = (MethodExpression) UIComponentBase.restoreAttachedState(facesContext, values[0]);
  }

  public boolean isTransient() {
    return methodExpression instanceof StateHolder && ((StateHolder) methodExpression).isTransient();
  }

  public void setTransient(boolean bool) {
    if (methodExpression instanceof StateHolder) {
      ((StateHolder) methodExpression).setTransient(bool);
    }
  }

  public boolean isAuthorized(FacesContext facesContext) {
    return AuthorizationUtils.isAuthorized(facesContext, getExpressionString());
  }
}
