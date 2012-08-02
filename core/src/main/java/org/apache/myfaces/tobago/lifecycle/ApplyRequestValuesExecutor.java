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

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;

import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIViewRoot;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.Callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.component.UIComponent;
import java.util.Map;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * <p/>
 * Apply request values phase (JSF Spec 2.2.2)
 */
class ApplyRequestValuesExecutor implements PhaseExecutor {

  @SuppressWarnings({"UnusedDeclaration"})
  private static final Log LOG = LogFactory.getLog(ApplyRequestValuesExecutor.class);

  private Callback callback;


  public ApplyRequestValuesExecutor() {
    callback = new ApplyRequestValuesCallback();
  }

  public boolean execute(FacesContext facesContext) {
    Map<String, UIComponent> ajaxComponents
        = AjaxUtils.parseAndStoreComponents(facesContext);
    if (ajaxComponents != null) {
      // first decode the page
      UIPage page = ComponentUtil.findPage(facesContext);
      page.decode(facesContext);
      page.markSubmittedForm(facesContext);

      // decode the action if actioncomponent not inside one of the ajaxcomponets
      // otherwise it is decoded there
      decodeActionComponent(facesContext, page, ajaxComponents);

      // and all ajax components
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        UIComponent ajaxComponent = entry.getValue();
        // TODO: invokeOnComponent()
        ComponentUtil.invokeOnComponent(facesContext, entry.getKey(), ajaxComponent, callback);
      }

      UIViewRoot viewRoot = ((UIViewRoot) facesContext.getViewRoot());
      viewRoot.broadcastEventsForPhase(facesContext, APPLY_REQUEST_VALUES);

    } else {
      facesContext.getViewRoot().processDecodes(facesContext);
    }
    return false;
  }

  private void decodeActionComponent(FacesContext facesContext, UIPage page, Map<String, UIComponent> ajaxComponents) {
    String actionId = page.getActionId();
    UIComponent actionComponent = null;
    if (actionId != null) {
      actionComponent = facesContext.getViewRoot().findComponent(actionId);
    }
    if (actionComponent == null) {
      return;
    }
    for (UIComponent ajaxComponent : ajaxComponents.values()) {
      UIComponent component = actionComponent;
      while (component != null) {
        if (component == ajaxComponent) {
          return;
        }
        component = component.getParent();
      }
    }
    // TODO: invokeOnComponent()
    ComponentUtil.invokeOnComponent(facesContext, actionId, actionComponent, callback);
  }

  public PhaseId getPhase() {
    return APPLY_REQUEST_VALUES;
  }
}
