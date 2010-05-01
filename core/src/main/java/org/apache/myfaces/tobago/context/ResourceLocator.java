package org.apache.myfaces.tobago.context;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.util.XmlUtils;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class helps to locate all resources of the ResourceManager.
 * It will be called in the initializaion phase.
 *
 * @since 1.0.7
 */
class ResourceLocator {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceLocator.class);

  private static final String META_INF_TOBAGO_THEME_XML = "META-INF/tobago-theme.xml";
  private static final String META_INF_RESOURCES = "META-INF/resources";

  private ServletContext servletContext;
  private ResourceManagerImpl resourceManager;
  private ThemeBuilder themeBuilder;

  public ResourceLocator(
      ServletContext servletContext, ResourceManagerImpl resourceManager, ThemeBuilder themeBuilder) {
    this.servletContext = servletContext;
    this.resourceManager = resourceManager;
    this.themeBuilder = themeBuilder;
  }

  public void locate()
      throws ServletException {
    // TODO should the resourcedir used from tobago-config.xml?
    locateResourcesInWar(servletContext, resourceManager, "/");
    locateResourcesFromClasspath(resourceManager);
    locateResourcesServlet30Alike(resourceManager);
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
      if (LOG.isDebugEnabled()) {
        LOG.debug("Skipping empty resource path: path='{}'", path);
      }
      return;
    }
    for (String childPath : resourcePaths) {
      if (childPath.endsWith("/")) {
        // ignore, because weblogic puts the path directory itself in the Set
        if (!childPath.equals(path)) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("childPath dir {}", childPath);
          }
          locateResourcesInWar(servletContext, resources, childPath);
        }
      } else {
        //Log.debug("add resc " + childPath);
        if (childPath.endsWith(".properties")) {
          InputStream inputStream = servletContext.getResourceAsStream(childPath);
          try {
            addProperties(inputStream, resources, childPath, false, 0);
          } finally {
            IOUtils.closeQuietly(inputStream);
          }
        } else if (childPath.endsWith(".properties.xml")) {
          InputStream inputStream = servletContext.getResourceAsStream(childPath);
          try {
            addProperties(inputStream, resources, childPath, true, 0);
          } catch (RuntimeException e) {
            LOG.error("childPath = \"" + childPath + "\" ", e);
            throw e;
          } finally {
            IOUtils.closeQuietly(inputStream);
          }
        } else {
          resources.add(childPath);
        }
      }
    }
  }

  private void locateResourcesFromClasspath(ResourceManagerImpl resources)
      throws ServletException {

    ThemeParser parser = new ThemeParser();
    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for " + META_INF_TOBAGO_THEME_XML);
      }
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Enumeration<URL> urls = classLoader.getResources(META_INF_TOBAGO_THEME_XML);

      while (urls.hasMoreElements()) {
        URL themeUrl = urls.nextElement();

        ThemeImpl theme = parser.parse(themeUrl);
        themeBuilder.addTheme(theme);
        String prefix = ensureSlash(theme.getResourcePath());

        String protocol = themeUrl.getProtocol();
        // tomcat uses jar
        // weblogic uses zip
        // IBM WebSphere uses wsjar
        if (!"jar".equals(protocol) && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
          LOG.warn("Unknown protocol '" + themeUrl + "'");
        }
        addResources(resources, themeUrl, prefix, 0);
      }
    } catch (IOException e) {
      String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    } catch (SAXException e) {
      String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }

  /**
   * Searches the /WEB-INF/lib directory for *.jar files which contains /META-INF/resources directory
   * to hold resources and add them to the ResourceManager.
   *
   * @param resources Resource Manager which collects all the resources.
   * @throws ServletException An error while accessing the resource.
   */
  private void locateResourcesServlet30Alike(ResourceManagerImpl resources) throws ServletException {

    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for " + META_INF_RESOURCES);
      }
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Enumeration<URL> urls = classLoader.getResources(META_INF_RESOURCES);

      while (urls.hasMoreElements()) {
        URL resourcesUrl = urls.nextElement();

        LOG.info("resourcesUrl='"+resourcesUrl + "'");
        if (!resourcesUrl.toString().matches(".*/WEB-INF/lib/.*\\.jar\\!.*")) {
          LOG.info("skip ...");
          continue;
          // only resources from jar files in the /WEB-INF/lib should be considered (like in Servlet 3.0 spec.)
        }
        LOG.info("going on ...");

        String protocol = resourcesUrl.getProtocol();
        // tomcat uses jar
        // weblogic uses zip
        // IBM WebSphere uses wsjar
        if (!"jar".equals(protocol) && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
          LOG.warn("Unknown protocol '" + resourcesUrl + "'");
        }
        addResources(resources, resourcesUrl, "/" + META_INF_RESOURCES, META_INF_RESOURCES.length() + 1);
      }
    } catch (IOException e) {
      String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }

  private void addResources(ResourceManagerImpl resources, URL themeUrl, String prefix, int skipPrefix)
      throws IOException, ServletException {
    String fileName = themeUrl.toString();
    int index = fileName.indexOf("!");
    String protocol = themeUrl.getProtocol();
    if (index != -1) {
      fileName = fileName.substring(protocol.length() + 1, index);
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("Adding resources from fileName='"+fileName + "' prefix='" + prefix + "' skip=" + skipPrefix + "");
    }

    // JBoss 5.0.0 introduced vfszip protocol
    if (!protocol.equals("vfszip") && fileName.endsWith(META_INF_TOBAGO_THEME_XML)) {
      try {
        URI uri = themeUrl.toURI();
        File tobagoThemeXml = new File(uri);
        File directoryFile = tobagoThemeXml.getParentFile().getParentFile();
        String resourcePath = "";
        resolveTheme(resources, directoryFile, resourcePath, prefix, false);
      } catch (URISyntaxException e) {
        LOG.error("", e);
      }
    } else {
      URL jarFile;
      try {
        // JBoss 5.0.0 introduced vfszip protocol
        if (protocol.equals("vfszip")) {
          fileName = new File(fileName).getParentFile().getParentFile().getPath();
        }
        jarFile = new URL(fileName);
      } catch (MalformedURLException e) {
        // workaround for weblogic on windows
        jarFile = new URL("file:" + fileName);
      }
      InputStream stream = null;
      ZipInputStream zipStream = null;
      try {
        stream = jarFile.openStream();
        zipStream = new ZipInputStream(stream);
        while (zipStream.available() > 0) {
          ZipEntry nextEntry = zipStream.getNextEntry();
          if (nextEntry == null || nextEntry.isDirectory()) {
            continue;
          }
          String name = "/" + nextEntry.getName();
          if (name.startsWith(prefix)) {
            addResource(resources, name, skipPrefix);
          }
        }
      } finally {
        IOUtils.closeQuietly(stream);
        IOUtils.closeQuietly(zipStream);
      }
    }
  }

  private void resolveTheme(ResourceManagerImpl resources, File directoryFile,
      String resourcePath, String prefix, boolean inResourcePath) throws ServletException {
    File[] files = directoryFile.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        String currentResourcePath = resourcePath + File.separator + file.getName();
        if (!inResourcePath && currentResourcePath.startsWith(prefix)) {
          inResourcePath = true;
        }
        resolveTheme(resources, file, currentResourcePath, prefix, inResourcePath);
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info(resourcePath + File.separator + file.getName());
        }
        if (inResourcePath) {
          addResource(resources, resourcePath + File.separator + file.getName(), 0);
        }
      }
    }
  }

  private void addResource(ResourceManagerImpl resources, String name, int skipPrefix)
      throws ServletException {

    if (name.endsWith(".class")) {
      // ignore the class files
    } else if (name.endsWith(".properties")) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Adding properties from: '" + name.substring(1) + "'");
      }
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.substring(1));
      try {
        addProperties(inputStream, resources, name, false, skipPrefix);
      } finally {
        IOUtils.closeQuietly(inputStream);
      }
    } else if (name.endsWith(".properties.xml")) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Adding properties from: '" + name.substring(1) + "'");
      }
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.substring(1));
      try {
        addProperties(inputStream, resources, name, true, skipPrefix);
      } finally {
        IOUtils.closeQuietly(inputStream);
      }
    } else {
      resources.add(name.substring(skipPrefix));
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
      InputStream stream, ResourceManagerImpl resources, String childPath, boolean xml, int skipPrefix)
      throws ServletException {

    String directory = childPath.substring(skipPrefix, childPath.lastIndexOf('/'));
    String filename = childPath.substring(childPath.lastIndexOf('/') + 1);

    int end = filename.lastIndexOf('.');
    if (xml) {
      end = filename.lastIndexOf('.', end - 1);
    }

    String locale = filename.substring(0, end);


    Properties temp = new Properties();
    try {
      if (xml) {
        // temp.loadFromXML(stream); XXX avoid to be able to retrotranslate it
        XmlUtils.load(temp, stream);
        if (LOG.isDebugEnabled()) {
          LOG.debug(childPath);
          LOG.debug("xml properties: {}", temp.size());
        }
      } else {
        temp.load(stream);
        if (LOG.isDebugEnabled()) {
          LOG.debug(childPath);
          LOG.debug("    properties: {}", temp.size());
        }
      }
    } catch (IOException e) {
      String msg = "while loading " + childPath;
      LOG.error(msg, e);
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
