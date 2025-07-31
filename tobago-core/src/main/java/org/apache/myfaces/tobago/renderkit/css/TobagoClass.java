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

import org.apache.myfaces.tobago.component.DecorationPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Declaration of the Tobago CSS classes.
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  ASCENDING("tobago-ascending"),
  AUTO__SPACING("tobago-auto-spacing"),
  BAR("tobago-bar"),
  BADGES("tobago-badges"),
  BEHAVIOR__CONTAINER("tobago-behavior-container"),
  BODY("tobago-body"),
  BOX__HEADER("tobago-box-header"),
  // tbd: can this be removed, when using <tobago-button>?
  BUTTON("tobago-button"),
  CIRCLE__1("tobago-circle-1"),
  CIRCLE__2("tobago-circle-2"),
  CIRCLE__3("tobago-circle-3"),
  CIRCLE__4("tobago-circle-4"),
  CIRCLE__5("tobago-circle-5"),
  CIRCLE__6("tobago-circle-6"),
  CIRCLE__7("tobago-circle-7"),
  CIRCLE__8("tobago-circle-8"),
  CIRCLE__9("tobago-circle-9"),
  CLOSE("tobago-close"),
  COLLAPSED("tobago-collapsed"),
  COLUMN__PANEL("tobago-column-panel"),
  CONTROLS("tobago-controls"),
  CUSTOM__FOOTER("tobago-custom-footer"),
  DELETED("tobago-deleted"),
  DESCENDING("tobago-descending"),
  DISABLED("tobago-disabled"),
  DISPLAY__INLINE__BLOCK("tobago-display-inline-block"),
  DROPDOWN__MENU("tobago-dropdown-menu"),
  DROPDOWN__SUBMENU("tobago-dropdown-submenu"),
  EXPANDED("tobago-expanded"),
  FOCUS("tobago-focus"),
  FOLDER("tobago-folder"),
  FILTER("tobago-filter"),
  HEADER("tobago-header"),
  HELP__CONTAINER("tobago-help-container"),
  HIDE_CLOSE_BUTTON("tobago-hideCloseButton"),
  HIDE_TOGGLE_ICON("tobago-hideToggleIcon"),
  LABEL("tobago-label"),
  LABEL__CONTAINER("tobago-label-container"),
  LARGE("tobago-large"),
  LEVEL("tobago-level"),
  LINK("tobago-link"),
  MESSAGES("tobago-messages"),
  MESSAGES__CONTAINER("tobago-messages-container"),
  NOW("tobago-now"),
  NUMBER("tobago-number"),
  NO__ENTRIES("tobago-no-entries"),
  NONE("tobago-none"),
  OBJECT("tobago-object"),
  OPTIONS("tobago-options"),
  OUT("tobago-out"),
  //  PAGE("tobago-page"),
  PAGE__MENU_STORE("tobago-page-menuStore"),
  PAGE__NOSCRIPT("tobago-page-noscript"),
  PAGE__POPOVER_STORE("tobago-page-popoverStore"),
  PAGE__TOAST_STORE("tobago-page-toastStore"),
  PAGING("tobago-paging"),
  PANEL("tobago-panel"),
  BUTTON__LEFT("tobago-button-left"),
  BUTTON__RIGHT("tobago-button-right"),
  RANGE("tobago-range"),
  READONLY("tobago-readonly"),
  REQUIRED("tobago-required"),
  RESIZE("tobago-resize"),
  ROW__FILLER("tobago-row-filler"),
  SCROLLBAR__FILLER("tobago-scrollbar-filler"),
  SECTION__CONTENT("tobago-section-content"),
  SELECT__FIELD("tobago-select-field"),
  SELECT__ITEM("tobago-select-item"),
  SELECT__ITEM__GROUP("tobago-select-item-group"),
  SELECTED("tobago-selected"),
  SELECTED__CONTAINER("tobago-selected-container"),
  SEPARATOR("tobago-separator"),
  SHOW("tobago-show"),
  SMALL("tobago-small"),
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
  TABLE_LAYOUT__FIXED("tobago-tableLayout-fixed"),
  TEXT__BOTTOM("tobago-text-bottom"),
  TEXT__JUSTIFY("tobago-text-justify"),
  TEXT__TOP("tobago-text-top"),
  TOGGLE("tobago-toggle"),
  TOOLTIP("tobago-tooltip"),
  UNSELECTED("tobago-unselected"),
  UNSELECTED__CONTAINER("tobago-unselected-container");

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final String name;

  TobagoClass(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public static CssItem valueOf(final DecorationPosition decorationPosition) {
    if (decorationPosition == null) {
      return null;
    } else {
      switch (decorationPosition) {
        case none:
          return NONE;
        case buttonLeft:
          return BUTTON__LEFT;
        case buttonRight:
          return BUTTON__RIGHT;
        case tooltip:
          return TOOLTIP;
        case textTop:
          return TEXT__TOP;
        case textBottom:
          return TEXT__BOTTOM;
        default:
          LOG.warn("Undefined decoration position: '{}'.", decorationPosition);
          return null;
      }
    }
  }
}
