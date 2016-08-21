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

package org.apache.myfaces.tobago.util;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AjaxUtils {

  public static boolean isAjaxRequest(final FacesContext facesContext) {
    return facesContext.getPartialViewContext().isAjaxRequest();
  }

  public static boolean isAjaxRequest(final ServletRequest request) {
    String requestType = null;
    if (request instanceof HttpServletRequest) {
      final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      requestType = httpServletRequest.getHeader("Faces-Request");
    }
    return "partial/ajax".equalsIgnoreCase(requestType)
        || "true".equalsIgnoreCase(request.getParameter("javax.faces.partial.ajax"));
  }

  public static void addRenderIds(final String... renderIds) {
    addRenderIds(FacesContext.getCurrentInstance(), renderIds);
  }

  public static void addRenderIds(final FacesContext facesContext, final String... renderIds) {
    Collections.addAll(facesContext.getPartialViewContext().getRenderIds(), renderIds);
  }

  public static void removeRenderIds(final String... renderIds) {
    removeRenderIds(FacesContext.getCurrentInstance(), renderIds);
  }

  public static void removeRenderIds(final FacesContext facesContext, final String... renderIds) {
    final Collection<String> collection = facesContext.getPartialViewContext().getRenderIds();
    for (String renderId : renderIds) {
      collection.remove(renderId);
    }
  }

  public static Set<String> getRenderIds(final FacesContext facesContext) {
    return new HashSet<String>(facesContext.getPartialViewContext().getRenderIds());
  }

  public static void navigate(FacesContext facesContext, Object outcome) {
    final Application application = facesContext.getApplication();
    NavigationHandler navigationHandler = application.getNavigationHandler();
    navigationHandler.handleNavigation(facesContext, null, outcome.toString());
    facesContext.renderResponse();
  }
}
