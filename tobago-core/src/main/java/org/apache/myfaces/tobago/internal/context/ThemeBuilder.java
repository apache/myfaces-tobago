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

package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ThemeBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(ThemeBuilder.class);

  private List<ThemeImpl> availableThemes = new ArrayList<ThemeImpl>();
  private TobagoConfigImpl tobagoConfig;

  ThemeBuilder(final TobagoConfigImpl tobagoConfig) {
    this.tobagoConfig = tobagoConfig;
  }

  public void resolveThemes() {
    final Map<String, ThemeImpl> map = new HashMap<String, ThemeImpl>();
    for (final ThemeImpl theme : availableThemes) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Theme from XML files: {} ", theme.getName());
      }
      map.put(theme.getName(), theme);
    }
    for (final ThemeImpl theme : availableThemes) {
      final String fallbackName = theme.getFallbackName();
      final ThemeImpl fallback = map.get(fallbackName);
      theme.setFallback(fallback);
    }
    for (final ThemeImpl theme : availableThemes) {
      theme.resolveFallbacks();
    }
    for (final ThemeImpl theme : availableThemes) {
      theme.resolveRendererConfig(tobagoConfig.getRenderersConfig());
    }
    final Map<String, Theme> result = new HashMap<String, Theme>();
    for (final ThemeImpl theme : availableThemes) {
      result.put(theme.getName(), theme);
    }
    for (final ThemeImpl theme : availableThemes) {
      theme.resolveResources();
    }
    for (final ThemeImpl theme : availableThemes) {
      theme.init();
    }
    tobagoConfig.setAvailableThemes(Collections.unmodifiableMap(result));
  }

  public void addTheme(final ThemeImpl theme) {
    availableThemes.add(theme);
  }
}
