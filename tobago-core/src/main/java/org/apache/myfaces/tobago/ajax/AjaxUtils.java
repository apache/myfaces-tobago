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
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class AjaxUtils {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxUtils.class);

  private AjaxUtils() {
  }

  public static boolean isAjaxRequest(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    return ajaxComponentIds != null;
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

  public static Set<String> getRequestPartialIds(FacesContext facesContext) {
    Map parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String ajaxComponentIds = (String) parameterMap.get(AjaxInternalUtils.TOBAGO_PARTIAL_IDS);
    if (ajaxComponentIds != null) {
      StringTokenizer tokenizer = new StringTokenizer(ajaxComponentIds, ",");
      Set<String> ajaxComponents = new HashSet<String>(tokenizer.countTokens());
      while (tokenizer.hasMoreTokens()) {
        String ajaxId = tokenizer.nextToken();
        ajaxComponents.add(ajaxId);
      }
      return ajaxComponents;
    }
    return Collections.emptySet();
  }

  /**
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

  /**
   * @deprecated since 2.0.0. Is no longer needed
   */
  public static boolean redirect(FacesContext facesContext, String url) throws IOException {
    if (!isAjaxRequest(facesContext)) {
      return false;
    }
    HttpServletResponse httpServletResponse
          = (HttpServletResponse) facesContext.getExternalContext().getResponse();
    Writer writer = httpServletResponse.getWriter();
    String contentType = "application/json; charset=UTF-8";
    ResponseUtils.ensureContentTypeHeader(facesContext, contentType);
    ResponseUtils.ensureNoCacheHeader(facesContext);
    redirectInternal(writer, url);
    writer.close();
    facesContext.responseComplete();
    return true;
  }

  /**
   * @deprecated since 2.0.0. Is no longer needed
   */
  private static void redirectInternal(Writer writer, String url) throws IOException {
    writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": 302,\n");
    writer.write("  \"location\": \"");
    writer.write(url);
    writer.write("\"\n}\n");
    writer.flush();
  }

  /**
   * @deprecated since 2.0.0. Is no longer needed
   */
  public static void redirect(HttpServletResponse response, String url) throws IOException {
    PrintWriter writer = response.getWriter();
    String contentType = "application/json; charset=UTF-8";
    ResponseUtils.ensureContentTypeHeader(response, contentType);
    ResponseUtils.ensureNoCacheHeader(response);
    redirectInternal(writer, url);
  }
}
