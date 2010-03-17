package org.apache.myfaces.tobago.util;

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

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtils {

  private LocaleUtils() {
  }

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
      default:
        // TODO
    }
    return locale;
  }

  /**
   * @deprecated since 1.5
   */
  @Deprecated
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

  /**
   * Returns a list of strings, which are used as suffix for resources from resource manager.
   * 
   * Sample: "de_DE" -> "_de_DE", "_de", "" 
   */
  public static List<String> getLocaleSuffixList(Locale locale) {

    if (locale == null) {
      return Arrays.asList("");
    }
    
    String string = locale.toString();
    String prefix = "_";
    List<String> locales = new ArrayList<String>(4);
    locales.add(prefix + string);
    int underscore;
    while ((underscore = string.lastIndexOf('_')) > 0) {
      string = string.substring(0, underscore);
      locales.add(prefix + string);
    }

    locales.add(""); // default suffix is nothing

    return locales;
  }

}
