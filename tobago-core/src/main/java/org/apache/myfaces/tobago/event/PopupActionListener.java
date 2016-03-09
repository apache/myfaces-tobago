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

public class PopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(PopupActionListener.class);

  private String popupId;

  public PopupActionListener() {
  }

  public PopupActionListener(final String popupId) {
    this.popupId = popupId;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Add ActionListener: {}", popupId);
    }
  }

  @Override
  protected AbstractUIPopup getPopup(final ActionEvent actionEvent) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final AbstractUIPopup popup = (AbstractUIPopup) ComponentUtils.findComponent(actionEvent.getComponent(), popupId);
    if (popup == null) {
      LOG.error("Found no popup for \"{}\"! Search base componentId : {}"
          + popupId, actionEvent.getComponent().getClientId(facesContext));
    }
    return popup;
  }

  @Override
  public boolean isTransient() {
    return false;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    popupId = (String) values[0];
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[1];
    values[0] = popupId;
    return values;
  }

  @Override
  public void setTransient(final boolean newTransientValue) {
    // ignore
  }
}
