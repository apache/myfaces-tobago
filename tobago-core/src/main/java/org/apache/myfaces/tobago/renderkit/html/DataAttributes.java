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
  public static final String COLUMN_INDEX = "data-tobago-column-index";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String COLUMNINDEX = COLUMN_INDEX;

  /**
   * TBD: needed? may replace with VALUE?
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
  public static final String DATE_INPUT_ID = "data-tobago-date-input-id";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String DATEINPUTID = DATE_INPUT_ID;

  /**
   * Marker for the OK-button of the date picker popup.
   */
  public static final String DATE_PICKER_OK = "data-tobago-date-picker-ok";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String DATEPICKEROK = DATE_PICKER_OK;

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

  /*
   * Holds the index of the row in a sheet, if the sheed has a rowRendered attribute.
   */
  public static final String ROW_INDEX = "data-tobago-row-index";

  /**
   * Holds the first day of a week of a calendar control.
   */
  public static final String FIRST_DAY_OF_WEEK = "data-tobago-first-day-of-week";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String FIRSTDAYOFWEEK = FIRST_DAY_OF_WEEK;

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
  public static final String MONTH_NAMES = "data-tobago-month-names";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String MONTHNAMES = MONTH_NAMES;

  public static final String PARTIALLY = "data-tobago-partially";

  /**
   * Custom attribute to describe a pattern, e. g. for an date input field.
   */
  public static final String PATTERN = "data-tobago-pattern";

  /**
   * Custom reload attribute. Used to reload a panel.
   */
  public static final String RELOAD = "data-tobago-reload";

  public static final String ROW_ACTION = "data-tobago-row-action";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String ROWACTION = ROW_ACTION;

  public static final String SELECTION_MODE = "data-tobago-selection-mode";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SELECTIONMODE = SELECTION_MODE;

  /**
   * The selectable attribute e. g. for trees.
   */
  public static final String SELECTABLE = "data-tobago-selectable";

  /**
   * Reference to a sheet.
   */
  public static final String SHEET_ID = "data-tobago-sheet-id";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SHEETID = SHEET_ID;

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRC_HOVER = "data-tobago-src-hover";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCHOVER = SRC_HOVER;

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRC_DEFAULT = "data-tobago-src-default";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCDEFAULT = SRC_DEFAULT;

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRC_CLOSE = "data-tobago-src-close";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCCLOSE = SRC_CLOSE;

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRC_OPEN = "data-tobago-src-open";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCOPEN = SRC_OPEN;

  /**
   * A way to transport style data in JSON format to the browser. With CSP the normal style attribute isn't allowed.
   */
  public static final String STYLE = "data-tobago-style";

  /**
   * Custom suggest attribute. Used for input suggest.
   * @deprecated Since Tobago 2.0.0. No longer needed.
   */
  @Deprecated
  public static final String SUGGEST = "data-tobago-suggest";

  public static final String SUGGEST_DELAY = "data-tobago-suggest-delay";

  public static final String SUGGEST_MAX_ITEMS = "data-tobago-suggest-max-items";

  public static final String SUGGEST_MIN_CHARS = "data-tobago-suggest-min-chars";

  public static final String SUGGEST_TOTAL_COUNT = "data-tobago-suggest-total-count";

  public static final String SUGGEST_UPDATE = "data-tobago-suggest-update";

  public static final String TRANSITION = "data-tobago-transition";

  /**
   * Id of the parent node in a tree node.
   */
  public static final String TREE_PARENT = "data-tobago-tree-parent";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String TREEPARENT = TREE_PARENT;

  /**
   * Defines the unit, e. g. to differ between hours, minutes and seconds in a time control.
   */
  public static final String UNIT = "data-tobago-unit";

  /**
   * Holds the value (for tags, that don't have a value in HTML).
   */
  public static final String VALUE = "data-tobago-value";

  /**
   * Holds the year of a calendar control.
   */
  public static final String YEAR = "data-tobago-year";

}
