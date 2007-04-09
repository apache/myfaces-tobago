package org.apache.myfaces.tobago.lifecycle;

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

import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.component.UIComponent;
import java.util.ArrayList;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 *
 * Apply request values phase (JSF Spec 2.2.2)
 */
class ApplyRequestValuesExecutor implements PhaseExecutor {
  public boolean execute(FacesContext facesContext) {
    ArrayList<UIComponent> ajaxComponents
        = AjaxUtils.parseAndStoreComponents(facesContext);
    if (ajaxComponents != null) {
      // first decode the page
      UIPage page = ComponentUtil.findPage(facesContext);
      page.decode(facesContext);
      page.markSubmittedForm(facesContext);

      // decode the action if actioncomponent not inside one of the ajaxcomponets
      // otherwise it is decoded there
      String actionId = page.getActionId();
      UIComponent actionComponent = null;
      if (actionId != null) {
        actionComponent = page.findComponent(actionId);
      }

      if (actionComponent != null) {
        boolean decodeNeeded = true;

        for (UIComponent ajaxComponent : ajaxComponents) {
          UIComponent tester = actionComponent;
          while (tester != null) {
            if (tester == ajaxComponent) {
              decodeNeeded = false;
              break;
            } else {
              tester = tester.getParent();
            }
          }
          if (!decodeNeeded) {
            break;
          }
        }        
        if (decodeNeeded) {
          actionComponent.processDecodes(facesContext);
        }
      }

      // and all ajax components
      for (UIComponent ajaxComponent : ajaxComponents) {
        ajaxComponent.processDecodes(facesContext);
      }

    } else {
      facesContext.getViewRoot().processDecodes(facesContext);
    }
    return false;
  }

  public PhaseId getPhase() {
    return PhaseId.APPLY_REQUEST_VALUES;
  }
}
