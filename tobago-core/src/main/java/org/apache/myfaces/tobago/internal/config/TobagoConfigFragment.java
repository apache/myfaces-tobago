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
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigFragment {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigFragment.class);

  private String name;
  private List<String> before;
  private List<String> after;

  private List<String> supportedThemeNames;
  private String defaultThemeName;
  private List<String> resourceDirs;
  private RenderersConfig renderersConfig;
  private Boolean createSessionSecret;
  private Boolean checkSessionSecret;
  private Boolean preventFrameAttacks;
  private List<String> contentSecurityPolicy;
  // todo
  private List<ThemeImpl> themeDefinitions;
  private URL url;

  public TobagoConfigFragment() {
    before = new ArrayList<String>();
    after = new ArrayList<String>();
    supportedThemeNames = new ArrayList<String>();
    resourceDirs = new ArrayList<String>();
    themeDefinitions = new ArrayList<ThemeImpl>();
    contentSecurityPolicy = new ArrayList<String>();
  }

  public void addSupportedThemeName(String name) {
    supportedThemeNames.add(name);
  }

  public List<String> getSupportedThemeNames() {
    return supportedThemeNames;
  }

  public String getDefaultThemeName() {
    return defaultThemeName;
  }

  public void setDefaultThemeName(String defaultThemeName) {
    this.defaultThemeName = defaultThemeName;
  }

  public void addResourceDir(String resourceDir) {
    if (!resourceDirs.contains(resourceDir)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("adding resourceDir = '{}'", resourceDir);
      }
      resourceDirs.add(resourceDir);
    }
  }

  public List<String> getResourceDirs() {
    return resourceDirs;
  }

  /** @deprecated since 1.5.0 */
  @Deprecated
  public void setAjaxEnabled(String value) {
    Deprecation.LOG.error("Ajax is always enabled!");
  }

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  public void setRenderersConfig(RenderersConfig renderersConfig) {
    this.renderersConfig = renderersConfig;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getBefore() {
    return before;
  }

  public void addBefore(String name) {
    before.add(name);
  }

  public List<String> getAfter() {
    return after;
  }

  public void addAfter(String name) {
    after.add(name);
  }

  public void addThemeDefinition(ThemeImpl theme) {
    themeDefinitions.add(theme);
  }

  public List<ThemeImpl> getThemeDefinitions() {
    return themeDefinitions;
  }

  public Boolean getCreateSessionSecret() {
    return createSessionSecret;
  }

  public void setCreateSessionSecret(String createSessionSecret) {
    this.createSessionSecret = Boolean.valueOf(createSessionSecret);
  }

  public Boolean getCheckSessionSecret() {
    return checkSessionSecret;
  }

  public void setCheckSessionSecret(String checkSessionSecret) {
    this.checkSessionSecret = Boolean.valueOf(checkSessionSecret);
  }

  public Boolean getPreventFrameAttacks() {
    return preventFrameAttacks;
  }

  public void setPreventFrameAttacks(Boolean preventFrameAttacks) {
    this.preventFrameAttacks = preventFrameAttacks;
  }

  public List<String> getContentSecurityPolicy() {
    return contentSecurityPolicy;
  }

  public void addContentSecurityPolicy(String directive) {
    contentSecurityPolicy.add(directive);
  }

  /** @deprecated since 1.5.0 */
  @Deprecated
  public void setFixResourceOrder(String value) {
    Deprecation.LOG.error("Config fix-resource-order not longer supported. (Is always activated).");
  }

  /** @deprecated since 1.5.0 */
  @Deprecated
  public void setFixLayoutTransparency(String value) {
    Deprecation.LOG.error("Config fix-layout-transparency not longer supported. (Is always activated).");
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return name != null ? name : "(id=" + System.identityHashCode(this) + ")";
  }
}
