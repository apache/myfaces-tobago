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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUnitTest extends AbstractJavaScriptTestBase {

  private static final int[] YEAR_MONTH_DAY
      = {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH};

  private static final List<Date> DATES = Arrays.asList(
    createDate(1970, 1, 1),
    createDate(2005, 11, 21),
    createDate(2005, 12, 24),
    createDate(1972, 1, 29)
  );

  private static Date createDate(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH); // XXX
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    return calendar.getTime();
  }

  protected void setUp() throws Exception {
    super.setUp();
    loadScriptFile("dateConverter.js");
  }

  @Test
  public void testNumberOnlyDateFormats() throws IOException {
    for (Date date : DATES) {
      checkFormat("yyyyMMdd", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("ddMMyyyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("d.M.yyyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("dd.MM.yyyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("dd/MM/yyyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("dd/MM/yyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("dd/MM/yy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
      checkFormat("EddMMyyyy", date, YEAR_MONTH_DAY, Locale.ENGLISH);
    }
  }

  @Test
  public void testEnglishMonths() throws IOException {
    for (int month = 1; month <= 12; ++month) {
      Date date = createDate(2005, month, 10);
      StringBuilder format = new StringBuilder("M");
      for (int i = 0; i < 4; ++i) {
        format.append('M');
        checkFormat(format.toString(), date, new int[] {Calendar.MONTH}, Locale.ENGLISH);
      }
    }
  }

  @Test
  public void testTwoDigitYears() throws IOException, ParseException {
    checkTwoDigitYears(Locale.ENGLISH);
    checkTwoDigitYears(Locale.GERMAN);
  }

  @Test
  public void checkTwoDigitYears(Locale locale) throws IOException, ParseException {
    DecimalFormat decimalFormat = new DecimalFormat("00");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy", locale);
    Calendar calendar = Calendar.getInstance(locale);
    for (int year = 0; year < 100; ++year) {
      String yearString = decimalFormat.format(year);
      calendar.setTime(simpleDateFormat.parse(yearString));
      Assert.assertEquals(calendar.get(Calendar.YEAR), evalParseDate(yearString, "yy", locale).get(Calendar.YEAR));
    }
  }

  @Test
  public void testEnglishWeekDays() throws IOException {
    for (int day = 1; day <= 7; ++day) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.DAY_OF_WEEK, day);
      Date date = calendar.getTime();

      StringBuilder format = new StringBuilder();
      for (int i = 0; i < 4; ++i) {
        format.append('E');
        checkFormat(format.toString(), date, new int[0], Locale.ENGLISH); // XXX new int[] {Calendar.DAY_OF_WEEK}
      }
    }
  }

  private Object evalFormatDate(Date date, String format) {
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    return eval("new SimpleDateFormat(\"" + format + "\")"
        + ".format(new Date(" + date.getTime() + "))");
  }

  @Test
  public void testLocalization() {
    Assert.assertEquals("January", eval("var s = new DateFormatSymbols(); s.months[0]"));
    Assert.assertEquals("Januar", eval(createSymbols("s", Locale.GERMAN) + "s.months[0]"));
  }

  private String createSymbols(String var, Locale locale) {
    return "var " + var + " = new DateFormatSymbols(); "
        + var + ".months = new Array("
        + createStringList(DateTestUtils.createMonthNames(true, locale)) + ");"
        + var + ".shortMonths = new Array("
        + createStringList(DateTestUtils.createMonthNames(false, locale)) + ");"
        + var + ".weekdays = new Array("
        + createStringList(DateTestUtils.createDayNames(true, locale)) + ");"
        + var + ".shortWeekdays = new Array("
        + createStringList(DateTestUtils.createDayNames(false, locale)) + ");";
  }

  private String createStringList(List<String> strings) {
    return "'" + join(strings, "', '") + "'";
  }

  private String join(List<String> strings, String separator) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < strings.size(); i++) {
      String s = strings.get(i);
      if (i > 0) {
        builder.append(separator);
      }
      builder.append(s);
    }
    return builder.toString();
  }

  private Calendar evalParseDate(String input, String format, Locale locale) {
    long time = evalLong(createSymbols("s", locale) + ";"
        + "new SimpleDateFormat(\"" + format + "\", s)"
        + ".parse(\"" + input + "\").getTime()");
    Calendar calendar = Calendar.getInstance(locale);
    calendar.setTime(new Date(time));
    return calendar;
  }

  private void checkFormat(String format, Date date, int[] fields,
      Locale locale) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
    Assert.assertEquals(simpleDateFormat.format(date), evalFormatDate(date, format));
    Calendar calendar1 = Calendar.getInstance(locale);
    calendar1.setTime(date);
    Calendar calendar2 = evalParseDate(
        simpleDateFormat.format(date), format, locale);
    for (int field : fields) {
      checkField(calendar1, calendar2, field);
    }
  }

  private void checkField(Calendar calendar1, Calendar calendar2, int field) {
    Assert.assertEquals(calendar1.get(field), calendar2.get(field));
  }

}
