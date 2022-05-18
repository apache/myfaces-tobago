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

import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.sanitizer.IgnoringSanitizer;
import org.apache.myfaces.tobago.sanitizer.JsoupSanitizer;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TobagoConfigMerger {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigMerger.class);

  private final List<TobagoConfigFragment> list;

  public TobagoConfigMerger(final List<TobagoConfigFragment> list) {
    this.list = list;
  }

  public TobagoConfigImpl merge() {

    final TobagoConfigImpl result = new TobagoConfigImpl();

    // default sanitizer
    String sanitizerClass = JsoupSanitizer.class.getName();
    Properties sanitizerProperties = new Properties();
    sanitizerProperties.setProperty("safelist", "relaxed");

    for (final TobagoConfigFragment fragment : list) {

      // default theme
      final String defaultTheme = fragment.getDefaultThemeName();
      if (defaultTheme != null) {
        result.setDefaultThemeName(defaultTheme);
      }

      // supported themes
      for (final String supported : fragment.getSupportedThemeNames()) {
        result.addSupportedThemeName(supported);
      }

      // resource dirs
      for (final String dir : fragment.getResourceDirs()) {
        result.addResourceDir(dir);
      }

      // renderers config
      if (fragment.getRenderersConfig() != null) {
        if (result.getRenderersConfig() instanceof RenderersConfigImpl) {
          ((RenderersConfigImpl) result.getRenderersConfig()).merge(fragment.getRenderersConfig(), false);
        } else if (result.getRenderersConfig() == null) {
          result.setRenderersConfig(fragment.getRenderersConfig());
        }
      }

      // session secret
      if (fragment.getCreateSessionSecret() != null) {
        result.setCreateSessionSecret(fragment.getCreateSessionSecret());
      }
      if (fragment.getCheckSessionSecret() != null) {
        result.setCheckSessionSecret(fragment.getCheckSessionSecret());
      }

      if (fragment.getPreventFrameAttacks() != null) {
        result.setPreventFrameAttacks(fragment.getPreventFrameAttacks());
      }

      if (fragment.getContentSecurityPolicy() != null) {
        result.getContentSecurityPolicy().merge(fragment.getContentSecurityPolicy());
      }

      if (fragment.getSetNosniffHeader() != null) {
        result.setSetNosniffHeader(fragment.getSetNosniffHeader());
      }

      if (fragment.getSanitizerClass() != null) {
        sanitizerClass = fragment.getSanitizerClass();
        sanitizerProperties = fragment.getSanitizerProperties();
      }

      if (fragment.getAutoAccessKeyFromLabel() != null) {
        result.setAutoAccessKeyFromLabel(fragment.getAutoAccessKeyFromLabel());
      }

      if (fragment.getClassicDateTimePicker() != null) {
        result.setClassicDateTimePicker(fragment.getClassicDateTimePicker());
      }

      // theme definition
      // todo
/*
      for (Theme theme : fragment.getThemeDefinitions()) {
        result.addThemeDefinition(theme);
      }
*/

      // url
      // todo???

      final Map<String, String> mimeTypes = result.getMimeTypes();
      for (final Map.Entry<String, String> entry : fragment.getMimeTypes().entrySet()) {
        mimeTypes.put(entry.getKey(), entry.getValue());
      }

    }

    if (sanitizerClass != null) {
      try {
        final Class<? extends Sanitizer> aClass = Class.forName(sanitizerClass).asSubclass(Sanitizer.class);
        final Sanitizer sanitizer = aClass.newInstance();
        sanitizer.setProperties(sanitizerProperties);
        result.setSanitizer(sanitizer);
      } catch (Throwable e) {
        LOG.error("Can't create sanitizer: '" + sanitizerClass + "'", e);
        result.setSanitizer(new IgnoringSanitizer());
      }
    }

    return result;
  }

  private void resolveThemes(final TobagoConfigImpl tobagoConfig, final Map<String, ThemeImpl> map) {
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

}
