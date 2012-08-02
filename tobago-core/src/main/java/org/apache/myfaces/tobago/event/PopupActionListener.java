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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/*
 * Date: Dec 23, 2006
 * Time: 10:59:53 AM
 */
public class PopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(PopupActionListener.class);

  private String popupId;

  public PopupActionListener() {
  }

  public PopupActionListener(String popupId) {
    this.popupId = popupId;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Add ActionListener: {}", popupId);
    }
  }

  @Override
  protected AbstractUIPopup getPopup(ActionEvent actionEvent) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    AbstractUIPopup popup = (AbstractUIPopup) ComponentUtils.findComponent(actionEvent.getComponent(), popupId);
    if (popup == null) {
      LOG.error("Found no popup for \"{}\"! Search base componentId : {}"
          + popupId, actionEvent.getComponent().getClientId(facesContext));
    }
    return popup;
  }

  public boolean isTransient() {
    return false;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    popupId = (String) values[0];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[1];
    values[0] = popupId;
    return values;
  }

  public void setTransient(boolean newTransientValue) {
    // ignore
  }
}
