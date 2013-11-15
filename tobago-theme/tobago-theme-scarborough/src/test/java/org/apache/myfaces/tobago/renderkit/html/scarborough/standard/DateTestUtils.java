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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard;

import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTestUtils {

  public static List<String> createMonthNames(final boolean longFormat, final Locale locale) {
    return createLocalizationNames(
        longFormat ? "MMMM" : "MMM", 0, 11, Calendar.MONTH, locale);
  }

  public static List<String> createDayNames(final boolean longFormat, final Locale locale) {
    return createLocalizationNames(
        longFormat ? "EEEE" : "E", 1, 7, Calendar.DAY_OF_WEEK, locale);
  }

  private static List<String> createLocalizationNames(final String format, final int min, final int max,
      final int field, final Locale locale) {
    final List<String> names = new ArrayList<String>();
    final SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
    for (int day = min; day <= max; ++day) {
      final Calendar calendar = Calendar.getInstance();
      calendar.clear();
      calendar.set(field, day);
      final Date date = calendar.getTime();
      names.add(dateFormat.format(date));
    }
    return names;
  }

}
