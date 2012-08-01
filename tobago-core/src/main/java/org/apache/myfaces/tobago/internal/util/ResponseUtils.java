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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResponseUtils {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseUtils.class);

  private ResponseUtils() {
    // utils class
  }

  public static void ensureNoCacheHeader(FacesContext facesContext) {
    // TODO PortletRequest
    ExternalContext externalContext = facesContext.getExternalContext();
    if (externalContext.getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
      ensureNoCacheHeader(response);
    }
  }

  public static void ensureNoCacheHeader(HttpServletResponse response) {
    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0,must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setDateHeader("max-age", 0);
  }

  public static void ensureContentTypeHeader(FacesContext facesContext, String contentType) {
    // TODO PortletRequest
    if (facesContext.getExternalContext().getResponse() instanceof HttpServletResponse) {
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      ensureContentTypeHeader(response, contentType);
    }
  }

  public static void ensureContentTypeHeader(HttpServletResponse response, String contentType) {
    if (!response.containsHeader("Content-Type")) {
      response.setContentType(contentType);
    } else {
      try {
        String responseContentType = response.getContentType();
        if (!responseContentType.equalsIgnoreCase(contentType)) {
          response.setContentType(contentType);
          if (LOG.isInfoEnabled()) {
            LOG.info("Reponse already contains Header Content-Type '" + responseContentType
                + "'. Setting Content-Type to '" + contentType + "'");
          }
        }
      } catch (Error e) {
        LOG.warn("The method ServletResponse.getContentType() is not available before Servlet 2.4");
      }
    }
  }

  public static void ensureContentSecurityPolicyHeader(FacesContext facesContext, List<String> contentSecurityPolicy) {
    // TODO PortletRequest
    if (facesContext.getExternalContext().getResponse() instanceof HttpServletResponse) {
      final UserAgent userAgent = ClientProperties.getInstance(facesContext).getUserAgent();
      final String cspHeader = userAgent.getCspHeader();
      if (cspHeader != null) {
        final StringBuilder value = new StringBuilder();
        for (String directive : contentSecurityPolicy) {
          value.append(directive);
          value.append(";");
        }
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setHeader(cspHeader, value.toString());
      }
    }
  }
}
