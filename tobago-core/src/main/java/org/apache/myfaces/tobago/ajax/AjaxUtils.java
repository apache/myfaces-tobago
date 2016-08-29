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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @deprecated use org.apache.myfaces.tobago.util.AjaxUtils
 */
@Deprecated
public final class AjaxUtils {

  private AjaxUtils() {
  }

  public static boolean isAjaxRequest(final FacesContext facesContext) {
    return org.apache.myfaces.tobago.util.AjaxUtils.isAjaxRequest(facesContext);
  }

  public static boolean isAjaxRequest(final ServletRequest request) {
    return org.apache.myfaces.tobago.util.AjaxUtils.isAjaxRequest(request);
  }

  public static void removeAjaxComponent(final FacesContext facesContext, final String clientId) {
    org.apache.myfaces.tobago.util.AjaxUtils.removeRenderIds(facesContext, clientId);
  }

  public static void addAjaxComponent(final FacesContext facesContext, final String clientId) {
    org.apache.myfaces.tobago.util.AjaxUtils.addRenderIds(facesContext, clientId);
  }

  public static void addAjaxComponent(final FacesContext facesContext, final UIComponent component) {
    org.apache.myfaces.tobago.util.AjaxUtils.addRenderIds(facesContext, component.getClientId(facesContext));
  }

  public static Set<String> getRequestPartialIds(final FacesContext facesContext) {
    return org.apache.myfaces.tobago.util.AjaxUtils.getRenderIds(facesContext);
  }

  public static boolean addUIMessagesToRenderedPartially(final FacesContext facesContext) {
    final String message = "org.apache.myfaces.tobago.ajax.AjaxUtils.addUIMessagesToRenderedPartially";
    throw new UnsupportedOperationException(message);
  }

  public static boolean redirect(final FacesContext facesContext, final String url) throws IOException {
    return AjaxInternalUtils.redirect(facesContext, url);
  }

  public static void redirect(final HttpServletResponse response, final String url) throws IOException {
    AjaxInternalUtils.redirect(response, url);
  }
}
