package org.apache.myfaces.tobago.ajax;

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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

public class AjaxUtils {

  private static final Log LOG = LogFactory.getLog(AjaxUtils.class);

  public static boolean isAjaxRequest(FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestMap().containsKey(AjaxInternalUtils.AJAX_COMPONENTS);
  }

  public static void removeAjaxComponent(FacesContext facesContext, String clientId) {
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.remove(clientId);
    }
  }

  public static void addAjaxComponent(FacesContext facesContext, String clientId) {
    addAjaxComponent(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void addAjaxComponent(FacesContext facesContext, UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.put(component.getClientId(facesContext), component);
    }
  }

  public static void ensureDecoded(FacesContext facesContext, String clientId) {
    ensureDecoded(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void ensureDecoded(FacesContext facesContext, UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      for (UIComponent uiComponent : ajaxComponents.values()) {
        // is component or a parent of it in the list?
        UIComponent parent = component;
        while (parent != null) {
          if (component == uiComponent) {
            // nothing to do, because it was already decoded (in the list)
            return;
          }
          parent = parent.getParent();
        }
      }
      component.processDecodes(facesContext);
    }
  }
  
}
