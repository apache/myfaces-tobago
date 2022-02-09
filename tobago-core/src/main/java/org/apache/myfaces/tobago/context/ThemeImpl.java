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

import org.apache.myfaces.tobago.internal.config.RendererConfig;
import org.apache.myfaces.tobago.internal.config.RenderersConfig;
import org.apache.myfaces.tobago.internal.config.RenderersConfigImpl;
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
  private String resourcePath;
  private ThemeImpl fallback;
  private String fallbackName;
  private List<Theme> fallbackList;
  private RenderersConfigImpl renderersConfig;
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
      throw new RuntimeException("The configuration must not be changed after initialization!");
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    unmodifiable = true;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    checkLocked();
    this.name = name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    checkLocked();
    this.displayName = displayName;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(final String resourcePath) {
    checkLocked();
    this.resourcePath = resourcePath;
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

  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public void resolveFallbacks() {
    checkLocked();
    fallbackList = new ArrayList<Theme>();
    ThemeImpl actual = this;
    while (actual != null) {
      fallbackList.add(actual);
      actual = actual.getFallback();
    }
    fallbackList = Collections.unmodifiableList(fallbackList);
    if (LOG.isDebugEnabled()) {
      for (final Theme theme : fallbackList) {
        LOG.debug("fallbackList: {}", theme.getName());
      }
    }
  }

  public void resolveRendererConfig(final RenderersConfig rendererConfigFromTobagoConfig) {
    checkLocked();
    if (renderersConfig == null) {
      renderersConfig = new RenderersConfigImpl();
    }
    if (!renderersConfig.isMerged()) {
      final ThemeImpl fallbackTheme = getFallback();
      if (fallbackTheme != null) {
        fallbackTheme.resolveRendererConfig(rendererConfigFromTobagoConfig);
        final RenderersConfigImpl fallbackRenderersConfig = fallbackTheme.getRenderersConfigImpl();
        if (fallbackRenderersConfig != null) {
          renderersConfig.merge(fallbackRenderersConfig, false);
          if (LOG.isDebugEnabled()) {
            LOG.debug("merge markupconfig from {} for {}", fallbackTheme.getName(), getName());
          }
        }
      }
      if (rendererConfigFromTobagoConfig != null) {
        renderersConfig.merge(rendererConfigFromTobagoConfig, true);
      }
      renderersConfig.setMerged(true);
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} {}", getName(), renderersConfig);
      }
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

  public void setRenderersConfig(final RenderersConfigImpl renderersConfig) {
    checkLocked();
    this.renderersConfig = renderersConfig;
  }

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  RenderersConfigImpl getRenderersConfigImpl() {
    return renderersConfig;
  }

  public ThemeResources getResources() {
    return resources;
  }

  public ThemeResources getProductionResources() {
    return productionResources;
  }

  public void addResources(final ThemeResources themeResources) {
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
    final List<ThemeResource> copy = new ArrayList<ThemeResource>(list);
    Collections.sort(copy, new ThemeResourceComparator());
    final String[] result = new String[copy.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = copy.get(i).getName();
    }
    return result;
  }

  private static class ThemeResourceComparator implements Comparator<ThemeResource> {
    public int compare(ThemeResource o1, ThemeResource o2) {
      return o1.getPriority() - o2.getPriority();
    }
  }

  public String[] getScriptResources(final boolean production) {
    if (production) {
      return productionScripts;
    }
    return scripts;
  }

  public String[] getStyleResources(final boolean production) {
    if (production) {
      return productionStyles;
    }
    return styles;
  }

  public boolean isVersioned() {
    checkLocked();
    return versioned;
  }

  public void setVersioned(final boolean versioned) {
    checkLocked();
    this.versioned = versioned;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    checkLocked();
    this.version = version;
  }

  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Theme: name='");
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
    for (String s : productionScripts != null ? productionScripts : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nscripts=[");
    for (String s : scripts != null ? scripts : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nproductionStyles=[");
    for (String s : productionStyles != null ? productionStyles : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    builder.append("], \nstyles=[");
    for (String s : styles != null ? styles : new String[0]) {
      builder.append("\n");
      builder.append(s);
    }
    if (renderersConfig != null) {
      builder.append("\n");
      for (final RendererConfig config : renderersConfig.getRendererConfigs()) {
        builder.append(config);
        builder.append("\n");
      }
    }
    return builder.toString();
  }
}
