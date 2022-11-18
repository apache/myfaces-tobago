/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jakarta.faces.application.ViewHandler;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebXmlUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Map<Class<Throwable>, String> ERROR_PAGE_LOCATIONS = new HashMap<>();

  public static String getErrorPageLocation(final Throwable exception) {
    if (ERROR_PAGE_LOCATIONS.size() <= 0) {
      init();
    }

    String location = null;

    if (exception != null) {
      Class<?> exceptionClass = exception.getClass();
      while (exceptionClass != null && location == null) {
        location = ERROR_PAGE_LOCATIONS.get(exceptionClass);
        exceptionClass = exceptionClass.getSuperclass();
      }
    }

    if (location == null) {
      location = ERROR_PAGE_LOCATIONS.get(null);
    }

    return location;
  }

  private static void init() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();

    try {
      final List<Document> webXmls = getWebXmls(facesContext);

      String locationDefault = null;
      String location500 = null;

      for (final Document document : webXmls) {
        if (document != null) {
          final NodeList errorPages = document.getElementsByTagName("error-page");

          for (int i = 0; i < errorPages.getLength(); i++) {
            final Node errorPage = errorPages.item(i);

            String errorCode = null;
            String exceptionType = null;
            String location = null;

            final NodeList children = errorPage.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
              final Node child = children.item(j);
              final String name = child.getNodeName();

              if ("error-code".equals(name)) {
                errorCode = child.getFirstChild().getNodeValue().trim();
              } else if ("exception-type".equals(name)) {
                exceptionType = child.getFirstChild().getNodeValue().trim();
              } else if ("location".equals(name)) {
                location = child.getFirstChild().getNodeValue().trim();
              }
            }

            if (exceptionType != null) {
              final Class<Throwable> key = (Class<Throwable>) Class.forName(exceptionType);
              final String value = normalizePath(externalContext, location);
              ERROR_PAGE_LOCATIONS.put(key, value);
            } else if ("500".equals(errorCode)) {
              location500 = location;
            } else if (errorCode == null && exceptionType == null) {
              locationDefault = location;
            }
          }
        }
      }

      if (!ERROR_PAGE_LOCATIONS.containsKey(null)) {
        final String value = normalizePath(externalContext, location500 != null ? location500 : locationDefault);
        ERROR_PAGE_LOCATIONS.put(null, value);
      }
    } catch (IOException | ParserConfigurationException | ClassNotFoundException | SAXException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  private static List<Document> getWebXmls(final FacesContext facesContext)
      throws ParserConfigurationException, IOException, SAXException {
    final List<Document> webXmls = new ArrayList<>();

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
      factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
      factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      factory.setXIncludeAware(false);
      factory.setExpandEntityReferences(false);
    } catch (ParserConfigurationException e) {
      LOG.info("ParserConfigurationException was thrown. A feature is probably not supported by your XML processor. "
          + e.getMessage());
    }
    final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
    for (final URL url : getWebXmlUrls(facesContext)) {
      webXmls.add(getWebXml(documentBuilder, url));
    }

    return webXmls;
  }

  private static List<URL> getWebXmlUrls(final FacesContext facesContext) throws IOException {
    final List<URL> urls = new ArrayList<>();
    final ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
    urls.add(servletContext.getResource("/WEB-INF/web.xml"));

    final Enumeration<URL> webFragments = Thread.currentThread().getContextClassLoader()
        .getResources("META-INF/web-fragment.xml");
    while (webFragments.hasMoreElements()) {
      urls.add(webFragments.nextElement());
    }

    return urls;
  }

  private static Document getWebXml(final DocumentBuilder documentBuilder, final URL url)
      throws ParserConfigurationException, IOException, SAXException {
    if (url != null) {
      final URLConnection connection = url.openConnection();
      connection.setUseCaches(false);

      try (InputStream input = connection.getInputStream()) {
        final Document document = documentBuilder.parse(input);
        document.getDocumentElement().normalize();
        return document;
      }
    } else {
      return null;
    }
  }

  private static String normalizePath(final ExternalContext externalContext, final String path) {
    if (path == null) {
      return null;
    }

    if (externalContext.getRequestPathInfo() != null) {
      final String prefix = externalContext.getRequestServletPath();
      if (path.startsWith(prefix)) {
        return path.substring(prefix.length());
      } else {
        return path;
      }
    } else {
      final String suffixInitParam = externalContext.getInitParameter(ViewHandler.FACELETS_SUFFIX_PARAM_NAME);
      final String suffix = suffixInitParam != null ? suffixInitParam : ViewHandler.DEFAULT_FACELETS_SUFFIX;

      if (path.endsWith(suffix)) {
        return path;
      } else {
        final int lastIndex = path.lastIndexOf('.');
        if (lastIndex >= 0) {
          return path.substring(0, lastIndex) + suffix;
        } else {
          return path;
        }
      }
    }
  }
}
