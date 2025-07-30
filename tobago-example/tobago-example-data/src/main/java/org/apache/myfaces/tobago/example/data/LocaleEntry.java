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

package org.apache.myfaces.tobago.example.data;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class LocaleEntry {

  private Locale locale;
  private Locale displayLocale;
  private String country;
  private String language;

  public LocaleEntry(final Locale locale, final Locale displayLocale) {
    this.locale = locale;
    this.displayLocale = displayLocale;
    country = locale.getDisplayCountry(displayLocale);
    language = locale.getDisplayLanguage(displayLocale);
  }

  public boolean isDisabled() {
    return StringUtils.isBlank(country);
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public Locale getDisplayLocale() {
    return displayLocale;
  }

  public void setDisplayLocale(final Locale displayLocale) {
    this.displayLocale = displayLocale;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(final String language) {
    this.language = language;
  }
}
