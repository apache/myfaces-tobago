/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 08:53:35.
 * Id: $
 */
package com.atanion.tobago.config;

import org.xml.sax.SAXException;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;

public class FacesConfigParser {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(FacesConfigParser.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void init(InputStream[] configStreams, String dtdUrl)
      throws IOException, SAXException, IllegalAccessException,
      InstantiationException, ClassNotFoundException {


    FacesConfig config;
    config = new FacesConfig();

    for (int i = 0; i < configStreams.length; i++) {
      Digester digester = new Digester();
      configureDigester(config, digester);
      if (dtdUrl != null) {
        registerDtd(dtdUrl, digester);
      }
      LOG.debug("parsing config");
      parseConfig(configStreams[i], digester);
    }

    LOG.debug("propergate config");
    config.propagate();
  }

  private Digester configureDigester(FacesConfig config, Digester digester) {
    digester.push(config);
    digester.setValidating(true);

    digester.addObjectCreate("faces-config/factory", Factory.class);
    digester.addCallMethod(
        "faces-config/factory/application-factory", "setApplicationFactory", 0);
    digester.addCallMethod(
        "faces-config/factory/faces-context-factory", "setFacesContextFactory",
        0);
    digester.addCallMethod(
        "faces-config/factory/lifecycle-factory", "setLifecycleFactory", 0);
    digester.addCallMethod(
        "faces-config/factory/render-kit-factory", "setRenderKitFactory", 0);

    digester.addObjectCreate(
        "faces-config/application", ApplicationConfig.class);
    digester.addSetNext(
        "faces-config/application", "addApplication");
    digester.addCallMethod(
        "faces-config/application/action-listener", "setActionListener", 0);
    digester.addCallMethod(
        "faces-config/application/navigation-handler", "setNavigationHandler", 0);
    digester.addCallMethod(
        "faces-config/application/property-resolver", "setPropertyResolver", 0);
    digester.addCallMethod(
        "faces-config/application/state-manager", "setStateManager", 0);
    digester.addCallMethod(
        "faces-config/application/variable-resolver", "setVariableResolver", 0);
    digester.addCallMethod(
        "faces-config/application/view-handler", "setViewHandler", 0);
    digester.addObjectCreate(
        "faces-config/application/locale-config", LocaleConfig.class);
    digester.addSetNext(
        "faces-config/application/locale-config", "setLocaleConfig");
    digester.addCallMethod(
        "faces-config/application/locale-config/default-locale", "setDefaultLocale", 0);
    digester.addCallMethod(
        "faces-config/application/locale-config/supported-locale", "addSupportedLocale", 0);


    digester.addObjectCreate("faces-config/component", Component.class);
    digester.addSetNext("faces-config/component", "addComponent");
    digester.addCallMethod(
        "faces-config/component/component-type", "setComponentType", 0);
    digester.addCallMethod(
        "faces-config/component/component-class", "setComponentClass", 0);

    digester.addObjectCreate("faces-config/converter", Converter.class);
    digester.addSetNext("faces-config/converter", "addConverter");
    digester.addCallMethod(
        "faces-config/converter/converter-id", "setConverterId", 0);
    digester.addCallMethod(
        "faces-config/converter/converter-for-class", "setConverterForClass",
        0);
    digester.addCallMethod(
        "faces-config/converter/converter-class", "setConverterClass", 0);

    digester.addObjectCreate("faces-config/validator", Validator.class);
    digester.addSetNext("faces-config/validator", "addValidator");
    digester.addCallMethod(
        "faces-config/validator/validator-id", "setValidatorId", 0);
    digester.addCallMethod(
        "faces-config/validator/validator-class", "setValidatorClass", 0);

    digester.addObjectCreate(
        "faces-config/navigation-rule", NavigationRule.class);
    digester.addSetNext("faces-config/navigation-rule", "addNavigationRule");
    digester.addCallMethod(
        "faces-config/navigation-rule/from-view-id", "setFromViewId", 0);
    digester.addObjectCreate(
        "faces-config/navigation-rule/navigation-case", NavigationCase.class);
    digester.addSetNext(
        "faces-config/navigation-rule/navigation-case", "addNavigationCase");
    digester.addCallMethod(
        "faces-config/navigation-rule/navigation-case/from-action-ref",
        "setFromActionRef", 0);
    digester.addCallMethod(
        "faces-config/navigation-rule/navigation-case/from-outcome",
        "setFromOutcome", 0);
    digester.addCallMethod(
        "faces-config/navigation-rule/navigation-case/to-view-id",
        "setToViewId", 0);

    return digester;
  }

  private void parseConfig(InputStream input, Digester digester)
      throws IOException, SAXException {
    digester.parse(input);
  }

  private void registerDtd(String dtdUrl, Digester digester) {
    digester.register(
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN",
        dtdUrl);
  }

// ///////////////////////////////////////////// bean getter + setter

}
