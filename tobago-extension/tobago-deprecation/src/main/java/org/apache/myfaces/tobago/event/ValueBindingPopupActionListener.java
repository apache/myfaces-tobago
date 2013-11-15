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

import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
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
 * @deprecated Since 2.0.0, please use ValueExpressionPopupActionListener
 */
@Deprecated
public class ValueBindingPopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(ValueBindingPopupActionListener.class);

  private ValueBinding popupIdBinding;

  /**
   * No-arg constructor used during restoreState
   */
  public ValueBindingPopupActionListener() {
  }

  public ValueBindingPopupActionListener(final Object binding) {
    popupIdBinding = (ValueBinding) binding;
  }

  @Override
  protected AbstractUIPopup getPopup(final ActionEvent actionEvent) {
    final String id = (String) popupIdBinding.getValue(FacesContext.getCurrentInstance());
    final UIComponent popup = FindComponentUtils.findComponent(actionEvent.getComponent(), id);
    if (popup instanceof AbstractUIPopup) {
      return (AbstractUIPopup) popup;
    } else {
      LOG.error("Found no popup for \""
          + popupIdBinding.getExpressionString() + "\" := \""
          + id + "\"! Search base componentId : "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
      return null;
    }
  }

  public boolean isTransient() {
    return false;
  }

  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    popupIdBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, popupIdBinding);
    return values;
  }


  public void setTransient(final boolean newTransientValue) {
    // ignore
  }
}
