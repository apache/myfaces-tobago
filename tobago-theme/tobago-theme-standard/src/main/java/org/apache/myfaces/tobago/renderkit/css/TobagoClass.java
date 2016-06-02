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

import org.apache.myfaces.tobago.layout.AlignItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TBD: preliminary
 *
 * @since 3.0.0
 */
public enum TobagoClass implements CssItem {

  ALIGN_ITEMS__BASELINE("alignItems-baseline"),
  ALIGN_ITEMS__CENTER("alignItems-center"),
  ALIGN_ITEMS__FLEX_END("alignItems-flexEnd"),
  ALIGN_ITEMS__FLEX_START("alignItems-flexStart"),
  ALIGN_ITEMS__STRETCH("alignItems-stretch"),
  DROPDOWN_SUBMENU("dropdown-submenu"),
  TABLE_LAYOUT__FIXED("tableLayout-fixed"),

  HAS_INFO("has-info"),
  REQUIRED("required"),

  FLEX_LAYOUT("tobago-flexLayout"),
  LABEL("tobago-label"),
  MENU_BAR("tobago-menuBar"),
  MENU__CONTEXT_MENU("tobago-menu-contextMenu"),
  MENU__DROP_DOWN_MENU("tobago-menu-dropDownMenu"),
  MESSAGES("tobago-messages"),
  PANEL("tobago-panel"),
  POPUP("tobago-popup"),
  SECTION__HEADER("tobago-section-header"),
  SHEET__PAGING_INPUT("tobago-sheet-pagingInput"),
  SHEET__PAGING_OUTPUT("tobago-sheet-pagingOutput"),
  SUGGEST("tobago-suggest");

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

}
