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

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import java.io.IOException;

public class FixCharacterEncodingFilter implements Filter {
  public void init(final FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(final ServletRequest servletRequest,
      final ServletResponse servletResponse, final FilterChain filterChain)
      throws IOException, ServletException {
    if (servletRequest.getCharacterEncoding() == null) {
      servletRequest.setCharacterEncoding("UTF-8");
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void destroy() {
  }
}
