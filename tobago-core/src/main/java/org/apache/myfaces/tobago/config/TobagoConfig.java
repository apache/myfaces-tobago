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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.context.RenderersConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.JndiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TobagoConfig {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfig.class);

  public static final String TOBAGO_CONFIG = "org.apache.myfaces.tobago.config.TobagoConfig";

  private List<Theme> supportedThemes;
  private List<String> supportedThemeNames;
  private Theme defaultTheme;
  private String defaultThemeName;
  private List<String> resourceDirs;
  private Map<String, Theme> availableThemes;
  private RenderersConfig renderersConfig;
  private ProjectStage projectStage;

  public TobagoConfig() {
    supportedThemeNames = new ArrayList<String>();
    supportedThemes = new ArrayList<Theme>();
    resourceDirs = new ArrayList<String>();
  }

  public void addSupportedThemeName(String name) {
    supportedThemeNames.add(name);
  }
  // TODO one init method
  public void resolveThemes() {
    if (defaultThemeName != null) {
      defaultTheme = availableThemes.get(defaultThemeName);
      checkThemeIsAvailable(defaultThemeName, defaultTheme);
      if (LOG.isDebugEnabled()) {
        LOG.debug("name = '{}'", defaultThemeName);
        LOG.debug("defaultTheme = '{}'", defaultTheme);
      }
    } else {
      int deep = 0;
      for (Map.Entry<String, Theme> entry : availableThemes.entrySet()) {
        Theme theme = entry.getValue();
        if (theme.getFallbackList().size() > deep) {
          defaultTheme = theme;
          deep = theme.getFallbackList().size();
        }
      }
      if (defaultTheme == null) {
        String error = "Did not found any theme! "
            + "Please ensure you have a tobago-theme.xml file in your "
            + "theme jar. Please add a theme jar to your WEB-INF/lib";
        LOG.error(error);
        throw new RuntimeException(error);
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info("Using default Theme {}", defaultTheme.getName());
        }
      }
    }
    if (!supportedThemeNames.isEmpty()) {
      for (String name : supportedThemeNames) {
        Theme theme = availableThemes.get(name);
        checkThemeIsAvailable(name, theme);
        supportedThemes.add(theme);
        if (LOG.isDebugEnabled()) {
          LOG.debug("name = '{}'",  name);
          LOG.debug("supportedThemes.last() = '{}'", supportedThemes.get(supportedThemes.size() - 1));
        }
      }
    }
  }

  private void checkThemeIsAvailable(String name, Theme theme) {
    if (theme == null) {
      String error = "Theme not found! name: '" + name + "'. "
          + "Please ensure you have a tobago-theme.xml file in your "
          + "theme jar. Found the following themes: " + availableThemes.keySet();
      LOG.error(error);
      throw new RuntimeException(error);
    }
  }

  public static TobagoConfig getInstance(FacesContext facesContext) {
    return (TobagoConfig) facesContext.getExternalContext().getApplicationMap().get(TOBAGO_CONFIG);
  }

  public static TobagoConfig getInstance(ServletContext servletContext) {
    return (TobagoConfig) servletContext.getAttribute(TOBAGO_CONFIG);
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
    LOG.debug("searching theme '{}' not found. Using default: {}", name, defaultTheme);
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
        LOG.info("adding resourceDir = '{}'", resourceDir);
      }
      resourceDirs.add(resourceDir);
    }
  }

  public List<String> getResourceDirs() {
    return resourceDirs;
  }

  /** @deprecated */
  @Deprecated
  public boolean isAjaxEnabled() {
    Deprecation.LOG.warn("Ajax is always enabled!");
    return true;
  }

  /** @deprecated */
  @Deprecated
  public void setAjaxEnabled(String value) {
    Deprecation.LOG.error("Ajax is always enabled!");
  }

  public Theme getDefaultTheme() {
    return defaultTheme;
  }

  public void setAvailableThemes(Map<String, Theme> availableThemes) {
    this.availableThemes = availableThemes;
    for (Theme theme : this.availableThemes.values()) {
      addResourceDir(theme.getResourcePath());
    }
  }

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  public void setRenderersConfig(RenderersConfig renderersConfig) {
    this.renderersConfig = renderersConfig;
  }

  public ProjectStage getProjectStage() {
    return projectStage;
  }
  // TODO one init method
  public void initProjectState(ServletContext servletContext) {
    String stageName = null;
    try {
      Context ctx = new InitialContext();
      Object obj = JndiUtils.getJndiProperty(ctx, "jsf", "ProjectStage");
      if (obj != null) {
        if (obj instanceof String) {
          stageName = (String) obj;
        } else {
          LOG.warn("JNDI lookup for key {} should return a java.lang.String value",
              ProjectStage.PROJECT_STAGE_JNDI_NAME);
        }
      }
    } catch (NamingException e) {
      // ignore
    }

    if (stageName == null) {
      stageName = servletContext.getInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME);
    }

    if (stageName == null) {
      stageName = System.getProperty("org.apache.myfaces.PROJECT_STAGE");
    }

    if (stageName != null) {
      try {
        projectStage = ProjectStage.valueOf(stageName);
      } catch (IllegalArgumentException e) {
        LOG.error("Couldn't discover the current project stage", e);
      }
    }
    if (projectStage == null) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Couldn't discover the current project stage, using {}", ProjectStage.Production);
      }
      projectStage = ProjectStage.Production;
    }
  }

  @Deprecated
  public void setFixResourceOrder(String value) {
    Deprecation.LOG.error("Config fix-resource-order not longer supported. (Is always activated).");
  }

  @Deprecated
  public void setFixLayoutTransparency(String value) {
    Deprecation.LOG.error("Config fix-layout-transparency not longer supported. (Is always activated).");
  }
}
