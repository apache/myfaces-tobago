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

/**
 * Custom data attributes.
 * These attributes may transport data to DOM which are not standardized.
 * The format is "data-tobago-*" which is conform to HTML 5, but also works in older browsers.
 */
public final class DataAttributes {

  /**
   * The index of the column of a sheet. This index means the position of the rendered column. It can differ, if there
   * are tc:column with rendered=false.
   */
  public static final String COLUMNINDEX = "data-tobago-columnindex";

  /**
   * Custom command attribute. Is used to mark different client side JavaScript buttons.
   * Should only contain the command name as a keyword, for security reasons.
   */
  public static final String COMMAND = "data-tobago-command";

  /**
   * The list of commands attached to an element.
   */
  public static final String COMMANDS = "data-tobago-commands";

  /**
   * Reference to the corresponding date input field. Used for date picker popups.
   */
  public static final String DATEINPUTID = "data-tobago-dateinputid";

  /**
   * Marker for the OK-button of the date picker popup.
   */
  public static final String DATEPICKEROK = "data-tobago-datepickerok";

  /**
   * Holds the day of a calendar control.
   */
  public static final String DAY = "data-tobago-day";

  /**
   * Custom disabled attribute. Use for element, that don't have the disabled attribute.
   */
  public static final String DISABLED = "data-tobago-disabled";

  /**
   * Custom form attribute. Used to show the virtual form of the component.
   */
  public static final String DEFAULT = "data-tobago-default";

  public static final String DELAY = "data-tobago-delay";

  public static final String FOR = "data-tobago-for";

  /**
   * Holds the id of the first row in a sheet.
   */
  public static final String FIRST = "data-tobago-first";

  /**
   * Holds the first day of a week of a calendar control.
   */
  public static final String FIRSTDAYOFWEEK = "data-tobago-firstdayofweek";

  /**
   * Defines a maximum value.
   */
  public static final String MAX = "data-tobago-max";

  /**
   * Holds the month of a calendar control.
   */
  public static final String MONTH = "data-tobago-month";

  /**
   * Holds the names of the months of a calendar control.
   */
  public static final String MONTHNAMES = "data-tobago-monthnames";

  public static final String PARTIALLY = "data-tobago-partially";

  /**
   * Custom attribute to describe a pattern, e. g. for an date input field.
   */
  public static final String PATTERN = "data-tobago-pattern";

  /**
   * Custom reload attribute. Used to reload a panel.
   */
  public static final String RELOAD = "data-tobago-reload";

  public static final String ROWACTION = "data-tobago-rowaction";

  public static final String SELECTIONMODE = "data-tobago-selectionmode";

  /**
   * The selectable attribute e. g. for trees.
   */
  public static final String SELECTABLE = "data-tobago-selectable";

  /**
   * Reference to a sheet.
   */
  public static final String SHEETID = "data-tobago-sheetid";

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRCHOVER = "data-tobago-srchover";

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRCDEFAULT = "data-tobago-srcdefault";

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRCCLOSE = "data-tobago-srcclose";

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRCOPEN = "data-tobago-srcopen";

  /**
   * A way to transport style data in JSON format to the browser. With CSP the normal style attribute isn't allowed.
   */
  public static final String STYLE = "data-tobago-style";

  /**
   * Custom suggest attribute. Used for input suggest.
   */
  public static final String SUGGEST = "data-tobago-suggest";

  public static final String SUGGEST_DELAY = "data-tobago-suggest-delay";

  public static final String SUGGEST_MIN_CHARS = "data-tobago-suggest-min-chars";

  public static final String TRANSITION = "data-tobago-transition";

  /**
   * Id of the parent node in a tree node.
   */
  public static final String TREEPARENT = "data-tobago-treeparent";

  /**
   * Defines the unit, e. g. to differ between hours, minutes and seconds in a time control.
   */
  public static final String UNIT = "data-tobago-unit";

  /**
   * Holds the year of a calendar control.
   */
  public static final String YEAR = "data-tobago-year";

}
