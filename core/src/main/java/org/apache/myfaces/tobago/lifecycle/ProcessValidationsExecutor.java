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

package org.apache.myfaces.tobago.lifecycle;

import static javax.faces.event.PhaseId.PROCESS_VALIDATIONS;

import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.UIViewRoot;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.util.Callback;
import org.apache.myfaces.tobago.util.ProcessValidationsCallback;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.Map;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * Process validations phase (JSF Spec 2.2.3)
 */
class ProcessValidationsExecutor implements PhaseExecutor {

  private Callback callback;

  public ProcessValidationsExecutor() {
    this.callback = new ProcessValidationsCallback();
  }

  public boolean execute(FacesContext facesContext) {
    Map<String, UIComponent> ajaxComponents = AjaxUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        UIComponent ajaxComponent = entry.getValue();
        // TODO: invokeOnComponent()
        ComponentUtil.invokeOnComponent(facesContext, entry.getKey(), ajaxComponent, callback);
//        ajaxComponent.processValidators(facesContext);
      }
      UIViewRoot viewRoot = ((UIViewRoot) facesContext.getViewRoot());
      viewRoot.broadcastEventsForPhase(facesContext, PROCESS_VALIDATIONS);
    } else {
      facesContext.getViewRoot().processValidators(facesContext);
    }
    return false;
  }

  public PhaseId getPhase() {
    return PROCESS_VALIDATIONS;
  }
}
