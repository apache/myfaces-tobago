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

import org.apache.myfaces.tobago.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateTimeI18n {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Map<Locale, DateTimeI18n> CACHE = new HashMap<>();

  private final String[] days = new String[7];
  private final String[] daysShort = new String[7];
  private final String[] daysMin = new String[7];
  private final String[] months = new String[12];
  private final String[] monthsShort = new String[12];
  private final String today;
//  private final String monthTitle;
  private final String clear;
  private final int weekStart;
//  private final String format;
//  private final String titleFormat;

  private final int minDays; // XXX why?
  private final String cancel; // XXX why?
  private final String week; // XXX why?

  private DateTimeI18n(final Locale locale) {

    LOG.debug("Creating DateTimeI18n for locale: " + locale);

    final Calendar calendar = Calendar.getInstance(locale);

    calendar.set(2000, Calendar.JANUARY, 1);
    final SimpleDateFormat dateFormatMMMMM = new SimpleDateFormat("MMMMM", locale);
    final SimpleDateFormat dateFormatMMM = new SimpleDateFormat("MMM", locale);
    for (int i = 0; i < months.length; i++) {
      months[i] = dateFormatMMMMM.format(calendar.getTime());
      monthsShort[i] = dateFormatMMM.format(calendar.getTime());
      calendar.add(Calendar.MONTH, 1);
    }

    final SimpleDateFormat dateFormatEEEEE = new SimpleDateFormat("EEEEE", locale);
    final SimpleDateFormat dateFormatEEE = new SimpleDateFormat("EEE", locale);
    final SimpleDateFormat dateFormatE = new SimpleDateFormat("E", locale);
    calendar.set(2000, Calendar.JANUARY, 2);
    for (int i = 0; i < days.length; i++) {
      days[i] = dateFormatEEEEE.format(calendar.getTime());
      daysShort[i] = dateFormatEEE.format(calendar.getTime());
      daysMin[i] = dateFormatE.format(calendar.getTime());
      calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    weekStart = calendar.getFirstDayOfWeek() - 1; // because Java: 1 = Sunday and JavaScript: 0 = Sunday
    minDays = calendar.getMinimalDaysInFirstWeek();

    FacesContext facesContext = FacesContext.getCurrentInstance();
    today = ResourceUtils.getString(facesContext, "date.today");
    cancel = ResourceUtils.getString(facesContext, "date.cancel");
    clear = ResourceUtils.getString(facesContext, "date.clear");
    week = ResourceUtils.getString(facesContext, "date.week");
  }

  public static synchronized DateTimeI18n valueOf(final Locale locale) {
    DateTimeI18n dateTimeI18n;
    dateTimeI18n = CACHE.get(locale);
    if (dateTimeI18n == null) {
      dateTimeI18n = new DateTimeI18n(locale);
      CACHE.put(locale, dateTimeI18n);
    }
    return dateTimeI18n;
  }

  public String[] getMonths() {
    return months;
  }

  public String[] getMonthsShort() {
    return monthsShort;
  }

  public String[] getDays() {
    return days;
  }

  public String[] getDaysShort() {
    return daysShort;
  }

  public String[] getDaysMin() {
    return daysMin;
  }

  public int getWeekStart() {
    return weekStart;
  }

  public int getMinDays() {
    return minDays;
  }

  public String getToday() {
    return today;
  }

  public String getCancel() {
    return cancel;
  }

  public String getClear() {
    return clear;
  }

  public String getWeek() {
    return week;
  }
}
