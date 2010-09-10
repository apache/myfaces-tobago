package org.apache.myfaces.tobago.config;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.RenderersConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TobagoConfig {
  private static final Log LOG = LogFactory.getLog(TobagoConfig.class);

  public static final String TOBAGO_CONFIG
      = "org.apache.myfaces.tobago.config.TobagoConfig";

  private List<Theme> supportedThemes;
  private List<String> supportedThemeNames;
  private Theme defaultTheme;
  private String defaultThemeName;
  private List<String> resourceDirs;
  private List<MappingRule> mappingRules;
  private boolean ajaxEnabled;
  private boolean fixResourceOrder;
  private Map<String, Theme> availableTheme;
  private RenderersConfig renderersConfig;


  public TobagoConfig() {
    supportedThemeNames = new ArrayList<String>();
    supportedThemes = new ArrayList<Theme>();
    resourceDirs = new ArrayList<String>();
    ajaxEnabled = true;
    fixResourceOrder = false;
  }

  public void addMappingRule(MappingRule mappingRule) {
    Deprecation.LOG.warn("mapping rules are deprecated");
    if (LOG.isDebugEnabled()) {
      LOG.debug("addMappingRule: {" + mappingRule + "}");
    }

    if (mappingRules == null) {
      mappingRules = new ArrayList<MappingRule>();
    }
    mappingRules.add(mappingRule);
  }

  public void addSupportedThemeName(String name) {
    supportedThemeNames.add(name);
  }

  public void resolveThemes() {

    defaultTheme = availableTheme.get(defaultThemeName);
    checkThemeIsAvailable(defaultThemeName, defaultTheme);
    if (LOG.isDebugEnabled()) {
      LOG.debug("name = '" + defaultThemeName + "'");
      LOG.debug("defaultTheme = '" + defaultTheme + "'");
    }

    for (String name : supportedThemeNames) {
      Theme theme = availableTheme.get(name);
      checkThemeIsAvailable(name, theme);
      supportedThemes.add(theme);
      if (LOG.isDebugEnabled()) {
        LOG.debug("name = '" + name + "'");
        LOG.debug("supportedThemes.last() = '" + supportedThemes.get(supportedThemes.size() - 1) + "'");
      }
    }
  }

  private void checkThemeIsAvailable(String name, Theme theme) {
    if (theme == null) {
      String error = "Theme not found! name: '" + name + "'. "
          + "Please ensure you have a tobago-theme.xml file in your "
          + "theme jar. Found the following themes: " + availableTheme.keySet();
      LOG.error(error);
      throw new RuntimeException(error);
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
    LOG.debug("searching theme '" + name + "' not found. "
        + "Using default: " + defaultTheme);
    return defaultTheme;
  }

  public void setDefaultThemeName(String defaultThemeName) {
    this.defaultThemeName = defaultThemeName;
  }

  public List<Theme> getSupportedThemes() {
    return Collections.unmodifiableList(supportedThemes);
  }

  public void addResourceDir(String resourceDir) {
    if (!resourceDirs.contains(resourceDir)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("adding resourceDir = '" + resourceDir + "'");
      }
      resourceDirs.add(resourceDir);
    }
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

  public boolean isFixResourceOrder() {
    return fixResourceOrder;
  }

  public void setFixResourceOrder(String fixResourceOrder) {
    this.fixResourceOrder = Boolean.valueOf(fixResourceOrder);
  }

  @Deprecated
  public void setLoadThemesFromClasspath(String loadThemesFromClasspath) {
    Deprecation.LOG.error("Deprecated: setting load-theme-resources-from-classpath is "
        + "no longer supported");
  }

  public Theme getDefaultTheme() {
    return defaultTheme;
  }

  public void setAvailableThemes(Map<String, Theme> availableTheme) {
    this.availableTheme = availableTheme;
  }

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  public void setRenderersConfig(RenderersConfig renderersConfig) {
    this.renderersConfig = renderersConfig;
  }
}

