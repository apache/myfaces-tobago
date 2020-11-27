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

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.layout.AlignItems;
import org.apache.myfaces.tobago.layout.JustifyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Declaration of the Tobago CSS classes.
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  // general classes

  /**
   * @deprecated since 4.5.0
   */
  @Deprecated
  INPUT_PSEUDO("tobago-inputPseudo"),

  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#ALIGN_ITEMS_BASELINE}
   */
  @Deprecated
  ALIGN_ITEMS__BASELINE("tobago-alignItems-baseline"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#ALIGN_ITEMS_CENTER}
   */
  @Deprecated
  ALIGN_ITEMS__CENTER("tobago-alignItems-center"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#ALIGN_ITEMS_END}
   */
  @Deprecated
  ALIGN_ITEMS__FLEX_END("tobago-alignItems-flexEnd"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#ALIGN_ITEMS_START}
   */
  @Deprecated
  ALIGN_ITEMS__FLEX_START("tobago-alignItems-flexStart"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#ALIGN_ITEMS_STRETCH}
   */
  @Deprecated
  ALIGN_ITEMS__STRETCH("tobago-alignItems-stretch"),

  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#JUSTIFY_CONTENT_CENTER}
   */
  @Deprecated
  JUSTIFY_CONTENT__CENTER("tobago-justifyContent-center"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#JUSTIFY_CONTENT_START}
   */
  @Deprecated
  JUSTIFY_CONTENT__FLEX_START("tobago-justifyContent-flexStart"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#JUSTIFY_CONTENT_END}
   */
  @Deprecated
  JUSTIFY_CONTENT__FLEX_END("tobago-justifyContent-flexEnd"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#JUSTIFY_CONTENT_BETWEEN}
   */
  @Deprecated
  JUSTIFY_CONTENT__SPACE_BETWEEN("tobago-justifyContent-spaceBetween"),
  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#JUSTIFY_CONTENT_AROUND}
   */
  @Deprecated
  JUSTIFY_CONTENT__SPACE_AROUND("tobago-justifyContent-spaceAround"),

  DROPDOWN__SUBMENU("tobago-dropdown-submenu"),
  /**
   * @deprecated Since 3.0.1. Please use {@link TobagoClass#DROPDOWN__SUBMENU}
   */
  @Deprecated
  DROPDOWN_SUBMENU(DROPDOWN__SUBMENU.getName()),
  TABLE_LAYOUT__FIXED("tobago-tableLayout-fixed"),

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  HAS__INFO("tobago-has-info"),
  /**
   * @deprecated Since 3.0.1. Please use {@link TobagoClass#HAS__INFO}
   */
  @Deprecated
  HAS_INFO(HAS__INFO.getName()),
  REQUIRED("tobago-required"),
  SPREAD("tobago-spread"),

  // component based classes

  BADGE("tobago-badge"),
  BAR("tobago-bar"),
  BOX__HEADER("tobago-box-header"),
  BUTTON("tobago-button"),
  BUTTONS("tobago-buttons"),
  COLLAPSED("tobago-collapsed"),
  DATE("tobago-date"),
  DATE__PICKER("tobago-date-picker"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  DROPDOWN__OPEN("tobago-dropdown-open"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  DROPDOWN__SELECTED("tobago-dropdown-selected"),
  FILE("tobago-file"),
  FIGURE("tobago-figure"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  FORM("tobago-form"),
  /**
   * @deprecated Since 5.0.0. Please use {@link TobagoClass#POPOVER__BUTTON}
   */
  @Deprecated
  HELP__BUTTON("tobago-help-button"),
  IMAGE("tobago-image"),
  IN("tobago-in"),
  INPUT__GROUP__OUTER("tobago-input-group-outer"),
  /**
   * @deprecated Since 3.0.1. Please use {@link TobagoClass#INPUT__GROUP__OUTER}
   */
  @Deprecated
  INPUT_GROUP_OUTER(INPUT__GROUP__OUTER.getName()),
  LABEL("tobago-label"),
  LABEL__CONTAINER("tobago-label-container"),
  LINK("tobago-link"),
  MARGIN__BOTTOM("tobago-margin-bottom"),
  MESSAGES("tobago-messages"),
  /**
   * @deprecated Since 5.0.0. Please use {@link TobagoClass#POPOVER__BUTTON}
   */
  @Deprecated
  MESSAGES__BUTTON("tobago-messages-button"),
  MESSAGES__CONTAINER("tobago-messages-container"),
  OBJECT("tobago-object"),
  OUT("tobago-out"),
  PAGE("tobago-page"),
  PAGE__MENU_STORE("tobago-page-menuStore"),
  PAGE__NOSCRIPT("tobago-page-noscript"),
  PANEL("tobago-panel"),
  POPOVER__BOX("tobago-popover-box"),
  POPOVER__BUTTON("tobago-popover-button"),
  SECTION__CONTENT("tobago-section-content"),
  SELECT_MANY_LISTBOX__OPTION("tobago-selectManyListbox-option"),
  SELECT_MANY_SHUTTLE("tobago-selectManyShuttle"),
  SELECT_MANY_SHUTTLE__ADD("tobago-selectManyShuttle-add"),
  SELECT_MANY_SHUTTLE__ADD_ALL("tobago-selectManyShuttle-addAll"),
  SELECT_MANY_SHUTTLE__HIDDEN("tobago-selectManyShuttle-hidden"),
  SELECT_MANY_SHUTTLE__OPTION("tobago-selectManyShuttle-option"),
  SELECT_MANY_SHUTTLE__REMOVE("tobago-selectManyShuttle-remove"),
  SELECT_MANY_SHUTTLE__REMOVE_ALL("tobago-selectManyShuttle-removeAll"),
  SELECT_MANY_SHUTTLE__SELECTED("tobago-selectManyShuttle-selected"),
  SELECT_MANY_SHUTTLE__SELECTED_LABEL("tobago-selectManyShuttle-selectedLabel"),
  SELECT_MANY_SHUTTLE__TOOL_BAR("tobago-selectManyShuttle-toolBar"),
  SELECT_MANY_SHUTTLE__UNSELECTED("tobago-selectManyShuttle-unselected"),
  SELECT_MANY_SHUTTLE__UNSELECTED_LABEL("tobago-selectManyShuttle-unselectedLabel"),
  SELECT_ONE_LISTBOX("tobago-selectOneListbox"),
  SELECT_ONE_LISTBOX__OPTION("tobago-selectOneListbox-option"),
  SEPARATOR("tobago-separator"),
  SHEET("tobago-sheet"),
  SHEET__CELL("tobago-sheet-cell"),
  SHEET__FOOTER("tobago-sheet-footer"),
  SHEET__BODY("tobago-sheet-body"),
  SHEET__HEADER_CELL("tobago-sheet-headerCell"),
  SHEET__HEADER_RESIZE("tobago-sheet-headerResize"),
  SHEET__EXPANDED("tobago-sheet-expanded"),
  SHEET__PAGING_TEXT("tobago-sheet-pagingText"),
  SHEET__HEADER("tobago-sheet-header"),
  SHEET__BODY_TABLE("tobago-sheet-bodyTable"),
  SHEET__COLUMN_SELECTOR("tobago-sheet-columnSelector"),
  SHEET__HEADER_TABLE("tobago-sheet-headerTable"),
  SHEET__PAGING("tobago-sheet-paging"),
  SHEET__PAGING_INPUT("tobago-sheet-pagingInput"),
  SHEET__PAGING_OUTPUT("tobago-sheet-pagingOutput"),
  SHEET__ROW("tobago-sheet-row"),
  STARS("tobago-stars"),
  STARS__CONTAINER("tobago-stars-container"),
  STARS__FOCUS_BOX("tobago-stars-focusBox"),
  STARS__PRESELECTED("tobago-stars-preselected"),
  STARS__SELECTED("tobago-stars-selected"),
  STARS__SLIDER("tobago-stars-slider"),
  STARS__TOOLTIP("tobago-stars-tooltip"),
  STARS__UNSELECTED("tobago-stars-unselected"),
  TAB("tobago-tab"),
  TAB__BAR_FACET("tobago-tab-barFacet"),
  TAB__CONTENT("tobago-tab-content"),
  TAB_GROUP("tobago-tabGroup"),
  TEXT__JUSTIFY("tobago-text-justify"),
  TREE("tobago-tree"),
  TREE__EXPANDED("tobago-tree-expanded"),
  TREE__SELECTED("tobago-tree-selected"),
  TREE_LABEL("tobago-treeLabel"),
  TREE_LISTBOX("tobago-treeListbox"),
  TREE_LISTBOX__LEVEL("tobago-treeListbox-level"),
  TREE_LISTBOX__SELECT("tobago-treeListbox-select"),
  TREE_NODE("tobago-treeNode"),
  TREE_NODE__TOGGLE("tobago-treeNode-toggle"),
  TREE_SELECT("tobago-treeSelect"),
  TREE_SELECT__LABEL("tobago-treeSelect-label");

  private final String name;

  TobagoClass(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#valueOf(AlignItems)}
   */
  @Deprecated
  public static CssItem valueOf(final AlignItems alignItems) {
    return BootstrapClass.valueOf(alignItems);
  }

  /**
   * @deprecated since 4.0.0, use {@link BootstrapClass#valueOf(JustifyContent)}
   */
  @Deprecated
  public static CssItem valueOf(final JustifyContent justifyContent) {
    return BootstrapClass.valueOf(justifyContent);
  }

  @Preliminary
  public CssItem[] createMarkup(final Markup markup) {
    if (markup != null) {
      final List<CssItem> markups = new ArrayList<>();
      for (final String markupString : markup) {
        markups.add(new MarkupClass(this, markupString));
      }
      return markups.toArray(new CssItem[0]);
    } else {
      return null;
    }
  }

  private static class MarkupClass implements CssItem {

    private final TobagoClass rendererClass;
    private final String markup;

    private MarkupClass(final TobagoClass rendererClass, final String markup) {
      this.rendererClass = rendererClass;
      this.markup = markup;
    }

    @Override
    public String getName() {
      // These values are statistically tested length of the html class attribute
      return rendererClass.getName() + "-markup-" + markup;
    }
  }
}
