package org.apache.myfaces.tobago.example.test;

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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ClientConfigController {

  private boolean debugMode;

  private Theme theme;
  private SelectItem[] themeItems;

  private Locale locale;

  private String contentType;
  private SelectItem[] contentTypeItems;

  public ClientConfigController() {

    // theme

    FacesContext facesContext = FacesContext.getCurrentInstance();
    TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    List<Theme> themes = new ArrayList<Theme>(tobagoConfig.getSupportedThemes());
    themes.add(0, tobagoConfig.getDefaultTheme());
    themeItems = new SelectItem[themes.size()];
    for (int i = 0; i < themeItems.length; i++) {
      Theme themeItem = themes.get(i);
      themeItems[i] = new SelectItem(themeItem, themeItem.getDisplayName());
    }

    // locale

    locale = facesContext.getViewRoot().getLocale();

    // contentType

    contentTypeItems = new SelectItem[]{
      new SelectItem("html"),
      new SelectItem("wml", "wml (experimental)"),
      new SelectItem("fo", "fo (experimental)")
    };

    // load

    loadFromClientProperties();
  }

  public String submit() {
    storeInClientProperties();
    return "navigation";
  }

// ///////////////////////////////////////////// logic

  public void storeInClientProperties() {
    ClientProperties client = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance());

    client.setDebugMode(debugMode);
    client.setTheme(theme);
    client.setContentType(contentType);
  }

  public void loadFromClientProperties() {
    ClientProperties client
        = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance());

    debugMode = client.isDebugMode();
    theme = client.getTheme();
    contentType = client.getContentType();
  }

  public List<SelectItem> getLocaleItems() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    Locale defaultLocale = application.getDefaultLocale();
    Iterator supportedLocales = application.getSupportedLocales();

    List<SelectItem> localeItems = new ArrayList<SelectItem>();
    localeItems.add(createLocaleItem(defaultLocale));
    while (supportedLocales.hasNext()) {
      Locale locale = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(locale));
    }
    return localeItems;
  }

  private SelectItem createLocaleItem(Locale localeItem) {
    if (locale != null) {
      return new SelectItem(localeItem, localeItem.getDisplayName(locale));
    } else {
      return new SelectItem(localeItem, localeItem.getDisplayName(localeItem));
    }
  }

  public static ClientConfigController getCurrentInstance(
      FacesContext facesContext, String beanName) {
    return (ClientConfigController) facesContext.getApplication()
        .getVariableResolver().resolveVariable(facesContext, beanName);
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  public Theme getTheme() {
    return theme;
  }

  public String getLocalizedTheme() {
    for (int i = 0; i < themeItems.length; i++) {
      SelectItem themeItem = themeItems[i];
      if (themeItem.getValue().equals(theme)) {
        return themeItem.getLabel();
      }
    }
    return "???";
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public SelectItem[] getThemeItems() {
    return themeItems;
  }

  public void setThemeItems(SelectItem[] themeItems) {
    this.themeItems = themeItems;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLocalizedLocale() {
    if (locale != null) {
      return locale.getDisplayName(locale);
    } else{
      return null;
    }
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public SelectItem[] getContentTypeItems() {
    return contentTypeItems;
  }

  public void setContentTypeItems(SelectItem[] contentTypeItems) {
    this.contentTypeItems = contentTypeItems;
  }
}
