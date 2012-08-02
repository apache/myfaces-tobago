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

package org.apache.myfaces.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/*
 * User: lofwyr
 * Date: 25.03.2006 08:21:06
 */
class ThemeBuilder {

  private static final Log LOG = LogFactory.getLog(ThemeBuilder.class);

  private List<ThemeImpl> availableThemes = new ArrayList<ThemeImpl>();

  public Map<String, Theme> resolveThemes(RenderersConfig renderersConfig) {
    Map<String, ThemeImpl> map = new HashMap<String, ThemeImpl>();
    for (ThemeImpl theme : availableThemes) {
      LOG.debug("theme from tobago-theme.xml files:" + theme.getName());
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
      theme.resolveRendererConfig(renderersConfig);
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

    return Collections.unmodifiableMap(result);
  }

  public void addTheme(ThemeImpl theme) {
    availableThemes.add(theme);
  }
}
