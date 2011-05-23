package org.apache.myfaces.tobago.internal.config;

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

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.context.ThemeResources;
import org.apache.myfaces.tobago.context.ThemeScript;
import org.apache.myfaces.tobago.context.ThemeStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigParser {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigParser.class);

  private static final String TOBAGO_CONFIG_DTD_1_0 = "/org/apache/myfaces/tobago/config/tobago-config_1_0.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_29 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.29.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_30 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.30.dtd";
  private static final String TOBAGO_CONFIG_DTD_1_0_34 = "/org/apache/myfaces/tobago/config/tobago-config-1.0.34.dtd";
  @Deprecated
  private static final String TOBAGO_CONFIG_DTD_1_5 = "/org/apache/myfaces/tobago/config/tobago-config-1.5.dtd";
  private static final String TOBAGO_CONFIG_XSD_1_5 = "/org/apache/myfaces/tobago/config/tobago-config-1.5.xsd";

  private Digester digester;

  public TobagoConfigParser() {
    digester = new Digester();
    digester.setUseContextClassLoader(true);
    configure();
    registerDtds();
  }

  public TobagoConfigFragment parse(URL url) throws IOException, SAXException, FacesException {

    if (LOG.isInfoEnabled()) {
      LOG.info("Parsing configuration file: '{}'", url);
    }

    TobagoConfigFragment tobagoConfig = new TobagoConfigFragment();
    digester.push(tobagoConfig);
    InputStream inputStream = null;
    try {
      configureValidation(url);
      inputStream = url.openStream();
      digester.parse(inputStream);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    digester.pop();
    return tobagoConfig;
  }

  private void configureValidation(URL url) {
    // TODO: validating is turned of in case of a schema
    try {
      final String xml = IOUtils.toString(url.openStream());
      if (xml.indexOf("tobago-config-1.5.xsd") > 0) {
        digester.setValidating(false);
        return;
      }
    } catch (Exception e) {
      LOG.warn("Error while checking: '" + url + "'", e);
    }
    digester.setValidating(true);
  }

  private Digester configure() {

    // ordering
    digester.addCallMethod("tobago-config/name", "setName", 0);
    digester.addCallMethod("tobago-config/ordering/before/name", "addBefore", 0);
    digester.addCallMethod("tobago-config/ordering/after/name", "addAfter", 0);

    // theme-config
    digester.addCallMethod("tobago-config/theme-config/default-theme", "setDefaultThemeName", 0);
    digester.addCallMethod("tobago-config/theme-config/supported-theme", "addSupportedThemeName", 0);

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

    // renderer config
    digester.addObjectCreate("tobago-config/renderers", RenderersConfigImpl.class);
    digester.addSetNext("tobago-config/renderers", "setRenderersConfig");
    digester.addObjectCreate("tobago-config/renderers/renderer", RendererConfig.class);
    digester.addSetNext("tobago-config/renderers/renderer", "addRenderer");
    digester.addCallMethod("tobago-config/renderers/renderer/name", "setName", 0);
    digester.addCallMethod("tobago-config/renderers/renderer/supported-markup/markup", "addSupportedMarkup", 0);

    // theme definition
    digester.addObjectCreate("tobago-config/theme-definitions/theme-definition", ThemeImpl.class);
    digester.addSetNext("tobago-config/theme-definitions/theme-definition", "addThemeDefinition");
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/name", "setName", 0);
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/deprecated-name", "setDeprecatedName", 0);
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/display-name", "setDisplayName", 0);
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/resource-path", "setResourcePath", 0);
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/fallback", "setFallbackName", 0);
    digester.addObjectCreate("tobago-config/theme-definitions/theme-definition/renderers", RenderersConfigImpl.class);
    digester.addSetNext("tobago-config/theme-definitions/theme-definition/renderers", "setRenderersConfig");
    digester.addObjectCreate(
        "tobago-config/theme-definitions/theme-definition/renderers/renderer", RendererConfig.class);
    digester.addSetNext("tobago-config/theme-definitions/theme-definition/renderers/renderer", "addRenderer");
    digester.addCallMethod("tobago-config/theme-definitions/theme-definition/renderers/renderer/name", "setName", 0);
    digester.addCallMethod(
        "tobago-config/theme-definitions/theme-definition/renderers/renderer/supported-markup/markup",
        "addSupportedMarkup", 0);
    digester.addObjectCreate("tobago-config/theme-definitions/theme-definition/resources", ThemeResources.class);
    digester.addSetProperties("tobago-config/theme-definitions/theme-definition/resources");
    digester.addSetNext("tobago-config/theme-definitions/theme-definition/resources", "addResources");
    digester.addObjectCreate("tobago-config/theme-definitions/theme-definition/resources/script", ThemeScript.class);
    digester.addSetProperties("tobago-config/theme-definitions/theme-definition/resources/script");
    digester.addSetNext("tobago-config/theme-definitions/theme-definition/resources/script", "addScript");
    digester.addObjectCreate("tobago-config/theme-definitions/theme-definition/resources/style", ThemeStyle.class);
    digester.addSetProperties("tobago-config/theme-definitions/theme-definition/resources/style");
    digester.addSetNext("tobago-config/theme-definitions/theme-definition/resources/style", "addStyle");

    return digester;
  }

  private void registerDtds() {
    registerDtd("-//Atanion GmbH//DTD Tobago Config 1.0//EN", TOBAGO_CONFIG_DTD_1_0);
    registerDtd("-//The Apache Software Foundation//DTD Tobago Config 1.0//EN", TOBAGO_CONFIG_DTD_1_0);
    registerDtd("-//The Apache Software Foundation//DTD Tobago Config 1.0.29//EN", TOBAGO_CONFIG_DTD_1_0_29);
    registerDtd("-//The Apache Software Foundation//DTD Tobago Config 1.0.30//EN", TOBAGO_CONFIG_DTD_1_0_30);
    registerDtd("-//The Apache Software Foundation//DTD Tobago Config 1.0.34//EN", TOBAGO_CONFIG_DTD_1_0_34);
    registerDtd("-//The Apache Software Foundation//DTD Tobago Config 1.5//EN", TOBAGO_CONFIG_DTD_1_5);
    // todo: find a way to register the schema
    //    registerDtd("http://myfaces.apache.org/tobago/tobago-config", TOBAGO_CONFIG_XSD_1_5);
    //    registerXsd("http://myfaces.apache.org/tobago/tobago-config", TOBAGO_CONFIG_XSD_1_5);
  }

  private void registerDtd(String publicId, String entityUrl) {
    URL url = TobagoConfigParser.class.getResource(entityUrl);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Registering dtd: url='{}'", url);
    }
    if (null != url) {
      digester.register(publicId, url.toString());
    } else {
      LOG.warn("Unable to retrieve local DTD '" + entityUrl + "'; trying external URL");
    }
  }

/*
  private void registerXsd(String publicId, String entityUrl) {
    URL url = TobagoConfigParser.class.getResource(entityUrl);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Registering dtd: url='{}'", url);
    }
    if (null != url) {
      digester.setSchema(publicId);
    //  digester.setSchemaLanguage();
    } else {
      LOG.warn("Unable to retrieve local DTD '" + entityUrl + "'; trying external URL");
    }
  }
*/
}
