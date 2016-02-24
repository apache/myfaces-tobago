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
public enum DataAttributes implements MarkupLanguageAttributes {

  /**
   * The index of the column of a sheet. This index means the position of the rendered column. It can differ, if there
   * are tc:column with rendered=false.
   */
  COLUMN_INDEX("data-tobago-column-index"),

  /**
   * Custom command attribute. Is used to mark different client side JavaScript buttons.
   * Should only contain the command name as a keyword, for security reasons.
   */
  COMMAND("data-tobago-command"),

  /**
   * The list of commands attached to an element.
   */
  COMMANDS("data-tobago-commands"),

  /**
   * The context path of the application, may be needed in the Client.
   */
  CONTEXT_PATH("data-tobago-context-path"),

  DATE_TIME_I18N("data-tobago-date-time-i18n"),

  /**
   * Holds the day of a calendar control.
   */
  DAY("data-tobago-day"),

  /**
   * Custom disabled attribute. Use for element, that don't have the disabled attribute.
   */
  DISABLED("data-tobago-disabled"),

  DISMISS("data-dismiss"),

  /**
   * Custom form attribute. Used to show the virtual form of the component.
   */
  DEFAULT("data-tobago-default"),

  DELAY("data-tobago-delay"),

  FOR("data-tobago-for"),

  /**
   * Holds the id of the first row in a sheet.
   */
  FIRST("data-tobago-first"),

  /*
   * Holds the index of the row in a sheet, if the sheed has a rowRendered attribute.
   */
  ROW_INDEX("data-tobago-row-index"),

  /**
   * Holds the first day of a week of a calendar control.
   */
  FIRST_DAY_OF_WEEK("data-tobago-first-day-of-week"),

  /**
   * Defines a maximum value.
   */
  LAYOUT("data-tobago-layout"),

  /**
   * Defines a maximum value.
   */
  MAX("data-tobago-max"),

  /**
   * Holds the month of a calendar control.
   */
  MONTH("data-tobago-month"),

  /**
   * Holds the names of the months of a calendar control.
   */
  MONTH_NAMES("data-tobago-month-names"),

  PARTIAL_IDS("data-tobago-partial-ids"),

  PARTIAL_ACTION("data-tobago-partial-action"),

  /**
   * Custom attribute to describe a pattern, e. g. for an date input field.
   */
  PATTERN("data-tobago-pattern"),

  /**
   * Custom reload attribute. Used to reload a panel.
   */
  RELOAD("data-tobago-reload"),

  ROW_ACTION("data-tobago-row-action"),

  SELECTION_MODE("data-tobago-selection-mode"),

  /**
   * The selectable attribute e. g. for trees.
   */
  SELECTABLE("data-tobago-selectable"),

  /**
   * Reference to a sheet.
   */
  SHEET_ID("data-tobago-sheet-id"),

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  SRC_HOVER("data-tobago-src-hover"),

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  SRC_DEFAULT("data-tobago-src-default"),

  /**
   * Alternate to the src attribute, icon open and close.
   */
  SRC_CLOSE("data-tobago-src-close"),

  /**
   * Alternate to the src attribute, icon open and close.
   */
  SRC_OPEN("data-tobago-src-open"),

  /**
   * A way to transport style data in JSON format to the browser. With CSP the normal style attribute isn't allowed.
   */
  STYLE("data-tobago-style"),

  SCROLL_PANEL("data-tobago-scroll-panel"),

  SCROLL_POSITION("data-tobago-scroll-position"),

  SUGGEST_DATA("data-tobago-suggest-data"),

  SUGGEST_DELAY("data-tobago-suggest-delay"),

  SUGGEST_FOR("data-tobago-suggest-for"),

  SUGGEST_MAX_ITEMS("data-tobago-suggest-max-items"),

  SUGGEST_MIN_CHARS("data-tobago-suggest-min-chars"),

  SUGGEST_TOTAL_COUNT("data-tobago-suggest-total-count"),

  SUGGEST_UPDATE("data-tobago-suggest-update"),

  TARGET("data-target"),

  TO_PAGE("data-tobago-to-page"),

  TOGGLE("data-toggle"),

  TRANSITION("data-tobago-transition"),

  /**
   * Id of the parent node in a tree node.
   */
  TREE_PARENT("data-tobago-tree-parent"),

  /**
   * Defines the unit, e. g. to differ between hours, minutes and seconds in a time control.
   */
  UNIT("data-tobago-unit"),

  /**
   * Holds the value (for tags, that don't have a value in HTML).
   */
  VALUE("data-tobago-value"),

  /**
   * Holds the year of a calendar control.
   */
  YEAR("data-tobago-year");

  private final String value;

  DataAttributes(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static MarkupLanguageAttributes dynamic(final String withoutPrefix) {
    return new MarkupLanguageAttributes() {
      @Override
      public String getValue() {
        return "data-" + withoutPrefix;
      }
    };
  }

}
