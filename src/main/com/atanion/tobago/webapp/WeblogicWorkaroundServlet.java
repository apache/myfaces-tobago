/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 13.07.2004 12:34:10.
 * $Id$
 */
package com.atanion.tobago.webapp;

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

// ///////////////////////////////////////////// constant

  private static final Log LOG
      = LogFactory.getLog(WeblogicWorkaroundServlet.class);

  public static final String[] CONFIGURE_LISTENER_CLASS_NAMES = {
      "com.sun.faces.config.ConfigureListener",
      "com.atanion.tobago.webapp.TobagoServletContextListener"
  };

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void init() throws ServletException {

    LifecycleFactory factory = (LifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    if (factory != null) {
      return;
    }

    try {
      for (int i = 0; i < CONFIGURE_LISTENER_CLASS_NAMES.length; i++) {
        callInit(CONFIGURE_LISTENER_CLASS_NAMES[i]);
      }

    } catch (Exception e) {
      LOG.fatal("The ConfigureListener could not be triggered", e);
    }
  }

  private void callInit(String className)
      throws ClassNotFoundException, InstantiationException,
      IllegalAccessException {

    Class aClass = Class.forName(className);
    ServletContextListener listener = (ServletContextListener)
        aClass.newInstance();
    listener.contextInitialized(
        new ServletContextEvent(getServletContext()));
  }

// ///////////////////////////////////////////// bean getter + setter

}
