/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 13.07.2004 12:34:10.
 * $Id$
 */
package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Workaround: Weblogic 8.1 calls the ContextListeners after calling
 * Servlet.init() but, JSF assume it does it before.
 * Maybe weblogic doesn't call ContextListeners from *.jar!
 * http://forum.java.sun.com/thread.jsp?forum=427&thread=499690
 */

public class WeblogicWorkaroundServlet extends HttpServlet {
// ------------------------------------------------------------------ constants

  private static final Log LOG
      = LogFactory.getLog(WeblogicWorkaroundServlet.class);

// ----------------------------------------------------------- business methods

  public void init() throws ServletException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("1st");
    }
    LifecycleFactory factory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    if (factory == null) { // Faces ConfigureListener is not called until now!
      final String className = "com.sun.faces.config.ConfigureListener";
      if (LOG.isDebugEnabled()) {
        LOG.debug("Init of " + className + " by servlet!");
      }
      callInit(className);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("2nd");
    }
    TobagoConfig tobagoConfig = (TobagoConfig)
        getServletContext().getAttribute(TobagoConfig.TOBAGO_CONFIG);

    if (tobagoConfig == null) { // TobagoServletContextListener is not called until now!
      final String className = TobagoServletContextListener.class.getName();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Init of " + className + " by servlet!");
      }
      callInit(className);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("3rd");
    }
  }

  private void callInit(String className) {
    try {
      Class aClass = Class.forName(className);
      ServletContextListener listener = (ServletContextListener)
          aClass.newInstance();
      listener.contextInitialized(
          new ServletContextEvent(getServletContext()));
    } catch (Exception e) {
      LOG.error("", e);
    }
  }
}

