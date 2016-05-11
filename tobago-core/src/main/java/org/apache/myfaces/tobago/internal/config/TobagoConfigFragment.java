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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
  private ContentSecurityPolicy contentSecurityPolicy;
  private Boolean setNosniffHeader;
  private List<ThemeImpl> themeDefinitions;
  private URL url;
  private String sanitizerClass;
  private Properties sanitizerProperties;
  private Map<String, String> mimeTypes;

  public TobagoConfigFragment() {
    before = new ArrayList<String>();
    after = new ArrayList<String>();
    supportedThemeNames = new ArrayList<String>();
    resourceDirs = new ArrayList<String>();
    themeDefinitions = new ArrayList<ThemeImpl>();
    mimeTypes = new HashMap<String, String>();
  }

  public void addSupportedThemeName(final String name) {
    supportedThemeNames.add(name);
  }

  public List<String> getSupportedThemeNames() {
    return supportedThemeNames;
  }

  public String getDefaultThemeName() {
    return defaultThemeName;
  }

  public void setDefaultThemeName(final String defaultThemeName) {
    this.defaultThemeName = defaultThemeName;
  }

  public void addResourceDir(final String resourceDir) {
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

  public RenderersConfig getRenderersConfig() {
    return renderersConfig;
  }

  public void setRenderersConfig(final RenderersConfig renderersConfig) {
    this.renderersConfig = renderersConfig;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getBefore() {
    return before;
  }

  public void addBefore(final String name) {
    before.add(name);
  }

  public List<String> getAfter() {
    return after;
  }

  public void addAfter(final String name) {
    after.add(name);
  }

  public void addThemeDefinition(final ThemeImpl theme) {
    themeDefinitions.add(theme);
  }

  public List<ThemeImpl> getThemeDefinitions() {
    return themeDefinitions;
  }

  public Boolean getCreateSessionSecret() {
    return createSessionSecret;
  }

  public void setCreateSessionSecret(final String createSessionSecret) {
    this.createSessionSecret = Boolean.valueOf(createSessionSecret);
  }

  public Boolean getCheckSessionSecret() {
    return checkSessionSecret;
  }

  public void setCheckSessionSecret(final String checkSessionSecret) {
    this.checkSessionSecret = Boolean.valueOf(checkSessionSecret);
  }

  public Boolean getPreventFrameAttacks() {
    return preventFrameAttacks;
  }

  public void setPreventFrameAttacks(final Boolean preventFrameAttacks) {
    this.preventFrameAttacks = preventFrameAttacks;
  }

  public ContentSecurityPolicy getContentSecurityPolicy() {
    return contentSecurityPolicy;
  }

  public void setContentSecurityPolicy(final ContentSecurityPolicy contentSecurityPolicy) {
    this.contentSecurityPolicy = contentSecurityPolicy;
  }

  public Boolean getSetNosniffHeader() {
    return setNosniffHeader;
  }

  public void setSetNosniffHeader(final Boolean setNosniffHeader) {
    this.setNosniffHeader = setNosniffHeader;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(final URL url) {
    this.url = url;
  }

  public String getSanitizerClass() {
    return sanitizerClass;
  }

  public void setSanitizerClass(String sanitizerClass) {
    this.sanitizerClass = sanitizerClass;
  }

  public Properties getSanitizerProperties() {
    return sanitizerProperties;
  }

  public void setSanitizerProperties(Properties sanitizerProperties) {
    this.sanitizerProperties = sanitizerProperties;
  }

  public void addMimeType(String extension, String type) {
    this.mimeTypes.put(extension, type);
  }

  public Map<String, String> getMimeTypes() {
    return mimeTypes;
  }

  @Override
  public String toString() {
    return name != null ? name : "(id=" + System.identityHashCode(this) + ")";
  }
}
