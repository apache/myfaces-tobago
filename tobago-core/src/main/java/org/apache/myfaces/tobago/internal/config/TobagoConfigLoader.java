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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

public class TobagoConfigLoader {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String WEB_INF_TOBAGO_CONFIG_XML = "/WEB-INF/tobago-config.xml";
  private static final String[] META_INF_TOBAGO_CONFIG_XML = {"META-INF/tobago-config.xml"};

  private TobagoConfigLoader() {
  }

  public static void load(
      final List<TobagoConfigFragment> fragments, final ServletContext servletContext, final String... alternative)
      throws URISyntaxException, SAXException, ParserConfigurationException, IOException, ServletException {

    TobagoConfigLoader loader = new TobagoConfigLoader();
    loader.loadFromWebInf(fragments, servletContext);
    loader.loadFromClasspath(fragments, alternative);
  }

  private void loadFromWebInf(
      final List<TobagoConfigFragment> configFragmentList, final ServletContext servletContext)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    if (servletContext != null) {
      final URL url = servletContext.getResource(WEB_INF_TOBAGO_CONFIG_XML);
      if (url != null) {
        configFragmentList.add(new TobagoConfigParser().parse(url));
      }
    } else {
      LOG.warn("No ServletContext to look for files in {}", WEB_INF_TOBAGO_CONFIG_XML);
    }
  }

  private void loadFromClasspath(final List<TobagoConfigFragment> configFragmentList, final String... alternative)
      throws ServletException {

    final String[] configFiles;
    if (ArrayUtils.isEmpty(alternative)) {
      configFiles = META_INF_TOBAGO_CONFIG_XML;
    } else {
      configFiles = alternative;
    }

    try {
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      for (String configFile : configFiles) {
        final Enumeration<URL> urls = classLoader.getResources(configFile);
        while (urls.hasMoreElements()) {
          final URL themeUrl = urls.nextElement();
          try {
            final TobagoConfigFragment fragment = new TobagoConfigParser().parse(themeUrl);
            fragment.setUrl(themeUrl);
            configFragmentList.add(fragment);

            // tomcat uses jar
            // weblogic uses zip
            // IBM WebSphere uses wsjar
            final String protocol = themeUrl.getProtocol();
            if (!"file".equals(protocol) && !"jar".equals(protocol)
                && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
              LOG.warn("Unknown protocol '" + themeUrl + "'");
            }
          } catch (final Exception e) {
            throw new Exception(e.getClass().getName() + " on themeUrl: " + themeUrl, e);
          }
        }
      }
    } catch (final Exception e) {
      final String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }
}
