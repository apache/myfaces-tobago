package org.apache.myfaces.tobago.webapp;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TobagoMultipartFormdataFilter implements Filter {

  private static final Log LOG
      = LogFactory.getLog(TobagoMultipartFormdataFilter.class);

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
//    Log.debug("filter filter ....");

    ServletRequest wrapper;
    if (request instanceof HttpServletRequest) {
      if (request instanceof TobagoMultipartFormdataRequest) {
        wrapper = request;
      } else {
        String contentType = request.getContentType();
        if (contentType != null
            && contentType.toLowerCase().startsWith("multipart/form-data")) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Wrapping " + request.getClass().getName()
                + " with ContentType=\"" + contentType + "\" "
                + "into TobagoMultipartFormdataRequest");
          }
          wrapper = new TobagoMultipartFormdataRequest(
              (HttpServletRequest) request);
        } else {
          wrapper = request;
        }
      }
    } else {
      LOG.error("Not implemented for non HttpServletRequest");
      wrapper = request;
    }

    TobagoResponse wrappedResponse = new TobagoResponse((HttpServletResponse) response);
    chain.doFilter(wrapper, wrappedResponse);
  }

  public void destroy() {
  }



}
