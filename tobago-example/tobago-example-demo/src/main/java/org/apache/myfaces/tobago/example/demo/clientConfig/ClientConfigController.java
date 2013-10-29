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

package org.apache.myfaces.tobago.example.demo.clientConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOG = LoggerFactory.getLogger(ClientConfigController.class);

  private boolean debugMode;

  private Theme theme;
  private SelectItem[] themeItems;

  private Locale locale;

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

    // load

    loadFromClientProperties();
  }

  public String submit() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("invoke!!!");
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();

    storeInClientProperties();

    for (int i = 0; i < ClientConfigPhaseListener.BEAN_NAMES.length; i++) {
      String beanName = ClientConfigPhaseListener.BEAN_NAMES[i];
      ClientConfigController controller
          = getCurrentInstance(facesContext, beanName);
      if (controller != null) {
        controller.setLocale(locale);
      }
    }

    return null;
  }

// ///////////////////////////////////////////// logic

  public void storeInClientProperties() {
    ClientProperties client
        = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance());

    client.setDebugMode(debugMode);
    client.setTheme(theme);
  }

  public void loadFromClientProperties() {
    ClientProperties client
        = VariableResolverUtils.resolveClientProperties(FacesContext.getCurrentInstance());

    debugMode = client.isDebugMode();
    theme = client.getTheme();
  }

  public List<SelectItem> getLocaleItems() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    Locale defaultLocale = application.getDefaultLocale();
    Iterator supportedLocales = application.getSupportedLocales();

    boolean defaultInList = false;
    List<SelectItem> localeItems = new ArrayList<SelectItem>();
    while (supportedLocales.hasNext()) {
      Locale locale = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(locale));
      if (locale.equals(defaultLocale)) {
        defaultInList = true;
      }
    }
    // If the default is already in the list, don't add it.
    // Background: Must the default be in the supported list? Yes or No?
    // This question is not specified explicit and different implemented in the RI and MyFaces
    if (!defaultInList) {
      localeItems.add(0, createLocaleItem(defaultLocale));
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

}
