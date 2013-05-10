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

package org.apache.myfaces.tobago.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class NonFacesRequestServlet extends HttpServlet {

  private static final long serialVersionUID = -7448621953821447997L;

  private static final Logger LOG = LoggerFactory.getLogger(NonFacesRequestServlet.class);

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LifecycleFactory lFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    FacesContextFactory fcFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = fcFactory.getFacesContext(getServletContext(), request, response, lifecycle);
    try {

      // invoke application
      String outcome = invokeApplication(facesContext);

      if (facesContext.getResponseComplete()) {
        return;
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("outcome = '" + outcome + "'");
      }

      Application application = facesContext.getApplication();
      if (facesContext.getViewRoot() == null) {
        facesContext.setViewRoot(createViewRoot(facesContext));
      }

      NavigationHandler navigationHandler = application.getNavigationHandler();
      navigationHandler.handleNavigation(facesContext, null, outcome);


      lifecycle.render(facesContext);
    } finally {
      facesContext.release();
    }
  }

  protected UIViewRoot createViewRoot(FacesContext facesContext) {
    return facesContext.getApplication().getViewHandler().createView(facesContext, getFromViewId());
  }

  public abstract String invokeApplication(FacesContext facesContext);

  /**
   * will be called to initialize the first ViewRoot,
   * may be overwritten by extended classes
   */
  public String getFromViewId() {
    return "";
  }
}
