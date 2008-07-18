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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

  private static final Log LOG = LogFactory.getLog(ResourceLocator.class);

  private ServletContext servletContext;
  private ResourceManagerImpl resourceManager;
  private ThemeBuilder themeBuilder;
  private static final String META_INF_TOBAGO_THEME_XML = "META-INF/tobago-theme.xml";

  public ResourceLocator(
      ServletContext servletContext, ResourceManagerImpl resourceManager,
      ThemeBuilder tobagoConfig) {
    this.servletContext = servletContext;
    this.resourceManager = resourceManager;
    this.themeBuilder = tobagoConfig;
  }

  public void locate()
      throws ServletException {
    // TODO should the resourcedir used from tobago-config.xml?
    locateResourcesInWar(servletContext, resourceManager, "/");
    locateResourcesFromClasspath(resourceManager);
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
      LOG.info("ResourcePath empty! Please check the tobago-config.xml file!"
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
          try {
            addProperties(inputStream, resources, childPath, false);
          } finally {
            IOUtils.closeQuietly(inputStream);
          }
        } else if (childPath.endsWith(".properties.xml")) {
          InputStream inputStream = servletContext.getResourceAsStream(childPath);
          try {
            addProperties(inputStream, resources, childPath, true);
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
        LOG.info("Loading tobago-theme.xml");
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
        if ("jar".equals(protocol) || "zip".equals(protocol) || "wsjar".equals(protocol)) {
          addResources(resources, themeUrl, prefix);
        } else {
          LOG.warn("Unknown protocol '" + themeUrl + "'");
          addResources(resources, themeUrl, prefix);
        }
      }
    } catch (IOException e) {
      String msg = "while loading ";
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    } catch (SAXException e) {
      String msg = "while loading ";
      if (LOG.isErrorEnabled()) {
        LOG.error(msg, e);
      }
      throw new ServletException(msg, e);
    }
  }

  private void addResources(ResourceManagerImpl resources, URL themeUrl,
      String prefix) throws IOException, ServletException {
    String fileName = themeUrl.toString();
    int index = fileName.indexOf("!");
    String protocol = themeUrl.getProtocol();
    if (index != -1) {
      fileName = fileName.substring(protocol.length() + 1, index);
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
            addResource(resources, name);
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
          addResource(resources, resourcePath + File.separator + file.getName());
        }
      }
    }
  }

  private void addResource(ResourceManagerImpl resources, String name)
      throws ServletException {

    if (name.endsWith(".class")) {
      // ignore the class files
    } else if (name.endsWith(".properties")) {
      if (LOG.isInfoEnabled()) {
        LOG.info("** " + name.substring(1));
      }
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.substring(1));
      try {
        addProperties(inputStream, resources, name, false);
      } finally {
        IOUtils.closeQuietly(inputStream);
      }
    } else if (name.endsWith(".properties.xml")) {
      if (LOG.isInfoEnabled()) {
        LOG.info("** " + name.substring(1));
      }
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.substring(1));
      try {
        addProperties(inputStream, resources, name, true);
      } finally {
        IOUtils.closeQuietly(inputStream);
      }
    } else {
      resources.add(name);
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
