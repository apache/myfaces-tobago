package org.apache.myfaces.tobago.internal.lifecycle;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.UIViewRoot;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.Map;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * <p/>
 * Apply request values phase (JSF Spec 2.2.2)
 */
class ApplyRequestValuesExecutor implements PhaseExecutor {

  @SuppressWarnings("UnusedDeclaration")
  private static final Logger LOG = LoggerFactory.getLogger(ApplyRequestValuesExecutor.class);

  private ContextCallback contextCallback;

  public ApplyRequestValuesExecutor() {
    contextCallback = new ApplyRequestValuesCallback();
  }

  public boolean execute(FacesContext facesContext) {
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.parseAndStoreComponents(facesContext);
    if (ajaxComponents != null) {
      // first decode the page
      AbstractUIPage page = ComponentUtils.findPage(facesContext);
      page.decode(facesContext);
      page.markSubmittedForm(facesContext);
      if (facesContext instanceof TobagoFacesContext) {
        ((TobagoFacesContext) facesContext).setAjax(true);
      }
      // decode the action if action component not inside one of the ajax components
      // otherwise it is decoded there
      decodeActionComponent(facesContext, page, ajaxComponents);

      // and all ajax components
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        if (facesContext instanceof TobagoFacesContext) {
          ((TobagoFacesContext) facesContext).setAjaxComponentId(entry.getKey());
        }
        FacesUtils.invokeOnComponent(facesContext, facesContext.getViewRoot(), entry.getKey(), contextCallback);
      }

      UIViewRoot viewRoot = ((UIViewRoot) facesContext.getViewRoot());
      viewRoot.broadcastEventsForPhase(facesContext, PhaseId.APPLY_REQUEST_VALUES);

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
    return PhaseId.APPLY_REQUEST_VALUES;
  }
}
