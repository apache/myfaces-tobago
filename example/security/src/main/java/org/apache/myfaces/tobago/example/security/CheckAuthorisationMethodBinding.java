package org.apache.myfaces.tobago.example.security;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 19.07.2006
 * Time: 16:11:43
 * To change this template use File | Settings | File Templates.
 */
public class CheckAuthorisationMethodBinding extends MethodBinding implements StateHolder {
  private static final Log LOG = LogFactory.getLog(CheckAuthorisationMethodBinding.class);
  private MethodBinding methodBinding;

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
      throws EvaluationException, MethodNotFoundException {
    // TODO check Authorisation and don't invoke method binding if user is not authorised
    // TODO add FacesMessage if user is not authorised
    if (LOG.isDebugEnabled()) {
      LOG.debug("MethodBinding invoke " + getExpressionString());
    }
    return methodBinding.invoke(facesContext, objects);
  }

  public Object saveState(FacesContext facesContext) {
    if (methodBinding instanceof StateHolder) {
      return ((StateHolder)methodBinding).saveState(facesContext);
    }
    return null;
  }

  public void restoreState(FacesContext facesContext, Object object) {
    if (methodBinding instanceof StateHolder) {
      ((StateHolder)methodBinding).restoreState(facesContext, object);
    }
  }

  public boolean isTransient() {
    if (methodBinding instanceof StateHolder) {
      return ((StateHolder)methodBinding).isTransient();
    }
    return false;
  }

  public void setTransient(boolean bool) {
    if (methodBinding instanceof StateHolder) {
      ((StateHolder)methodBinding).setTransient(bool);
    }
  }
}
