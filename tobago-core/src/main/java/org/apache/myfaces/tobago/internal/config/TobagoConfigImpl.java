/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.internal.util.JndiUtils;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Implementation of the Tobago configuration.
 * </p>
 * <p>
 * All setters must are protected, so EL can't modify this config.
 * </p>
 */
public class TobagoConfigImpl extends TobagoConfig {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigImpl.class);

  private List<Theme> supportedThemes;
  private List<String> supportedThemeNames;
  private Theme defaultTheme;
  private String defaultThemeName;
  private List<String> resourceDirs;
  private Map<String, ThemeImpl> availableThemes;
  private RenderersConfig renderersConfig;
  private ProjectStage projectStage;
  private boolean createSessionSecret;
  private boolean checkSessionSecret;
  private boolean preventFrameAttacks;
  private ContentSecurityPolicy contentSecurityPolicy;
  private boolean setNosniffHeader;
  private Map<String, String> defaultValidatorInfo;
  private Sanitizer sanitizer;
  private Map<String, String> mimeTypes;

  private boolean unmodifiable = false;

  protected TobagoConfigImpl() {
    supportedThemeNames = new ArrayList<String>();
    supportedThemes = new ArrayList<Theme>();
    availableThemes = new HashMap<String, ThemeImpl>();
    resourceDirs = new ArrayList<String>();
    createSessionSecret = true;
    checkSessionSecret = true;
    preventFrameAttacks = true;
    setNosniffHeader = true;
    contentSecurityPolicy = new ContentSecurityPolicy(ContentSecurityPolicy.Mode.OFF.getValue());
    mimeTypes = new HashMap<String, String>();
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  protected void lock() {
    unmodifiable = true;
    supportedThemes = Collections.unmodifiableList(supportedThemes);
    for (Theme theme : supportedThemes) {
      ((ThemeImpl) theme).lock();
    }
    supportedThemeNames = Collections.unmodifiableList(supportedThemeNames);
    resourceDirs = Collections.unmodifiableList(resourceDirs);
    availableThemes = Collections.unmodifiableMap(availableThemes);

    if (renderersConfig instanceof RenderersConfigImpl) {
      ((RenderersConfigImpl) renderersConfig).lock();
    }
    contentSecurityPolicy.lock();

    mimeTypes = Collections.unmodifiableMap(mimeTypes);
  }

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new RuntimeException("The configuration must not be changed after initialization!");
    }
  }

  protected void addSupportedThemeName(final String name) {
    checkLocked();
    supportedThemeNames.add(name);
  }

  // TODO one init method
  protected void resolveThemes() {
    checkLocked();

    for (final Theme theme : availableThemes.values()) {
      addResourceDir(theme.getResourcePath());
    }

    if (defaultThemeName != null) {
      defaultTheme = availableThemes.get(defaultThemeName);
      checkThemeIsAvailable(defaultThemeName, defaultTheme);
      if (LOG.isDebugEnabled()) {
        LOG.debug("name = '{}'", defaultThemeName);
        LOG.debug("defaultTheme = '{}'", defaultTheme);
      }
    } else {
      int deep = 0;
      for (final Map.Entry<String, ThemeImpl> entry : availableThemes.entrySet()) {
        final Theme theme = entry.getValue();
        if (theme.getFallbackList().size() > deep) {
          defaultTheme = theme;
          deep = theme.getFallbackList().size();
        }
      }
      if (defaultTheme == null) {
        final String error = "Did not found any theme! "
            + "Please ensure you have a tobago-config.xml with a theme-definition in your "
            + "theme JAR. Please add a theme JAR to your WEB-INF/lib";
        LOG.error(error);
        throw new RuntimeException(error);
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info("Using default Theme {}", defaultTheme.getName());
        }
      }
    }
    if (!supportedThemeNames.isEmpty()) {
      for (final String name : supportedThemeNames) {
        final Theme theme = availableThemes.get(name);
        checkThemeIsAvailable(name, theme);
        supportedThemes.add(theme);
        if (LOG.isDebugEnabled()) {
          LOG.debug("name = '{}'", name);
          LOG.debug("supportedThemes.last() = '{}'", supportedThemes.get(supportedThemes.size() - 1));
        }
      }
    }
  }

  private void checkThemeIsAvailable(final String name, final Theme theme) {
    if (theme == null) {
      final String error = "Theme not found! name: '" + name + "'. "
          + "Please ensure you have a tobago-config.xml with a theme-definition in your "
          + "theme JAR. Found the following themes: " + availableThemes.keySet();
      LOG.error(error);
      throw new RuntimeException(error);
    }
  }

  @Override
  public Theme getTheme(final String name) {
    if (name == null) {
      LOG.debug("searching theme: null");
      return defaultTheme;
    }
    if (defaultTheme.getName().equals(name)) {
      return defaultTheme;
    }
    for (final Theme theme : supportedThemes) {
      if (theme.getName().equals(name)) {
        return theme;
      }
    }
    LOG.debug("searching theme '{}' not found. Using default: {}", name, defaultTheme);
    return defaultTheme;
  }

  protected void setDefaultThemeName(final String defaultThemeName) {
    checkLocked();
    this.defaultThemeName = defaultThemeName;
  }

  @Override
  public List<Theme> getSupportedThemes() {
    return supportedThemes;
  }

  protected void addResourceDir(final String resourceDir) {
    checkLocked();
    if (!resourceDirs.contains(resourceDir)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("adding resourceDir = '{}'", resourceDir);
      }
      resourceDirs.add(0, resourceDir);
    }
  }

  public List<String> getResourceDirs() {
    return resourceDirs;
  }

  @Override
  public Theme getDefaultTheme() {
    return defaultTheme;
  }

  protected void addAvailableTheme(ThemeImpl availableTheme) {
    checkLocked();
    availableThemes.put(availableTheme.getName(), availableTheme);
  }

  public Map<String, ThemeImpl> getAvailableThemes() {
    return availableThemes;
  }

  protected RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  protected void setRenderersConfig(final RenderersConfig renderersConfig) {
    checkLocked();
    this.renderersConfig = renderersConfig;
  }

  @Override
  public ProjectStage getProjectStage() {
    return projectStage;
  }

  // TODO one init method
  protected void initProjectState(final ServletContext servletContext) {
    checkLocked();
    String stageName = null;
    try {
      final Context ctx = new InitialContext();
      final Object obj = JndiUtils.getJndiProperty(ctx, "jsf", "ProjectStage");
      if (obj != null) {
        if (obj instanceof String) {
          stageName = (String) obj;
        } else {
          LOG.warn("JNDI lookup for key {} should return a java.lang.String value",
              ProjectStage.PROJECT_STAGE_JNDI_NAME);
        }
      }
    } catch (final NamingException e) {
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
      } catch (final IllegalArgumentException e) {
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

  protected synchronized void initDefaultValidatorInfo() {
    if (defaultValidatorInfo != null) {
      checkLocked();
    }
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    if (facesContext != null) {
      try {
        final Application application = facesContext.getApplication();
        final Map<String, String> map = application.getDefaultValidatorInfo();
        if (map.size() > 0) {
          defaultValidatorInfo = Collections.unmodifiableMap(map);
        } else {
          defaultValidatorInfo = Collections.emptyMap();
        }
      } catch (final Exception e) {
        LOG.error("Can't initialize default validators (this happens with JBoss GateIn 3.6.0).", e);
        defaultValidatorInfo = Collections.emptyMap();
      }
    }
  }

  @Override
  public boolean isCreateSessionSecret() {
    return createSessionSecret;
  }

  protected void setCreateSessionSecret(final boolean createSessionSecret) {
    checkLocked();
    this.createSessionSecret = createSessionSecret;
  }

  @Override
  public boolean isCheckSessionSecret() {
    return checkSessionSecret;
  }

  protected void setCheckSessionSecret(final boolean checkSessionSecret) {
    checkLocked();
    this.checkSessionSecret = checkSessionSecret;
  }


  @Override
  public boolean isPreventFrameAttacks() {
    return preventFrameAttacks;
  }

  protected void setPreventFrameAttacks(final boolean preventFrameAttacks) {
    checkLocked();
    this.preventFrameAttacks = preventFrameAttacks;
  }

  @Override
  public ContentSecurityPolicy getContentSecurityPolicy() {
    return contentSecurityPolicy;
  }

  @Override
  public boolean isSetNosniffHeader() {
    return setNosniffHeader;
  }

  protected void setSetNosniffHeader(final boolean setNosniffHeader) {
    checkLocked();
    this.setNosniffHeader = setNosniffHeader;
  }

  public Map<String, String> getDefaultValidatorInfo() {
    // TODO: if the startup hasn't found a FacesContext and Application, this may depend on the order of the listeners.
    if (defaultValidatorInfo == null) {
      initDefaultValidatorInfo();
    }
    return defaultValidatorInfo;
  }

  @Override
  public Sanitizer getSanitizer() {
    return sanitizer;
  }

  protected void setSanitizer(Sanitizer sanitizer) {
    checkLocked();
    this.sanitizer = sanitizer;
  }

  @Override
  public Map<String, String> getMimeTypes() {
    return mimeTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public boolean isClassicDateTimePicker() {
    return false;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("TobagoConfigImpl{");
    builder.append("\nsupportedThemes=[");
    for (final Theme supportedTheme : supportedThemes) {
      builder.append(supportedTheme.getName());
      builder.append(", ");
    }
    builder.append("], \ndefaultTheme=");
    builder.append(defaultTheme != null ? defaultTheme.getName() : null);
    builder.append(", \nresourceDirs=");
    builder.append(resourceDirs);
    builder.append(", \navailableThemes=");
    builder.append(availableThemes.keySet());
    builder.append(", \nprojectStage=");
    builder.append(projectStage);
    builder.append(", \ncreateSessionSecret=");
    builder.append(createSessionSecret);
    builder.append(", \ncheckSessionSecret=");
    builder.append(checkSessionSecret);
    builder.append(", \npreventFrameAttacks=");
    builder.append(preventFrameAttacks);
    builder.append(", \ncontentSecurityPolicy=");
    builder.append(contentSecurityPolicy);
    builder.append(", \nsetNosniffHeader=");
    builder.append(setNosniffHeader);
    builder.append(", \ndefaultValidatorInfo=");
    builder.append(defaultValidatorInfo);
    builder.append(", \nsanitizer=");
    builder.append(sanitizer);
    // to see only different (ignore alternative names for the same theme)
    builder.append(", \nthemes=");
    final Set<Theme> all = new HashSet<Theme>(availableThemes.values());
    builder.append(all);
    builder.append(", \nmimeTypes=");
    builder.append(mimeTypes);
    builder.append('}');
    return builder.toString();
  }
}
