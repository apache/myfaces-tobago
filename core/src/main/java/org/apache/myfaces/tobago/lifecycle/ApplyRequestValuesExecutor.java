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

import static javax.faces.event.PhaseId.APPLY_REQUEST_VALUES;

import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.component.UIViewRoot;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.compat.FacesUtils;

import javax.faces.component.ContextCallback;

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

  @SuppressWarnings("UnusedDeclaration")
  private static final Log LOG = LogFactory.getLog(ApplyRequestValuesExecutor.class);

  private ContextCallback contextCallback;


  public ApplyRequestValuesExecutor() {
    contextCallback = new ApplyRequestValuesCallback();
  }

  public boolean execute(FacesContext facesContext) {
    Map<String, UIComponent> ajaxComponents
        = AjaxUtils.parseAndStoreComponents(facesContext);
    if (ajaxComponents != null) {
      // first decode the page
      AbstractUIPage page = ComponentUtil.findPage(facesContext);
      page.decode(facesContext);
      page.markSubmittedForm(facesContext);

      // decode the action if actioncomponent not inside one of the ajaxcomponets
      // otherwise it is decoded there
      decodeActionComponent(facesContext, page, ajaxComponents);

      // and all ajax components
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        FacesUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), entry.getKey(), contextCallback);
      }

      UIViewRoot viewRoot = ((UIViewRoot) facesContext.getViewRoot());
      viewRoot.broadcastEventsForPhase(facesContext, APPLY_REQUEST_VALUES);

    } else {
      facesContext.getViewRoot().processDecodes(facesContext);
    }
    return false;
  }

  private void decodeActionComponent(FacesContext facesContext, AbstractUIPage page, Map<String,
      UIComponent> ajaxComponents) {
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
    FacesUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), actionId, contextCallback);
  }

  public PhaseId getPhase() {
    return APPLY_REQUEST_VALUES;
  }
}
