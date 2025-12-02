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

import org.apache.myfaces.tobago.component.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeImpl implements Theme, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String name;
  private String displayName;
  private ThemeImpl fallback;
  private String fallbackName;
  private List<Theme> fallbackList;
  private ThemeResources productionResources;
  private ThemeResources developmentResources;
  private ThemeScript[] productionScripts;
  private ThemeStyle[] productionStyles;
  private ThemeScript[] scripts;
  private ThemeStyle[] styles;
  private String version;
  private final Map<String, String> tagAttributeDefaults;

  private boolean locked = false;

  public ThemeImpl() {
    developmentResources = new ThemeResources();
    productionResources = new ThemeResources();
    tagAttributeDefaults = new HashMap<>();
  }

  public static ThemeImpl merge(ThemeImpl base, ThemeImpl add) {
    base.checkUnlocked();
    add.checkUnlocked();
    final ThemeImpl result = new ThemeImpl();
    assert add.name.equals(base.name);
    result.name = add.name;
    result.displayName = add.displayName != null ? add.displayName : base.displayName;
    result.fallbackName = add.fallbackName != null ? add.fallbackName : base.fallbackName;
    result.version = add.version != null ? add.version : base.version;
    result.productionResources = ThemeResources.merge(base.productionResources, add.productionResources);
    result.developmentResources = ThemeResources.merge(base.developmentResources, add.developmentResources);
    return result;
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
      productionResources = ThemeResources.merge(fallbackTheme.getProductionResources(), productionResources);
      developmentResources = ThemeResources.merge(fallbackTheme.getDevelopmentResources(), developmentResources);
    }
  }

  @Deprecated(since = "5.0.0", forRemoval = true)
  public ThemeResources getResources() {
    return developmentResources;
  }

  /**
   * @since 5.0.0
   */
  public ThemeResources getDevelopmentResources() {
    return developmentResources;
  }

  public ThemeResources getProductionResources() {
    return productionResources;
  }

  public void init() {
    checkUnlocked();
    productionScripts = sortScripts(productionResources.getScriptList());
    productionStyles = sortStyles(productionResources.getStyleList());
    scripts = sortScripts(developmentResources.getScriptList());
    styles = sortStyles(developmentResources.getStyleList());
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

  public void addTagDefault(String tag, String attribute, String defaultValue) {
    tagAttributeDefaults.put(tag + ' ' + attribute, defaultValue);
  }

  @Override
  public String getTagAttributeDefault(Tags tag, String attribute) {
    final String value = tagAttributeDefaults.get(tag.name() + ' ' + attribute);
    if (value != null) {
      return value;
    } else {
      final String fallbackValue = fallback != null ? fallback.getTagAttributeDefault(tag, attribute) : null;
      // cache values from fallback
      tagAttributeDefaults.put(tag.name() + ' ' + attribute, fallbackValue);
      return fallbackValue;
    }
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
