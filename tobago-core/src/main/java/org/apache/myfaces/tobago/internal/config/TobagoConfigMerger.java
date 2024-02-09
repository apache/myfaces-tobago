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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
import org.apache.myfaces.tobago.sanitizer.IgnoringSanitizer;
import org.apache.myfaces.tobago.sanitizer.JsoupSanitizer;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TobagoConfigMerger {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final List<TobagoConfigFragment> fragments;
  private final TobagoConfig tobagoConfig;

  private TobagoConfigMerger(final List<TobagoConfigFragment> fragments, final TobagoConfig tobagoConfig) {
    this.fragments = fragments;
    this.tobagoConfig = tobagoConfig;
  }

  public static void merge(final List<TobagoConfigFragment> fragments, final TobagoConfig tobagoConfig) {
    final TobagoConfigMerger merger = new TobagoConfigMerger(fragments, tobagoConfig);
    merger.merge();
    merger.resolveThemes();
  }

  private void merge() {

    // default sanitizer
    String sanitizerClass = JsoupSanitizer.class.getName();
    Properties sanitizerProperties = new Properties();
    sanitizerProperties.setProperty("safelist", "relaxed");

    for (TobagoConfigFragment fragment : fragments) {

      // default theme
      final String defaultTheme = fragment.getDefaultThemeName();
      if (defaultTheme != null) {
        tobagoConfig.setDefaultThemeName(defaultTheme);
      }

      // supported themes
      for (final String supported : fragment.getSupportedThemeNames()) {
        tobagoConfig.addSupportedThemeName(supported);
      }

      // theme cookie
      if (fragment.getThemeCookie() != null) {
        tobagoConfig.setThemeCookie(fragment.getThemeCookie());
      }

      // theme session
      if (fragment.getThemeSession() != null) {
        tobagoConfig.setThemeSession(fragment.getThemeSession());
      }

      // session secret
      if (fragment.getCreateSessionSecret() != null) {
        tobagoConfig.setCreateSessionSecret(fragment.getCreateSessionSecret());
      }
      if (fragment.getCheckSessionSecret() != null) {
        tobagoConfig.setCheckSessionSecret(fragment.getCheckSessionSecret());
      }

      if (fragment.getPreventFrameAttacks() != null) {
        tobagoConfig.setPreventFrameAttacks(fragment.getPreventFrameAttacks());
      }

      if (fragment.getContentSecurityPolicy() != null) {
        tobagoConfig.getContentSecurityPolicy().merge(fragment.getContentSecurityPolicy());
      }

      if (fragment.getSecurityAnnotation() != null) {
        tobagoConfig.setSecurityAnnotation(fragment.getSecurityAnnotation());
      }

      if (fragment.getSetNosniffHeader() != null) {
        tobagoConfig.setSetNosniffHeader(fragment.getSetNosniffHeader());
      }

      if (fragment.getSanitizerClass() != null) {
        sanitizerClass = fragment.getSanitizerClass();
        sanitizerProperties = fragment.getSanitizerProperties();
      }

      if (fragment.getDecodeLineFeed() != null) {
        tobagoConfig.setDecodeLineFeed(fragment.getDecodeLineFeed());
      }

      if (fragment.getEnableTobagoExceptionHandler() != null) {
        tobagoConfig.setEnableTobagoExceptionHandler(fragment.getEnableTobagoExceptionHandler());
      }

      // theme definition
      for (final ThemeImpl theme : fragment.getThemeDefinitions()) {
        tobagoConfig.addAvailableTheme(theme);
      }

      // url
      // todo???

      final Map<String, String> mimeTypes = tobagoConfig.getMimeTypes();
      for (final Map.Entry<String, String> entry : fragment.getMimeTypes().entrySet()) {
        mimeTypes.put(entry.getKey(), entry.getValue());
      }

    }

    resolveThemes(tobagoConfig.getAvailableThemes());

    if (sanitizerClass != null) {
      try {
        final Class<? extends Sanitizer> aClass = Class.forName(sanitizerClass).asSubclass(Sanitizer.class);
        final Sanitizer sanitizer = aClass.newInstance();
        sanitizer.setProperties(sanitizerProperties);
        tobagoConfig.setSanitizer(sanitizer);
      } catch (final Exception e) {
        LOG.error("Can't create sanitizer: '" + sanitizerClass + "'", e);
        tobagoConfig.setSanitizer(new IgnoringSanitizer());
      }
    }
  }

  private void resolveThemes(final Map<String, ThemeImpl> map) {
    for (final ThemeImpl theme : map.values()) {
      final String fallbackName = theme.getFallbackName();
      final ThemeImpl fallback = map.get(fallbackName);
      theme.setFallback(fallback);
    }
    for (final ThemeImpl theme : map.values()) {
      theme.resolveFallbacks();
    }
    for (final ThemeImpl theme : map.values()) {
      theme.resolveResources();
    }
    for (final ThemeImpl theme : map.values()) {
      theme.init();
    }
  }

  private void resolveThemes() {

    final Map<String, ThemeImpl> availableThemes = tobagoConfig.getAvailableThemes();

    if (tobagoConfig.getDefaultThemeName() != null) {
      final String defaultThemeName = tobagoConfig.getDefaultThemeName();
      final ThemeImpl defaultTheme = availableThemes.get(defaultThemeName);
      tobagoConfig.setDefaultTheme(defaultTheme);
      checkThemeIsAvailable(defaultThemeName, defaultTheme, availableThemes);
      if (LOG.isDebugEnabled()) {
        LOG.debug("name = '{}'", defaultThemeName);
        LOG.debug("defaultTheme = '{}'", defaultTheme);
      }
    } else {
      int deep = 0;
      Theme defaultTheme = null;
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
            + "theme JAR. Please add a theme JAR to your classpath. Usually "
            + "tobago-theme-standard.jar in WEB-INF/lib";
        LOG.error(error);
        throw new TobagoConfigurationException(error);
      } else {
        tobagoConfig.setDefaultTheme(defaultTheme);
        if (LOG.isInfoEnabled()) {
          LOG.info("Using default Theme {}", defaultTheme.getName());
        }
      }
    }
    if (!tobagoConfig.getSupportedThemeNames().isEmpty()) {
      for (final String name : tobagoConfig.getSupportedThemeNames()) {
        final Theme theme = availableThemes.get(name);
        checkThemeIsAvailable(name, theme, availableThemes);
        tobagoConfig.getSupportedThemes().add(theme);
        if (LOG.isDebugEnabled()) {
          LOG.debug("name = '{}'", name);
          LOG.debug("last added theme = '{}'", theme);
        }
      }
    }
  }

  private void checkThemeIsAvailable(
      final String name, final Theme theme, final Map<String, ThemeImpl> availableThemes) {
    if (theme == null) {
      final String error = "Theme not found! name: '" + name + "'. "
          + "Please ensure you have a tobago-config.xml with a theme-definition in your "
          + "theme JAR. Found the following themes: " + availableThemes.keySet();
      LOG.error(error);
      throw new TobagoConfigurationException(error);
    }
  }


}
