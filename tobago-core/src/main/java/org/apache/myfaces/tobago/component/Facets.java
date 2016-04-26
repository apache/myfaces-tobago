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

package org.apache.myfaces.tobago.component;

public enum Facets {

  action,
  change,
  checkbox,
  click,
  complete,
  confirmation,
  constraints,
  contextMenu,
  dropDownMenu,
  label,
  /**
   * @deprecated since 3.0.0. The layout manager tag should surround the content instead.
   */
  @Deprecated
  layout,
  layoutDefault,
  menuBar,
  pagerPage,
  pagerPageDirect,
  pagerRow,
  popup,
  radio,
  reload,
  resize,
  sorter,
  toolBar;

  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String ACTION = "action";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CHANGE = "change";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CHECKBOX = "checkbox";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CLICK = "click";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String COMPLETE = "complete";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CONFIRMATION = "confirmation";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CONSTRAINTS = "constraints";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String CONTEXT_MENU = "contextMenu";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String DROP_DOWN_MENU = "dropDownMenu";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String LABEL = "label";
  /**
   * @deprecated since 3.0.0. The layout manager tag should surround the content instead. Try to use the enum.
   */
  @Deprecated
  public static final String LAYOUT = "layout";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String LAYOUT_DEFAULT = "layoutDefault";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String MENUBAR = "menuBar";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String PAGER_PAGE = "pagerPage";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String PAGER_PAGE_DIRECT = "pagerPageDirect";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String PAGER_ROW = "pagerRow";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String POPUP = "popup";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String RADIO = "radio";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String RELOAD = "reload";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String RESIZE = "resize";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String SORTER = "sorter";
  /**
   * @deprecated since 3.0.0. Try to use the enum.
   */
  @Deprecated
  public static final String TOOL_BAR = "toolBar";
}
