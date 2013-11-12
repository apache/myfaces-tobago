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

import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.portlet.ClientDataRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public final class RequestUtils {

  private static final Logger LOG = LoggerFactory.getLogger(RequestUtils.class);

  private RequestUtils() {
  }

  public static void ensureEncoding(final FacesContext facesContext) {
    final Object request = facesContext.getExternalContext().getRequest();
    try {
      if (request instanceof HttpServletRequest) {
        final HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getCharacterEncoding() == null) {
          servletRequest.setCharacterEncoding("UTF-8");
        }
      } else if (PortletUtils.isPortletApiAvailable() && request instanceof ClientDataRequest) {
        final ClientDataRequest portletRequest = (ClientDataRequest) request;
        if (portletRequest.getCharacterEncoding() == null) {
          portletRequest.setCharacterEncoding("UTF-8");
        }
      }
    } catch (final UnsupportedEncodingException e) {
      LOG.error("" + e, e);
    }
  }
}
