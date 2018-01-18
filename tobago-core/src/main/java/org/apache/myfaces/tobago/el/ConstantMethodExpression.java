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

package org.apache.myfaces.tobago.el;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

public class ConstantMethodExpression extends MethodExpression implements StateHolder {

  private String outcome;

  private boolean transientFlag;

  public ConstantMethodExpression() {
  }

  public ConstantMethodExpression(final String outcome) {
    this.outcome = outcome;
  }

  @Override
  public MethodInfo getMethodInfo(final ELContext context)
      throws NullPointerException, ELException {
    return null;
  }

  @Override
  public Object invoke(final ELContext context, final Object[] params)
      throws NullPointerException, ELException {
    return outcome;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ConstantMethodExpression that = (ConstantMethodExpression) o;

    return !(outcome != null ? !outcome.equals(that.outcome) : that.outcome != null);

  }

  @Override
  public int hashCode() {
    return outcome.hashCode();
  }

  @Override
  public String getExpressionString() {
    return outcome;
  }

  @Override
  public boolean isLiteralText() {
    return true;
  }

  @Override
  public Object saveState(final FacesContext context) {
    return outcome;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    this.outcome = (String) state;
  }

  @Override
  public void setTransient(final boolean transientFlagParameter) {
    this.transientFlag = transientFlag;
  }

  @Override
  public boolean isTransient() {
    return transientFlag;
  }
}
