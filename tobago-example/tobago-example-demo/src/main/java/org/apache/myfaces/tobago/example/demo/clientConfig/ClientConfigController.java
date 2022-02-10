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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ClientConfigController {

  private static final Logger LOG = LoggerFactory.getLogger(ClientConfigController.class);

  private Theme theme;
  private SelectItem[] themeItems;

  private Locale locale;

  public ClientConfigController() {

    // theme

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    final List<Theme> themes = new ArrayList<Theme>(tobagoConfig.getSupportedThemes());
    themes.add(0, tobagoConfig.getDefaultTheme());
    themeItems = new SelectItem[themes.size()];
    for (int i = 0; i < themeItems.length; i++) {
      final Theme themeItem = themes.get(i);
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
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    storeInClientProperties();

    for (int i = 0; i < ClientConfigPhaseListener.BEAN_NAMES.length; i++) {
      final String beanName = ClientConfigPhaseListener.BEAN_NAMES[i];
      final ClientConfigController controller
          = getCurrentInstance(facesContext, beanName);
      if (controller != null) {
        controller.setLocale(locale);
      }
    }

    return null;
  }

// ///////////////////////////////////////////// logic

  public void storeInClientProperties() {
    final ClientProperties client = ClientProperties.getInstance(FacesContext.getCurrentInstance());
    client.setTheme(theme);
  }

  public void loadFromClientProperties() {
    final ClientProperties client = ClientProperties.getInstance(FacesContext.getCurrentInstance());
    theme = client.getTheme();
  }

  public List<SelectItem> getLocaleItems() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    final Locale defaultLocale = application.getDefaultLocale();
    final Iterator supportedLocales = application.getSupportedLocales();

    boolean defaultInList = false;
    final List<SelectItem> localeItems = new ArrayList<SelectItem>();
    while (supportedLocales.hasNext()) {
      final Locale supported = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(supported));
      if (supported.equals(defaultLocale)) {
        defaultInList = true;
      }
    }
    // If the default is already in the list, don't add it.
    // Background: Must the default be in the supported list? Yes or No?
    // This question is not specified explicit and different implemented in the RI and MyFaces
    if (!defaultInList && defaultLocale != null) {
      localeItems.add(0, createLocaleItem(defaultLocale));
    }
    return localeItems;
  }

  private SelectItem createLocaleItem(final Locale localeItem) {
    if (locale != null) {
      return new SelectItem(localeItem, localeItem.getDisplayName(locale));
    } else {
      return new SelectItem(localeItem, localeItem.getDisplayName(localeItem));
    }
  }

  public static ClientConfigController getCurrentInstance(
      final FacesContext facesContext, final String beanName) {
    return (ClientConfigController) facesContext.getApplication()
        .getVariableResolver().resolveVariable(facesContext, beanName);
  }

  public Theme getTheme() {
    return theme;
  }

  public String getLocalizedTheme() {
    for (int i = 0; i < themeItems.length; i++) {
      final SelectItem themeItem = themeItems[i];
      if (ObjectUtils.equals(themeItem.getValue(), theme)) {
        return themeItem.getLabel();
      }
    }
    return "???";
  }

  public void setTheme(final Theme theme) {
    this.theme = theme;
  }

  public SelectItem[] getThemeItems() {
    return themeItems;
  }

  public void setThemeItems(final SelectItem[] themeItems) {
    this.themeItems = themeItems;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLocalizedLocale() {
    if (locale != null) {
      return locale.getDisplayName(locale);
    } else {
      return null;
    }
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

}
