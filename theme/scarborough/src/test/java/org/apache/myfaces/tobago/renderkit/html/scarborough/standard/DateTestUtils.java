package org.apache.myfaces.tobago.renderkit.html.scarborough.standard;

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

import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTestUtils {

  public static List<String> createMonthNames(boolean longFormat, Locale locale) {
    return createLocalizationNames(
        longFormat ? "MMMM" : "MMM", 0, 11, Calendar.MONTH, locale);
  }

  public static List<String> createDayNames(boolean longFormat, Locale locale) {
    return createLocalizationNames(
        longFormat ? "EEEE" : "E", 1, 7, Calendar.DAY_OF_WEEK, locale);
  }

  private static List<String> createLocalizationNames(String format, int min, int max,
      int field, Locale locale) {
    List<String> names = new ArrayList<String>();
    SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
    for (int day = min; day <= max; ++day) {
      Calendar calendar = Calendar.getInstance();
      calendar.clear();
      calendar.set(field, day);
      Date date = calendar.getTime();
      names.add(dateFormat.format(date));
    }
    return names;
  }

}
