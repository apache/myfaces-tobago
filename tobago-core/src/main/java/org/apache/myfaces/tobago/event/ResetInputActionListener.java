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

import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Collection;

public class ResetInputActionListener extends AbstractResetInputActionListener implements StateHolder {

  private String[] clientIds;

  public ResetInputActionListener() {
  }

  public ResetInputActionListener(final String[] clientIds) {
    this.clientIds = clientIds;
  }

  public ResetInputActionListener(final Collection<String> clientIds) {
     this.clientIds = clientIds.toArray(new String[clientIds.size()]);
  }

  @Override
  public void processAction(final ActionEvent event) {
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
  public void setTransient(final boolean newTransientValue) {
    // ignore
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    clientIds = (String[]) values[0];
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[1];
    values[0] = clientIds;
    return values;
  }
}
