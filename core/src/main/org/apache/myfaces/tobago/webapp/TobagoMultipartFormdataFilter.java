/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.01.2003 17:00:56.
 * $Id$
 */
package org.apache.myfaces.tobago.webapp;

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

// ///////////////////////////////////////////// constant

  private static final Log LOG
      = LogFactory.getLog(TobagoMultipartFormdataFilter.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

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
        if (contentType != null &&
            contentType.toLowerCase().startsWith("multipart/form-data")) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Wrapping " + request.getClass().getName()
                + " with ContentType=\"" + contentType + "\" " +
                "into TobagoMultipartFormdataRequest");
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
