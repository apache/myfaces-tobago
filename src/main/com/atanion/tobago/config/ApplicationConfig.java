/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.04.2004 09:10:45.
 * $Id$
 */
package com.atanion.tobago.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import java.util.List;
import java.util.Iterator;

public class ApplicationConfig {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ApplicationConfig.class);

// ///////////////////////////////////////////// attribute

  private String actionListener;

  private String navigationHandler;

  private String propertyResolver;

  private String stateManager;

  private String variableResolver;

  private String viewHandler;

  private LocaleConfig localeConfig;

// ///////////////////////////////////////////// constructor

  public ApplicationConfig() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("constructor of ApplicationConfig");
    }
  }
  
// ///////////////////////////////////////////// code

  protected static void propergate(
      List applicationConfigs, Application application)
      throws ClassNotFoundException, IllegalAccessException,
      InstantiationException {

    ClassLoader classLoader = ApplicationConfig.class.getClassLoader();
    Object instance;

//*    action-listener
//    default-render-kit-id
//    message-bundle
//*    navigation-handler
//*    view-handler
//*    state-manager
//*    property-resolver
//*    variable-resolver
//    locale-config

    for (Iterator i = applicationConfigs.iterator(); i.hasNext(); ) {
      ApplicationConfig config = (ApplicationConfig) i.next();

      if (config.actionListener != null) {
        instance = classLoader.loadClass(config.actionListener).newInstance();
        application.setActionListener((ActionListener) instance);
      }

      if (config.navigationHandler != null) {
        instance = classLoader.loadClass(config.navigationHandler).newInstance();
        application.setNavigationHandler((NavigationHandler) instance);
      }

      if (config.propertyResolver != null) {
        instance = classLoader.loadClass(config.propertyResolver).newInstance();
        application.setPropertyResolver((PropertyResolver) instance);
      }

      if (config.stateManager != null) {
        instance = classLoader.loadClass(config.stateManager).newInstance();
        application.setStateManager((StateManager) instance);
      }

      if (config.variableResolver != null) {
        instance = classLoader.loadClass(config.variableResolver).newInstance();
        application.setVariableResolver((VariableResolver) instance);
      }

      if (config.viewHandler != null) {
        instance = classLoader.loadClass(config.viewHandler).newInstance();
        application.setViewHandler((ViewHandler) instance);
      }

      if (config.localeConfig != null) {
        config.localeConfig.propergate(application);
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setActionListener(String actionListener) {

    if (LOG.isDebugEnabled()) {
      LOG.debug("actionListener = '" + actionListener + "'");
    }
    this.actionListener = actionListener;
  }

  public void setNavigationHandler(String navigationHandler) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("navigationHandler = '" + navigationHandler + "'");
    }

    this.navigationHandler = navigationHandler;
  }

  public void setPropertyResolver(String propertyResolver) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("propertyResolver = '" + propertyResolver + "'");
    }

    this.propertyResolver = propertyResolver;
  }

  public void setStateManager(String stateManager) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("stateManager = '" + stateManager + "'");
    }
    this.stateManager = stateManager;
  }

  public void setVariableResolver(String variableResolver) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("variableResolver = '" + variableResolver + "'");
    }
    this.variableResolver = variableResolver;
  }

  public void setViewHandler(String viewHandler) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("viewHandler = '" + viewHandler + "'");
    }
    this.viewHandler = viewHandler;
  }

  public void setLocaleConfig(LocaleConfig localeConfig) {
    this.localeConfig = localeConfig;
  }
}
