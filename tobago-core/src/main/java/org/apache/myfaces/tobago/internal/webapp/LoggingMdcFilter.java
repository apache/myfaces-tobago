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

package org.apache.myfaces.tobago.internal.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class LoggingMdcFilter implements Filter {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    if (LOG.isInfoEnabled()) {
      LOG.info("init " + getClass().getName());
    }
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {

    try {
      if (request instanceof HttpServletRequest) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpSession session = httpRequest.getSession(false);
        if (session != null) {
          MDC.put("sessionId", session.getId());
        }
        final String remoteAddr = httpRequest.getRemoteAddr();
        if (remoteAddr != null) {
          MDC.put("ip", remoteAddr);
        }
        final String remoteUser = httpRequest.getRemoteUser();
        if (remoteUser != null) {
          MDC.put("user", remoteUser);
        }
      }

      chain.doFilter(request, response);

    } finally {
      MDC.clear();
    }
  }

  @Override
  public void destroy() {

  }
}
