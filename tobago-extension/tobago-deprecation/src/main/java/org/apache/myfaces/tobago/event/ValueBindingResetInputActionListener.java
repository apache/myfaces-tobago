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


import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;


/**
 * @deprecated Since 2.0.0, please use ValueExpressionResetInputActionListener
 */
@Deprecated
public class ValueBindingResetInputActionListener extends AbstractResetInputActionListener implements StateHolder {
  private static final Logger LOG = LoggerFactory.getLogger(ValueBindingResetInputActionListener.class);

  private ValueBinding clientIdsBinding;

  /**
   * No-arg constructor used during restoreState
   */
  public ValueBindingResetInputActionListener() {
  }

  public ValueBindingResetInputActionListener(final Object binding) {
    clientIdsBinding = (ValueBinding) binding;
  }

  public void processAction(final ActionEvent event) {
    final Object obj = clientIdsBinding.getValue(FacesContext.getCurrentInstance());
    final String[] clientIds;
    if (obj instanceof String[]) {
      clientIds = (String[]) obj;
    } else if (obj instanceof String) {
      clientIds= StringUtils.split((String) obj, ", ");
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

  public boolean isTransient() {
    return false;
  }

  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    clientIdsBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, clientIdsBinding);
    return values;
  }

  public void setTransient(final boolean newTransientValue) {
    // ignore
  }

}
