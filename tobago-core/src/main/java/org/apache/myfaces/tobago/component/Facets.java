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

  after,
  bar,
  before,
  brand,
  checkbox,
  confirmation,
  constraints,
  /**
  * @deprecated currently not supported
   */
  @Deprecated
  contextMenu,
  label,
  /**
   * @deprecated since 3.0.0. The layout manager tag should surround the content instead.
   * Is only for &lt;tc:gridLayout&gt; needed.
   */
  @Deprecated
  layout,
  pagerPage,
  pagerPageDirect,
  pagerRow,
  radio,
  reload(true),
  sorter;

  Facets() {
  }

  @Deprecated
  Facets(boolean event) {
  }

  public static final String AFTER = "after";
  public static final String BAR = "bar";
  public static final String BEFORE = "before";
  public static final String BRAND = "brand";
  public static final String CHECKBOX = "checkbox";
  public static final String CONFIRMATION = "confirmation";
  public static final String CONSTRAINTS = "constraints";
  /**
   * @deprecated currently not supported
   */
  @Deprecated
  public static final String CONTEXT_MENU = "contextMenu";
  public static final String LABEL = "label";
  /**
   * @deprecated since 3.0.0. The layout manager tag should surround the content instead.
   */
  @Deprecated
  public static final String LAYOUT = "layout";
  public static final String PAGER_PAGE = "pagerPage";
  public static final String PAGER_PAGE_DIRECT = "pagerPageDirect";
  public static final String PAGER_ROW = "pagerRow";
  public static final String RADIO = "radio";
  /**
   * TODO: Must be replaced by a behavior
   */
  public static final String RELOAD = "reload";
  public static final String SORTER = "sorter";
}
