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
 * Declaration of the Tobago CSS classes.
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  ASCENDING("tobago-ascending"),
  AUTO__SPACING("tobago-auto-spacing"),
  //  BADGE("tobago-badge"),
  BAR("tobago-bar"),
  BODY("tobago-body"),
  BOX__HEADER("tobago-box-header"),
  // tbd: can this be removed, when using <tobago-button>?
  BUTTON("tobago-button"),
  //  BUTTONS("tobago-buttons"),
  CIRCLE__1("tobago-circle-1"),
  CIRCLE__2("tobago-circle-2"),
  CIRCLE__3("tobago-circle-3"),
  CIRCLE__4("tobago-circle-4"),
  CIRCLE__5("tobago-circle-5"),
  CIRCLE__6("tobago-circle-6"),
  CIRCLE__7("tobago-circle-7"),
  CIRCLE__8("tobago-circle-8"),
  CIRCLE__9("tobago-circle-9"),
  COLLAPSED("tobago-collapsed"),
  //  DATE("tobago-date"),
//  DATE__PICKER("tobago-date-picker"),
  DELETED("tobago-deleted"),
  DESCENDING("tobago-descending"),
  DISABLED("tobago-disabled"),
  DISPLAY__INLINE__BLOCK("tobago-display-inline-block"),
  DROPDOWN__SUBMENU("tobago-dropdown-submenu"),
  EXPANDED("tobago-expanded"),
  //  FILE("tobago-file"),
//  FIGURE("tobago-figure"),
  FOCUS("tobago-focus"),
  FOLDER("tobago-folder"),
  FILTER("tobago-filter"),
  HEADER("tobago-header"),
  //  IMAGE("tobago-image"),
  // tbd: can be removed?
//  IN("tobago-in"),
//  INPUT__GROUP__OUTER("tobago-input-group-outer"),
//  LABEL("tobago-label"),
  LABEL__CONTAINER("tobago-label-container"),
  LEVEL("tobago-level"),
  LINK("tobago-link"),
  MESSAGES("tobago-messages"),
  MESSAGES__CONTAINER("tobago-messages-container"),
  NOW("tobago-now"),
  NUMBER("tobago-number"),
  OBJECT("tobago-object"),
  OPTIONS("tobago-options"),
  OUT("tobago-out"),
  //  PAGE("tobago-page"),
  PAGE__MENU_STORE("tobago-page-menuStore"),
  PAGE__NOSCRIPT("tobago-page-noscript"),
  PAGING("tobago-paging"),
  PANEL("tobago-panel"),
  POPOVER__BOX("tobago-popover-box"),
  RANGE("tobago-range"),
  REQUIRED("tobago-required"),
  RESIZE("tobago-resize"),
  SECTION__CONTENT("tobago-section-content"),
  SELECT__FIELD("tobago-select-field"),
  SELECT_MANY_LISTBOX__OPTION("tobago-selectManyListbox-option"),
  //  SELECT_MANY_SHUTTLE("tobago-selectManyShuttle"),
//  SELECT_MANY_SHUTTLE__ADD("tobago-selectManyShuttle-add"),
//  SELECT_MANY_SHUTTLE__ADD_ALL("tobago-selectManyShuttle-addAll"),
//  SELECT_MANY_SHUTTLE__HIDDEN("tobago-selectManyShuttle-hidden"),
//  SELECT_MANY_SHUTTLE__OPTION("tobago-selectManyShuttle-option"),
//  SELECT_MANY_SHUTTLE__REMOVE("tobago-selectManyShuttle-remove"),
//  SELECT_MANY_SHUTTLE__REMOVE_ALL("tobago-selectManyShuttle-removeAll"),
//  SELECT_MANY_SHUTTLE__SELECTED("tobago-selectManyShuttle-selected"),
//  SELECT_MANY_SHUTTLE__SELECTED_LABEL("tobago-selectManyShuttle-selectedLabel"),
//  SELECT_MANY_SHUTTLE__TOOL_BAR("tobago-selectManyShuttle-toolBar"),
//  SELECT_MANY_SHUTTLE__UNSELECTED("tobago-selectManyShuttle-unselected"),
//  SELECT_MANY_SHUTTLE__UNSELECTED_LABEL("tobago-selectManyShuttle-unselectedLabel"),
  SELECT_ONE_LISTBOX("tobago-selectOneListbox"),
  SELECT_ONE_LISTBOX__OPTION("tobago-selectOneListbox-option"),
  SELECTED("tobago-selected"),
  SEPARATOR("tobago-separator"),
  //  SHEET("tobago-sheet"),
//  SHEET__CELL("tobago-sheet-cell"),
//  SHEET__FOOTER("tobago-sheet-footer"),
//  SHEET__BODY("tobago-sheet-body"),
//  SHEET__HEADER_CELL("tobago-sheet-headerCell"),
//  SHEET__HEADER_RESIZE("tobago-sheet-headerResize"),
//  SHEET__EXPANDED("tobago-sheet-expanded"),
//  SHEET__PAGING_TEXT("tobago-sheet-pagingText"),
//  SHEET__HEADER("tobago-sheet-header"),
//  SHEET__BODY_TABLE("tobago-sheet-bodyTable"),
//  SHEET__COLUMN_SELECTOR("tobago-sheet-columnSelector"),
//  SHEET__HEADER_TABLE("tobago-sheet-headerTable"),
//  SHEET__PAGING_INPUT("tobago-sheet-pagingInput"),
//  SHEET__PAGING_OUTPUT("tobago-sheet-pagingOutput"),
//  SHEET__ROW("tobago-sheet-row"),
  SORTABLE("tobago-sortable"),
  SPREAD("tobago-spread"),
  STARS("tobago-stars"),
  STARS__CONTAINER("tobago-stars-container"),
  STARS__FOCUS_BOX("tobago-stars-focusBox"),
  STARS__PRESELECTED("tobago-stars-preselected"),
  STARS__SELECTED("tobago-stars-selected"),
  STARS__SLIDER("tobago-stars-slider"),
  STARS__TOOLTIP("tobago-stars-tooltip"),
  STARS__UNSELECTED("tobago-stars-unselected"),
  //  TAB("tobago-tab"),
//  TAB__BAR_FACET("tobago-tab-barFacet"),
//  TAB__CONTENT("tobago-tab-content"),
//  TAB_GROUP("tobago-tabGroup"),
  TABLE_LAYOUT__FIXED("tobago-tableLayout-fixed"),
  TEXT__JUSTIFY("tobago-text-justify"),
  TOGGLE("tobago-toggle"),
  TOOLTIP("tobago-tooltip"),
  //  TREE_LABEL("tobago-treeLabel"),
//  TREE_LISTBOX("tobago-treeListbox");
//  TREE_LISTBOX__LEVEL("tobago-treeListbox-level"),
//  TREE_LISTBOX__SELECT("tobago-treeListbox-select"),
//  TREE_NODE("tobago-treeNode"),
//  TREE_SELECT("tobago-treeSelect");
//  TREE_SELECT__LABEL("tobago-treeSelect-label");
  UNSELECTED("tobago-unselected");

  private final String name;

  TobagoClass(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
