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
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigBuilder.class);

  private static final String WEB_INF_TOBAGO_CONFIG_XML = "WEB-INF/tobago-config.xml";
  private static final String META_INF_TOBAGO_CONFIG_XML = "META-INF/tobago-config.xml";
  /**
   * @deprecated Since 1.5.0
   */
  private static final String META_INF_TOBAGO_THEME_XML = "META-INF/tobago-theme.xml";

  private List<TobagoConfigFragment> list;

  private TobagoConfigBuilder(ServletContext servletContext) throws ServletException, IOException, SAXException {
    list = new ArrayList<TobagoConfigFragment>();
    configFromClasspath();
    configFromWebInf(servletContext);
    final TobagoConfigImpl tobagoConfig = mergeList();

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

  private void configFromWebInf(ServletContext servletContext) throws IOException, SAXException {

    final URL url = servletContext.getResource("/WEB-INF/tobago-config.xml");
    if (url != null) {
      list.add(new TobagoConfigParser().parse(url));
    }
  }

  private TobagoConfigImpl mergeList() {
// todo
    LOG.warn("Merge implementation in progress...)");

    TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.sort();
    TobagoConfigImpl result = new TobagoConfigImpl();

    for (TobagoConfigFragment fragment : list) {
      // default theme
      final String defaultTheme = fragment.getDefaultThemeName();
      if (defaultTheme != null) {
        result.setDefaultThemeName(defaultTheme);
      }

      // supported themes
      for (String supported : fragment.getSupportedThemeNames()) {
        result.addSupportedThemeName(supported);
      }

      // resource dirs
      for (String dir : fragment.getResourceDirs()) {
        result.addResourceDir(dir);
      }

      // renderers config
      // TODO: merging not implemented yet!!!
      result.setRenderersConfig(fragment.getRenderersConfig());

      // session secret
      if (fragment.getCreateSessionSecret() != null) {
        result.setCreateSessionSecret(fragment.getCreateSessionSecret());
      }
      if (fragment.getCheckSessionSecret() != null) {
        result.setCheckSessionSecret(fragment.getCheckSessionSecret());
      }

      result.setPreventFrameAttacks(fragment.isPreventFrameAttacks());

      for(String directive : fragment.getContentSecurityPolicy()) {
        result.addContentSecurityPolicy(directive);
      }

      // theme definition
      // todo
/*
      for (Theme theme : fragment.getThemeDefinitions()) {
        result.addThemeDefinition(theme);
      }
*/

      // url
      // todo???

    }
    return result;
  }

  private void configFromClasspath() throws ServletException {

    ThemeParser parser = new ThemeParser();
    try {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for '" + META_INF_TOBAGO_THEME_XML + "' and '" + META_INF_TOBAGO_CONFIG_XML + "'");
      }
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      List<URL> urls = new ArrayList<URL>();
      CollectionUtils.addAll(urls, classLoader.getResources(META_INF_TOBAGO_CONFIG_XML));
      CollectionUtils.addAll(urls, classLoader.getResources(META_INF_TOBAGO_THEME_XML));

      for (URL themeUrl : urls) {
        TobagoConfigFragment tobagoConfig;
        if (themeUrl.toString().endsWith(META_INF_TOBAGO_CONFIG_XML)) {
          tobagoConfig = new TobagoConfigParser().parse(themeUrl);
        } else {
          // the old way
          tobagoConfig = new TobagoConfigFragment();
          final ThemeImpl theme = parser.parse(themeUrl);
          tobagoConfig.addThemeDefinition(theme);
        }
        tobagoConfig.setUrl(themeUrl);
        String protocol = themeUrl.getProtocol();
        // tomcat uses jar
        // weblogic uses zip
        // IBM WebSphere uses wsjar
        if (!"jar".equals(protocol) && !"zip".equals(protocol) && !"wsjar".equals(protocol)) {
          LOG.warn("Unknown protocol '" + themeUrl + "'");
        }

        list.add(tobagoConfig);
      }
    } catch (Exception e) {
      String msg = "while loading ";
      LOG.error(msg, e);
      throw new ServletException(msg, e);
    }
  }


}
