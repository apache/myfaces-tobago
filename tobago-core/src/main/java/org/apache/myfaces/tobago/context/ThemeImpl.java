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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ThemeImpl implements Theme, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(ThemeImpl.class);

  private String name;

  private String displayName;

  private String deprecatedName;

  private String resourcePath;

  private ThemeImpl fallback;

  private String fallbackName;

  private List<Theme> fallbackList;

  private RenderersConfigImpl renderersConfig;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDeprecatedName() {
    return deprecatedName;
  }

  public void setDeprecatedName(String deprecatedName) {
    this.deprecatedName = deprecatedName;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public ThemeImpl getFallback() {
    return fallback;
  }

  public void setFallback(ThemeImpl fallback) {
    this.fallback = fallback;
  }

  public String getFallbackName() {
    return fallbackName;
  }

  public void setFallbackName(String fallbackName) {
    this.fallbackName = fallbackName;
  }

  public List<Theme> getFallbackList() {
    return fallbackList;
  }

  public void resolveFallbacks() {
    fallbackList = new ArrayList<Theme>();
    ThemeImpl actual = this;
    while (actual != null) {
      fallbackList.add(actual);
      actual = actual.getFallback();
    }
    fallbackList = Collections.unmodifiableList(fallbackList);
    if (LOG.isDebugEnabled()) {
      for (Theme theme : fallbackList) {
        LOG.debug("fallbackList: {}", theme.getName());
      }
    }
  }

  public void resolveRendererConfig(RenderersConfig rendererConfigFromTobagoConfig) {
    if (renderersConfig == null) {
      renderersConfig = new RenderersConfigImpl();
    }
    if (!renderersConfig.isMerged()) {
      ThemeImpl fallback = getFallback();
      if (fallback != null) {
        fallback.resolveRendererConfig(rendererConfigFromTobagoConfig);
        RenderersConfigImpl fallbackRenderersConfig = fallback.getRenderersConfigImpl();
        if (fallbackRenderersConfig != null) {
          renderersConfig.merge(fallbackRenderersConfig, false);
          if (LOG.isDebugEnabled()) {
            LOG.debug("merge markupconfig from {} for {}", fallback.getName(), getName());
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

  public String toString() {
//    LOG.warn("Should not be called!", new Exception());
    return name;
  }

  public void setRenderersConfig(RenderersConfigImpl renderersConfig) {
    this.renderersConfig = renderersConfig;
  }

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  RenderersConfigImpl getRenderersConfigImpl() {
    return renderersConfig;
  }
}
