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

import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the script and style files for production and development stage.
 *
 * @since 1.5.0
 */
public final class ThemeResources implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  private boolean production;

  private final List<ThemeScript> includeScripts = new ArrayList<>();
  private final List<ThemeScript> excludeScripts = new ArrayList<>();
  private final List<ThemeStyle> includeStyles = new ArrayList<>();
  private final List<ThemeStyle> excludeStyles = new ArrayList<>();

  public ThemeResources() {
  }

  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  public ThemeResources(final boolean production) {
    this.production = production;
  }

  /**
   * @deprecated since 5.0.0, use static {@link #merge}
   */
  @Deprecated
  public void merge(final ThemeResources fallback) {
    if (this == fallback) {
      return;
    }
    for (int i = fallback.includeScripts.size() - 1; i >= 0; i--) {
      final ThemeScript script = fallback.includeScripts.get(i);
      includeScripts.remove(script);
      if (!excludeScripts.contains(script)) {
        includeScripts.add(0, script);
      }
    }
    for (int i = fallback.includeStyles.size() - 1; i >= 0; i--) {
      final ThemeStyle style = fallback.includeStyles.get(i);
      includeStyles.remove(style);
      if (!excludeStyles.contains(style)) {
        includeStyles.add(0, style);
      }
    }
  }

  /**
   * @since 5.0.0
   */
  public static ThemeResources merge(final ThemeResources base, final ThemeResources add) {
    if (base.production != add.production) {
      throw new TobagoConfigurationException("Resources mismatch!");
    }
    final ThemeResources result = new ThemeResources(base.production);

    for (ThemeScript includeScript : base.includeScripts) {
      if (!add.excludeScripts.contains(includeScript)) {
        result.addIncludeScript(includeScript);
      }
    }
    for (ThemeScript includeScript : add.includeScripts) {
      result.addIncludeScript(includeScript);
    }

    for (ThemeStyle includeStyle : base.includeStyles) {
      if (!add.excludeStyles.contains(includeStyle)) {
        result.addIncludeStyle(includeStyle);
      }
    }
    for (ThemeStyle includeStyle : add.includeStyles) {
      result.addIncludeStyle(includeStyle);
    }

    return result;
  }

  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  public boolean isProduction() {
    return production;
  }

  /**
   * @since 5.0.0
   */
  public boolean addIncludeScript(final ThemeScript script) {
    for (ThemeScript resource : includeScripts) {
      if (resource.getName().equals(script.getName())) {
        LOG.warn("Overwriting include script '{}'", script.getName());
        includeScripts.remove(resource);
        break;
      }
    }
    return includeScripts.add(script);
  }

  /**
   * @since 5.0.0
   */
  public boolean addExcludeScript(final ThemeScript script) {
    for (ThemeScript resource : excludeScripts) {
      if (resource.getName().equals(script.getName())) {
        LOG.warn("Overwriting exclude script '{}'", script.getName());
        includeScripts.remove(resource);
        break;
      }
    }
    return excludeScripts.add(script);
  }

  /**
   * @deprecated since 5.0.0, use {@link #addIncludeScript} or {@link #addExcludeScript}
   */
  @Deprecated
  public boolean addScript(final ThemeScript script, final boolean exclude) {
    return exclude ? addExcludeScript(script) : addIncludeScript(script);
  }

  /**
   * @since 5.0.0
   */
  public boolean addIncludeStyle(final ThemeStyle style) {
    for (ThemeStyle resource : includeStyles) {
      if (resource.getName().equals(style.getName())) {
        LOG.warn("Overwriting include style '{}'", style.getName());
        includeStyles.remove(resource);
        break;
      }
    }
    return includeStyles.add(style);
  }

  /**
   * @since 5.0.0
   */
  public boolean addExcludeStyle(final ThemeStyle style) {
    for (ThemeStyle resource : excludeStyles) {
      if (resource.getName().equals(style.getName())) {
        LOG.warn("Overwriting exclude style '{}'", style.getName());
        includeStyles.remove(resource);
        break;
      }
    }
    return excludeStyles.add(style);
  }

  /**
   * @deprecated since 5.0.0, use {@link #addIncludeStyle} or {@link #addExcludeStyle}
   */
  @Deprecated
  public boolean addStyle(final ThemeStyle style, final boolean exclude) {
    return exclude ? addExcludeStyle(style) : addIncludeStyle(style);
  }

  public List<ThemeScript> getScriptList() {
    return includeScripts;
  }

  public List<ThemeStyle> getStyleList() {
    return includeStyles;
  }
}
