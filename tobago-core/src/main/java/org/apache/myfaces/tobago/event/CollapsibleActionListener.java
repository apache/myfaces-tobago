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

import org.apache.myfaces.tobago.internal.component.AbstractUICollapsiblePanel;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;

import java.lang.invoke.MethodHandles;

public class CollapsibleActionListener implements ActionListener, StateHolder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String forId;

  private boolean transientFlag;

  public CollapsibleActionListener() {
    // for state holder
  }

  public CollapsibleActionListener(final String forId) {
    this.forId = forId;
  }

  @Override
  public void processAction(final ActionEvent actionEvent) throws AbortProcessingException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      final String forClientId = ComponentUtils.evaluateClientId(facesContext, actionEvent.getComponent(), forId);

      final UIComponent component = viewRoot.findComponent(forClientId);
      if (component instanceof AbstractUICollapsiblePanel) {
        ((AbstractUICollapsiblePanel) component).processState();
      } else {
        LOG.error("Wrong component class for id: '{}'. Type is {} but expected type is {}",
            forClientId, component.getClass().getName(), AbstractUICollapsiblePanel.class.getName());
      }
    }
  }

  @Override
  public Object saveState(final FacesContext context) {
    return forId;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    this.forId = (String) state;
  }

  @Override
  public boolean isTransient() {
    return transientFlag;
  }

  @Override
  public void setTransient(final boolean newTransientValue) {
    this.transientFlag = newTransientValue;
  }
}
