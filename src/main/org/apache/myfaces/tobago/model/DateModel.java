/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 10:00:32 PM
 * $Id: DateModel.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */
package com.atanion.tobago.model;

import java.util.Calendar;
import java.util.Locale;

public class DateModel {

  private int year;
  private int month;
  private int day;

  public DateModel(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public DateModel(Calendar calendar) {
    this.year = calendar.get(Calendar.YEAR);
    this.month = calendar.get(Calendar.MONTH) + 1;
    this.day = calendar.get(Calendar.DAY_OF_MONTH);
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public Calendar getCalendar() {
    return getCalendar(null);
  }

  public Calendar getCalendar(Locale locale) {
    Calendar calendar = locale != null
        ? Calendar.getInstance(locale) : Calendar.getInstance();
    calendar.set(year, month-1, day);
    return calendar;
  }

}
