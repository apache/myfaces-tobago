/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 11:06:46.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.context.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TobagoConfig {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(TobagoConfig.class);

  public static final String TOBAGO_CONFIG
      = "com.atanion.tobago.config.TobagoConfig";

// ----------------------------------------------------------------- attributes

  private List supportedThemes;
  private Theme defaultTheme;
  private List resourceDirs;
  private List mappingRules;

// ----------------------------------------------------------- business methods

  public TobagoConfig() {
    supportedThemes = new ArrayList();
    resourceDirs = new ArrayList();
  }

  public void addMappingRule(MappingRule mappingRule) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("addMappingRule: {" + mappingRule + "}");
    }

    if (mappingRules == null) {
      mappingRules = new ArrayList();
    }
    mappingRules.add(mappingRule);
  }

  public void setDefaultThemeClass(String name) {
    try {
      defaultTheme = (Theme)Class.forName(name).newInstance();
    } catch (Exception e) {
      String error = "Cannot create Theme from name: '" + name + "'";
      LOG.error(error, e);
      throw new RuntimeException(error, e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("name = '" + name + "'");
      LOG.debug("defaultTheme = '" + defaultTheme + "'");
    }
  }

  public void addSupportedThemeClass(String name) {
    try {
      Theme theme = (Theme)Class.forName(name).newInstance();
      supportedThemes.add(theme);
    } catch (Exception e) {
      String error = "Cannot create Theme from name: '" + name + "'";
      LOG.error(error, e);
      throw new RuntimeException(error, e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("name = '" + name + "'");
      LOG.debug("supportedThemes.last() = '" + supportedThemes.get(supportedThemes.size() - 1) + "'");
    }
  }

  public static TobagoConfig getInstance(FacesContext facesContext) {
    return (TobagoConfig) facesContext.getExternalContext()
        .getApplicationMap().get(TOBAGO_CONFIG);
  }

  public MappingRule getMappingRule(String requestUri) {
    for (Iterator i = getMappingRules(); i.hasNext();) {
      MappingRule rule = (MappingRule) i.next();
      if (rule.getRequestUri().equals(requestUri)) {
        return rule;
      }
    }
    return null;
  }

  public Iterator getMappingRules() {
    if (mappingRules == null) {
      return Collections.EMPTY_LIST.iterator();
    } else {
      return mappingRules.iterator();
    }
  }

  public Theme getTheme(String name) {
    if (name == null) {
      LOG.debug("searching theme: null");
      return defaultTheme;
    }
    if (defaultTheme.getName().equals(name)) {
      return defaultTheme;
    }
    for (Iterator i = supportedThemes.iterator(); i.hasNext();) {
      Theme theme = (Theme) i.next();
      if (theme.getName().equals(name)) {
        return theme;
      }
    }
    LOG.debug("searching theme '" + name + "' not found. Using default: " + defaultTheme);
    return defaultTheme;
  }

  public List getSupportedThemes() {
    return Collections.unmodifiableList(supportedThemes);
  }

  public void addResourceDir(String resourceDir) {

    LOG.info("resourceDir = '" + resourceDir + "'");
    resourceDirs.add(resourceDir);
  }

  public List getResourceDirs() {
    return resourceDirs;
  }

// ------------------------------------------------------------ getter + setter

  public Theme getDefaultTheme() {
    return defaultTheme;
  }
}

