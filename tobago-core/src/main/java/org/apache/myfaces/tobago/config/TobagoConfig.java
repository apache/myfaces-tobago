package org.apache.myfaces.tobago.config;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.Theme;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TobagoConfig {
  private static final Log LOG = LogFactory.getLog(TobagoConfig.class);

  public static final String TOBAGO_CONFIG
      = "org.apache.myfaces.tobago.config.TobagoConfig";

  private List<Theme> supportedThemes;
  private Theme defaultTheme;
  private List<String> resourceDirs;
  private List<MappingRule> mappingRules;
  private boolean ajaxEnabled;


  public TobagoConfig() {
    supportedThemes = new ArrayList<Theme>();
    resourceDirs = new ArrayList<String>();
  }

  public void addMappingRule(MappingRule mappingRule) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("addMappingRule: {" + mappingRule + "}");
    }

    if (mappingRules == null) {
      mappingRules = new ArrayList<MappingRule>();
    }
    mappingRules.add(mappingRule);
  }

  public void setDefaultThemeClass(String name) {
    try {
      defaultTheme = (Theme) Class.forName(name).newInstance();
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
      Theme theme = (Theme) Class.forName(name).newInstance();
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

  public Iterator<MappingRule> getMappingRules() {
    if (mappingRules == null) {
      List<MappingRule> objects = Collections.emptyList();
      return objects.iterator();
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
    for (Theme theme : supportedThemes) {
      if (theme.getName().equals(name)) {
        return theme;
      }
    }
    LOG.debug("searching theme '" + name + "' not found. Using default: " + defaultTheme);
    return defaultTheme;
  }

  public List<Theme> getSupportedThemes() {
    return Collections.unmodifiableList(supportedThemes);
  }

  public void addResourceDir(String resourceDir) {
    LOG.info("resourceDir = '" + resourceDir + "'");
    resourceDirs.add(resourceDir);
  }

  public List<String> getResourceDirs() {
    return resourceDirs;
  }

  public boolean isAjaxEnabled() {
    return ajaxEnabled;
  }

  public void setAjaxEnabled(String value) {
    this.ajaxEnabled = Boolean.valueOf(value);
  }

  public Theme getDefaultTheme() {
    return defaultTheme;
  }
}

