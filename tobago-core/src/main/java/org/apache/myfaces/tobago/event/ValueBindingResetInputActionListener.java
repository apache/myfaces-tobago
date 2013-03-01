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


import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.internal.util.FindComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;


/**
 * @deprecated Since 1.6.0, please use ValueExpressionResetInputActionListener
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

  public ValueBindingResetInputActionListener(Object binding) {
    clientIdsBinding = (ValueBinding) binding;
  }

  public void processAction(ActionEvent event) {
    Object obj = clientIdsBinding.getValue(FacesContext.getCurrentInstance());
    String [] clientIds;
    if (obj instanceof String[]) {
      clientIds = (String[]) obj;
    } else if (obj instanceof String) {
      clientIds= StringUtils.split((String) obj, ", ");
    } else {
      LOG.error("Ignore unknown value of " + obj + " for reset.");
      return;
    }
    for (String clientId : clientIds) {
      UIComponent component = FindComponentUtils.findComponent(event.getComponent(), clientId);
      if (component != null) {
        resetChildren(component);
      }
    }
  }

  public boolean isTransient() {
    return false;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    clientIdsBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, clientIdsBinding);
    return values;
  }

  public void setTransient(boolean newTransientValue) {
    // ignore
  }

}
