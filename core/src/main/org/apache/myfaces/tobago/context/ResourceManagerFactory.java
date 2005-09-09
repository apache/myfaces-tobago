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
 * Created 30.03.2004 12:47:02.
 * $Id: ResourceManagerUtil.java 1361 2005-08-15 09:46:20Z lofwyr $
 */
package org.apache.myfaces.tobago.context;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

public class ResourceManagerFactory {

  private static final Log LOG = LogFactory.getLog(ResourceManagerFactory.class);

  public static final String RESOURCE_MANAGER
      = "org.apache.myfaces.tobago.context.ResourceManager";

  private ResourceManagerFactory() {
  }

  private static boolean initialized;

  public static ResourceManager getResourceManager(FacesContext facesContext) {
    assert initialized;
    return (ResourceManager) facesContext.getExternalContext()
        .getApplicationMap().get(RESOURCE_MANAGER);
  }

  public static ResourceManager getResourceManager(ServletContext servletContext) {
    assert initialized;
    return (ResourceManager) servletContext
        .getAttribute(RESOURCE_MANAGER);
  }

  public static void init(ServletContext servletContext, TobagoConfig tobagoConfig)
      throws ServletException {
    assert !initialized;
    ResourceManagerImpl resources = new ResourceManagerImpl();
    locateResources(servletContext, resources, "/");
    for (String dir : tobagoConfig.getResourceDirs()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Locating resources in dir: " + dir);
      }
      resources.addResourceDirectory(dir);
    }
    servletContext.setAttribute(RESOURCE_MANAGER, resources);

    resources.setTobagoConfig(tobagoConfig);
    initialized = true;
  }

  private static void locateResources(
      ServletContext servletContext, ResourceManagerImpl resources, String path)
      throws ServletException {

    Set<String> resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      if (LOG.isErrorEnabled()) {
        LOG.error("ResourcePath empty! Please check the web.xml file!" +
            " path='" + path + "'");
      }
      return;
    }
    for (String childPath : resourcePaths) {
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
        } else if (childPath.endsWith(".properties.xml")) {
          addProperties(servletContext, resources, childPath, true);
        } else {
          resources.add(childPath);
//          Log.debug(childPath);
        }
      }
    }
  }

  private static void addProperties(
      ServletContext servletContext, ResourceManagerImpl resources,
      String childPath, boolean xml)
      throws ServletException {

    String directory = childPath.substring(0, childPath.lastIndexOf('/'));
    String filename = childPath.substring(childPath.lastIndexOf('/') + 1);

    int begin = filename.indexOf('_') + 1;
    int end = filename.lastIndexOf('.');
    if (xml) {
      end = filename.lastIndexOf('.', end - 1);
    }

    String locale;
/*
    if (begin > 0) {
      locale = filename.substring(begin, end);
    } else {
      locale = "default";
    }
*/
    locale = filename.substring(0, end);


    Properties temp = new Properties();
    InputStream stream = null;
    try {
      stream = servletContext.getResourceAsStream(childPath);
      if (xml) {
        temp.loadFromXML(stream);
        if (LOG.isDebugEnabled()) {
          LOG.debug(childPath);
          LOG.debug("xml properties: " + temp.size());
        }
      } else {
        temp.load(stream);
        if (LOG.isDebugEnabled()) {
          LOG.debug(childPath);
          LOG.debug("    properties: " + temp.size());
        }
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


  public static void release(ServletContext servletContext) {
    assert initialized;
    initialized = false;
    servletContext.removeAttribute(RESOURCE_MANAGER);
  }


}
