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

public enum CustomAttributes implements MarkupLanguageAttributes {

  COLLAPSE_OPERATION("collapse-operation"),
  COLLAPSE_TARGET("collapse-target"),
  CONFIRMATION("confirmation"),
  CLIENT_ID("client-id"),
  CUSTOM_CLASS("custom-class"),
  CUSTOM_EVENT_NAME("custom-event-name"),
  DATE_PATTERN("date-pattern"),
  DELAY("delay"),
  DROP_ZONE("drop-zone"),
  EVENT("event"),
  /**
   * &lt;f:ajax&gt; attribute
   */
  ESCAPE("escape"),
  EXECUTE("execute"),
  EXPANDABLE("expandable"),
  FIELD_ID("field-id"),
  FILTER("filter"),
  FOCUS_ID("focus-id"),
  /**
   * Internationalization strings for e.g. date specific labels, e.g. January or Monday.
   */
  I18N("i18n"),
  IMMEDIATE("immediate"),
  /**
   * The index of the tab inside the tab group.
   */
  INDEX("index"),
  ITEMS("items"),
  /**
   * Lazy loading in sheet.
   */
  LABEL("label"),
  LAZY("lazy"),
  LAZY_ROWS("lazy-rows"),
  /**
   * Is this request/response an update of lazy loaded data in sheet.
   */
  LAZY_UPDATE("lazy-update"),
  LOCALE("locale"),
  LOCAL_MENU("local-menu"),
  MAX_SIZE("max-size"),
  MAX_SIZE_MESSAGE("max-size-message"),
  MAX_ITEMS("max-items"),
  MIN_CHARS("min-chars"),
  OMIT("omit"),
  ORIENTATION("orientation"),
  PARENT("parent"),
  SANITIZE("sanitize"),
  SELECTABLE("selectable"),
  SELECTED("selected"),
  STOP_PROPAGATION("stop-propagation"),
  /**
   * &lt;f:ajax&gt; attribute
   */
  RENDER("render"),

  RESET_VALUES("reset-values"),
  /**
   * Number of rows to show/load for lazy loading in sheet.
   */
  ROWS("rows"),
  /**
   * Number of all rows in sheet.
   */
  ROW_COUNT("row-count"),
  /**
   * Index of a specific row in the sheet.
   */
  ROW_INDEX("row-index"),
  /**
   * The mode of the tab switch: client, reloadTab, reloadPage.
   */
  SWITCH_TYPE("switch-type"),
  TIME_PATTERN("time-pattern"),
  TOTAL_COUNT("total-count"),
  DECOUPLED("decoupled"),
  UPDATE("update");

  private final String value;

  CustomAttributes(final String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

}
