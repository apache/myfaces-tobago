/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 08.04.2004 13:21:10.
 * $Id$
 */
package com.atanion.tobago.config;

import javax.faces.application.Application;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;

public class LocaleConfig {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String defaultLocale;
  private List supportedLocales;

// ///////////////////////////////////////////// constructor

  public LocaleConfig() {
    supportedLocales = new ArrayList();
  }

// ///////////////////////////////////////////// code

  public void propergate(Application application) {
    application.setDefaultLocale(createLocale(defaultLocale));
    List collect = new ArrayList(supportedLocales.size());
    for (Iterator i = supportedLocales.iterator(); i.hasNext(); ) {
      collect.add(createLocale((String) i.next()));
    }
    application.setSupportedLocales(collect);
  }

  private Locale createLocale(String string) {
    return new Locale(string);
  }

  public void addSupportedLocale(String locale) {
    supportedLocales.add(locale);
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setDefaultLocale(String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

}
