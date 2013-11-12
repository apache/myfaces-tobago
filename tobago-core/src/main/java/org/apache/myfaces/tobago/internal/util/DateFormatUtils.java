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

import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This code is taken from myfaces core.
 * TODO: Should be sharable (e.g. myfaces-commons).
 * <p/>
 */
public final class DateFormatUtils {

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
  public static String findPattern(DateTimeConverter converter) {
    String pattern = converter.getPattern();

    if (pattern == null) {
      DateFormat dateFormat = getDateFormat(
          converter.getType(), converter.getDateStyle(),
          converter.getTimeStyle(), converter.getLocale());
      if (dateFormat instanceof SimpleDateFormat) {
        SimpleDateFormat format = (SimpleDateFormat) dateFormat;
        pattern = format.toPattern();
      }
    }

    return pattern;
  }

  public static DateFormat getDateFormat(String type, String dateStyle, String timeStyle, Locale locale) {
    DateFormat format;
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

  private static int calcStyle(String name) {
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

}
