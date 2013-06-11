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
   * Custom disabled attribute. Use for element, that don't have the disabled attribute.
   */
  public static final String DISABLED = "data-tobago-disabled";

  /**
   * Custom reload attribute. Used to reload a panel.
   */
  public static final String RELOAD = "data-tobago-reload";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SELECTIONMODE = "data-tobago-selectionmode";

  public static final String SELECTION_MODE = SELECTIONMODE;

  /**
   * The selectable attribute e. g. for trees.
   */
  public static final String SELECTABLE = "data-tobago-selectable";

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SHEETID = "data-tobago-sheetid";

  /**
   * Reference to a sheet.
   */
  public static final String SHEET_ID = SHEETID;

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCHOVER = "data-tobago-srchover";

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRC_HOVER = SRCHOVER;

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCDEFAULT = "data-tobago-srcdefault";

  /**
   * Alternate to the src attribute, to implement a hover effect.
   */
  public static final String SRC_DEFAULT = SRCDEFAULT;

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCCLOSE = "data-tobago-srcclose";

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRC_CLOSE = SRCCLOSE;

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String SRCOPEN = "data-tobago-srcopen";

  /**
   * Alternate to the src attribute, icon open and close.
   */
  public static final String SRC_OPEN = SRCOPEN;

  /** @deprecated Since 1.5.11 */
  @Deprecated
  public static final String TREEPARENT = "data-tobago-treeparent";

  /**
   * Id of the parent node in a tree node.
   */
  public static final String TREE_PARENT = TREEPARENT;
}
