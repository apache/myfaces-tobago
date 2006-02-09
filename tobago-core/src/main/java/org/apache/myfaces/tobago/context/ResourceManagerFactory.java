package org.apache.myfaces.tobago.context;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceManagerFactory {

  private static final Log LOG = LogFactory.getLog(ResourceManagerFactory.class);

  public static final String RESOURCE_MANAGER
      = "org.apache.myfaces.tobago.context.ResourceManager";

  // todo: Test for new theme build mechanism still under development
  // http://issues.apache.org/jira/browse/MYFACES-1106
  // to activate you have to do:
  // 1. set USE_JAR_THEME_RESOURCE = true, of course
  // 2. add resource-path in tobago-config.xml
  // 3. add ResourceServlet in web.xml
  public static final boolean USE_JAR_THEME_RESOURCE = false;

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
    locateResourcesInWar(servletContext, resources, "/");
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

  private static void locateResourcesInWar(
      ServletContext servletContext, ResourceManagerImpl resources, String path)
      throws ServletException {

    if (path.equals("/WEB-INF/") || path.equals("/WEB-INF/lib/")) {
      if (USE_JAR_THEME_RESOURCE) {
        // continue
      } else {
        return; // ignore
      }
    } else if (path.startsWith("/WEB-INF/")) {
      return; // ignore
    }

    Set<String> resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      if (LOG.isErrorEnabled()) {
        LOG.error("ResourcePath empty! Please check the tobago-config.xml file!"
            + " path='" + path + "'");
      }
      return;
    }
    for (String childPath : resourcePaths) {
      if (childPath.endsWith("/")) {
        // ignore, because weblogic puts the path directory itself in the Set
        if (!childPath.equals(path)) {
          LOG.debug("childPath dir " + childPath);
          locateResourcesInWar(servletContext, resources, childPath);
        }
      } else {
        //Log.debug("add resc " + childPath);
        if (childPath.endsWith(".properties")) {
          InputStream inputStream = servletContext.getResourceAsStream(childPath);
          addProperties(inputStream, resources, childPath, false);
        } else if (childPath.endsWith(".properties.xml")) {
          InputStream inputStream = servletContext.getResourceAsStream(childPath);
          addProperties(inputStream, resources, childPath, true);
        } else if (childPath.startsWith("/WEB-INF/lib/") && childPath.endsWith(".jar")) {
          if (USE_JAR_THEME_RESOURCE) {
            locateResourcesInLib(servletContext, resources, childPath);
          }
        } else {
          resources.add(childPath);
          //Log.debug(childPath);
        }
      }
    }
  }

  private static void locateResourcesInLib(
      ServletContext servletContext, ResourceManagerImpl resources, String jarPath)
      throws ServletException {

    InputStream stream = null;
    try {
      if (findThemeDescriptor(servletContext, jarPath)) {

        ClassLoader classLoader = ResourceManagerFactory.class.getClassLoader();

        stream = servletContext.getResourceAsStream(jarPath);
        ZipInputStream zip = new ZipInputStream(stream);
        while (zip.available() > 0) {
          ZipEntry nextEntry = zip.getNextEntry();
          if (nextEntry == null || nextEntry.isDirectory()) {
            continue;
          }
          String name = "/" + nextEntry.getName();
          LOG.info("name = '" + name + "'");
          String prefix = "/org/apache/myfaces/tobago/renderkit/";
          if (name.startsWith(prefix)) {
            if (name.endsWith(".class")) {
              // ignore the class files
            } else if (name.endsWith(".properties")) {
              LOG.info("** " + name.substring(1));
              InputStream inputStream = classLoader.getResourceAsStream(name.substring(1));
              addProperties(inputStream, resources, name, false);
            } else if (name.endsWith(".properties.xml")) {
              LOG.info("** " + name.substring(1));
              InputStream inputStream = classLoader.getResourceAsStream(name.substring(1));
              LOG.info(inputStream);
              addProperties(inputStream, resources, name, true);
            } else {
              resources.add(name);
            }
          }
        }
      }
    } catch (IOException e) {
      String msg = "while loading " + jarPath;
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    } finally {
      IOUtils.closeQuietly(stream);
    }
  }

  private static boolean findThemeDescriptor(
      ServletContext servletContext, String jarPath) throws IOException {
    InputStream stream = servletContext.getResourceAsStream(jarPath);
    ZipInputStream zip = new ZipInputStream(stream);
    while (zip.available() > 0) {
      ZipEntry nextEntry = zip.getNextEntry();
      if (nextEntry == null || nextEntry.isDirectory()) {
        continue;
      }
      String name = nextEntry.getName();
      if (name.equals("META-INF/tobago-theme.xml")) {
        return true;
      }
    }
    return false;
  }

  private static void addProperties(
      InputStream stream, ResourceManagerImpl resources,
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
    try {
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
