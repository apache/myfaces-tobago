/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 14.06.2004 11:51:12.
 * $Id$
 */
package org.apache.myfaces.tobago.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.Attribute;
import org.apache.myfaces.tobago.config.MappingRule;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
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

    if (requestUri.endsWith(".view")) { // todo: make .view configurable
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
    } catch (IOException e) {
      LOG.error("requestUri '" + requestUri + "' "
          + "viewId '" + viewId + "' ", e);
      throw e;
    } catch (ServletException e) {
      LOG.error("requestUri '" + requestUri + "' "
          + "viewId '" + viewId + "' ", e);
      throw e;
    }
  }

  private String remap(ServletRequest request, String requestURI) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    TobagoConfig config = TobagoConfig.getInstance(facesContext);
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
