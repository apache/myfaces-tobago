package org.apache.myfaces.tobago.application;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.application.ViewHandlerImpl;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponse;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Date: Nov 7, 2006
 * Time: 9:09:52 PM
 */
public class FoViewHandlerImpl extends ViewHandlerImpl {

  private static final Logger LOG = LoggerFactory.getLogger(FoViewHandlerImpl.class);

  public FoViewHandlerImpl(ViewHandler base) {
    super(base);
  }

  public void renderView(FacesContext facesContext, UIViewRoot viewRoot)
      throws IOException, FacesException {
    String requestUri = viewRoot.getViewId();

    String contentType = VariableResolverUtils.resolveClientProperties(facesContext).getContentType();
    if (LOG.isDebugEnabled()) {
      LOG.debug("contentType = '" + contentType + "'");
    }
    if (contentType.indexOf("fo") == -1) {
      // standard
      super.renderView(facesContext, viewRoot);
    } else {
      try {
        // TODO PortletResponse ??
        if (facesContext.getExternalContext().getResponse() instanceof TobagoResponse) {
          ((TobagoResponse) facesContext.getExternalContext().getResponse()).setBuffering();
          // own dispatch
          HttpServletRequest request = (HttpServletRequest)
              facesContext.getExternalContext().getRequest();
          HttpServletResponse response = (HttpServletResponse)
              facesContext.getExternalContext().getResponse();
          RequestDispatcher requestDispatcher
              = request.getRequestDispatcher(requestUri);
          requestDispatcher.include(request, response);
          response.setContentType("application/pdf");
          String buffer =
              ((TobagoResponse) facesContext.getExternalContext().getResponse()).getBufferedString();
          ServletResponse servletResponse = (ServletResponse)
              facesContext.getExternalContext().getResponse();
          if (LOG.isDebugEnabled()) {
            LOG.debug("fo buffer: " + buffer);
          }
          FopConverter.fo2Pdf(servletResponse, buffer);
        }
      } catch (ServletException e) {
        IOException ex = new IOException();
        ex.initCause(e);
        throw ex;
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("VIEW");
      LOG.debug(DebugUtils.toString(facesContext.getViewRoot(), 0));
    }
  }

}
