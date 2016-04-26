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

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

public class ResetFormActionListener extends AbstractResetInputActionListener implements Serializable{

  @Override
  public void processAction(final ActionEvent actionEvent) throws AbortProcessingException {
    UIComponent component = actionEvent.getComponent();
    while ((component = component.getParent()) != null) {
      if (component instanceof UIForm) {
        resetChildren(component);
        return;
      }
    }
  }
}
