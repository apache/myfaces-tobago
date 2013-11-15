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
import org.apache.myfaces.tobago.internal.config.ContentSecurityPolicy;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.portlet.MimeResponse;
import javax.servlet.http.HttpServletResponse;

public final class ResponseUtils {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseUtils.class);

  private ResponseUtils() {
    // utils class
  }

  public static void ensureNoCacheHeader(final FacesContext facesContext) {
    final Object response = facesContext.getExternalContext().getResponse();
    if (response instanceof HttpServletResponse) {
      ensureNoCacheHeader((HttpServletResponse) response);
    } else if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
      ensureNoCacheHeader((MimeResponse) response);
    }
  }

  public static void ensureNoCacheHeader(final HttpServletResponse response) {
    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0,must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setDateHeader("max-age", 0);
  }

  public static void ensureNoCacheHeader(final MimeResponse response) {
    // TODO validate this
    response.getCacheControl().setExpirationTime(0);
  }

  public static void ensureContentTypeHeader(final FacesContext facesContext, final String contentType) {
    final Object response = facesContext.getExternalContext().getResponse();
    if (response instanceof HttpServletResponse) {
      ensureContentTypeHeader((HttpServletResponse) response, contentType);
    } else if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
      ensureContentTypeHeader((MimeResponse) response, contentType);
    }
  }

  public static void ensureContentTypeHeader(final HttpServletResponse response, final String contentType) {
    if (!response.containsHeader("Content-Type")) {
      response.setContentType(contentType);
    } else {
      final String responseContentType = response.getContentType();
      if (!StringUtils.equalsIgnoreCaseAndWhitespace(responseContentType, contentType)) {
        response.setContentType(contentType);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Response already contains Header Content-Type '" + responseContentType
              + "'. Overwriting with '" + contentType + "'");
        }
      }
    }
  }

  public static void ensureContentTypeHeader(final MimeResponse response, final String contentType) {
    final String responseContentType = response.getContentType();
    if (!StringUtils.equalsIgnoreCaseAndWhitespace(responseContentType, contentType)) {
      response.setContentType(contentType);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Response already contains Header Content-Type '" + responseContentType
            + "'. Overwriting with '" + contentType + "'");
      }
    }
  }

  public static void ensureContentSecurityPolicyHeader(
      final FacesContext facesContext, final ContentSecurityPolicy contentSecurityPolicy) {
    final Object response = facesContext.getExternalContext().getResponse();
    if (response instanceof HttpServletResponse) {
      final HttpServletResponse servletResponse = (HttpServletResponse) response;
      final UserAgent userAgent = ClientProperties.getInstance(facesContext).getUserAgent();
      final String[] cspHeaders;
      switch (contentSecurityPolicy.getMode()) {
        case OFF:
          cspHeaders = new String[0];
          break;
        case ON:
          cspHeaders = userAgent.getCspHeaders();
          break;
        case REPORT_ONLY:
          cspHeaders = userAgent.getCspReportOnlyHeaders();
          break;
        default:
          throw new IllegalArgumentException("Undefined mode: " + contentSecurityPolicy.getMode());
      }
      final StringBuilder builder = new StringBuilder();
      for (final String directive : contentSecurityPolicy.getDirectiveList()) {
        builder.append(directive);
        builder.append(";");
      }
      final String value = builder.toString();
      for (final String cspHeader : cspHeaders) {
        servletResponse.setHeader(cspHeader, value);
      }
    } else if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
     // TODO Portlet
      if (contentSecurityPolicy.getMode() != ContentSecurityPolicy.Mode.OFF) {
        LOG.warn("CSP not implemented for Portlet!");
      }
    }
  }
}
