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

package org.apache.myfaces.tobago.internal.ajax;


import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIMessages;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AjaxInternalUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxInternalUtils.class);

  public static final String AJAX_COMPONENTS = AjaxUtils.class.getName() + ".AJAX_COMPONENTS";

  private static final String TOBAGO_MESSAGES_CLIENT_IDS = "tobago.messages.clientIds";
  public static final String TOBAGO_PARTIAL_IDS = "tobago::partialIds";

  public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass) {
    if (facesContext == null) {
      throw new NullPointerException("facesContext may not be null");
    }
    if (uiComponent == null) {
      throw new NullPointerException("uiComponent may not be null");
    }
    //if (compClass != null && !(compClass.isAssignableFrom(uiComponent.getClass())))
    // why isAssignableFrom with additional getClass method call if isInstance does the same?
    if (compClass != null && !(compClass.isInstance(uiComponent))) {
      throw new IllegalArgumentException("uiComponent : "
          + uiComponent.getClass().getName() + " is not instance of "
          + compClass.getName() + " as it should be");
    }
  }

  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return;
    }
    RendererBase renderer = ComponentUtils.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }

  public static void setRenderAllComponents(FacesContext facesContext) {
    Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>();
    facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
    javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
    UIComponent page = (UIComponent) viewRoot.getChildren().get(0);
    ajaxComponents.put(page.getClientId(facesContext), page);
  }

  public static void storeMessagesClientIds(FacesContext facesContext, AbstractUIMessages messages) {
    Map attributes = FacesUtils.getFacesContextAttributes(facesContext);
    List<String> messageClientIds;
    if (attributes.containsKey(TOBAGO_MESSAGES_CLIENT_IDS)) {
      messageClientIds = (List<String>) attributes.get(TOBAGO_MESSAGES_CLIENT_IDS);
    } else {
      messageClientIds = new ArrayList<String>();
      attributes.put(TOBAGO_MESSAGES_CLIENT_IDS, messageClientIds);
    }
    messageClientIds.add(messages.getClientId(facesContext));
  }

  public static List<String> getMessagesClientIds(FacesContext facesContext) {
     return (List<String>) FacesUtils.getFacesContextAttributes(facesContext).get(TOBAGO_MESSAGES_CLIENT_IDS);
  }

  public static List<String> getMessagesComponentIds(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    UIComponent component = (UIComponent) facesContext.getViewRoot().getChildren().get(0);
    String clientId = component.getClientId(facesContext);
    String ids = (String) parameterMap.get(clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds");
    List<String> list = new ArrayList<String>();
    if (ids != null) {
      StringTokenizer tokenizer = new StringTokenizer(ids, ",");
      while (tokenizer.hasMoreTokens()) {
        String id = tokenizer.nextToken();
        list.add(id);
      }
    }
    return list;

  }

  public static Map<String, UIComponent> parseAndStoreComponents(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(TOBAGO_PARTIAL_IDS);
    if (ajaxComponentIds != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("ajaxComponentIds = \"" + ajaxComponentIds + "\"");
      }
      StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>(tokenizer.countTokens());
      //noinspection unchecked
      facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
      javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
      while (tokenizer.hasMoreTokens()) {
        String ajaxId = tokenizer.nextToken();
        UIComponent ajaxComponent = viewRoot.findComponent(ajaxId);
        if (ajaxComponent != null) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("ajaxComponent for \"" + ajaxId + "\" = \"" + ajaxComponent + "\"");
          }
          ajaxComponents.put(ajaxId, ajaxComponent);
        }
      }
      return ajaxComponents;
    }
    return null;
  }

  public static Map<String, UIComponent> getAjaxComponents(FacesContext facesContext) {
    //noinspection unchecked
    return (Map<String, UIComponent>)
        facesContext.getExternalContext().getRequestMap().get(AJAX_COMPONENTS);
  }

  public static void addAjaxComponent(FacesContext facesContext, UIComponent component) {
    Map<String, UIComponent> ajaxComponents =
        (Map<String, UIComponent>) facesContext.getExternalContext().getRequestMap().get(AJAX_COMPONENTS);
    if (ajaxComponents != null) {
      if (!alreadyContained(component, ajaxComponents)) {
        ajaxComponents.put(component.getClientId(facesContext), component);
      }
    }
  }

  public static String encodeJavaScriptString(String value) {
    String result = StringUtils.replace(value, "\\", "\\\\");
    result = StringUtils.replace(result, "\n", "\\n");
    result = StringUtils.replace(result, "\r", "\\r");
    return StringUtils.replace(result, "\"", "\\\"");
  }

  public static boolean alreadyContained(UIComponent component, Map<String, UIComponent> ajaxComponents) {
    for (UIComponent uiComponent : ajaxComponents.values()) {
      // is component or a parent of it in the list?
      UIComponent parent = component;
      while (parent != null) {
        if (component == uiComponent) {
          // nothing to do, because it was already decoded (in the list)
          return true;
        }
        parent = parent.getParent();
      }
    }
    return false;
  }

  public static boolean addNextPossibleAjaxComponent(FacesContext context, String componentClientId) {
    UIComponent component = ComponentUtils.findComponent(context.getViewRoot(), componentClientId);
    component = component.getParent();
    if (component instanceof UIPanel) {
      addAjaxComponent(context, component);
      return true;
    } else {
      LOG.error("Ignore adding ajax component (no instance of UIPanel) id: " + componentClientId + " component: "
          + component);
      return false;
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
    Map<String, UIComponent> ajaxComponents = getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      if (!alreadyContained(component, ajaxComponents)) {
        component.processDecodes(facesContext);
      }
    }
  }
}
