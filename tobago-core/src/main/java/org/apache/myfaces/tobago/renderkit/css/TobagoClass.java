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
  COLLAPSED("tobago-collapsed"),
  DELETED("tobago-deleted"),
  DESCENDING("tobago-descending"),
  DISABLED("tobago-disabled"),
  DISPLAY__INLINE__BLOCK("tobago-display-inline-block"),
  DROPDOWN__SUBMENU("tobago-dropdown-submenu"),
  EXPANDED("tobago-expanded"),
  FOCUS("tobago-focus"),
  FOLDER("tobago-folder"),
  FILTER("tobago-filter"),
  HEADER("tobago-header"),
  HELP__CONTAINER("tobago-help-container"),
  LABEL("tobago-label"),
  LABEL__CONTAINER("tobago-label-container"),
  LEVEL("tobago-level"),
  LINK("tobago-link"),
  MESSAGES("tobago-messages"),
  MESSAGES__CONTAINER("tobago-messages-container"),
  NOW("tobago-now"),
  NUMBER("tobago-number"),
  NONE("tobago-none"),
  OBJECT("tobago-object"),
  OPTIONS("tobago-options"),
  OUT("tobago-out"),
  //  PAGE("tobago-page"),
  PAGE__MENU_STORE("tobago-page-menuStore"),
  PAGE__NOSCRIPT("tobago-page-noscript"),
  PAGING("tobago-paging"),
  PANEL("tobago-panel"),
  POPOVER__BOX("tobago-popover-box"),
  BUTTON__LEFT("tobago-button-left"),
  BUTTON__RIGHT("tobago-button-right"),
  RANGE("tobago-range"),
  REQUIRED("tobago-required"),
  RESIZE("tobago-resize"),
  SECTION__CONTENT("tobago-section-content"),
  SELECT__FIELD("tobago-select-field"),
  SELECTED("tobago-selected"),
  SEPARATOR("tobago-separator"),
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
  UNSELECTED("tobago-unselected");

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
