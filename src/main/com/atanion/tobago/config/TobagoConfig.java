/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 11:06:46.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.context.ResourceManager;
import com.atanion.tobago.context.Theme;
import com.atanion.tobago.context.ResourceManagerUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
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

  private List themes;
  private Theme defaultTheme;
  private List mappingRules;

// ----------------------------------------------------------- business methods

  public void addMappingRule(MappingRule mappingRule) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("addMappingRule: {" + mappingRule + "}");
    }

    if (mappingRules == null) {
      mappingRules = new ArrayList();
    }
    mappingRules.add(mappingRule);
  }

  public void addTheme(Theme theme) {
    if (themes == null) {
      themes = new ArrayList();
    }
    themes.add(theme);
    LOG.debug("adding theme " + theme);
  }

  public static TobagoConfig getInstance(FacesContext facesContext) {
    return (TobagoConfig) facesContext.getExternalContext()
        .getApplicationMap().get(TOBAGO_CONFIG);
  }

  public static TobagoConfig getInstance(ExternalContext externalContext) {
    return (TobagoConfig) externalContext
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

  public List getThemes() {
    if (themes == null) {
      return Collections.EMPTY_LIST;
    } else {
      return themes;
    }
  }

  // todo: refactor: drop this method
  public void propagate(ServletContext context) {
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

    ResourceManagerUtil.getResourceManager(context).setTobagoConfig(this);
  }

// ------------------------------------------------------------ getter + setter

  public Theme getDefaultTheme() {
    return defaultTheme;
  }
}

