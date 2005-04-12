/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 12.04.2005 17:59:24.
 * $Id$
 */
package com.atanion.tobago.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.io.IOException;
import java.util.Map;

public abstract class NonFacesRequestServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(NonFacesRequestServlet.class);

  protected void service(HttpServletRequest request,
      HttpServletResponse response) throws ServletException,
      IOException {

    LifecycleFactory lFactory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle =
        lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    FacesContextFactory fcFactory = (FacesContextFactory)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext =
        fcFactory.getFacesContext(getServletContext(), request, response,
            lifecycle);
    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    String viewId = getFromViewId();
    UIViewRoot view = viewHandler.createView(facesContext, viewId);
    facesContext.setViewRoot(view);

    // invoke application
    String outcome = invokeApplication(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("outcome = '" + outcome + "'");
    }
    NavigationHandler navigationHandler = application.getNavigationHandler();
    navigationHandler.handleNavigation(facesContext, null, outcome);
    lifecycle.render(facesContext);
  }

  public abstract String invokeApplication(FacesContext facesContext);

  /** will be called to initilize the first ViewRoot,
   * may be overwritten by extended classes */
  public String getFromViewId() {
    return "";
  }
}
