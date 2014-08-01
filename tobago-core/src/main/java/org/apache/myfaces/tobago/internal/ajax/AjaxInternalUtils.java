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


import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIMessages;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public final class AjaxInternalUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxInternalUtils.class);

  public static final String AJAX_COMPONENTS = AjaxUtils.class.getName() + ".AJAX_COMPONENTS";

  private static final String TOBAGO_MESSAGES_CLIENT_IDS = "tobago.messages.clientIds";
  public static final String TOBAGO_PARTIAL_IDS = "tobago::partialIds";

  private AjaxInternalUtils() {
  }

  public static void checkParamValidity(
      final FacesContext facesContext, final UIComponent uiComponent, final Class compClass) {
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

  public static void encodeAjaxComponent(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    if (facesContext == null) {
      throw new NullPointerException("facesContext");
    }
    if (!component.isRendered()) {
      return;
    }
    final RendererBase renderer = ComponentUtils.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer) {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }

  public static void setRenderAllComponents(final FacesContext facesContext) {
    final Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>();
    facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
    final javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
    final UIComponent page = viewRoot.getChildren().get(0);
    ajaxComponents.put(page.getClientId(facesContext), page);
  }

  public static void storeMessagesClientIds(final FacesContext facesContext, final AbstractUIMessages messages) {
    final Map attributes = facesContext.getAttributes();
    final List<String> messageClientIds;
    if (attributes.containsKey(TOBAGO_MESSAGES_CLIENT_IDS)) {
      messageClientIds = (List<String>) attributes.get(TOBAGO_MESSAGES_CLIENT_IDS);
    } else {
      messageClientIds = new ArrayList<String>();
      attributes.put(TOBAGO_MESSAGES_CLIENT_IDS, messageClientIds);
    }
    messageClientIds.add(messages.getClientId(facesContext));
  }

  public static List<String> getMessagesClientIds(final FacesContext facesContext) {
     return (List<String>) facesContext.getAttributes().get(TOBAGO_MESSAGES_CLIENT_IDS);
  }

  public static List<String> getMessagesComponentIds(final FacesContext facesContext) {
    final Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final UIComponent component = facesContext.getViewRoot().getChildren().get(0);
    final String clientId = component.getClientId(facesContext);
    final String ids = (String) parameterMap.get(clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds");
    final List<String> list = new ArrayList<String>();
    if (ids != null) {
      final StringTokenizer tokenizer = new StringTokenizer(ids, ",");
      while (tokenizer.hasMoreTokens()) {
        final String id = tokenizer.nextToken();
        list.add(id);
      }
    }
    return list;

  }

  public static Map<String, UIComponent> parseAndStoreComponents(final FacesContext facesContext) {
    final Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String ajaxComponentIds = (String) parameterMap.get(TOBAGO_PARTIAL_IDS);
    if (ajaxComponentIds != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("ajaxComponentIds = \"" + ajaxComponentIds + "\"");
      }
      final StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      final Map<String, UIComponent> ajaxComponents = new HashMap<String, UIComponent>(tokenizer.countTokens());
      //noinspection unchecked
      facesContext.getExternalContext().getRequestMap().put(AJAX_COMPONENTS, ajaxComponents);
      final javax.faces.component.UIViewRoot viewRoot = facesContext.getViewRoot();
      while (tokenizer.hasMoreTokens()) {
        final String ajaxId = tokenizer.nextToken();
        final UIComponent ajaxComponent = viewRoot.findComponent(ajaxId);
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

  public static Map<String, UIComponent> getAjaxComponents(final FacesContext facesContext) {
    //noinspection unchecked
    return (Map<String, UIComponent>)
        facesContext.getExternalContext().getRequestMap().get(AJAX_COMPONENTS);
  }

  public static void addAjaxComponent(final FacesContext facesContext, final UIComponent component) {
    final Map<String, UIComponent> ajaxComponents =
        (Map<String, UIComponent>) facesContext.getExternalContext().getRequestMap().get(AJAX_COMPONENTS);
    if (ajaxComponents != null) {
      if (!alreadyContained(component, ajaxComponents)) {
        ajaxComponents.put(component.getClientId(facesContext), component);
      }
    }
  }

  public static String encodeJavaScriptString(final String value) {
    String result = StringUtils.replace(value, "\\", "\\\\");
    result = StringUtils.replace(result, "\n", "\\n");
    result = StringUtils.replace(result, "\r", "\\r");
    return StringUtils.replace(result, "\"", "\\\"");
  }

  public static boolean alreadyContained(final UIComponent component, final Map<String, UIComponent> ajaxComponents) {
    for (final UIComponent uiComponent : ajaxComponents.values()) {
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

  public static boolean addNextPossibleAjaxComponent(final FacesContext context, final String componentClientId) {
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

  public static void ensureDecoded(final FacesContext facesContext, final String clientId) {
    ensureDecoded(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void ensureDecoded(final FacesContext facesContext, final UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    final Map<String, UIComponent> ajaxComponents = getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      if (!alreadyContained(component, ajaxComponents)) {
        component.processDecodes(facesContext);
      }
    }
  }

  public static boolean isAjaxRequest(final FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestParameterMap().get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS) != null;
  }

  public static boolean isAjaxRequest(final Map<String, String[]> parameterMap) {
    final String[] ajaxComponentIds = parameterMap.get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    return ajaxComponentIds != null && ajaxComponentIds.length > 0;
  }

  public static boolean redirect(final FacesContext facesContext, final String url) throws IOException {
    if (!isAjaxRequest(facesContext)) {
      return false;
    }
    redirect((HttpServletResponse) facesContext.getExternalContext().getResponse(), url);
    facesContext.responseComplete();
    return true;
  }

  public static void redirect(final HttpServletResponse response, final String url) throws IOException {
    final Writer writer = response.getWriter();
    final String contentType = "application/json; charset=UTF-8";
    ResponseUtils.ensureContentTypeHeader(response, contentType);
    ResponseUtils.ensureNoCacheHeader(response);
    redirectInternal(writer, url);
  }

  private static void redirectInternal(final Writer writer, final String url) throws IOException {
    writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": 302,\n");
    writer.write("  \"location\": \"");
    writer.write(url);
    writer.write("\"\n}\n");
    writer.flush();
  }

  public static void requestNavigationReload(FacesContext facesContext) throws IOException {
    final ExternalContext externalContext = facesContext.getExternalContext();
    final String pathPrefix = externalContext.getRequestContextPath() + externalContext.getRequestServletPath();
    final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
    final String contentType = "application/json; charset=UTF-8";
    ResponseUtils.ensureContentTypeHeader(response, contentType);
    ResponseUtils.ensureNoCacheHeader(response);
    final Writer writer = response.getWriter();
    writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": " + AjaxResponseRenderer.CODE_RELOAD_REQUIRED + ",\n");
    writer.write("  \"location\": \"");
    writer.write(pathPrefix + facesContext.getViewRoot().getViewId());
    writer.write("\"\n}\n");
    writer.flush();
    facesContext.responseComplete();
  }
}
