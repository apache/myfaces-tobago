/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 08:53:35.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.context.Theme;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigParser {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(TobagoConfigParser.class);

// ///////////////////////////////////////////// attribute

  private Digester digester;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void init(ServletContext context) {

    TobagoConfig config = TobagoConfig.getInstance();
    digester = new Digester();
    configureNavigation(config);
    parse(context);
    config.propagate();
  }

  private Digester configureNavigation(TobagoConfig config) {

    digester.push(config);
    digester.setValidating(true);

    digester.addObjectCreate("tobago-config/theme", Theme.class);
    digester.addSetProperties("tobago-config/theme");
    digester.addSetNext("tobago-config/theme", "addTheme");
    digester.addCallMethod("tobago-config/theme/name", "setName", 0);
    digester.addCallMethod(
        "tobago-config/theme/display-name", "setDisplayName", 0);
    digester.addCallMethod("tobago-config/theme/fallback", "setFallback", 0);

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

    return digester;
  }

  private void parse(ServletContext context) {

    final String configPath = "/WEB-INF/tobago-config.xml";
    InputStream input = null;
    registerDtd(context);
    try {
      input = context.getResourceAsStream(configPath);
      if (input != null) {
        digester.parse(input);
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info(
              "No config file found: '" + configPath + "'. " +
              "Continuing without TobagoConfig");
        }
      }
    } catch (Throwable e) {
      LOG.error(configPath, e);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          ;
        }
      }
    }
  }

  private void registerDtd(ServletContext context) {

    final String TOBAGO_CONFIG_DTD
        = "/com/atanion/tobago/config/tobago-config_1_0.dtd";
    URL url = this.getClass().getResource(TOBAGO_CONFIG_DTD);
    LOG.debug("registering dtd: url=" + url);
    if (null != url) {
      digester.register(
          "-//Atanion GmbH//DTD Tobago Config 1.0//EN",
          url.toString());
    } else {
      LOG.warn(
          "unable to retrieve local DTD '" + TOBAGO_CONFIG_DTD
          + "'; trying external URL");
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
