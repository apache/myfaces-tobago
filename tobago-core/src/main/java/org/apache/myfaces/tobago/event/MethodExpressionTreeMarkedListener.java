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

package org.apache.myfaces.tobago.event;

import org.apache.myfaces.tobago.compat.FacesUtilsEL;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;


public class MethodExpressionTreeMarkedListener implements TreeMarkedListener, StateHolder {

  private MethodExpression methodExpression;

  private boolean isTransient = false;

  public MethodExpressionTreeMarkedListener() {
  }

  public MethodExpressionTreeMarkedListener(MethodExpression methodExpression) {
    this.methodExpression = methodExpression;
  }

  public void treeMarked(TreeMarkedEvent event) {
    FacesUtilsEL.invokeMethodExpression(FacesContext.getCurrentInstance(), methodExpression, event);
  }

  private ELContext elContext() {
    return FacesContext.getCurrentInstance().getELContext();
  }

  public void restoreState(FacesContext context, Object state) {
    methodExpression = (MethodExpression) state;
  }

  public Object saveState(FacesContext context) {
    return methodExpression;
  }

  public void setTransient(boolean newTransientValue) {
    isTransient = newTransientValue;
  }

  public boolean isTransient() {
    return isTransient;
  }
}
