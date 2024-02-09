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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TobagoConfigFragment {

  private String name;
  private final List<String> before;
  private final List<String> after;

  private final List<String> supportedThemeNames;
  private String defaultThemeName;
  private Boolean themeCookie;
  private Boolean themeSession;
  private Boolean createSessionSecret;
  private Boolean checkSessionSecret;
  private Boolean preventFrameAttacks;
  private ContentSecurityPolicy contentSecurityPolicy;
  private SecurityAnnotation securityAnnotation;
  private Boolean setNosniffHeader;
  private final List<ThemeImpl> themeDefinitions;
  private URL url;
  private String sanitizerClass;
  private Boolean decodeLineFeed;
  private Properties sanitizerProperties;
  private final Map<String, String> mimeTypes;
  private Boolean enableTobagoExceptionHandler;

  public TobagoConfigFragment() {
    before = new ArrayList<>();
    after = new ArrayList<>();
    supportedThemeNames = new ArrayList<>();
    themeDefinitions = new ArrayList<>();
    mimeTypes = new HashMap<>();
  }

  public void addSupportedThemeName(final String supportedThemeName) {
    supportedThemeNames.add(supportedThemeName);
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

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<String> getBefore() {
    return before;
  }

  public void addBefore(final String nameBefore) {
    before.add(nameBefore);
  }

  public List<String> getAfter() {
    return after;
  }

  public void addAfter(final String nameAfter) {
    after.add(nameAfter);
  }

  public void addThemeDefinition(final ThemeImpl theme) {
    themeDefinitions.add(theme);
  }

  public List<ThemeImpl> getThemeDefinitions() {
    return themeDefinitions;
  }

  public Boolean getThemeCookie() {
    return themeCookie;
  }

  public void setThemeCookie(final String themeCookie) {
    this.themeCookie = Boolean.valueOf(themeCookie);
  }

  public Boolean getThemeSession() {
    return themeSession;
  }

  public void setThemeSession(final String themeSession) {
    this.themeSession = Boolean.valueOf(themeSession);
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

  public SecurityAnnotation getSecurityAnnotation() {
    return securityAnnotation;
  }

  public void setSecurityAnnotation(final SecurityAnnotation securityAnnotation) {
    this.securityAnnotation = securityAnnotation;
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

  public void setSanitizerClass(final String sanitizerClass) {
    this.sanitizerClass = sanitizerClass;
  }

  public Properties getSanitizerProperties() {
    return sanitizerProperties;
  }

  public void setSanitizerProperties(final Properties sanitizerProperties) {
    this.sanitizerProperties = sanitizerProperties;
  }

  public Boolean getDecodeLineFeed() {
    return decodeLineFeed;
  }

  public void setDecodeLineFeed(final Boolean decodeLineFeed) {
    this.decodeLineFeed = decodeLineFeed;
  }

  public void addMimeType(final String extension, final String type) {
    this.mimeTypes.put(extension, type);
  }

  public Map<String, String> getMimeTypes() {
    return mimeTypes;
  }

  public Boolean getEnableTobagoExceptionHandler() {
    return enableTobagoExceptionHandler;
  }

  public void setEnableTobagoExceptionHandler(Boolean enableTobagoExceptionHandler) {
    this.enableTobagoExceptionHandler = enableTobagoExceptionHandler;
  }

  @Override
  public String toString() {
    return name != null ? name : "(id=" + System.identityHashCode(this) + ")";
  }
}
