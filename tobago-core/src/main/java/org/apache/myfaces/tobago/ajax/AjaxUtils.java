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


import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AjaxUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxUtils.class);

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

  /**
   *
   * @param context
   * @return true if a UIMessage component has added to renderedPartially
   */
  public static boolean addUIMessagesToRenderedPartially(FacesContext context) {
    if (!isAjaxRequest(context)) {
      return false;
    }
    List<String> list = AjaxInternalUtils.getMessagesComponentIds(context);
    Iterator clientIds = context.getClientIdsWithMessages();
    boolean added = false;

    if (clientIds.hasNext()) { // messages in the partial part
      for (String componentClientId: list) {
        added = AjaxInternalUtils.addNextPossibleAjaxComponent(context, componentClientId);
      }
    } else {  // checking for an existing shown error on page
      for (String componentClientId: list) {
        if (context.getExternalContext().getRequestParameterMap().containsKey(
            componentClientId + ComponentUtils.SUB_SEPARATOR + "messagesExists")) {
          added = AjaxInternalUtils.addNextPossibleAjaxComponent(context, componentClientId);
        }
      }
    }
    return added;
  }
}
