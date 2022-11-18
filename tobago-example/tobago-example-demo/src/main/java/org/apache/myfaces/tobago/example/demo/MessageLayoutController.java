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

package org.apache.myfaces.tobago.example.demo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class MessageLayoutController implements Serializable {

  private UIComponent component;

  public UIComponent getComponent() {
    return component;
  }

  public void setComponent(final UIComponent component) {
    this.component = component;
  }

  public void addInfo() {
    final FacesMessage message = new FacesMessage(
        FacesMessage.SEVERITY_INFO, "Custom info", "This is an info message on an output field");
    FacesContext.getCurrentInstance().addMessage(component.getClientId(), message);
  }
}
