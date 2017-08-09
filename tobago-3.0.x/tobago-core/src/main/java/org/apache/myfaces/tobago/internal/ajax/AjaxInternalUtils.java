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


import org.apache.myfaces.tobago.internal.util.ResponseUtils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * XXX this must be refactored, because of TOBAGO-1524 (see TOBAGO-1563)
 * @deprecated
 */
@Deprecated
public final class AjaxInternalUtils {

  private AjaxInternalUtils() {
  }

  public static boolean redirect(final FacesContext facesContext, final String url) throws IOException {
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
    writer.write("{\n  \"tobagoAjaxResponse\": true,\n");
    writer.write("  \"responseCode\": " + /*AjaxResponseRenderer.CODE_RELOAD_REQUIRED*/ 309 + ",\n");
    writer.write("  \"location\": \"");
    writer.write(pathPrefix + facesContext.getViewRoot().getViewId());
    writer.write("\"\n}\n");
    writer.flush();
    facesContext.responseComplete();
  }
}
