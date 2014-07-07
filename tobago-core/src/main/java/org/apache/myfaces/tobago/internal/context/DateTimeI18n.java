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

package org.apache.myfaces.tobago.internal.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateTimeI18n {

  private static final Logger LOG = LoggerFactory.getLogger(DateTimeI18n.class);

  private static final Map<Locale, DateTimeI18n> CACHE = new HashMap<Locale, DateTimeI18n>();

  private final String[] monthNames = new String[12];
  private final String[] monthNamesShort = new String[12];
  private final String[] dayNames = new String[7];
  private final String[] dayNamesShort = new String[7];
  private final String[] dayNamesMin = new String[7];
  private final int firstDay;

  private DateTimeI18n(Locale locale) {

    LOG.debug("Creating DateTimeI18n for locale: " + locale);

    final Calendar calendar = Calendar.getInstance(locale);

    calendar.set(2000, Calendar.JANUARY, 1);
    final SimpleDateFormat dateFormatMMMMM = new SimpleDateFormat("MMMMM", locale);
    final SimpleDateFormat dateFormatMMM = new SimpleDateFormat("MMM", locale);
    for (int i = 0; i < monthNames.length; i++) {
      monthNames[i] = dateFormatMMMMM.format(calendar.getTime());
      monthNamesShort[i] = dateFormatMMM.format(calendar.getTime());
      calendar.add(java.util.Calendar.MONTH, 1);
    }

    final SimpleDateFormat dateFormatEEEEE = new SimpleDateFormat("EEEEE", locale);
    final SimpleDateFormat dateFormatEEE = new SimpleDateFormat("EEE", locale);
    final SimpleDateFormat dateFormatE = new SimpleDateFormat("E", locale);
    calendar.set(2000, Calendar.JANUARY, 2);
    for (int i = 0; i < dayNames.length; i++) {
      dayNames[i] = dateFormatEEEEE.format(calendar.getTime());
      dayNamesShort[i] = dateFormatEEE.format(calendar.getTime());
      dayNamesMin[i] = dateFormatE.format(calendar.getTime());
      calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    firstDay = calendar.getFirstDayOfWeek() - 1; // because Java: 1 = Sunday and jQuery UI DatePicker: 0 = Sunday
  }

  public static synchronized DateTimeI18n valueOf(Locale locale) {
    DateTimeI18n dateTimeI18n;
    dateTimeI18n = CACHE.get(locale);
    if (dateTimeI18n == null) {
      dateTimeI18n = new DateTimeI18n(locale);
      CACHE.put(locale, dateTimeI18n);
    }
    return dateTimeI18n;
  }

  public String[] getMonthNames() {
    return monthNames;
  }

  public String[] getMonthNamesShort() {
    return monthNamesShort;
  }

  public String[] getDayNames() {
    return dayNames;
  }

  public String[] getDayNamesShort() {
    return dayNamesShort;
  }

  public String[] getDayNamesMin() {
    return dayNamesMin;
  }

  public int getFirstDay() {
    return firstDay;
  }
}
