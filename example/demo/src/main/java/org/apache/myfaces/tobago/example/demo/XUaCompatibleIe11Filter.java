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

package org.apache.myfaces.tobago.example.demo;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a workaround for IE11 with Tobago 1.0.x
 * You need also a CSS in a style.css file:
 * * {
 * box-sizing: border-box;
 * }
 */
public class XUaCompatibleIe11Filter implements Filter {

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    final String userAgent = httpServletRequest.getHeader("User-Agent");
    if (userAgent != null && userAgent.contains("Trident") && userAgent.contains("rv:11")) { // is IE 11
      final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
      httpServletResponse.setHeader("X-UA-Compatible", "IE=10");
      // known problems
      //                 box of input    menu arrow key     access-key
      // EmulateIE11     +               -                  -
      // EmulateIE10     +               -                  +
      // 10              +               -                  +
      // EmulateIE9      -               +                  +
      // 9               +               -                  +
      // EmulateIE8      -               +                  +
      // 8               -               +                  +
      // EmulateIE7      -               +                  +
      // 7               -               +                  +
      // 5               -               +                  +
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void destroy() {
  }
}
