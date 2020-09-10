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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThemeImpl implements Theme, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String name;
  private String displayName;
  private ThemeImpl fallback;
  private String fallbackName;
  private List<Theme> fallbackList;
  private final ThemeResources productionResources;
  private final ThemeResources resources;
  private ThemeScript[] productionScripts;
  private ThemeStyle[] productionStyles;
  private ThemeScript[] scripts;
  private ThemeStyle[] styles;
  private String version;

  private boolean locked = false;

  public ThemeImpl() {
    resources = new ThemeResources(false);
    productionResources = new ThemeResources(true);
  }

  private void checkUnlocked() throws IllegalStateException {
    if (locked) {
      throw new IllegalStateException("The configuration must not be changed after initialization!");
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    locked = true;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    checkUnlocked();
    this.name = name;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    checkUnlocked();
    this.displayName = displayName;
  }

  public ThemeImpl getFallback() {
    return fallback;
  }

  public void setFallback(final ThemeImpl fallback) {
    checkUnlocked();
    this.fallback = fallback;
  }

  public String getFallbackName() {
    return fallbackName;
  }

  public void setFallbackName(final String fallbackName) {
    checkUnlocked();
    this.fallbackName = fallbackName;
  }

  @Override
  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public void resolveFallbacks() {
    checkUnlocked();
    fallbackList = new ArrayList<>();
    ThemeImpl actual = this;
    while (actual != null) {
      fallbackList.add(actual);
      actual = actual.getFallback();
    }
    fallbackList = Collections.unmodifiableList(fallbackList);
    if (LOG.isDebugEnabled()) {
      LOG.debug("fallbackList: {");
      for (final Theme theme : fallbackList) {
        LOG.debug("  theme: {}", theme.getName());
      }
      LOG.debug("}");
    }
  }

  public void resolveResources() {
    checkUnlocked();
    final ThemeImpl fallbackTheme = getFallback();
    if (fallbackTheme != null) {
      fallbackTheme.resolveResources();
      addResources(fallbackTheme.getProductionResources());
      addResources(fallbackTheme.getResources());
    }
  }

  public ThemeResources getResources() {
    return resources;
  }

  public ThemeResources getProductionResources() {
    return productionResources;
  }

  private void addResources(final ThemeResources themeResources) {
    checkUnlocked();

    if (themeResources.isProduction()) {
      productionResources.merge(themeResources);
    } else {
      resources.merge(themeResources);
    }
  }

  public void init() {
    checkUnlocked();
    productionScripts = sortScripts(productionResources.getScriptList());
    productionStyles = sortStyles(productionResources.getStyleList());
    scripts = sortScripts(resources.getScriptList());
    styles = sortStyles(resources.getStyleList());
  }

  private ThemeScript[] sortScripts(List<ThemeScript> list) {
    final List<ThemeScript> copy = new ArrayList<>(list);
    copy.sort(Comparator.comparingInt(ThemeResource::getPriority));
    return copy.toArray(new ThemeScript[0]);
  }

  private ThemeStyle[] sortStyles(List<ThemeStyle> list) {
    final List<ThemeStyle> copy = new ArrayList<>(list);
    copy.sort(Comparator.comparingInt(ThemeResource::getPriority));
    return copy.toArray(new ThemeStyle[0]);
  }

  @Override
  public ThemeScript[] getScriptResources(final boolean production) {
    if (production) {
      return productionScripts;
    } else {
      return scripts;
    }
  }

  @Override
  public ThemeStyle[] getStyleResources(final boolean production) {
    if (production) {
      return productionStyles;
    } else {
      return styles;
    }
  }

  @Override
  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    checkUnlocked();
    this.version = version;
  }

  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Theme:  name='");
    builder.append(name);
    builder.append("' fallback=");
    if (fallback != null) {
      builder.append("'");
      builder.append(fallback.getName());
      builder.append("'");
    } else {
      builder.append("null");
    }
    builder.append(" version='");
    builder.append(version);
    builder.append("', \nproductionScripts=[");
    for (final ThemeScript s : productionScripts) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nscripts=[");
    for (final ThemeScript s : scripts) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nproductionStyles=[");
    for (final ThemeStyle s : productionStyles) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nstyles=[");
    for (final ThemeStyle s : styles) {
      builder.append("\n");
      builder.append(s);
    }
    return builder.toString();
  }
}
