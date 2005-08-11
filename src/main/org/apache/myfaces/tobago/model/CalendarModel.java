/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 20, 2002 10:05:10 PM
 * $Id: CalendarModel.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */
package org.apache.myfaces.tobago.model;

import java.util.Calendar;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CalendarModel {

  DateModel[][] calendarArray;
  int firstDayOffset;
  int firstDayOfWeek;

  public CalendarModel(Calendar calendar) {
//    int weekCount = CalendarUtils.weekCount(calendar);
    int weekCount = 6; // switching off dynamic weekCount!
    calendarArray = new DateModel[weekCount][7];
    Calendar c = (Calendar) calendar.clone();
    c.clear(Calendar.DAY_OF_MONTH);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // assert c.isLenient() : "'add -x days' may not work in a non-lenient calendar";
    firstDayOffset = firstDayOffset(c);
    firstDayOfWeek = c.getFirstDayOfWeek();
    c.add(Calendar.DAY_OF_WEEK, -firstDayOffset);
    for (int week = 0; week < weekCount; ++week) {
      for (int day = 0; day < 7; ++day) {
        calendarArray[week][day] = new DateModel(c);
        c.add(Calendar.DAY_OF_MONTH, 1);
      }
    }
  }

  public int getWeekCount() {
    return calendarArray.length;
  }

  public int getMonth() {
    return calendarArray[0][firstDayOffset].getMonth();
  }

  public int getYear() {
    return calendarArray[0][firstDayOffset].getYear();
  }

  public DateModel getDate(int week, int day) {
    return calendarArray[week][day];
  }

  private int firstDayOffset(Calendar calendar) {
    Calendar c = (Calendar) calendar.clone();
    c.clear(Calendar.DAY_OF_MONTH);
    c.set(Calendar.DAY_OF_MONTH, 1);
    int day = c.get(Calendar.DAY_OF_WEEK);
    int firstDayOfWeek = c.getFirstDayOfWeek();
    // Fails: assertEquals((1+7-3)%7, (1-3)%7);
    return (day + 7 - firstDayOfWeek) % 7;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Month: ").append(getMonth()).append("\n");
    int weekCount = getWeekCount();
    DecimalFormat format = new DecimalFormat("00");
    SimpleDateFormat dateFormat = new SimpleDateFormat("E");
    for (int day = 0; day < 7; ++day) {
      DateModel date = getDate(0, day);
      String dayName = dateFormat.format(date.getCalendar().getTime());
      buffer.append(dayName.substring(0, 2)).append(" ");
    }
    buffer.append("\n");
    for (int week = 0; week < weekCount; ++week) {
      for (int day = 0; day < 7; ++day) {
        DateModel date = getDate(week, day);
        buffer.append(format.format(date.getDay())).append(" ");
      }
      buffer.append("\n");
    }
    return buffer.toString();
  }

}
