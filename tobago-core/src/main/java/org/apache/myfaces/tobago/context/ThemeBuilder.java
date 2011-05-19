package org.apache.myfaces.tobago.context;

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

import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Date: 25.03.2006 08:21:06
 */
class ThemeBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(ThemeBuilder.class);

  private List<ThemeImpl> availableThemes = new ArrayList<ThemeImpl>();
  private TobagoConfigImpl tobagoConfig;

  ThemeBuilder(TobagoConfigImpl tobagoConfig) {
    this.tobagoConfig = tobagoConfig;
  }

  public void resolveThemes() {
    Map<String, ThemeImpl> map = new HashMap<String, ThemeImpl>();
    for (ThemeImpl theme : availableThemes) {
      LOG.debug("theme from tobago-theme.xml files: {} ", theme.getName());
      map.put(theme.getName(), theme);
    }
    for (ThemeImpl theme : availableThemes) {
      String fallbackName = theme.getFallbackName();
      ThemeImpl fallback = map.get(fallbackName);
      theme.setFallback(fallback);
    }
    for (ThemeImpl theme : availableThemes) {
      theme.resolveFallbacks();
    }
    for (ThemeImpl theme : availableThemes) {
      theme.resolveRendererConfig(tobagoConfig.getRenderersConfig());
    }
    Map<String, Theme> result = new HashMap<String, Theme>();
    for (ThemeImpl theme : availableThemes) {
      result.put(theme.getName(), theme);
    }
    for (ThemeImpl theme : availableThemes) {
      if (theme.getDeprecatedName() != null) {
        result.put(theme.getDeprecatedName(), theme);
      }
    }
    for (ThemeImpl theme : availableThemes) {
      theme.resolveResources();
    }
    for (ThemeImpl theme : availableThemes) {
      theme.init();
    }
    tobagoConfig.setAvailableThemes(Collections.unmodifiableMap(result));
  }

  public void addTheme(ThemeImpl theme) {
    availableThemes.add(theme);
  }
}
