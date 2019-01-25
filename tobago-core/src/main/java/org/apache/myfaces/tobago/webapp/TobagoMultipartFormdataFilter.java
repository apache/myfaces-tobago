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

package org.apache.myfaces.tobago.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @deprecated since 2.4.0
 */
@Deprecated
public class TobagoMultipartFormdataFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoMultipartFormdataFilter.class);

  public void init(final FilterConfig filterConfig) throws ServletException {
    LOG.error("This class is no longer needed! Please remove from web.xml. "
        + "See the release notes for Tobago 2.4.0.");
  }

  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    chain.doFilter(request, response);
  }

  public void destroy() {
  }

}
