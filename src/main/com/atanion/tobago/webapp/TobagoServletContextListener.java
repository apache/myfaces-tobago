/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 13, 2002 at 8:43:20 AM.
 * $Id$
 */
package com.atanion.tobago.webapp;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.config.TobagoConfigParser;
import com.atanion.tobago.context.ResourceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

public class TobagoServletContextListener implements ServletContextListener {

// ///////////////////////////////////////////// constants

  private static final Log LOG
      = LogFactory.getLog(TobagoServletContextListener.class);

// ///////////////////////////////////////////// code

  public void contextInitialized(ServletContextEvent event) {

    if (LOG.isInfoEnabled()) {
      LOG.info("*** contextInitialized ***");
    }
    // resources
    try {
      initResources(event.getServletContext());
    } catch (ServletException e) {
      if (LOG.isFatalEnabled()) {
        LOG.fatal(
            "Error while deploy. Tobago can't be initized! " +
            "Application will not run!", e);
      }
    }

    // servlet mapping
    ServletContext servletContext = event.getServletContext();
    TobagoServletMapping mapping = new TobagoServletMapping(servletContext);
    servletContext.setAttribute(
        TobagoServletMapping.TOBAGO_SERVLET_MAPPING, mapping);

    // tobago-config.xml
    TobagoConfig.init();
    TobagoConfigParser tobagoConfigParser = new TobagoConfigParser();
    tobagoConfigParser.init(servletContext);
    TobagoConfig config = TobagoConfig.getInstance();
    servletContext.setAttribute("tobagoConfig", config);
  }

  public void contextDestroyed(ServletContextEvent event) {
    if (LOG.isInfoEnabled()) {
      LOG.info(
          "*** contextDestroyed ***\n--- snip ---------"
          + "--------------------------------------------------------------");
    }
    LogFactory.releaseAll();
  }

  protected void initResources(ServletContext servletContext)
      throws ServletException {
    ResourceManager resources = ResourceManager.getInstance();
    String resourceDirs = servletContext.getInitParameter(
        TobagoConstants.CONTEXT_PARAM_RESOURCE_DIRECTORIES);
    if (resourceDirs == null) {
      resourceDirs = "tobago,tobago-custom";
      if (LOG.isInfoEnabled()) {
        LOG.info(
            "Init parameter '"
            + TobagoConstants.CONTEXT_PARAM_RESOURCE_DIRECTORIES
            + "' not found. " +
            "Using default: '" + resourceDirs + "'");
      }
    }
    locateResources(servletContext, resources, "/");
    StringTokenizer tokenizer = new StringTokenizer(resourceDirs, ",");
    while (tokenizer.hasMoreTokens()) {
      String dir = tokenizer.nextToken();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Locating resources in dir: " + dir);
      }
      resources.addResourceDirectory(dir);
    }
  }

  private void locateResources(
      ServletContext servletContext, ResourceManager resources, String path)
      throws ServletException {

    Set resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      if (LOG.isErrorEnabled()) {
        LOG.error(
            "ResourcePath empty! Please check the web.xml file!" +
            " path='" + path + "'");
      }
      return;
    }
    for (Iterator i = resourcePaths.iterator(); i.hasNext();) {
      String childPath = (String) i.next();
      if (childPath.endsWith("/")) {
        if (childPath.equals(path)) {
          // ignore, because weblogic puts the path directory itself in the Set
        } else {
//          Log.debug("dir      " + childPath);
          locateResources(servletContext, resources, childPath);
        }
      } else {
//        Log.debug("add resc " + childPath);
        if (childPath.endsWith(".properties")) {
          addProperties(servletContext, resources, childPath);
        } else {
          resources.add(childPath);
//          Log.debug(childPath);
        }
      }
    }
  }

  private void addProperties(
      ServletContext servletContext, ResourceManager resources,
      String childPath)
      throws ServletException {

    String directory = childPath.substring(0, childPath.lastIndexOf('/'));
    String filename = childPath.substring(childPath.lastIndexOf('/') + 1);

    int begin = filename.indexOf('_') + 1;
    int end = filename.lastIndexOf('.');

    String locale;
    if (begin > 0) {
      locale = filename.substring(begin, end);
    } else {
      locale = "default";
    }
    locale = filename.substring(0, end);


    Properties temp = new Properties();
    try {
      temp.load(servletContext.getResourceAsStream(childPath));
    } catch (IOException e) {
      String msg = "while loading " + childPath;
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    }
    for (Enumeration e = temp.propertyNames(); e.hasMoreElements();) {
      String key = (String) e.nextElement();
      resources.add(
          directory + '/' + locale + '/' + key, temp.getProperty(key));
      LOG.debug(
          directory + '/' + locale + '/' + key + "=" + temp.getProperty(key));
    }
  }

}
