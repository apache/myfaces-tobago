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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThemeImpl implements Theme, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(ThemeImpl.class);

  private String name;
  private String displayName;
  private ThemeImpl fallback;
  private String fallbackName;
  private List<Theme> fallbackList;
  private ThemeResources productionResources;
  private ThemeResources resources;
  private String[] productionScripts;
  private String[] productionStyles;
  private String[] scripts;
  private String[] styles;
  private boolean versioned;
  private String version;

  private boolean unmodifiable = false;

  public ThemeImpl() {
    resources = new ThemeResources(false);
    productionResources = new ThemeResources(true);
  }

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new IllegalStateException("The configuration must not be changed after initialization!");
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    unmodifiable = true;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    checkLocked();
    this.name = name;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    checkLocked();
    this.displayName = displayName;
  }

  public ThemeImpl getFallback() {
    return fallback;
  }

  public void setFallback(final ThemeImpl fallback) {
    checkLocked();
    this.fallback = fallback;
  }

  public String getFallbackName() {
    return fallbackName;
  }

  public void setFallbackName(final String fallbackName) {
    checkLocked();
    this.fallbackName = fallbackName;
  }

  @Override
  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public void resolveFallbacks() {
    checkLocked();
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
    checkLocked();
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
    checkLocked();

    if (themeResources.isProduction()) {
      productionResources.merge(themeResources);
    } else {
      resources.merge(themeResources);
    }
  }

  public void init() {
    checkLocked();
    productionScripts = sort(productionResources.getScriptList());
    productionStyles = sort(productionResources.getStyleList());
    scripts = sort(resources.getScriptList());
    styles = sort(resources.getStyleList());
  }

  private String[] sort(List<? extends ThemeResource> list) {
    final List<ThemeResource> copy = new ArrayList<>(list);
    copy.sort(Comparator.comparingInt(ThemeResource::getPriority));
    return copy.stream().map(ThemeResource::getName).toArray(String[]::new);
  }

  @Override
  public String[] getScriptResources(final boolean production) {
    if (production) {
      return productionScripts;
    } else {
      return scripts;
    }
  }

  @Override
  public String[] getStyleResources(final boolean production) {
    if (production) {
      return productionStyles;
    } else {
      return styles;
    }
  }

  public boolean isVersioned() {
    checkLocked();
    return versioned;
  }

  public void setVersioned(final boolean versioned) {
    checkLocked();
    this.versioned = versioned;
  }

  @Override
  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    checkLocked();
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
    builder.append(", \nproductionScripts=[");
    for (final String s : productionScripts != null ? productionScripts : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nscripts=[");
    for (final String s : scripts != null ? scripts : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nproductionStyles=[");
    for (final String s : productionStyles != null ? productionStyles : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nstyles=[");
    for (final String s : styles != null ? styles : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    return builder.toString();
  }
}
