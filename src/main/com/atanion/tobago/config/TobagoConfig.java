/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 11:06:46.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.context.ResourceManager;
import com.atanion.tobago.context.Theme;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TobagoConfig {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoConfig.class);

// ///////////////////////////////////////////// attribute

  private static TobagoConfig instance;

  private List themes;
  private Theme defaultTheme;
  private List mappingRules;

// ///////////////////////////////////////////// constructor

  private TobagoConfig() {
  }

// ///////////////////////////////////////////// code

  public static void init() {
    instance = new TobagoConfig();
  }

  public static TobagoConfig getInstance() {
    return instance;
  }

  public void propagate() {

    if (themes == null) {
      String error = "No themes found!";
      FacesException e = new FacesException(error);
      LOG.error(error, e);
      throw e;
    }

    for (Iterator i = themes.iterator(); i.hasNext();) {
      Theme theme = (Theme) i.next();
      theme.init(this);
      if (theme.isDefault()) {
        defaultTheme = theme;
      }
    }
    if (defaultTheme == null) {
      if (themes.size() > 0) {
        defaultTheme = (Theme) themes.get(0);
        LOG.warn("No default theme defined. Using: " + defaultTheme);
      } else {
        LOG.error("No themes available!");
      }
    }

    ResourceManager.getInstance().setTobagoConfig(this);
  }

  public void addTheme(Theme theme) {
    if (themes == null) {
      themes = new ArrayList();
    }
    themes.add(theme);
    LOG.debug("adding theme " + theme);
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

  public Theme getTheme(String name) {
    if (name == null) {
      LOG.warn("searching theme: null");
      return defaultTheme;
    }
    for (Iterator i = getThemes().iterator(); i.hasNext();) {
      Theme theme = (Theme) i.next();
      if (theme.getName().equals(name)) {
        return theme;
      }
    }
    LOG.warn("searching theme '" + name + "' not found. Using default: " + defaultTheme);
    return defaultTheme;
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

// ///////////////////////////////////////////// bean getter + setter

  public List getThemes() {
    if (themes == null) {
      return Collections.EMPTY_LIST;
    } else {
      return themes;
    }
  }

  public Theme getDefaultTheme() {
    return defaultTheme;
  }

  public Iterator getMappingRules() {
    if (mappingRules == null) {
      return Collections.EMPTY_LIST.iterator();
    } else {
      return mappingRules.iterator();
    }
  }

}
