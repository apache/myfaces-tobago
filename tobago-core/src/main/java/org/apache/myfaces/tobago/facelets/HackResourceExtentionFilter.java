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

package org.apache.myfaces.tobago.facelets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

// XXX hack, whould be nice to find a ES6/TypeScript conform way.

@WebFilter(urlPatterns = "/*")
public class HackResourceExtentionFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String prefix;

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    if (LOG.isInfoEnabled()) {
      LOG.info("Filter initialized.");
    }
    final String version = Package.getPackage("org.apache.myfaces.tobago.component").getImplementationVersion();
    final String contextPath = filterConfig.getServletContext().getContextPath();
    prefix = contextPath + "/tobago/standard/" + version + "/js/";
  }

  @Override
  public void doFilter(
      final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
      throws IOException, ServletException {
    if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
      final HttpServletRequest request = (HttpServletRequest) servletRequest;
      final String requestUri = request.getRequestURI();
      if (requestUri.startsWith(prefix) && !requestUri.endsWith(".js") && !requestUri.endsWith(".map")) {
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
          @Override
          public String getRequestURI() {
            return super.getRequestURI() + ".js";
          }
          @Override
          public String getServletPath() {
            return super.getServletPath() + ".js";
          }

          @Override
          public StringBuffer getRequestURL() {
            final StringBuffer buffer = super.getRequestURL();
            buffer.append(".js");
            return buffer;
          }
        };
        filterChain.doFilter(wrapper, servletResponse);
      } else {
        filterChain.doFilter(servletRequest, servletResponse);
      }
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {
  }
}
