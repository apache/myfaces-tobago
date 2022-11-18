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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import java.lang.invoke.MethodHandles;

public class ValueExpressionResetInputActionListener extends AbstractResetInputActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private ValueExpression clientIdsExpression;

  /**
   * No-arg constructor used during restoreState
   */
  public ValueExpressionResetInputActionListener() {
  }

  public ValueExpressionResetInputActionListener(final ValueExpression clientIdsExpression) {
    this.clientIdsExpression = clientIdsExpression;
  }

  @Override
  public void processAction(final ActionEvent event) {
    final Object obj = clientIdsExpression.getValue(FacesContext.getCurrentInstance().getELContext());
    final String[] clientIds;
    if (obj instanceof String[]) {
      clientIds = (String[]) obj;
    } else if (obj instanceof String) {
      clientIds = StringUtils.split((String) obj, ", ");
    } else {
      LOG.error("Ignore unknown value of " + obj + " for reset.");
      return;
    }
    for (final String clientId : clientIds) {
      final UIComponent component = ComponentUtils.findComponent(event.getComponent(), clientId);
      if (component != null) {
        resetChildren(component);
      }
    }
  }

  @Override
  public boolean isTransient() {
    return false;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    clientIdsExpression = (ValueExpression) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, clientIdsExpression);
    return values;
  }

  @Override
  public void setTransient(final boolean newTransientValue) {
    // ignore
  }

}
