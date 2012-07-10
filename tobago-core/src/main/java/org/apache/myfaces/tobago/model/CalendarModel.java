package org.apache.myfaces.tobago.model;

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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarModel {

  private DateModel[][] calendarArray;
  private int firstDayOffset;

  public CalendarModel(Calendar calendar) {
//    int weekCount = CalendarUtils.weekCount(calendar);
    int weekCount = 6; // switching off dynamic weekCount!
    calendarArray = new DateModel[weekCount][7];
    Calendar c = (Calendar) calendar.clone();
    c.clear(Calendar.DAY_OF_MONTH);
    c.set(Calendar.DAY_OF_MONTH, 1);
    // assert c.isLenient() : "'add -x days' may not work in a non-lenient calendar";
    firstDayOffset = firstDayOffset(c);
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
    StringBuilder buffer = new StringBuilder();
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
