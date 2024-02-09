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

package org.apache.myfaces.tobago.config;

import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
import org.apache.myfaces.tobago.internal.config.ContentSecurityPolicy;
import org.apache.myfaces.tobago.internal.config.SecurityAnnotation;
import org.apache.myfaces.tobago.internal.config.TobagoConfigFragment;
import org.apache.myfaces.tobago.internal.config.TobagoConfigLoader;
import org.apache.myfaces.tobago.internal.config.TobagoConfigMerger;
import org.apache.myfaces.tobago.internal.config.TobagoConfigSorter;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TobagoConfig {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String TOBAGO_CONFIG = "org.apache.myfaces.tobago.config.TobagoConfig";

  private List<Theme> supportedThemes;
  private List<String> supportedThemeNames;
  private Theme defaultTheme;
  private String defaultThemeName;
  private Map<String, ThemeImpl> availableThemes;
  private boolean themeCookie;
  private boolean themeSession;
  private boolean createSessionSecret;
  private boolean checkSessionSecret;
  private boolean preventFrameAttacks;
  private final ContentSecurityPolicy contentSecurityPolicy;
  private SecurityAnnotation securityAnnotation;
  private boolean setNosniffHeader;
  private Map<String, String> defaultValidatorInfo;
  private Sanitizer sanitizer;
  private boolean decodeLineFeed;
  private Map<String, String> mimeTypes;
  private boolean enableTobagoExceptionHandler;

  private boolean locked = false;

  public static TobagoConfig getInstance(final FacesContext facesContext) {
    final Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();

    TobagoConfig tobagoConfig = (TobagoConfig) applicationMap.get(TOBAGO_CONFIG);
    if (tobagoConfig != null) {
      return tobagoConfig;
    }

    tobagoConfig = new TobagoConfig((ServletContext) facesContext.getExternalContext().getContext());
    applicationMap.put(TOBAGO_CONFIG, tobagoConfig);

    return tobagoConfig;
  }

  public static TobagoConfig getInstance(final ServletContext servletContext) {
    TobagoConfig tobagoConfig = (TobagoConfig) servletContext.getAttribute(TOBAGO_CONFIG);
    if (tobagoConfig != null) {
      return tobagoConfig;
    }

    tobagoConfig = new TobagoConfig(servletContext);
    servletContext.setAttribute(TOBAGO_CONFIG, tobagoConfig);

    return tobagoConfig;
  }

  /**
   * @param servletContext From the container. If null, the WEB-INF/tobago-config.xml will be ignored.
   * @param alternative    Alternative tobago-config-files, only needed for testing.
   */
  public TobagoConfig(final ServletContext servletContext, final String... alternative) {

    supportedThemeNames = new ArrayList<>();
    supportedThemes = new ArrayList<>();
    availableThemes = new HashMap<>();
    themeCookie = false;
    themeSession = true;
    createSessionSecret = true;
    checkSessionSecret = true;
    preventFrameAttacks = true;
    setNosniffHeader = true;
    securityAnnotation = SecurityAnnotation.disable;
    decodeLineFeed = true;
    contentSecurityPolicy = new ContentSecurityPolicy(ContentSecurityPolicy.Mode.OFF.getValue());
    mimeTypes = new HashMap<>();
    enableTobagoExceptionHandler = true;

    // internal
    final List<TobagoConfigFragment> fragments = new ArrayList<>();

    try {
      TobagoConfigLoader.load(fragments, servletContext, alternative);
      TobagoConfigSorter.sort(fragments);
      TobagoConfigMerger.merge(fragments, this);
      initDefaultValidatorInfo();
      lock();
//todo?        servletContext.setAttribute(TobagoConfig.TOBAGO_CONFIG, this);
      if (LOG.isInfoEnabled()) {
        LOG.info(this.toString());
      }
    } catch (final Exception e) {
      final String error = "Tobago can't be initialized! Application will not run correctly!";
      LOG.error(error, e);
      throw new TobagoConfigurationException(error, e);
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    locked = true;
    supportedThemes = Collections.unmodifiableList(supportedThemes);
    for (final Theme theme : supportedThemes) {
      ((ThemeImpl) theme).lock();
    }
    supportedThemeNames = Collections.unmodifiableList(supportedThemeNames);
    availableThemes = Collections.unmodifiableMap(availableThemes);

    contentSecurityPolicy.lock();

    mimeTypes = Collections.unmodifiableMap(mimeTypes);
  }

  private void checkUnlocked() throws IllegalStateException {
    if (locked) {
      throw new TobagoConfigurationException("The configuration must not be changed after initialization!");
    }
  }

  public void addSupportedThemeName(final String name) {
    checkUnlocked();
    supportedThemeNames.add(name);
  }

  public List<String> getSupportedThemeNames() {
    return supportedThemeNames;
  }

  public Theme getTheme(final String name) {
    if (name == null) {
      LOG.debug("searching theme: null");
      return defaultTheme;
    }
    final Theme found = getThemeIfExists(name);
    if (found != null) {
      return found;
    }
    LOG.debug("searching theme '{}' not found. Using default: {}", name, defaultTheme);
    return defaultTheme;
  }

  public Theme getThemeIfExists(final String name) {
    if (defaultTheme != null && defaultTheme.getName().equals(name)) {
      return defaultTheme;
    }
    for (final Theme theme : supportedThemes) {
      if (theme.getName().equals(name)) {
        return theme;
      }
    }
    return null;
  }

  public void setDefaultThemeName(final String defaultThemeName) {
    checkUnlocked();
    this.defaultThemeName = defaultThemeName;
  }

  public String getDefaultThemeName() {
    return defaultThemeName;
  }

  public List<Theme> getSupportedThemes() {
    return supportedThemes;
  }

  public void setDefaultTheme(final Theme defaultTheme) {
    this.defaultTheme = defaultTheme;
  }

  public Theme getDefaultTheme() {
    return defaultTheme;
  }

  public void addAvailableTheme(final ThemeImpl availableTheme) {
    checkUnlocked();
    final String name = availableTheme.getName();
    if (availableThemes.containsKey(name)) {
      final ThemeImpl base = availableThemes.get(name);
      availableThemes.put(name, ThemeImpl.merge(base, availableTheme));
    } else {
      availableThemes.put(name, availableTheme);
    }
  }

  public Map<String, ThemeImpl> getAvailableThemes() {
    return availableThemes;
  }

  public boolean isThemeCookie() {
    return themeCookie;
  }

  public void setThemeCookie(boolean themeCookie) {
    this.themeCookie = themeCookie;
  }

  public boolean isThemeSession() {
    return themeSession;
  }

  public void setThemeSession(boolean themeSession) {
    this.themeSession = themeSession;
  }

  public boolean isCreateSessionSecret() {
    return createSessionSecret;
  }

  public void setCreateSessionSecret(final boolean createSessionSecret) {
    checkUnlocked();
    this.createSessionSecret = createSessionSecret;
  }

  public boolean isCheckSessionSecret() {
    return checkSessionSecret;
  }

  public void setCheckSessionSecret(final boolean checkSessionSecret) {
    checkUnlocked();
    this.checkSessionSecret = checkSessionSecret;
  }

  public boolean isPreventFrameAttacks() {
    return preventFrameAttacks;
  }

  public void setPreventFrameAttacks(final boolean preventFrameAttacks) {
    checkUnlocked();
    this.preventFrameAttacks = preventFrameAttacks;
  }

  public ContentSecurityPolicy getContentSecurityPolicy() {
    return contentSecurityPolicy;
  }

  public boolean isSetNosniffHeader() {
    return setNosniffHeader;
  }

  public void setSetNosniffHeader(final boolean setNosniffHeader) {
    checkUnlocked();
    this.setNosniffHeader = setNosniffHeader;
  }

  public SecurityAnnotation getSecurityAnnotation() {
    return securityAnnotation;
  }

  public void setSecurityAnnotation(final SecurityAnnotation securityAnnotation) {
    checkUnlocked();
    this.securityAnnotation = securityAnnotation;
  }

  public Map<String, String> getDefaultValidatorInfo() {
    // TODO: if the startup hasn't found a FacesContext and Application, this may depend on the order of the listeners.
    if (defaultValidatorInfo == null) {
      initDefaultValidatorInfo();
    }
    return defaultValidatorInfo;
  }

  public Sanitizer getSanitizer() {
    return sanitizer;
  }

  public void setSanitizer(final Sanitizer sanitizer) {
    checkUnlocked();
    this.sanitizer = sanitizer;
  }

  public boolean isDecodeLineFeed() {
    return decodeLineFeed;
  }

  public void setDecodeLineFeed(final boolean decodeLineFeed) {
    checkUnlocked();
    this.decodeLineFeed = decodeLineFeed;
  }

  public boolean isEnableTobagoExceptionHandler() {
    return enableTobagoExceptionHandler;
  }

  public void setEnableTobagoExceptionHandler(boolean enableTobagoExceptionHandler) {
    checkUnlocked();
    this.enableTobagoExceptionHandler = enableTobagoExceptionHandler;
  }

  public Map<String, String> getMimeTypes() {
    return mimeTypes;
  }

  private void initDefaultValidatorInfo() {
    if (defaultValidatorInfo != null) {
      checkUnlocked();
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
    builder.append(", \navailableThemes=");
    builder.append(availableThemes.keySet());
    builder.append(", \nthemeCookie=");
    builder.append(themeCookie);
    builder.append(", \nthemeSession=");
    builder.append(themeSession);
    builder.append(", \ncreateSessionSecret=");
    builder.append(createSessionSecret);
    builder.append(", \ncheckSessionSecret=");
    builder.append(checkSessionSecret);
    builder.append(", \npreventFrameAttacks=");
    builder.append(preventFrameAttacks);
    builder.append(", \ncontentSecurityPolicy=");
    builder.append(contentSecurityPolicy);
    builder.append(", \nsecurityAnnotation=");
    builder.append(securityAnnotation);
    builder.append(", \nsetNosniffHeader=");
    builder.append(setNosniffHeader);
    builder.append(", \ndefaultValidatorInfo=");
    builder.append(defaultValidatorInfo);
    builder.append(", \nsanitizer=");
    builder.append(sanitizer);
    builder.append(", \ndecodeLineFeed=");
    builder.append(decodeLineFeed);
    // to see only different (ignore alternative names for the same theme)
    builder.append(", \nthemes=");
    final Set<Theme> all = new HashSet<>(availableThemes.values());
    builder.append(all);
    builder.append(", \nmimeTypes=");
    builder.append(mimeTypes);
    builder.append(", \nenableTobagoExceptionHandler=");
    builder.append(enableTobagoExceptionHandler);
    builder.append('}');
    return builder.toString();
  }
}
