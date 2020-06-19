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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Named
@ApplicationScoped
public class TobagoConfigBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String WEB_INF_TOBAGO_CONFIG_XML = "WEB-INF/tobago-config.xml";
  private static final String META_INF_TOBAGO_CONFIG_XML = "META-INF/tobago-config.xml";

  private final List<TobagoConfigFragment> configFragmentList = new ArrayList<>();

  @Inject
  private ServletContext servletContext;

  private TobagoConfigImpl tobagoConfig;

  @Produces
  public TobagoConfig buildTobagoConfig() {
    if (tobagoConfig!= null) {
      return tobagoConfig;
    } else {
      try {
        tobagoConfig = initializeConfigFromFiles();
        // prepare themes
        tobagoConfig.resolveThemes();
        tobagoConfig.initDefaultValidatorInfo();
        tobagoConfig.lock();

        servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, tobagoConfig);
        return tobagoConfig;
      } catch (final Exception e) {
        final String error = "Tobago can't be initialized! Application will not run correctly!";
        LOG.error(error, e);
        throw new TobagoConfigurationException(error, e);
      }
    }
  }

  protected TobagoConfigImpl initializeConfigFromFiles()
      throws ServletException, IOException, SAXException, ParserConfigurationException, URISyntaxException {
    configFromClasspath();
    configFromWebInf();
    final TobagoConfigSorter sorter = new TobagoConfigSorter(configFragmentList);
    final TobagoConfigMerger merger = new TobagoConfigMerger(sorter.topologicalSort());
    return merger.merge();
  }

  private void configFromWebInf()
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final URL url = servletContext.getResource("/" + WEB_INF_TOBAGO_CONFIG_XML);
    if (url != null) {
      configFragmentList.add(new TobagoConfigParser().parse(url));
    }
  }

  private void configFromClasspath() throws ServletException {

    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for '" + META_INF_TOBAGO_CONFIG_XML + "'");
      }
      final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      final Enumeration<URL> urls = classLoader.getResources(META_INF_TOBAGO_CONFIG_XML);
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
          if (!"file".equals(protocol) && !"jar".equals(protocol) && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
            LOG.warn("Unknown protocol '" + themeUrl + "'");
          }
        } catch (final Exception e) {
          throw new Exception(e.getClass().getName() + " on themeUrl: " + themeUrl, e);
        }
      }
    } catch (final Exception e) {
      final String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }
}
