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

import org.apache.myfaces.tobago.util.FacesELUtils;

import jakarta.el.ELContext;
import jakarta.el.MethodExpression;
import jakarta.faces.component.StateHolder;
import jakarta.faces.context.FacesContext;

public class MethodExpressionTreeExpansionListener implements TreeExpansionListener, StateHolder {

  private MethodExpression methodExpression;

  private boolean isTransient = false;

  public MethodExpressionTreeExpansionListener() {
  }

  public MethodExpressionTreeExpansionListener(final MethodExpression methodExpression) {
    this.methodExpression = methodExpression;
  }

  @Override
  public void treeExpanded(final TreeExpansionEvent event) {
    FacesELUtils.invokeMethodExpression(FacesContext.getCurrentInstance(), methodExpression, event);
  }

  private ELContext elContext() {
    return FacesContext.getCurrentInstance().getELContext();
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    methodExpression = (MethodExpression) state;
  }

  @Override
  public Object saveState(final FacesContext context) {
    return methodExpression;
  }

  @Override
  public void setTransient(final boolean newTransientValue) {
    isTransient = newTransientValue;
  }

  @Override
  public boolean isTransient() {
    return isTransient;
  }
}
