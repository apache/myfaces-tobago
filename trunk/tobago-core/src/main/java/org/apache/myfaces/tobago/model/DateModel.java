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

package org.apache.myfaces.tobago.model;

import java.util.Calendar;
import java.util.Locale;

public class DateModel {

  private int year;
  private int month;
  private int day;

  public DateModel(final int year, final int month, final int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public DateModel(final Calendar calendar) {
    this.year = calendar.get(Calendar.YEAR);
    this.month = calendar.get(Calendar.MONTH) + 1;
    this.day = calendar.get(Calendar.DAY_OF_MONTH);
  }

  public int getYear() {
    return year;
  }

  public void setYear(final int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(final int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(final int day) {
    this.day = day;
  }

  public Calendar getCalendar() {
    return getCalendar(null);
  }

  public Calendar getCalendar(final Locale locale) {
    final Calendar calendar = locale != null
        ? Calendar.getInstance(locale) : Calendar.getInstance();
    calendar.set(year, month - 1, day);
    return calendar;
  }

}
