/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 24.06.2003 08:53:35.
 * Id: $
 */
package org.apache.myfaces.tobago.config;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigParser {

  private static final Log LOG = LogFactory.getLog(TobagoConfigParser.class);

  public static void parse(ServletContext context, TobagoConfig tobagoConfig)
      throws IOException, SAXException, FacesException {

    Digester digester = new Digester();
    configure(tobagoConfig, digester);
    parse(context, digester);
  }

  private static Digester configure(TobagoConfig config, Digester digester) {

    digester.push(config);
    digester.setValidating(true);

    // theme-config
    digester.addCallMethod("tobago-config/theme-config/default-theme", "setDefaultThemeClass", 0);
    digester.addCallMethod("tobago-config/theme-config/supported-theme", "addSupportedThemeClass", 0);

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

    // resource dirs
    digester.addCallMethod("tobago-config/resource-dir", "addResourceDir", 0);

    // enable ajax
    digester.addCallMethod("tobago-config/enable-ajax", "setAjaxEnabled", 0);

    return digester;
  }

  // TODO: make it runnable without config file, using defaults
  private static void parse(ServletContext context, Digester digester)
      throws IOException, SAXException, FacesException {

    final String configPath = "/WEB-INF/tobago-config.xml";
    InputStream input = null;
    registerDtd(digester);
    try {
      input = context.getResourceAsStream(configPath);
      if (input != null) {
        digester.parse(input);
      } else {
          throw new FacesException(
              "No config file found: '" + configPath + "'. " +
              "Tobago can't run without configuration.");
      }
    } finally {
      IOUtils.closeQuietly(input);
    }
  }

  private static void registerDtd(Digester digester) {

    final String TOBAGO_CONFIG_DTD
        = "/org/apache/myfaces/tobago/config/tobago-config_1_0.dtd";
    URL url = TobagoConfigParser.class.getResource(TOBAGO_CONFIG_DTD);
    if (LOG.isDebugEnabled()) {
      LOG.debug("registering dtd: url=" + url);
    }
    if (null != url) {
      digester.register(
          "-//Atanion GmbH//DTD Tobago Config 1.0//EN",
          url.toString());
      digester.register(
          "-//The Apache Software Foundation//DTD Tobago Config 1.0//EN",
          url.toString());
    } else {
      LOG.warn(
          "unable to retrieve local DTD '" + TOBAGO_CONFIG_DTD
          + "'; trying external URL");
    }
  }

}
