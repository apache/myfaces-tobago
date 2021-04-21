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

package org.apache.myfaces.tobago.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This code is taken from myfaces core.
 * TODO: Should be sharable (e.g. myfaces-commons).
 */
public final class DateFormatUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String TYPE_DATE = "date";
  private static final String TYPE_TIME = "time";
  private static final String TYPE_BOTH = "both";
  private static final String STYLE_DEFAULT = "default";
  private static final String STYLE_MEDIUM = "medium";
  private static final String STYLE_SHORT = "short";
  private static final String STYLE_LONG = "long";
  private static final String STYLE_FULL = "full";

  private DateFormatUtils() {
  }

  /**
   * Find a pattern for the converter.
   * Returns the pattern inside the converter, if any.
   * Otherwise compute the pattern.
   *
   * @return the patter or null, if DateFormat.getDateInstance() returns no SimpleDateFormat.
   */
  public static String findPattern(final DateTimeConverter converter) {
    String pattern = converter.getPattern();

    if (pattern == null) {
      final DateFormat dateFormat = getDateFormat(
          converter.getType(), converter.getDateStyle(),
          converter.getTimeStyle(), converter.getLocale());
      if (dateFormat instanceof SimpleDateFormat) {
        final SimpleDateFormat format = (SimpleDateFormat) dateFormat;
        pattern = format.toPattern();
      }
    }

    return pattern;
  }

  private static DateFormat getDateFormat(
      final String type, final String dateStyle, final String timeStyle, final Locale locale) {
    final DateFormat format;
    if (type.equals(TYPE_DATE)) {
      format = DateFormat.getDateInstance(calcStyle(dateStyle), locale);
    } else if (type.equals(TYPE_TIME)) {
      format = DateFormat.getTimeInstance(calcStyle(timeStyle), locale);
    } else if (type.equals(TYPE_BOTH)) {
      format = DateFormat.getDateTimeInstance(calcStyle(dateStyle),
          calcStyle(timeStyle),
          locale);
    } else {
      throw new ConverterException("invalid type '" + type + "'");
    }

    // format cannot be lenient (JSR-127)
    format.setLenient(false);
    return format;
  }

  private static int calcStyle(final String name) {
    if (name.equals(STYLE_DEFAULT)) {
      return DateFormat.DEFAULT;
    }
    if (name.equals(STYLE_MEDIUM)) {
      return DateFormat.MEDIUM;
    }
    if (name.equals(STYLE_SHORT)) {
      return DateFormat.SHORT;
    }
    if (name.equals(STYLE_LONG)) {
      return DateFormat.LONG;
    }
    if (name.equals(STYLE_FULL)) {
      return DateFormat.FULL;
    }

    throw new ConverterException("invalid style '" + name + "'");
  }

  /**
   * Get the pattern from the "Java world",
   * see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html
   * and convert it to 'vanillajs-datepicker', see https://mymth.github.io/vanillajs-datepicker/#/date-string+format
   * Attention: Not every pattern char is supported.
   */
  public static class DateTimeJavaScriptPattern {

    private StringBuilder buffer = new StringBuilder();
    private StringBuilder datePattern;
    private StringBuilder timePattern;
    private String separator;

    private boolean dateMode = false;
    private boolean timeMode = false;

    private int lastDateIndex = -1;
    private int lastTimeIndex = -1;

    public DateTimeJavaScriptPattern(final String javaPattern) {
      String pattern;
      if (javaPattern == null) {
        LOG.warn("Empty pattern not supported!");
        // XXX an missing pattern might be supporable without Datepicker, but with simple <tc:in>
        pattern = "";
      } else if (javaPattern.length() > 100) {
        LOG.warn("Pattern too long (max = 100): '{}'!", javaPattern);
        pattern = "";
      } else if (javaPattern.indexOf('\'') > -1) {
        LOG.warn("Pattern escape char ' is not supported: '{}'!", javaPattern);
        pattern = "";
      } else if (StringUtils.containsAny(javaPattern, "GWFKzX")) {
        LOG.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: '{}'!", javaPattern);
        pattern = "";
      } else {
        pattern = javaPattern;
      }

      for (int i = 0; i < pattern.length(); i++) {
        final char c = pattern.charAt(i);
        checkSpit(i, c);
        append(c);
      }
    }

    private void checkSpit(final int index, final char c) {

      boolean isDate = isDate(c);
      boolean isTime = isTime(c);

      if (isDate){
        lastDateIndex = index;
      }
      if (isTime){
        lastTimeIndex = index;
      }

      if (!dateMode && !timeMode && isDate) {
        dateMode = true;
        datePattern = new StringBuilder(buffer);
        buffer.setLength(0);
      } else if (!dateMode && !timeMode && isTime) {
        timeMode = true;
        timePattern = new StringBuilder(buffer);
        buffer.setLength(0);
      } else if (!dateMode && !timeMode) {
        LOG.warn("Prefix without date/time char currently not supported!");
      } else if (dateMode && isTime) {
        timeMode = true;
        dateMode = false;
        timePattern = new StringBuilder();
        separator = datePattern.substring(lastDateIndex + 1);
        datePattern.setLength(lastDateIndex + 1);
        if (timePattern != null) {
          LOG.warn("Pattern mixed!");
        }
      } else if (timeMode && isDate) {
        timeMode = false;
        dateMode = true;
        datePattern = new StringBuilder();
        separator = timePattern.substring(lastTimeIndex + 1);
        timePattern.setLength(lastTimeIndex + 1);
        if (datePattern != null) {
          LOG.warn("Pattern mixed!");
        }
      } else {
        // nothing to switch
      }
    }

    private void append(final char c) {
      final char converted = convert(c);

      if (dateMode) {
        datePattern.append(converted);
      } else if (timeMode) {
        timePattern.append(converted);
      } else {
        buffer.append(converted);
      }
    }

    public String getDatePattern() {
      return datePattern != null ? datePattern.toString() : null;
    }

    public String getTimePattern() {
      return timePattern != null ? timePattern.toString() : null;
    }

    public String getSeparator() {
      return separator;
    }

    public static boolean isDate(final char c) {
      return c == 'y' || c == 'Y' || c == 'M' || c == 'L' || c == 'd';
    }

    private static boolean isTime(final char c) {
      return c == 'H' || c == 'm' || c == 's';
    }

    private char convert(char c) {

      if (c == 'M') {
        return 'm';
      } else {
        return c;
      }
    }
  }
}
