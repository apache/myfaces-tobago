/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 14.06.2005 13:45:11.
 * $Id: LocaleUtil.java,v 1.2 2005/06/15 15:24:42 lofwyr Exp $
 */
package com.atanion.tobago.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtil {

  public static Locale createLocale(String value) {
    Locale locale = null;
    String[] strings = StringUtils.split(value, "_");
    switch (strings.length) {
      case 1:
        locale = new Locale(strings[0]);
        break;
      case 2:
        locale = new Locale(strings[0], strings[1]);
        break;
      case 3:
        locale = new Locale(strings[0], strings[1], strings[2]);
        break;
    }
    return locale;
  }

  public static List<Locale> getLocaleList(Locale locale) {

    String string = locale.toString();
    List<Locale> locales = new ArrayList<Locale>(3);
    locales.add(locale);
    int underscore;
    while ((underscore = string.lastIndexOf('_')) > 0) {
      string = string.substring(0, underscore);
      locales.add(createLocale(string));
    }

    return locales;
  }
}
