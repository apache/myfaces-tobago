/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 21.08.2002 at 18:00:22.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.config.TobagoConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Locale;

public class ClientConfigModel {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(ClientConfigModel.class);

// ///////////////////////////////////////////// attributes

  private boolean jspComment;

  private boolean debugMode;

  private String theme;
  private SelectItem[] themeItems;

  private String language;
  private SelectItem[] languageItems;

  private String contentType;
  private SelectItem[] contentTypeItems;

// ///////////////////////////////////////////// constructor

  public ClientConfigModel() {
    TobagoConfig tobagoConfig = TobagoConfig.getInstance();
    List themes = tobagoConfig.getThemes();
    themeItems = new SelectItem[themes.size()];
    for (int i = 0; i < themeItems.length; i++) {
      themeItems[i] = new SelectItem(
          ((Theme)(themes.get(i))).getName(),
          ((Theme)(themes.get(i))).getDisplayName());
    }

    languageItems = new SelectItem[]{
      new SelectItem("en", "English"),
      new SelectItem("de", "Deutsch")
    };

    contentTypeItems = new SelectItem[]{
      new SelectItem("html"),
      new SelectItem("wml")
    };

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

    client.setJspComment(jspComment);
    client.setDebugMode(debugMode);
    client.setTheme(theme);
    Locale locale = new Locale(language,
        client.getLocale().getCountry(),
        client.getLocale().getVariant());
    client.setLocale(locale);
    client.setContentType(contentType);
  }

  public void loadFromClientProperties() {
    ClientProperties client = ClientProperties.getInstance(FacesContext.getCurrentInstance());

    jspComment = client.isJspComment();
    debugMode = client.isDebugMode();
    theme = client.getTheme();
    language = client.getLocale().getLanguage();
    contentType = client.getContentType();
  }

// ///////////////////////////////////////////// bean getter + setter

  public boolean isJspComment() {
    return jspComment;
  }

  public void setJspComment(boolean jspComment) {
    this.jspComment = jspComment;
  }

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

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public SelectItem[] getLanguageItems() {
    return languageItems;
  }

  public void setLanguageItems(SelectItem[] languageItems) {
    this.languageItems = languageItems;
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
