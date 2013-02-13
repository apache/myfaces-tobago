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

import org.apache.myfaces.tobago.internal.util.FindComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


public class ValueExpressionPopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(ValueExpressionPopupActionListener.class);

  private ValueExpression popupIdExpression;

  public ValueExpressionPopupActionListener(ValueExpression expression) {
    popupIdExpression = expression;
  }

  /**
   * @deprecated Since 1.6.0, please use the other constructor with explicit type
   */
  @Deprecated
  public ValueExpressionPopupActionListener(Object expression) {
    popupIdExpression = (ValueExpression) expression;
  }

  @Override
  protected UIComponent getPopup(ActionEvent actionEvent) {
    String id = (String) popupIdExpression.getValue(FacesContext.getCurrentInstance().getELContext());
    UIComponent popup = FindComponentUtils.findComponent(actionEvent.getComponent(), id);
    if (popup == null) {
      LOG.error("Found no popup for \""
          + popupIdExpression.getExpressionString() + "\" := \""
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
    popupIdExpression = (ValueExpression) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, popupIdExpression);
    return values;
  }


  public void setTransient(boolean newTransientValue) {
    // ignore
  }
}
