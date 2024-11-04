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

package org.apache.myfaces.tobago.layout;

/**
 * Describes the mode of automatically displayed paginator.
 *
 * @since 5.15.0, 6.7.0
 */
public enum PaginatorMode {

  /**
   * A paginator is selected and displayed automatically.
   * Will be the default in future versions.
   */
  auto,

  /**
   * If a paginator is desired, it must be specified on the page, e.g. with a
   * <code>&lt;tc:paginatorList&gt;</code> inside of a <code>&lt;tc:paginatorPanel&gt;</code> tag.
   */
  custom,

  /**
   * The paginator is displayed as a list.
   */
  list,

  /**
   * The page can be selected directly.
   */
  page,

  /**
   * The row can be selected directly.
   */
  row,

  /**
   * The show attributes of the sheet (e.g. showDirectLinks, showPageRange, showPageLinks, showRowCount)
   * are used to display the paginator. This is the default, and emulates the old behavior.
   *
   * @deprecated Please use {@link #auto} if possible, or write a custom paginator.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  useShowAttributes;

  public static final String AUTO = "auto";
  public static final String CUSTOM = "custom";
  public static final String LIST = "list";
  public static final String PAGE = "page";
  public static final String ROW = "row";
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  public static final String USE_SHOW_ATTRIBUTES = "useShowAttributes";

  /**
   * @param name Name of the PaginatorMode.
   * @throws IllegalArgumentException When the name doesn't match any {@link PaginatorMode}.
   */
  public static PaginatorMode parse(final Object name) throws IllegalArgumentException {
    if (name == null) {
      return null;
    }
    if (name instanceof PaginatorMode) {
      return (PaginatorMode) name;
    }
    return valueOf(name.toString());
  }
}
