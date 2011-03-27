package org.apache.myfaces.tobago.security;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponentBase;
import javax.faces.application.FacesMessage;

public class CheckAuthorisationMethodBinding extends MethodBinding implements StateHolder {
  private static final Logger LOG = LoggerFactory.getLogger(CheckAuthorisationMethodBinding.class);

  private MethodBinding methodBinding;

  public CheckAuthorisationMethodBinding() {
  }

  public CheckAuthorisationMethodBinding(MethodBinding methodBinding) {
    this.methodBinding = methodBinding;
  }

  public String getExpressionString() {
    return methodBinding.getExpressionString();
  }

  public Class getType(FacesContext facesContext) throws MethodNotFoundException {
    return methodBinding.getType(facesContext);
  }

  public Object invoke(FacesContext facesContext, Object[] objects)
      throws EvaluationException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MethodBinding invoke " + getExpressionString());
    }
    // MethodBindings with a argument list would not be checked for authorisation
    if ((objects!=null && objects.length > 0) || AuthorizationUtils.isAuthorized(facesContext, getExpressionString())) {
      return methodBinding.invoke(facesContext, objects);
    } else {
      // TODO better message
      facesContext.addMessage(null, new FacesMessage("Not authorised"));
      return null;
    }
  }

  public Object saveState(FacesContext facesContext) {
    Object[] saveState = new Object[1];
    saveState[0] = UIComponentBase.saveAttachedState(facesContext, methodBinding);
    return saveState;
  }

  public void restoreState(FacesContext facesContext, Object savedState) {
    Object[] values = (Object[]) savedState;
    methodBinding = (MethodBinding) UIComponentBase.restoreAttachedState(facesContext, values[0]);
  }

  public boolean isTransient() {
    return methodBinding instanceof StateHolder && ((StateHolder) methodBinding).isTransient();
  }

  public void setTransient(boolean bool) {
    if (methodBinding instanceof StateHolder) {
      ((StateHolder) methodBinding).setTransient(bool);
    }
  }

  public boolean isAuthorized(FacesContext facesContext) {
    return AuthorizationUtils.isAuthorized(facesContext, getExpressionString());
  }
}
