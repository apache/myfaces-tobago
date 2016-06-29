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

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class CollapsibleActionListener implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(CollapsibleActionListener.class);

  private UIComponent component;
  private String forId;

  public CollapsibleActionListener(final UIComponent component, final String forId) {
    this.component = component;
    this.forId = forId;
  }

  @Override
  public void processAction(final ActionEvent actionEvent) throws AbortProcessingException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      final String forClientId = ComponentUtils.evaluateClientId(facesContext, component, forId);

      final UIComponent component = viewRoot.findComponent(forClientId);
      if (component instanceof AbstractUICollapsiblePanel) {
        ((AbstractUICollapsiblePanel) component).processState();
      } else {
        LOG.error("Wrong component class for id: '{}'. Type is {} but expected type is {}",
            forClientId, component.getClass().getName(), AbstractUICollapsiblePanel.class.getName());
      }
    }
  }
}
