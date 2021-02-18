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
  public static String toJavaScriptPattern(final String originalPattern) {
    String pattern;
    if (originalPattern == null || originalPattern.length() > 100) {
      LOG.warn("Pattern not supported: " + originalPattern);
      pattern = "";
    } else {
      pattern = originalPattern;
    }

    StringBuilder analyzedPattern = new StringBuilder();
    StringBuilder nextSegment = new StringBuilder();
    boolean escMode = false;
    for (int i = 0; i < pattern.length(); i++) {
      final char currentChar = pattern.charAt(i);
      if (currentChar == '\'' && !escMode) {
        escMode = true;
        analyzedPattern.append(toJavaScriptPatternPart(nextSegment.toString()));
        nextSegment = new StringBuilder();
      } else if (currentChar == '\'' && pattern.charAt(i + 1) == '\'') {
        nextSegment.append("\\");
        nextSegment.append("'");
        i++;
      } else if (currentChar == '\'') {
        escMode = false;
        analyzedPattern.append(nextSegment);
        nextSegment = new StringBuilder();
      } else {
        if (escMode) {
          nextSegment.append("\\");
        }
        nextSegment.append(currentChar);
      }
    }
    if (nextSegment.toString().length() > 0) {
      if (escMode) {
        analyzedPattern.append(nextSegment);
      } else {
        analyzedPattern.append(toJavaScriptPatternPart(nextSegment.toString()));
      }
    }

    return analyzedPattern.toString();
  }

  public static String toJavaScriptPatternPart(String originalPattern) {

    String pattern = originalPattern;

    if (pattern.contains("G") || pattern.contains("W") || pattern.contains("F")
        || pattern.contains("K") || pattern.contains("z") || pattern.contains("X")) {
      LOG.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
      pattern = "";
    }

    if (pattern.contains("M")) {
      pattern = pattern.replaceAll("M", "m");
    }

    if (pattern.contains("d")) {
      pattern = pattern.replaceAll("dd+", "dd");
    }

    return pattern;
  }

}
