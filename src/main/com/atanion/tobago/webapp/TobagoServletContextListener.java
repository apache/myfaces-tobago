/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 13, 2002 at 8:43:20 AM.
 * $Id$
 */
package com.atanion.tobago.webapp;

import com.atanion.tobago.config.ThemeConfig;
import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.config.TobagoConfigParser;
import com.atanion.tobago.context.ResourceManager;
import com.atanion.license.LicenseCheck;
import com.atanion.license.License;
import com.atanion.license.LicenseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class TobagoServletContextListener implements ServletContextListener {

// ///////////////////////////////////////////// constants

  private static final Log LOG
      = LogFactory.getLog(TobagoServletContextListener.class);

// ///////////////////////////////////////////// code

  public void contextInitialized(ServletContextEvent event) {

    if (LOG.isInfoEnabled()) {
      LOG.info("*** contextInitialized ***");
    }

    try {


      final ClassLoader classLoader = getClass().getClassLoader();

      final InputStream stream = classLoader.getResourceAsStream("/license.ser");

//      LOG.info("stream == " + stream);


      final License license = LicenseCheck.loadLicense(stream);
      if (! license.isValid("tobago")) {
        throw new LicenseException("invald license found: " + license.toString());
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info("Valid tobago license found!");
        }
      }

      // servlet mapping
      ServletContext servletContext = event.getServletContext();
      TobagoServletMapping mapping = new TobagoServletMapping(servletContext);
      servletContext.setAttribute(
          TobagoServletMapping.TOBAGO_SERVLET_MAPPING, mapping);

      // tobago-config.xml
      TobagoConfig tobagoConfig = new TobagoConfig();
      TobagoConfigParser.parse(servletContext, tobagoConfig);
      tobagoConfig.propagate(servletContext);
      servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);

      // resources
      initResources(servletContext, tobagoConfig);

      // theme config cache
      servletContext.setAttribute(ThemeConfig.THEME_CONFIG_CACHE, new HashMap());

    } catch (Throwable e) {
      if (LOG.isFatalEnabled()) {
        String error = "Error while deploy. Tobago can't be initized! " +
                    "Application will not run!";
        LOG.fatal(error, e);
        throw new RuntimeException(error, e);
      }
    }
  }

  public void contextDestroyed(ServletContextEvent event) {
    if (LOG.isInfoEnabled()) {
      LOG.info(
          "*** contextDestroyed ***\n--- snip ---------"
          + "--------------------------------------------------------------");
    }

    ServletContext servletContext = event.getServletContext();

    servletContext.removeAttribute(TobagoServletMapping.TOBAGO_SERVLET_MAPPING);
    servletContext.removeAttribute(TobagoConfig.TOBAGO_CONFIG);
    servletContext.removeAttribute(ResourceManager.RESOURCE_MANAGER);
    servletContext.removeAttribute(ThemeConfig.THEME_CONFIG_CACHE);

    LogFactory.releaseAll();
//    LogManager.shutdown();
  }

  protected void initResources(
      ServletContext servletContext, TobagoConfig tobagoConfig)
      throws ServletException {
    ResourceManager resources = new ResourceManager();
    locateResources(servletContext, resources, "/");
    for (Iterator i = tobagoConfig.getResourceDirs().iterator(); i.hasNext(); ) {
      String dir = (String) i.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Locating resources in dir: " + dir);
      }
      resources.addResourceDirectory(dir);
    }
    servletContext.setAttribute(ResourceManager.RESOURCE_MANAGER, resources);

    resources.setTobagoConfig(tobagoConfig);
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
      if (LOG.isDebugEnabled()) {
        LOG.debug(
            directory + '/' + locale + '/' + key + "=" + temp.getProperty(key));
      }
    }
  }

}
