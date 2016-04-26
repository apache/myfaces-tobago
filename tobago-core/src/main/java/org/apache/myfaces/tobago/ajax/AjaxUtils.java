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

package org.apache.myfaces.tobago.ajax;


import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public final class AjaxUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxUtils.class);

  private AjaxUtils() {
  }

  public static boolean isAjaxRequest(final FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestParameterMap().get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS) != null;
  }

  public static boolean isAjaxRequest(final ServletRequest request) {
    final String[] ajaxComponentIds = request.getParameterMap().get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    return ajaxComponentIds != null && ajaxComponentIds.length > 0;
  }

  public static void removeAjaxComponent(final FacesContext facesContext, final String clientId) {
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.remove(clientId);
    }
  }

  public static void addAjaxComponent(final FacesContext facesContext, final String clientId) {
    addAjaxComponent(facesContext, facesContext.getViewRoot().findComponent(clientId));
  }

  public static void addAjaxComponent(final FacesContext facesContext, final UIComponent component) {
    if (component == null) {
      LOG.warn("Ignore AjaxComponent: null");
      return;
    }
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(facesContext);
    if (ajaxComponents != null) {
      ajaxComponents.put(component.getClientId(facesContext), component);
    }
  }

  public static Set<String> getRequestPartialIds(final FacesContext facesContext) {
    final Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String ajaxComponentIds = (String) parameterMap.get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    if (ajaxComponentIds != null) {
      final StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      final Set<String> ajaxComponents = new HashSet<String>(tokenizer.countTokens());
      while (tokenizer.hasMoreTokens()) {
        final String ajaxId = tokenizer.nextToken();
        ajaxComponents.add(ajaxId);
      }
      return ajaxComponents;
    }
    return Collections.emptySet();
  }

  /**
   * @param facesContext Current instance of the {@link FacesContext}
   * @return true if a UIMessage component has added to renderedPartially
   */
  public static boolean addUIMessagesToRenderedPartially(final FacesContext facesContext) {
    if (!isAjaxRequest(facesContext)) {
      return false;
    }
    final List<String> list = AjaxInternalUtils.getMessagesComponentIds(facesContext);
    final Iterator clientIds = facesContext.getClientIdsWithMessages();
    boolean added = false;

    if (clientIds.hasNext()) { // messages in the partial part
      for (final String componentClientId: list) {
        added = AjaxInternalUtils.addNextPossibleAjaxComponent(facesContext, componentClientId);
      }
    } else {  // checking for an existing shown error on page
      for (final String componentClientId: list) {
        if (facesContext.getExternalContext().getRequestParameterMap().containsKey(
            componentClientId + ComponentUtils.SUB_SEPARATOR + "messagesExists")) {
          added = AjaxInternalUtils.addNextPossibleAjaxComponent(facesContext, componentClientId);
        }
      }
    }
    return added;
  }

  public static boolean redirect(final FacesContext facesContext, final String url) throws IOException {
    return AjaxInternalUtils.redirect(facesContext, url);
  }

  public static void redirect(final HttpServletResponse response, final String url) throws IOException {
    AjaxInternalUtils.redirect(response, url);
  }
}
