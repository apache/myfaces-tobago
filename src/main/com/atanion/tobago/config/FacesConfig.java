/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 11:06:46.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.application.NavigationHandlerImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FacesConfig {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(FacesConfig.class);

// ///////////////////////////////////////////// attribute

  private List navigationRules;
  private List components;
  private List converters;
  private List validators;
  private List applicationConfigs;

// ///////////////////////////////////////////// constructor

  public FacesConfig() {
    this.navigationRules = new ArrayList();
    this.components = new ArrayList();
    this.converters = new ArrayList();
    this.validators = new ArrayList();
    this.applicationConfigs = new ArrayList();
  }

// ///////////////////////////////////////////// code

  public void propagate() throws ClassNotFoundException,
      IllegalAccessException, InstantiationException {

    ApplicationFactory factory = (ApplicationFactory)
        FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    Application application = factory.getApplication();

    // components
    for (int i = 0; i < components.size(); i++) {
      Component component = (Component) components.get(i);
      application.addComponent(
          component.getComponentType(),
          component.getComponentClass());
    }

    // converters
    Converter.addConvertersToApplication(converters, application);

    // validators
    Validator.addValidatorsToApplication(validators, application);

    // application
    ApplicationConfig.propergate(applicationConfigs, application);

    // navigation rules
    NavigationHandlerImpl navigationHandler
        = (NavigationHandlerImpl) application.getNavigationHandler();
    navigationHandler.setConfig(this);

  }

  public void addNavigationRule(NavigationRule navigationRule) {
    navigationRules.add(navigationRule);
  }

  public void addComponent(Component component) {
    components.add(component);
  }

  public void addConverter(Converter converter) {
    converters.add(converter);
  }

  public void addValidator(Validator validator) {
    validators.add(validator);
  }

  public void addApplication(ApplicationConfig applicationConfig) {
    applicationConfigs.add(applicationConfig);
  }

// ///////////////////////////////////////////// bean getter + setter

  public Iterator getNavigationRules() {
    if (navigationRules == null) {
      return Collections.EMPTY_LIST.iterator();
    } else {
      return navigationRules.iterator();
    }
  }
}
