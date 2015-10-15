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

package org.apache.myfaces.tobago.renderkit.css;

/**
 * TBD: preliminary
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  DROPDOWN_SUBMENU("dropdown-submenu"),
  FLEX_LAYOUT("tobago-flexLayout"),
  LABEL("tobago-label"),
  MENU_BAR("tobago-menuBar"),
  MENU__CONTEXT_MENU("tobago-menu-contextMenu"),
  MENU__DROP_DOWN_MENU("tobago-menu-dropDownMenu"),
  MESSAGES("tobago-messages"),
  PANEL("tobago-panel"),
  POPUP("tobago-popup"),
  SHEET__PAGING_INPUT("tobago-sheet-pagingInput"),
  SHEET__PAGING_LINKS("tobago-sheet-pagingLinks"),
  SHEET__PAGING_OUTPUT("tobago-sheet-pagingOutput"),
  SHEET__PAGING_PAGES("tobago-sheet-pagingPages"),
  SUGGEST("tobago-suggest");

  private final String name;

  TobagoClass(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
