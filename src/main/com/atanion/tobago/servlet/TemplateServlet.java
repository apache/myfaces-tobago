/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 14.06.2004 11:51:12.
 * $Id$
 */
package com.atanion.tobago.servlet;

import com.atanion.tobago.config.Attribute;
import com.atanion.tobago.config.MappingRule;
import com.atanion.tobago.config.TobagoConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

public class TemplateServlet extends HttpServlet {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TemplateServlet.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String viewId = request.getRequestURI().substring(
        request.getContextPath().length());

    if (LOG.isDebugEnabled()) {
      LOG.debug("viewId = '" + viewId + "'");
    }
    String requestUri = remap(request, viewId);
    if (LOG.isDebugEnabled()) {
      LOG.debug("requestUri = '" + requestUri + "'");
    }

    if (requestUri.endsWith(".view")) {
      String error = "cannot find URI in config file: '" + requestUri + "'";
      LOG.error(error);
      throw new ServletException(error);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("requestUri = '" + requestUri + "'");
    }

    try {
      RequestDispatcher dispatcher = request.getRequestDispatcher(requestUri);
      dispatcher.forward(request, response);
    } catch (Exception e) {
      LOG.error("requestUri '" + requestUri + "'", e);
      throw new RuntimeException(e);
    }
  }

  private String remap(ServletRequest request, String requestURI) {
    TobagoConfig config = TobagoConfig.getInstance();
    MappingRule mappingRule = config.getMappingRule(requestURI);
    if (mappingRule == null) {
      return requestURI;
    }
    for (Iterator i = mappingRule.getAttributes().iterator(); i.hasNext();) {
      Attribute attribute = (Attribute) i.next();
      request.setAttribute(attribute.getKey(), attribute.getValue());
    }
    return mappingRule.getForwardUri();
  }


// ///////////////////////////////////////////// bean getter + setter

}
