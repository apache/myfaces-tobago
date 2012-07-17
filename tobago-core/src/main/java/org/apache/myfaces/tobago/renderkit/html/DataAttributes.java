package org.apache.myfaces.tobago.renderkit.html;

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

/**
 * Custom data attributes.
 * These attributes may transport data to DOM which are not standardized.
 * The format is "data-tobago-*" which is conform to HTML 5, but also works in older browsers.
 */
public final class DataAttributes {

  /**
   * Custom command attribute. Is used to mark different client side JavaScript buttons.
   * Should only contain the command name as a keyword, for security reasons.
   */
  public static final String COMMAND = "data-tobago-command";

  /**
   * Reference to the corresponding date input field. Used for date picker popups.
   */
  public static final String DATEINPUTID = "data-tobago-dateinputid";

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

  /**
   * Custom attribute to describe a pattern, e. g. for an date input field.
   */
  public static final String PATTERN = "data-tobago-pattern";

  /**
   * Custom reload attribute. Used to reload a panel.
   */
  public static final String RELOAD = "data-tobago-reload";

  /**
   * The selectable attribute e. g. for trees.
   */
  public static final String SELECTABLE = "data-tobago-selectable";

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
