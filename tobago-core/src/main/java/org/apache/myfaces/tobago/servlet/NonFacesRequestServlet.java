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

import jakarta.faces.FactoryFinder;
import jakarta.faces.application.Application;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.FacesContextFactory;
import jakarta.faces.lifecycle.Lifecycle;
import jakarta.faces.lifecycle.LifecycleFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.lang.invoke.MethodHandles;

/**
 * @deprecated Please use &lt;f:initParam&gt; instead - see also TOBAGO-1456
 */
@Deprecated(since = "2.0.8", forRemoval = true)
public abstract class NonFacesRequestServlet extends HttpServlet {

  @Serial
  private static final long serialVersionUID = -7448621953821447997L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {

    final LifecycleFactory lFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    final Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    final FacesContextFactory fcFactory
        = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    final FacesContext facesContext = fcFactory.getFacesContext(getServletContext(), request, response, lifecycle);
    try {

      // invoke application
      final String outcome = invokeApplication(facesContext);

      if (facesContext.getResponseComplete()) {
        return;
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("outcome = '" + outcome + "'");
      }

      final Application application = facesContext.getApplication();
      if (facesContext.getViewRoot() == null) {
        facesContext.setViewRoot(createViewRoot(facesContext));
      }

      final NavigationHandler navigationHandler = application.getNavigationHandler();
      navigationHandler.handleNavigation(facesContext, null, outcome);

      lifecycle.render(facesContext);
    } finally {
      facesContext.release();
    }
  }

  protected UIViewRoot createViewRoot(final FacesContext facesContext) {
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
