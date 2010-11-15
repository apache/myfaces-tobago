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

import org.apache.myfaces.tobago.internal.util.FindComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

public class ValueBindingPopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(ValueBindingPopupActionListener.class);

  private ValueBinding popupIdBinding;

  public ValueBindingPopupActionListener(Object binding) {
    popupIdBinding = (ValueBinding) binding;
  }

  @Override
  protected UIComponent getPopup(ActionEvent actionEvent) {
    String id = (String) popupIdBinding.getValue(FacesContext.getCurrentInstance());
    UIComponent popup = FindComponentUtils.findComponent(actionEvent.getComponent(), id);
    if (popup == null) {
      LOG.error("Found no popup for \""
          + popupIdBinding.getExpressionString() + "\" := \""
          + id + "\"! Search base componentId : "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
    }
    return popup;
  }

  public boolean isTransient() {
    return false;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    popupIdBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, popupIdBinding);
    return values;
  }


  public void setTransient(boolean newTransientValue) {
    // ignore
  }
}
