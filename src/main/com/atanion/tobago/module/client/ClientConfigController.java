/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 21.08.2002 at 18:00:22.
 * $Id$
 */
package com.atanion.tobago.module.client;

import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.context.Theme;
import com.atanion.tobago.context.ClientProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.ArrayList;

public class ClientConfigController {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(ClientConfigController.class);

// ///////////////////////////////////////////// attributes

  private boolean debugMode;

  private String theme;
  private SelectItem[] themeItems;

  private Locale locale;

  private String contentType;
  private SelectItem[] contentTypeItems;

// ///////////////////////////////////////////// constructor

  public ClientConfigController() {

    // theme

    TobagoConfig tobagoConfig = TobagoConfig.getInstance();

    List themes = tobagoConfig.getThemes();
    themeItems = new SelectItem[themes.size()];
    for (int i = 0; i < themeItems.length; i++) {
      themeItems[i] = new SelectItem(
          ((Theme)(themes.get(i))).getName(),
          ((Theme)(themes.get(i))).getDisplayName());
    }

    // locale

    // contentType

    contentTypeItems = new SelectItem[]{
      new SelectItem("html"),
      new SelectItem("wml")
    };

    // load

    loadFromClientProperties();
  }

// ///////////////////////////////////////////// action

  public String submit() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("invoke!!!");
    }

    storeInClientProperties();

    return "view";
  }

// ///////////////////////////////////////////// logic

  public void storeInClientProperties() {
    ClientProperties client
        = ClientProperties.getInstance(FacesContext.getCurrentInstance());

    client.setDebugMode(debugMode);
    client.setTheme(theme);
    client.setLocale(locale);
    client.setContentType(contentType);
  }

  public void loadFromClientProperties() {
    ClientProperties client
        = ClientProperties.getInstance(FacesContext.getCurrentInstance());

    debugMode = client.isDebugMode();
    theme = client.getTheme();
    locale = client.getLocale();
    contentType = client.getContentType();
  }

// ///////////////////////////////////////////// item getter

  public List getLocaleItems() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    Locale defaultLocale = application.getDefaultLocale();
    Iterator supportedLocales = application.getSupportedLocales();

    List localeItems = new ArrayList();
    localeItems.add(createLocaleItem(defaultLocale));
    while (supportedLocales.hasNext()) {
      Locale locale = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(locale));
    }
    return localeItems;
  }

  private SelectItem createLocaleItem(Locale localeItem) {
    return new SelectItem(localeItem, localeItem.getDisplayName(locale));
  }

// ///////////////////////////////////////////// bean getter + setter

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
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
