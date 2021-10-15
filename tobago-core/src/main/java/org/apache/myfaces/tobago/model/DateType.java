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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

/**
 * Types of date/time-based input fields.
 *
 * @since 5.0.0
 */
public final class DateType implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String STRING_DATE = "date";
  public static final String STRING_DATETIME_LOCAL = "datetime-local";
  public static final String STRING_MONTH = "month";
  public static final String STRING_TIME = "time";
  public static final String STRING_WEEK = "week";

  public static final String PATTERN_DATE = "yyyy-MM-dd";
  public static final String PATTERN_DATETIME_LOCAL_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String PATTERN_DATETIME_LOCAL_SECONDS = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String PATTERN_DATETIME_LOCAL = "yyyy-MM-dd'T'HH:mm";
  public static final String PATTERN_TIME_MILLIS = "HH:mm:ss.SSS";
  public static final String PATTERN_TIME_SECONDS = "HH:mm:ss";
  public static final String PATTERN_TIME = "HH:mm";
  public static final String PATTERN_MONTH = "yyyy-MM";
  public static final String PATTERN_WEEK = "yyyy-'W'ww";

  public static final DateType DATE = new DateType(STRING_DATE, PATTERN_DATE);
  public static final DateType DATETIME_LOCAL = new DateType(STRING_DATETIME_LOCAL, PATTERN_DATETIME_LOCAL);
  public static final DateType MONTH = new DateType(STRING_MONTH, PATTERN_MONTH);
  public static final DateType TIME = new DateType(STRING_TIME, PATTERN_TIME);
  public static final DateType WEEK = new DateType(STRING_WEEK, PATTERN_WEEK);

  private String name;
  private String pattern;

  public DateType(final String name, final String pattern) {
    this.name = name;
    this.pattern = pattern;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public static DateType valueOf(String text) {
    switch (text) {
      case "date":
        return DATE;
      case "datetime-local":
        return DATETIME_LOCAL;
      case "month":
        return MONTH;
      case "time":
        return TIME;
      case "week":
        return WEEK;
      default:
        LOG.error("Unknown date type '{}'", text);
        return null;
    }
  }
}
