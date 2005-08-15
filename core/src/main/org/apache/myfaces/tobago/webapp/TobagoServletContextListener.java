/*
 * Copyright 2002-2005 atanion GmbH.
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
 * Created Nov 13, 2002 at 8:43:20 AM.
 * $Id$
 */
package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.config.TobagoConfigParser;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.commons.io.IOUtils;
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

    ServletContext servletContext = event.getServletContext();

    if (servletContext.getAttribute(TobagoConfig.TOBAGO_CONFIG) != null) {
      LOG.warn("Tobago has been already initialized. Do nothing.");
      return;
    }

    try {

      // tobago-config.xml
      TobagoConfig tobagoConfig = new TobagoConfig();
      TobagoConfigParser.parse(servletContext, tobagoConfig);
      servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);

      // resources
      initResources(servletContext, tobagoConfig);

      // theme config cache
      servletContext.setAttribute(ThemeConfig.THEME_CONFIG_CACHE, new HashMap());

    } catch (Throwable e) {
      if (LOG.isFatalEnabled()) {
        String error = "Error while deploy process. Tobago can't be initialized! " +
            "Application will not run!";
        LOG.fatal(error, e);
        throw new RuntimeException(error, e);
      }
    }
  }

  public void contextDestroyed(ServletContextEvent event) {
    if (LOG.isInfoEnabled()) {
      LOG.info("*** contextDestroyed ***\n--- snip ---------"
          + "--------------------------------------------------------------");
    }

    ServletContext servletContext = event.getServletContext();

    servletContext.removeAttribute(TobagoConfig.TOBAGO_CONFIG);
    servletContext.removeAttribute(ResourceManager.RESOURCE_MANAGER);
    servletContext.removeAttribute(ThemeConfig.THEME_CONFIG_CACHE);

    LogFactory.releaseAll();
//    LogManager.shutdown();
  }

  protected void initResources(ServletContext servletContext, TobagoConfig tobagoConfig)
      throws ServletException {
    ResourceManager resources = new ResourceManager();
    locateResources(servletContext, resources, "/");
    for (Iterator i = tobagoConfig.getResourceDirs().iterator(); i.hasNext();) {
      String dir = (String) i.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Locating resources in dir: " + dir);
      }
      resources.addResourceDirectory(dir);
    }
    servletContext.setAttribute(ResourceManager.RESOURCE_MANAGER, resources);

    resources.setTobagoConfig(tobagoConfig);
  }

  private void locateResources(ServletContext servletContext, ResourceManager resources, String path)
      throws ServletException {

    Set resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      if (LOG.isErrorEnabled()) {
        LOG.error("ResourcePath empty! Please check the web.xml file!" +
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
          addProperties(servletContext, resources, childPath, false);
//        } else if (childPath.endsWith(".properties.xml")) {
//          addProperties(servletContext, resources, childPath, true);
        } else {
          resources.add(childPath);
//          Log.debug(childPath);
        }
      }
    }
  }

  private void addProperties(ServletContext servletContext, ResourceManager resources,
      String childPath, boolean xml)
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
    InputStream stream = null;
    try {
      stream = servletContext.getResourceAsStream(childPath);
//      LOG.error(xml);
      if (xml) {
//        LOG.error(childPath);
        temp.loadFromXML(stream);
      } else {
        temp.load(stream);
      }
    } catch (IOException e) {
      String msg = "while loading " + childPath;
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    } finally {
      IOUtils.closeQuietly(stream);
    }

    for (Enumeration e = temp.propertyNames(); e.hasMoreElements();) {
      String key = (String) e.nextElement();
      resources.add(directory + '/' + locale + '/' + key, temp.getProperty(key));
      if (LOG.isDebugEnabled()) {
        LOG.debug(directory + '/' + locale + '/' + key + "=" + temp.getProperty(key));
      }
    }
  }

}
