/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 13.07.2004 12:34:10.
 * $Id$
 */
package com.atanion.tobago.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.atanion.tobago.config.TobagoConfig;

/**
 * Workaround: Weblogic 8.1 calls the ContextListeners after calling
 * Servlet.init() but, JSF assume it does it before.
 * Maybe weblogic doesn't call ContextListeners from *.jar!
 * http://forum.java.sun.com/thread.jsp?forum=427&thread=499690
 */

public class WeblogicWorkaroundServlet extends HttpServlet {

// ///////////////////////////////////////////// constant

  private static final Log LOG
      = LogFactory.getLog(WeblogicWorkaroundServlet.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void init() throws ServletException {

    LOG.debug("1st");
    LifecycleFactory factory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    if (factory == null) { // Faces ConfigureListener is not called until now!
      final String className = "com.sun.faces.config.ConfigureListener";
      LOG.debug("Init of " + className + " by servlet!");
      callInit(className);
    }

    LOG.debug("2nd");
    FacesContext facesContext = FacesContext.getCurrentInstance();
    TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    if (tobagoConfig == null) { // Tobago ConfigureListener is not called until now!
      final String className = "com.atanion.tobago.webapp.TobagoServletContextListener";
      LOG.debug("Init of " + className + " by servlet!");
      callInit(className);
    }

    LOG.debug("3rd");
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

// ///////////////////////////////////////////// bean getter + setter

}
