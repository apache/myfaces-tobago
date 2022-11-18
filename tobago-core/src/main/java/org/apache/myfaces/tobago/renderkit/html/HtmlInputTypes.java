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

package org.apache.myfaces.tobago.renderkit.html;

public enum HtmlInputTypes implements HtmlTypes {

  TEXT("text"),
  PASSWORD("password"),
  CHECKBOX("checkbox"),
  RADIO("radio"),
  RANGE("range"),
  SUBMIT("submit"),
  RESET("reset"),
  FILE("file"),
  HIDDEN("hidden"),
  IMAGE("image"),
  BUTTON("button"),
  COLOR("color"),
  DATE("date"),
  /**
   * @deprecated
   */
  @Deprecated
  DATETIME("datetime"),
  DATETIME_LOCAL("datetime-local"),
  EMAIL("email"),
  MONTH("month"),
  NUMBER("number"),
  SEARCH("search"),
  TEL("tel"),
  TIME("time"),
  URL("url"),
  WEEK("week");

  public static final String STRING_TEXT = "text";
  public static final String STRING_PASSWORD = "password";
  public static final String STRING_CHECKBOX = "checkbox";
  public static final String STRING_RADIO = "radio";
  public static final String STRING_RANGE = "range";
  public static final String STRING_SUBMIT = "submit";
  public static final String STRING_RESET = "reset";
  public static final String STRING_FILE = "file";
  public static final String STRING_HIDDEN = "hidden";
  public static final String STRING_IMAGE = "image";
  public static final String STRING_BUTTON = "button";
  public static final String STRING_COLOR = "color";
  public static final String STRING_DATE = "date";
  /**
   * @deprecated
   */
  @Deprecated
  public static final String STRING_DATETIME = "datetime";
  public static final String STRING_DATETIME_LOCAL = "datetime-local";
  public static final String STRING_EMAIL = "email";
  public static final String STRING_MONTH = "month";
  public static final String STRING_NUMBER = "number";
  public static final String STRING_SEARCH = "search";
  public static final String STRING_TEL = "tel";
  public static final String STRING_TIME = "time";
  public static final String STRING_URL = "url";
  public static final String STRING_WEEK = "week";

  private final String value;

  HtmlInputTypes(final String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  public final boolean supportsDate() {
    return this == DATE || this == DATETIME_LOCAL || this == WEEK || this == MONTH;
  }

  public final boolean supportsTime() {
    return this == TIME || this == DATETIME_LOCAL;
  }

}
