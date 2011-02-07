package org.apache.myfaces.tobago.config;

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

/*
 * Created 24.06.2003 08:53:35.
 * $Id$
 */

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.MarkupConfig;
import org.apache.myfaces.tobago.context.RendererConfig;
import org.apache.myfaces.tobago.context.RenderersConfigImpl;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigParser {

  private static final Log LOG = LogFactory.getLog(TobagoConfigParser.class);
  private static final String TOBAGO_CONFIG_DTD_1_0 = "/org/apache/myfaces/tobago/config/tobago-config_1_0.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_29 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.29.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_30 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.30.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_34 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.34.dtd";

  public TobagoConfig parse(ServletContext context)
      throws IOException, SAXException, FacesException {

    TobagoConfig tobagoConfig = new TobagoConfig();
    Digester digester = configure(tobagoConfig);
    parse(context, digester);
    return tobagoConfig;
  }

  private Digester configure(TobagoConfig config) {
    Digester digester = new Digester();
    digester.setUseContextClassLoader(true);
    digester.push(config);
    digester.setValidating(true);

    // theme-config
    digester.addCallMethod("tobago-config/theme-config/default-theme", "setDefaultThemeName", 0);
    digester.addCallMethod("tobago-config/theme-config/supported-theme", "addSupportedThemeName", 0);

    // mapping rules
    digester.addObjectCreate("tobago-config/mapping-rule", MappingRule.class);
    digester.addSetNext("tobago-config/mapping-rule", "addMappingRule");
    digester.addCallMethod(
        "tobago-config/mapping-rule/request-uri", "setRequestUri", 0);
    digester.addCallMethod(
        "tobago-config/mapping-rule/forward-uri", "setForwardUri", 0);
    digester.addObjectCreate(
        "tobago-config/mapping-rule/attribute", Attribute.class);
    digester.addSetNext(
        "tobago-config/mapping-rule/attribute", "addAttribute");
    digester.addCallMethod(
        "tobago-config/mapping-rule/attribute/key", "setKey", 0);
    digester.addCallMethod(
        "tobago-config/mapping-rule/attribute/value", "setValue", 0);

    // XXX: deprecated! will ever be true (will be removed in next release after 1.0.7)
    digester.addCallMethod("tobago-config/load-theme-resources-from-classpath", "setLoadThemesFromClasspath", 0);

    // resource dirs
    digester.addCallMethod("tobago-config/resource-dir", "addResourceDir", 0);

    // enable ajax
    digester.addCallMethod("tobago-config/ajax-enabled", "setAjaxEnabled", 0);

    // see bug TOBAGO-912
    digester.addCallMethod("tobago-config/fix-resource-order", "setFixResourceOrder", 0);

    // see bug TOBAGO-916
    digester.addCallMethod("tobago-config/fix-layout-transparency", "setFixLayoutTransparency", 0);

    // session secret
    digester.addCallMethod("tobago-config/create-session-secret", "setCreateSessionSecret", 0);
    digester.addCallMethod("tobago-config/check-session-secret", "setCheckSessionSecret", 0);

    digester.addObjectCreate("tobago-config/renderers", RenderersConfigImpl.class);
    digester.addSetNext("tobago-config/renderers", "setRenderersConfig");
    digester.addObjectCreate("tobago-config/renderers/renderer", RendererConfig.class);
    digester.addSetNext("tobago-config/renderers/renderer", "addRenderer");
    digester.addCallMethod("tobago-config/renderers/renderer/name", "setName", 0);
    digester.addObjectCreate("tobago-config/renderers/renderer/supported-markup", MarkupConfig.class);
    digester.addSetNext("tobago-config/renderers/renderer/supported-markup", "setMarkupConfig");
    digester.addCallMethod("tobago-config/renderers/renderer/supported-markup/markup", "addMarkup", 0);

    return digester;
  }

  // TODO: make it runnable without config file, using defaults
  private void parse(ServletContext context, Digester digester)
      throws IOException, SAXException, FacesException {

    String configPath = "/WEB-INF/tobago-config.xml";
    InputStream input = null;
    registerDtds(digester);
    try {
      input = context.getResourceAsStream(configPath);
      if (input != null) {
        digester.parse(input);
      } else {
        throw new FacesException(
            "No config file found: '" + configPath + "'. Tobago can't run without configuration.");
      }
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  private void registerDtds(Digester digester) {
    registerDtd(digester, "-//Atanion GmbH//DTD Tobago Config 1.0//EN", TOBAGO_CONFIG_DTD_1_0);
    registerDtd(digester, "-//The Apache Software Foundation//DTD Tobago Config 1.0//EN", TOBAGO_CONFIG_DTD_1_0);
    registerDtd(digester, "-//The Apache Software Foundation//DTD Tobago Config 1.0.29//EN", TOBAGO_CONFIG_DTD_1_0_29);
    registerDtd(digester, "-//The Apache Software Foundation//DTD Tobago Config 1.0.30//EN", TOBAGO_CONFIG_DTD_1_0_30);
    registerDtd(digester, "-//The Apache Software Foundation//DTD Tobago Config 1.0.34//EN", TOBAGO_CONFIG_DTD_1_0_34);
  }

  private void registerDtd(Digester digester, String publicId, String entityUrl) {
    URL url = TobagoConfigParser.class.getResource(entityUrl);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Registering dtd: url='" + url + "'");
    }
    if (null != url) {
      digester.register(publicId, url.toString());
    } else {
      LOG.warn("Unable to retrieve local DTD '" + entityUrl + "'; trying external URL");
    }
  }
}
