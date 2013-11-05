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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AjaxNavigationState {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxNavigationState.class);

  private static final String SESSION_KEY = "tobago-AjaxNavigationState";

  private static final String VIEW_ROOT_KEY = "tobago-AjaxNavigationState-VIEW_ROOT_KEY";

  private UIViewRoot viewRoot;

  private Map<String, List<FacesMessage>> messages;

  private AjaxNavigationState(FacesContext facesContext) {
    ExternalContext externalContext = facesContext.getExternalContext();
    externalContext.getSessionMap().put(SESSION_KEY, this);
    viewRoot = facesContext.getViewRoot();
    messages = new HashMap<String, List<FacesMessage>>();
    Iterator<String> iterator = facesContext.getClientIdsWithMessages();
    while (iterator.hasNext()) {
      addFacesMessages(facesContext, iterator.next());
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace("Saved viewRoot.getViewId() = \"{}\"", viewRoot.getViewId());
      for (Map.Entry<String, List<FacesMessage>> entry : messages.entrySet()) {
        for (FacesMessage message : entry.getValue()) {
          LOG.trace("Saved message \"{}\" : \"{}\"", entry.getKey(), message);
        }
      }
    }
  }

  private void addFacesMessages(FacesContext facesContext, String clientId) {
    Iterator<FacesMessage> facesMessages = facesContext.getMessages(clientId);
    while (facesMessages.hasNext()) {
      addFacesMessage(clientId, facesMessages.next());
    }
  }

  private void addFacesMessage(String clientId, FacesMessage facesMessage) {
    List<FacesMessage> facesMessages = messages.get(clientId);
    if (facesMessages == null) {
      facesMessages = new ArrayList<FacesMessage>();
      messages.put(clientId, facesMessages);
    }
    facesMessages.add(facesMessage);
  }

  private void restoreView(FacesContext facesContext) {
    facesContext.setViewRoot(viewRoot);
    for (Map.Entry<String, List<FacesMessage>> entry : messages.entrySet()) {
      for (FacesMessage facesMessage : entry.getValue()) {
        facesContext.addMessage(entry.getKey(), facesMessage);
      }
    }
    facesContext.renderResponse();
    if (LOG.isTraceEnabled()) {
      LOG.trace("Restored viewRoot.getViewId() = \"{}\"", viewRoot.getViewId());
      for (Map.Entry<String, List<FacesMessage>> entry : messages.entrySet()) {
        for (FacesMessage message : entry.getValue()) {
          LOG.trace("Restored message \"{}\" : \"{}\"", entry.getKey(), message);
        }
      }
    }

  }

  public static void storeIncomingView(FacesContext facesContext) {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (LOG.isTraceEnabled()) {
      if (viewRoot != null) {
        LOG.trace("incoming viewId = '{}'", viewRoot.getViewId());
      } else {
        LOG.trace("incoming viewRoot is null");
      }
    }
    facesContext.getExternalContext().getRequestMap().put(AjaxNavigationState.VIEW_ROOT_KEY, viewRoot);
  }

  public static boolean isNavigation(FacesContext facesContext) {

    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("current viewId = '{}'", viewRoot.getViewId());
      }
    } else {
      LOG.warn("current viewRoot is null");
      return false;
    }

    final ExternalContext externalContext = facesContext.getExternalContext();
    final Map<String, Object> requestMap = externalContext.getRequestMap();
    final UIViewRoot incomingViewRoot = (UIViewRoot) requestMap.get(VIEW_ROOT_KEY);
    if (viewRoot != incomingViewRoot) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("requesting full page reload because of navigation to {} from {}",
            viewRoot.getViewId(), incomingViewRoot.getViewId());
      }
      externalContext.getSessionMap().put(SESSION_KEY, new AjaxNavigationState(facesContext));
      return true;
    }
    return false;
  }

  public static void handleNavigation(FacesContext facesContext) {
    Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
    AjaxNavigationState navigationState = (AjaxNavigationState) sessionMap.remove(AjaxNavigationState.SESSION_KEY);
    if (navigationState != null) {
      navigationState.restoreView(facesContext);
      LOG.trace("force render requested navigation view");
    }
  }
}
