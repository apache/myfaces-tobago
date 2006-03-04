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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;
import java.util.Properties;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This class helps to locate all resources of the ResourceManager.
 * It will be called in the initializaion phase.
 *
 * @author lofwyr
 * @since 1.0.7
 */
class ResourceLocator {

  private static final Log LOG = LogFactory.getLog(ResourceLocator.class);

// todo: Test for new theme build mechanism still under development
// http://issues.apache.org/jira/browse/MYFACES-1106
// to activate you have to do:
// 1. set load-theme-resources-from-classpath = true in the tobago-config.xml
// 2. add resource-path in tobago-config.xml
// 3. add ResourceServlet in web.xml

  private ServletContext servletContext;
  private ResourceManagerImpl resourceManager;
  private List<String> resourceDirs;
  private TobagoConfig tobagoConfig;

  public ResourceLocator(
      ServletContext servletContext, ResourceManagerImpl resourceManager,
      List<String> resourceDirs, TobagoConfig tobagoConfig) {
    this.servletContext = servletContext;
    this.resourceManager = resourceManager;
    this.resourceDirs = resourceDirs;
    this.tobagoConfig = tobagoConfig;
  }

  public void init()
      throws ServletException {
    locateResourcesInWar(servletContext, resourceManager, "/");

    locateResourcesInLib(resourceManager);
    
    for (String dir : resourceDirs) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Locating resources in dir: " + dir);
      }
      resourceManager.addResourceDirectory(dir);
    }
  }

  private void locateResourcesInWar(
      ServletContext servletContext, ResourceManagerImpl resources, String path)
      throws ServletException {

    if (path.startsWith("/WEB-INF/")) {
      return; // ignore
    }
    // fix for jetty6
    if (path.endsWith("/") && path.length() > 1) {
      path = path.substring(0, path.length() - 1);
    }
    Set<String> resourcePaths = servletContext.getResourcePaths(path);
    if (resourcePaths == null || resourcePaths.isEmpty()) {
      LOG.error("ResourcePath empty! Please check the tobago-config.xml file!"
            + " path='" + path + "'");
      return;
    }
    for (String childPath : resourcePaths) {
      if (childPath.endsWith("/")) {
        // ignore, because weblogic puts the path directory itself in the Set
        if (!childPath.equals(path)) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("childPath dir " + childPath);
          }
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
        } else {
          resources.add(childPath);
        }
      }
    }
  }

  private void locateResourcesInLib(ResourceManagerImpl resources)
      throws ServletException {

    ThemeParser parser = new ThemeParser();
    try {
      LOG.error("Loading tobago-theme.xml");
      Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/tobago-theme.xml");

      while (urls.hasMoreElements()) {
        URL themeUrl = urls.nextElement();

        Theme theme = parser.parse(themeUrl);
        String prefix = ensureSlash(theme.getResourcePath());

        // TODO other protocols
        if ("jar".equals(themeUrl.getProtocol())) {
          String fileName = themeUrl.toString().substring(4, themeUrl.toString().indexOf("!"));
          ClassLoader classLoader = ResourceManagerFactory.class.getClassLoader();
          URL jarFile = new URL(fileName);
          InputStream stream = null;
          try {
            stream = jarFile.openStream();
            ZipInputStream zip = new ZipInputStream(stream);
            while (zip.available() > 0) {
              ZipEntry nextEntry = zip.getNextEntry();
              if (nextEntry == null || nextEntry.isDirectory()) {
                continue;
              }
              String name = "/" + nextEntry.getName();
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
                  if (tobagoConfig.isLoadThemesFromClasspath()) {
                    resources.add(name);
                  }
                }
              }
            }
          } finally {
            IOUtils.closeQuietly(stream);
          }
        } else {
          LOG.error("Unknown protocol "+themeUrl);
        }
      }
    } catch (Exception e) {
      String msg = "while loading ";
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    }
  }

  private String ensureSlash(String resourcePath) {
    if (!resourcePath.startsWith("/")) {
      resourcePath = '/' + resourcePath;
    }
    if (!resourcePath.endsWith("/")) {
      resourcePath = resourcePath + '/';
    }
    return resourcePath;
  }

  private void addProperties(
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
}
