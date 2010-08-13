package org.apache.myfaces.tobago.event;

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

import org.apache.myfaces.tobago.compat.FacesUtils;

import javax.el.MethodExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;

public class MethodExpressionTabChangeListener implements TabChangeListener, StateHolder {

  private MethodExpression methodExpression;

  private boolean isTransient = false;

  public MethodExpressionTabChangeListener() {
  }

  public MethodExpressionTabChangeListener(MethodExpression methodExpression) {
    this.methodExpression = methodExpression;
  }

  public void processTabChange(TabChangeEvent actionEvent) throws AbortProcessingException {
     FacesUtils.invokeMethodBinding(FacesContext.getCurrentInstance(), methodExpression, actionEvent);
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
