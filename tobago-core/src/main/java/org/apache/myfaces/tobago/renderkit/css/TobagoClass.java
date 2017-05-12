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
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.layout.AlignItems;
import org.apache.myfaces.tobago.layout.JustifyContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Declaration of the Tobago CSS classes.
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  // general classes

  ALIGN_ITEMS__BASELINE("tobago-alignItems-baseline"),
  ALIGN_ITEMS__CENTER("tobago-alignItems-center"),
  ALIGN_ITEMS__FLEX_END("tobago-alignItems-flexEnd"),
  ALIGN_ITEMS__FLEX_START("tobago-alignItems-flexStart"),
  ALIGN_ITEMS__STRETCH("tobago-alignItems-stretch"),

  JUSTIFY_CONTENT__CENTER("tobago-justifyContent-center"),
  JUSTIFY_CONTENT__FLEX_START("tobago-justifyContent-flexStart"),
  JUSTIFY_CONTENT__FLEX_END("tobago-justifyContent-flexEnd"),
  JUSTIFY_CONTENT__SPACE_BETWEEN("tobago-justifyContent-spaceBetween"),
  JUSTIFY_CONTENT__SPACE_AROUND("tobago-justifyContent-spaceAround"),

  DROPDOWN__SUBMENU("tobago-dropdown-submenu"),
  /** @deprecated Since 3.0.1. Please use {@link TobagoClass#DROPDOWN__SUBMENU} */
  @Deprecated
  DROPDOWN_SUBMENU(DROPDOWN__SUBMENU.getName()),
  TABLE_LAYOUT__FIXED("tobago-tableLayout-fixed"),

  HAS__INFO("tobago-has-info"),
  /** @deprecated Since 3.0.1. Please use {@link TobagoClass#HAS__INFO} */
  @Deprecated
  HAS_INFO(HAS__INFO.getName()),
  REQUIRED("tobago-required"),
  SPREAD("tobago-spread"),

  // component based classes

  BOX("tobago-box"),
  BOX__HEADER("tobago-box-header"),
  BUTTON("tobago-button"),
  BUTTONS("tobago-buttons"),
  COLLAPSED("tobago-collapsed"),
  DATE("tobago-date"),
  FILE("tobago-file"),
  FILE__PRETTY("tobago-file-pretty"),
  FILE__REAL("tobago-file-real"),
  FLEX_LAYOUT("tobago-flexLayout"),
  FLOW_LAYOUT("tobago-flowLayout"),
  FIGURE("tobago-figure"),
  FOOTER("tobago-footer"),
  FORM("tobago-form"),
  GRID_LAYOUT("tobago-gridLayout"),
  HEADER("tobago-header"),
  IMAGE("tobago-image"),
  IN("tobago-in"),
  INPUT__GROUP__OUTER("tobago-input-group-outer"),
  /** @deprecated Since 3.0.1. Please use {@link TobagoClass#INPUT__GROUP__OUTER} */
  @Deprecated
  INPUT_GROUP_OUTER(INPUT__GROUP__OUTER.getName()),
  LABEL("tobago-label"),
  LABEL__CONTAINER("tobago-label-container"),
  LINK("tobago-link"),
  LINKS("tobago-links"),
  MESSAGES("tobago-messages"),
  MESSAGES__BUTTON("tobago-messages-button"),
  MESSAGES__CONTAINER("tobago-messages-container"),
  OBJECT("tobago-object"),
  OUT("tobago-out"),
  PAGE("tobago-page"),
  PAGE__MENU_STORE("tobago-page-menuStore"),
  PAGE__NOSCRIPT("tobago-page-noscript"),
  PANEL("tobago-panel"),
  POPUP("tobago-popup"),
  PROGRESS("tobago-progress"),
  SECTION("tobago-section"),
  SECTION__HEADER("tobago-section-header"),
  SECTION__CONTENT("tobago-section-content"),
  SEGMENT_LAYOUT("tobago-segmentLayout"),
  SELECT_BOOLEAN_CHECKBOX("tobago-selectBooleanCheckbox"),
  SELECT_MANY_CHECKBOX("tobago-selectManyCheckbox"),
  SELECT_MANY_CHECKBOX__INLINE("tobago-selectManyCheckbox-inline"),
  SELECT_MANY_LISTBOX("tobago-selectManyListbox"),
  SELECT_MANY_SHUTTLE("tobago-selectManyShuttle"),
  SELECT_MANY_SHUTTLE__ADD("tobago-selectManyShuttle-add"),
  SELECT_MANY_SHUTTLE__ADD_ALL("tobago-selectManyShuttle-addAll"),
  SELECT_MANY_SHUTTLE__HIDDEN("tobago-selectManyShuttle-hidden"),
  SELECT_MANY_SHUTTLE__REMOVE("tobago-selectManyShuttle-remove"),
  SELECT_MANY_SHUTTLE__REMOVE_ALL("tobago-selectManyShuttle-removeAll"),
  SELECT_MANY_SHUTTLE__SELECTED("tobago-selectManyShuttle-selected"),
  SELECT_MANY_SHUTTLE__SELECTED_LABEL("tobago-selectManyShuttle-selectedLabel"),
  SELECT_MANY_SHUTTLE__TOOL_BAR("tobago-selectManyShuttle-toolBar"),
  SELECT_MANY_SHUTTLE__UNSELECTED("tobago-selectManyShuttle-unselected"),
  SELECT_MANY_SHUTTLE__UNSELECTED_LABEL("tobago-selectManyShuttle-unselectedLabel"),
  SELECT_ONE_CHOICE("tobago-selectOneChoice"),
  SELECT_ONE_LISTBOX("tobago-selectOneListbox"),
  SELECT_ONE_RADIO("tobago-selectOneRadio"),
  SELECT_ONE_RADIO__INLINE("tobago-selectOneRadio-inline"),
  SEPARATOR("tobago-separator"),
  SHEET("tobago-sheet"),
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
  SHEET__PAGING_INPUT("tobago-sheet-pagingInput"),
  SHEET__PAGING_OUTPUT("tobago-sheet-pagingOutput"),
  SHEET__CELL__MARKUP__RIGHT("tobago-sheet-cell-markup-right"),
  SHEET__CELL__MARKUP__CENTER("tobago-sheet-cell-markup-center"),
  SHEET__CELL__MARKUP__JUSTIFY("tobago-sheet-cell-markup-justify"),
  SUGGEST("tobago-suggest"),
  TAB__CONTENT("tobago-tab-content"),
  TAB_GROUP("tobago-tabGroup"),
  TAB_GROUP__HEADER("tobago-tabGroup-header"),
  TEXTAREA("tobago-textarea"),
  TREE("tobago-tree"),
  TREE__EXPANDED("tobago-tree-expanded"),
  TREE__SELECTED("tobago-tree-selected"),
  TREE_COMMAND("tobago-treeCommand"),
  TREE_LABEL("tobago-treeLabel"),
  TREE_LISTBOX("tobago-treeListbox"),
  TREE_LISTBOX__LEVEL("tobago-treeListbox-level"),
  TREE_LISTBOX__SELECT("tobago-treeListbox-select"),
  TREE_NODE__TOGGLE("tobago-treeNode-toggle"),
  TREE_SELECT("tobago-treeSelect"),
  TREE_SELECT__LABEL("tobago-treeSelect-label");

  private static final Logger LOG = LoggerFactory.getLogger(TobagoClass.class);

  private final String name;

  TobagoClass(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public static TobagoClass valueOf(AlignItems alignItems) {
    if (alignItems == null) {
      return null;
    } else {
      switch (alignItems) {
        case baseline:
          return ALIGN_ITEMS__BASELINE;
        case center:
          return ALIGN_ITEMS__CENTER;
        case flexEnd:
          return ALIGN_ITEMS__FLEX_END;
        case flexStart:
          return ALIGN_ITEMS__FLEX_START;
        case stretch:
          return ALIGN_ITEMS__STRETCH;
        default:
          LOG.warn("Undefined alignItems: '{}'.", alignItems);
          return null;
      }
    }
  }

  public static TobagoClass valueOf(JustifyContent justifyContent) {
    if (justifyContent == null) {
      return null;
    } else {
      switch (justifyContent) {
        case center:
          return JUSTIFY_CONTENT__CENTER;
        case flexEnd:
          return JUSTIFY_CONTENT__FLEX_END;
        case flexStart:
          return JUSTIFY_CONTENT__FLEX_START;
        case spaceBetween:
          return JUSTIFY_CONTENT__SPACE_BETWEEN;
        case spaceAround:
          return JUSTIFY_CONTENT__SPACE_AROUND;
        default:
          LOG.warn("Undefined justifyContent: '{}'.", justifyContent);
          return null;
      }
    }
  }

  @Preliminary
  public CssItem[] createMarkup(final Markup markup) {
    if (markup != null) {
      final List<CssItem> markups = new ArrayList<CssItem>();
      for (final String markupString : markup) {
        markups.add(new MarkupClass(this, markupString));
      }
      return markups.toArray(new CssItem[markups.size()]);
    } else {
      return null;
    }
  }

  private static class MarkupClass implements CssItem {

    final TobagoClass rendererClass;
    final String markup;

    private MarkupClass(final TobagoClass rendererClass, final String markup) {
      this.rendererClass = rendererClass;
      this.markup = markup;
    }

    @Override
    public String getName() {
      // These values are statistically tested length of the html class attribute
      final StringBuilder builder = new StringBuilder(80);
      final String rendererName = rendererClass.getName().substring("tobago-".length());
      final Theme theme = TobagoContext.getInstance(FacesContext.getCurrentInstance()).getTheme();

      if (theme.getRenderersConfig().isMarkupSupported(rendererName, markup)) {
        builder.append(rendererClass.getName());
        builder.append("-markup-");
        builder.append(markup);
      } else if ("none".equals(markup)) {
        Deprecation.LOG.warn("Markup 'none' is deprecated, please use a NULL pointer instead.");
      } else {
        LOG.warn("Ignoring unknown markup='" + markup + "' for rendererClass='" + rendererClass + "'");
      }

      return builder.toString();
    }
  }
}
