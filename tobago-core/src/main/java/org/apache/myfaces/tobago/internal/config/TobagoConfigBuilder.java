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

import org.apache.commons.collections.CollectionUtils;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigBuilder.class);

  private static final String WEB_INF_TOBAGO_CONFIG_XML = "WEB-INF/tobago-config.xml";
  private static final String META_INF_TOBAGO_CONFIG_XML = "META-INF/tobago-config.xml";

  private List<TobagoConfigFragment> list;

  private TobagoConfigBuilder(ServletContext servletContext)
      throws ServletException, IOException, SAXException, ParserConfigurationException, URISyntaxException {
    list = new ArrayList<TobagoConfigFragment>();
    configFromClasspath();
    configFromWebInf(servletContext);
    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.sort();
    final TobagoConfigImpl tobagoConfig = sorter.merge();

    // todo: cleanup, use one central TobagoConfig, no singleton ResourceManager
    // resources
    tobagoConfig.initProjectState(servletContext);
    ResourceManagerFactory.init(servletContext, tobagoConfig);
    // prepare themes
    tobagoConfig.resolveThemes();

    tobagoConfig.initDefaultValidatorInfo();

    servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);
  }

  public static void init(ServletContext servletContext) {
    try {
      final TobagoConfigBuilder builder = new TobagoConfigBuilder(servletContext);
    } catch (Throwable e) {
      if (LOG.isErrorEnabled()) {
        String error = "Error while deploy process. Tobago can't be initialized! Application will not run!";
        LOG.error(error, e);
        throw new RuntimeException(error, e);
      }
    }
  }

  private void configFromWebInf(ServletContext servletContext)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final URL url = servletContext.getResource("/" + WEB_INF_TOBAGO_CONFIG_XML);
    if (url != null) {
      list.add(new TobagoConfigParser().parse(url));
    }
  }

  private void configFromClasspath() throws ServletException {

    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for '" + META_INF_TOBAGO_CONFIG_XML + "'");
      }
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final List<URL> urls = new ArrayList<URL>();
      CollectionUtils.addAll(urls, classLoader.getResources(META_INF_TOBAGO_CONFIG_XML));

      for (final URL themeUrl : urls) {
        final TobagoConfigFragment fragment = new TobagoConfigParser().parse(themeUrl);
        fragment.setUrl(themeUrl);
        list.add(fragment);

        // tomcat uses jar
        // weblogic uses zip
        // IBM WebSphere uses wsjar
        final String protocol = themeUrl.getProtocol();
        if (!"jar".equals(protocol) && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
          LOG.warn("Unknown protocol '" + themeUrl + "'");
        }
      }
    } catch (Exception e) {
      final String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }
}
