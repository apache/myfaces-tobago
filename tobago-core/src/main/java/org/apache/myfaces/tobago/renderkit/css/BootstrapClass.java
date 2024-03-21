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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsSegmentLayout;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.layout.AlignItems;
import org.apache.myfaces.tobago.layout.JustifyContent;
import org.apache.myfaces.tobago.layout.Margin;
import org.apache.myfaces.tobago.layout.MarginTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.layout.SegmentJustify;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.layout.VerticalAlign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CSS classes for the Bootstrap Library.
 *
 * @since 3.0.0
 */
public enum BootstrapClass implements CssItem {

  ACTIVE("active"),
  ALERT("alert"),
  ALERT_DANGER("alert-danger"),
  ALERT_WARNING("alert-warning"),
  ALERT_INFO("alert-info"),
  ALERT_DISMISSIBLE("alert-dismissible"),
  ALIGN_ITEMS_BASELINE("align-items-baseline"),
  ALIGN_ITEMS_CENTER("align-items-center"),
  ALIGN_ITEMS_END("align-items-end"),
  ALIGN_ITEMS_START("align-items-start"),
  ALIGN_ITEMS_STRETCH("align-items-stretch"),
  ALIGN_BOTTOM("align-bottom"),
  ALIGN_MIDDLE("align-middle"),
  ALIGN_TOP("align-top"),
  /**
   * @deprecated Please use {@link #POPOVER_ARROW} or {@link #TOOLTIP_ARROW}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  ARROW("arrow"),
  BG_DANGER("bg-danger"),
  BG_DARK("bg-dark"),
  BG_INFO("bg-info"),
  BG_LIGHT("bg-light"),
  BG_PRIMARY("bg-primary"),
  BG_SECONDARY("bg-secondary"),
  BG_SUCCESS("bg-success"),
  BG_WARNING("bg-warning"),
  BADGE("badge"),
  /**
   * @deprecated Please use {@link #BG_DANGER}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_DANGER("bg-danger"),
  /**
   * @deprecated Please use {@link #BG_DARK}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_DARK("bg-dark"),
  /**
   * @deprecated Please use {@link #BG_INFO}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_INFO("bg-info"),
  /**
   * @deprecated Please use {@link #BG_LIGHT}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_LIGHT("bg-light"),
  /**
   * @deprecated Please use {@link #ROUNDED_PILL}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_PILL("rounded-pill"),
  /**
   * @deprecated Please use {@link #BG_PRIMARY}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_PRIMARY("bg-primary"),
  /**
   * @deprecated Please use {@link #BG_SECONDARY}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_SECONDARY("bg-secondary"),
  /**
   * @deprecated Please use {@link #BG_SUCCESS}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_SUCCESS("bg-success"),
  /**
   * @deprecated Please use {@link #BG_WARNING}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  BADGE_WARNING("bg-warning"),
  BORDER_DANGER("border-danger"),
  BORDER_INFO("border-info"),
  BORDER_WARNING("border-warning"),
  BTN("btn"),
  BTN_CLOSE("btn-close"),
  BTN_DANGER("btn-danger"),
  BTN_DARK("btn-dark"),
  BTN_GROUP("btn-group"),
  BTN_GROUP_VERTICAL("btn-group-vertical"),
  BTN_INFO("btn-info"),
  BTN_LG("btn-lg"),
  BTN_LIGHT("btn-light"),
  BTN_LINK("btn-link"),
  BTN_PRIMARY("btn-primary"),
  BTN_OUTLINE_DANGER("btn-outline-danger"),
  BTN_OUTLINE_DARK("btn-outline-dark"),
  BTN_OUTLINE_INFO("btn-outline-info"),
  BTN_OUTLINE_LIGHT("btn-outline-light"),
  BTN_OUTLINE_PRIMARY("btn-outline-primary"),
  BTN_OUTLINE_SECONDARY("btn-outline-secondary"),
  BTN_OUTLINE_SUCCESS("btn-outline-success"),
  BTN_OUTLINE_WARNING("btn-outline-warning"),
  BTN_SECONDARY("btn-secondary"),
  BTN_SM("btn-sm"),
  BTN_SUCCESS("btn-success"),
  BTN_TOOLBAR("btn-toolbar"),
  BTN_WARNING("btn-warning"),
  CARD("card"),
  CARD_BODY("card-body"),
  CARD_HEADER("card-header"),
  CARD_HEADER_TABS("card-header-tabs"),
  CARD_TITLE("card-title"),
  /**
   * @deprecated Please use {@link #BTN_CLOSE}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CLOSE("btn-close"),
  COLLAPSE("collapse"),
  COL_FORM_LABEL("col-form-label"),
  COL_FORM_LABEL_LG("col-form-label-lg"),
  COL_FORM_LABEL_SM("col-form-label-sm"),
  COL_LG("col-lg"),
  COL_LG_1("col-lg-1"),
  COL_LG_2("col-lg-2"),
  COL_LG_3("col-lg-3"),
  COL_LG_4("col-lg-4"),
  COL_LG_5("col-lg-5"),
  COL_LG_6("col-lg-6"),
  COL_LG_7("col-lg-7"),
  COL_LG_8("col-lg-8"),
  COL_LG_9("col-lg-9"),
  COL_LG_10("col-lg-10"),
  COL_LG_11("col-lg-11"),
  COL_LG_12("col-lg-12"),
  COL_LG_AUTO("col-lg-auto"),
  COL_MD("col-md"),
  COL_MD_1("col-md-1"),
  COL_MD_2("col-md-2"),
  COL_MD_3("col-md-3"),
  COL_MD_4("col-md-4"),
  COL_MD_5("col-md-5"),
  COL_MD_6("col-md-6"),
  COL_MD_7("col-md-7"),
  COL_MD_8("col-md-8"),
  COL_MD_9("col-md-9"),
  COL_MD_10("col-md-10"),
  COL_MD_11("col-md-11"),
  COL_MD_12("col-md-12"),
  COL_MD_AUTO("col-md-auto"),
  COL_SM("col-sm"),
  COL_SM_1("col-sm-1"),
  COL_SM_2("col-sm-2"),
  COL_SM_3("col-sm-3"),
  COL_SM_4("col-sm-4"),
  COL_SM_5("col-sm-5"),
  COL_SM_6("col-sm-6"),
  COL_SM_7("col-sm-7"),
  COL_SM_8("col-sm-8"),
  COL_SM_9("col-sm-9"),
  COL_SM_10("col-sm-10"),
  COL_SM_11("col-sm-11"),
  COL_SM_12("col-sm-12"),
  COL_SM_AUTO("col-sm-auto"),
  COL_XL("col-xl"),
  COL_XL_1("col-xl-1"),
  COL_XL_2("col-xl-2"),
  COL_XL_3("col-xl-3"),
  COL_XL_4("col-xl-4"),
  COL_XL_5("col-xl-5"),
  COL_XL_6("col-xl-6"),
  COL_XL_7("col-xl-7"),
  COL_XL_8("col-xl-8"),
  COL_XL_9("col-xl-9"),
  COL_XL_10("col-xl-10"),
  COL_XL_11("col-xl-11"),
  COL_XL_12("col-xl-12"),
  COL_XL_AUTO("col-xl-auto"),
  COL_XXL("col-xxl"),
  COL_XXL_1("col-xxl-1"),
  COL_XXL_2("col-xxl-2"),
  COL_XXL_3("col-xxl-3"),
  COL_XXL_4("col-xxl-4"),
  COL_XXL_5("col-xxl-5"),
  COL_XXL_6("col-xxl-6"),
  COL_XXL_7("col-xxl-7"),
  COL_XXL_8("col-xxl-8"),
  COL_XXL_9("col-xxl-9"),
  COL_XXL_10("col-xxl-10"),
  COL_XXL_11("col-xxl-11"),
  COL_XXL_12("col-xxl-12"),
  COL_XXL_AUTO("col-xxl-auto"),
  COL("col"),
  COL_1("col-1"),
  COL_2("col-2"),
  COL_3("col-3"),
  COL_4("col-4"),
  COL_5("col-5"),
  COL_6("col-6"),
  COL_7("col-7"),
  COL_8("col-8"),
  COL_9("col-9"),
  COL_10("col-10"),
  COL_11("col-11"),
  COL_12("col-12"),
  COL_AUTO("col-auto"),
  CONTAINER("container"),
  CONTAINER_FLUID("container-fluid"),
  /**
   * @deprecated Please use {@link #FORM_CHECK}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_CHECKBOX("form-check"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_CONTROL("custom-control"),
  /**
   * @deprecated Please use {@link #FORM_CHECK_LABEL} or maybe any other?
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_CONTROL_LABEL("form-check-label"),
  /**
   * @deprecated Please use {@link #FORM_CHECK_INLINE} or maybe any other?
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_CONTROL_INLINE("form-check-inline"),
  /**
   * @deprecated Please use {@link #FORM_CHECK_INPUT} or maybe any other?
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_CONTROL_INPUT("form-check-input"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_FILE("custom-file"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_FILE_INPUT("custom-file-input"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_FILE_LABEL("custom-file-label"),
  /**
   * @deprecated Please use {@link #FORM_CHECK}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_RADIO("form-check"),
  /**
   * @deprecated Please use {@link #FORM_SELECT}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_SELECT("form-select"),
  /**
   * @deprecated Please use {@link #FORM_SWITCH}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  CUSTOM_SWITCH("form-switch"),
  D_FLEX("d-flex"),
  D_INLINE("d-inline"),
  D_INLINE_FLEX("d-inline-flex"),
  D_NONE("d-none"),
  D_SM_NONE("d-sm-none"),
  DISABLED("disabled"),
  DROPDOWN("dropdown"),
  DROPDOWN_DIVIDER("dropdown-divider"),
  DROPDOWN_ITEM("dropdown-item"),
  DROPDOWN_MENU("dropdown-menu"),
  DROPDOWN_MENU_END("dropdown-menu-end"),
  DROPDOWN_TOGGLE("dropdown-toggle"),
  DROPDOWN_TOGGLE_SPLIT("dropdown-toggle-split"),
  ERROR_FEEDBACK("error-feedback"),
  ERROR_TOOLTIP("error-tooltip"),
  FADE("fade"),
  FIGURE("figure"),
  FIGURE_CAPTION("figure-caption"),
  FIGURE_IMG("figure-img"),
  FIXED_BOTTOM("fixed-bottom"),
  /**
   * @deprecated Please use {@link #STICKY_TOP}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FIXED_TOP("fixed-top"),
  FLEX_COLUMN("flex-column"),
  FLEX_COLUMN_REVERSE("flex-column-reverse"),
  FLEX_ROW("flex-row"),
  FLEX_ROW_REVERSE("flex-row-reverse"),
  FLOAT_END("float-end"),
  FLOAT_START("float-start"),
  /**
   * @deprecated Please use {@link #FST_ITALIC}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FONT_ITALIC("fst-italic"),
  /**
   * @deprecated Please use {@link #FW_BOLD}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FONT_WEIGHT_BOLD("font-weight-bold"),
  /**
   * @deprecated Please use {@link #FW_LIGHT}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FONT_WEIGHT_LIGHT("font-weight-light"),
  FORM_CHECK("form-check"),
  FORM_CHECK_INLINE("form-check-inline"),
  FORM_CHECK_INPUT("form-check-input"),
  FORM_CHECK_LABEL("form-check-label"),
  FORM_CONTROL("form-control"),
  FORM_CONTROL_LG("form-control-lg"),
  FORM_CONTROL_SM("form-control-sm"),
  FORM_CONTROL_PLAINTEXT("form-control-plaintext"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_FILE("form-file"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  FORM_FILE_BUTTON("form-file-button"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_FILE_INPUT("form-file-input"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_FILE_LABEL("form-file-label"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_FILE_TEXT("form-file-text"),
  /**
   * @deprecated Please use {@link #MB_3} or other
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_GROUP("form-group"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  FORM_INLINE("form-inline"),
  FORM_RANGE("form-range"),
  FORM_SELECT("form-select"),
  FORM_SELECT_LG("form-select-lg"),
  FORM_SELECT_SM("form-select-sm"),
  FORM_SWITCH("form-switch"),
  FST_ITALIC("fst-italic"),
  FW_BOLD("fw-bold"),
  FW_BOLDER("fw-bolder"),
  FW_NORMAL("fw-normal"),
  FW_LIGHT("fw-light"),
  FW_LIGHTER("fw-lighter"),
  HELP_FEEDBACK("help-feedback"),
  HELP_TOOLTIP("help-tooltip"),
  INFO_FEEDBACK("info-feedback"),
  INFO_TOOLTIP("info-tooltip"),
  INPUT_GROUP("input-group"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  INPUT_GROUP_APPEND("input-group-append"),
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  INPUT_GROUP_PREPEND("input-group-prepend"),
  INPUT_GROUP_TEXT("input-group-text"),
  INVISIBLE("invisible"),
  IS_ERROR("is-error"),
  IS_INFO("is-info"),
  IS_WARNING("is-warning"),
  JUSTIFY_CONTENT_AROUND("justify-content-around"),
  JUSTIFY_CONTENT_BETWEEN("justify-content-between"),
  JUSTIFY_CONTENT_CENTER("justify-content-center"),
  JUSTIFY_CONTENT_END("justify-content-end"),
  JUSTIFY_CONTENT_START("justify-content-start"),
  LIST_GROUP("list-group"),
  LIST_GROUP_ITEM("list-group-item"),
  MB_0("mb-0"),
  MB_1("mb-1"),
  MB_2("mb-2"),
  MB_3("mb-3"),
  MB_4("mb-4"),
  MB_5("mb-5"),
  MB_AUTO("mb-auto"),
  MS_0("ms-0"),
  MS_1("ms-1"),
  MS_2("ms-2"),
  MS_3("ms-3"),
  MS_4("ms-4"),
  MS_5("ms-5"),
  MS_AUTO("ms-auto"),
  MS_LG_AUTO("ms-lg-auto"),
  MS_MD_AUTO("ms-md-auto"),
  MS_SM_AUTO("ms-sm-auto"),
  MS_XL_AUTO("ms-xl-auto"),
  MODAL("modal"),
  MODAL_BODY("modal-body"),
  MODAL_CONTENT("modal-content"),
  MODAL_DIALOG("modal-dialog"),
  MODAL_FOOTER("modal-footer"),
  MODAL_HEADER("modal-header"),
  MODAL_LG("modal-lg"),
  MODAL_SM("modal-sm"),
  MODAL_XL("modal-xl"),
  MODAL_TITLE("modal-title"),
  ME_0("me-0"),
  ME_1("me-1"),
  ME_2("me-2"),
  ME_3("me-3"),
  ME_4("me-4"),
  ME_5("me-5"),
  ME_AUTO("me-auto"),
  ME_LG_AUTO("me-lg-auto"),
  ME_MD_AUTO("me-md-auto"),
  ME_SM_AUTO("me-sm-auto"),
  ME_XL_AUTO("me-xl-auto"),
  MT_0("mt-0"),
  MT_1("mt-1"),
  MT_2("mt-2"),
  MT_3("mt-3"),
  MT_4("mt-4"),
  MT_5("mt-5"),
  MT_AUTO("mt-auto"),
  MY_LG_0("my-lg-0"),
  MY_LG_1("my-lg-1"),
  MY_LG_2("my-lg-2"),
  MY_LG_3("my-lg-3"),
  MY_LG_4("my-lg-4"),
  MY_LG_5("my-lg-5"),
  MX_AUTO("mx-auto"),
  MX_LG_AUTO("mx-lg-auto"),
  MX_MD_AUTO("mx-md-auto"),
  MX_SM_AUTO("mx-sm-auto"),
  MX_XL_AUTO("mx-xl-auto"),
  NAV("nav"),
  NAV_ITEM("nav-item"),
  NAV_LINK("nav-link"),
  NAV_TABS("nav-tabs"),
  NAVBAR("navbar"),
  NAVBAR_COLLAPSE("navbar-collapse"),
  NAVBAR_BRAND("navbar-brand"),
  NAVBAR_DARK("navbar-dark"),
  NAVBAR_EXPAND("navbar-expand"),
  NAVBAR_EXPAND_LG("navbar-expand-lg"),
  NAVBAR_EXPAND_MD("navbar-expand-md"),
  NAVBAR_EXPAND_SM("navbar-expand-sm"),
  NAVBAR_EXPAND_XL("navbar-expand-xl"),
  NAVBAR_LIGHT("navbar-light"),
  NAVBAR_NAV("navbar-nav"),
  NAVBAR_TOGGLER("navbar-toggler"),
  NAVBAR_TOGGLER_ICON("navbar-toggler-icon"),
  OFFCANVAS("offcanvas"),
  OFFCANVAS_BODY("offcanvas-body"),
  OFFCANVAS_BOTTOM("offcanvas-bottom"),
  OFFCANVAS_END("offcanvas-end"),
  OFFCANVAS_HEADER("offcanvas-header"),
  OFFCANVAS_START("offcanvas-start"),
  OFFCANVAS_TITLE("offcanvas-title"),
  OFFCANVAS_TOP("offcanvas-top"),
  OFFSET_1("offset-1"),
  OFFSET_2("offset-2"),
  OFFSET_3("offset-3"),
  OFFSET_4("offset-4"),
  OFFSET_5("offset-5"),
  OFFSET_6("offset-6"),
  OFFSET_7("offset-7"),
  OFFSET_8("offset-8"),
  OFFSET_9("offset-9"),
  OFFSET_10("offset-10"),
  OFFSET_11("offset-11"),
  OFFSET_LG_0("offset-lg-0"),
  OFFSET_LG_1("offset-lg-1"),
  OFFSET_LG_2("offset-lg-2"),
  OFFSET_LG_3("offset-lg-3"),
  OFFSET_LG_4("offset-lg-4"),
  OFFSET_LG_5("offset-lg-5"),
  OFFSET_LG_6("offset-lg-6"),
  OFFSET_LG_7("offset-lg-7"),
  OFFSET_LG_8("offset-lg-8"),
  OFFSET_LG_9("offset-lg-9"),
  OFFSET_LG_10("offset-lg-10"),
  OFFSET_LG_11("offset-lg-11"),
  OFFSET_MD_0("offset-md-0"),
  OFFSET_MD_1("offset-md-1"),
  OFFSET_MD_2("offset-md-2"),
  OFFSET_MD_3("offset-md-3"),
  OFFSET_MD_4("offset-md-4"),
  OFFSET_MD_5("offset-md-5"),
  OFFSET_MD_6("offset-md-6"),
  OFFSET_MD_7("offset-md-7"),
  OFFSET_MD_8("offset-md-8"),
  OFFSET_MD_9("offset-md-9"),
  OFFSET_MD_10("offset-md-10"),
  OFFSET_MD_11("offset-md-11"),
  OFFSET_SM_0("offset-sm-0"),
  OFFSET_SM_1("offset-sm-1"),
  OFFSET_SM_2("offset-sm-2"),
  OFFSET_SM_3("offset-sm-3"),
  OFFSET_SM_4("offset-sm-4"),
  OFFSET_SM_5("offset-sm-5"),
  OFFSET_SM_6("offset-sm-6"),
  OFFSET_SM_7("offset-sm-7"),
  OFFSET_SM_8("offset-sm-8"),
  OFFSET_SM_9("offset-sm-9"),
  OFFSET_SM_10("offset-sm-10"),
  OFFSET_SM_11("offset-sm-11"),
  OFFSET_XL_0("offset-xl-0"),
  OFFSET_XL_1("offset-xl-1"),
  OFFSET_XL_2("offset-xl-2"),
  OFFSET_XL_3("offset-xl-3"),
  OFFSET_XL_4("offset-xl-4"),
  OFFSET_XL_5("offset-xl-5"),
  OFFSET_XL_6("offset-xl-6"),
  OFFSET_XL_7("offset-xl-7"),
  OFFSET_XL_8("offset-xl-8"),
  OFFSET_XL_9("offset-xl-9"),
  OFFSET_XL_10("offset-xl-10"),
  OFFSET_XL_11("offset-xl-11"),
  OFFSET_XXL_0("offset-xxl-0"),
  OFFSET_XXL_1("offset-xxl-1"),
  OFFSET_XXL_2("offset-xxl-2"),
  OFFSET_XXL_3("offset-xxl-3"),
  OFFSET_XXL_4("offset-xxl-4"),
  OFFSET_XXL_5("offset-xxl-5"),
  OFFSET_XXL_6("offset-xxl-6"),
  OFFSET_XXL_7("offset-xxl-7"),
  OFFSET_XXL_8("offset-xxl-8"),
  OFFSET_XXL_9("offset-xxl-9"),
  OFFSET_XXL_10("offset-xxl-10"),
  OFFSET_XXL_11("offset-xxl-11"),
  PAGE_ITEM("page-item"),
  PAGE_LINK("page-link"),
  PAGINATION("pagination"),
  POPOVER("popover"),
  POPOVER_ARROW("popover-arrow"),
  POPOVER_BODY("popover-body"),
  POPOVER_HEADER("popover-header"),
  PROGRESS("progress"),
  PROGRESS_BAR("progress-bar"),
  ROUNDED_PILL("rounded-pill"),
  ROW("row"),
  SHOW("show"),
  /**
   * @deprecated Please use {@link #VISUALLY_HIDDEN}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  SR_ONLY("sr-only"),
  STICKY_TOP("sticky-top"),
  TEXT_BG_DANGER("text-bg-danger"),
  TEXT_BG_DARK("text-bg-dark"),
  TEXT_BG_INFO("text-bg-info"),
  TEXT_BG_LIGHT("text-bg-light"),
  TEXT_BG_PRIMARY("text-bg-primary"),
  TEXT_BG_SECONDARY("text-bg-secondary"),
  TEXT_BG_SUCCESS("text-bg-success"),
  TEXT_BG_WARNING("text-bg-warning"),
  TEXT_DANGER("text-danger"),
  TEXT_DARK("text-dark"),
  TEXT_INFO("text-info"),
  TEXT_LIGHT("text-light"),
  TEXT_NOWRAP("text-nowrap"),
  TEXT_PRIMARY("text-primary"),
  TEXT_SECONDARY("text-secondary"),
  TEXT_SUCCESS("text-success"),
  TEXT_WARNING("text-warning"),
  TEXT_CENTER("text-center"),
  /**
   * @deprecated Please use {@link org.apache.myfaces.tobago.renderkit.css.TobagoClass#TEXT__JUSTIFY}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  TEXT_JUSTIFY("tobago-text-justify"),
  /**
   * @deprecated Use {@link #TEXT_START}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  TEXT_LEFT("text-start"),
  TEXT_START("text-start"),
  /**
   * @deprecated Use {@link #TEXT_END}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  TEXT_RIGHT("text-end"),
  TEXT_END("text-end"),
  TAB_CONTENT("tab-content"),
  TAB_PANE("tab-pane"),
  TABLE("table"),
  TABLE_BORDERED("table-bordered"),
  TABLE_DARK("table-dark"),
  TABLE_HOVER("table-hover"),
  TABLE_INFO("table-info"),
  TABLE_PRIMARY("table-primary"),
  TABLE_SM("table-sm"),
  TABLE_STRIPED("table-striped"),
  TOAST("toast"),
  TOAST_BODY("toast-body"),
  TOAST_HEADER("toast-header"),
  TOOLTIP_ARROW("tooltip-arrow"),
  VISUALLY_HIDDEN("visually-hidden"),
  WARNING_FEEDBACK("warning-feedback"),
  WARNING_TOOLTIP("warning-tooltip");

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final int SEVERITY_ERROR = FacesMessage.SEVERITY_ERROR.getOrdinal();
  private static final int SEVERITY_WARN = FacesMessage.SEVERITY_WARN.getOrdinal();
  private static final int SEVERITY_INFO = FacesMessage.SEVERITY_INFO.getOrdinal();

  private final String name;

  BootstrapClass(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public static CssItem alert(final FacesMessage.Severity severity) {

    // switch over severity.getOrdinal() doesn't work, because different implementations use different ordinals,
    // see MYFACES-3768
    // may be optimized with a cache...

    if (severity == null) {
      return null;
    } else if (severity.getOrdinal() >= SEVERITY_ERROR) {
      return ALERT_DANGER;
    } else if (severity.getOrdinal() >= SEVERITY_WARN) {
      return ALERT_WARNING;
    } else if (severity.getOrdinal() >= SEVERITY_INFO) {
      return ALERT_INFO;
    } else {
      return null;
    }
  }

  public static CssItem validationColor(final FacesMessage.Severity severity) {
    if (severity == null) {
      return null;
    }
    return getSeverityCssItem(severity, IS_INFO, IS_WARNING, IS_ERROR);
  }

  public static CssItem borderColor(final FacesMessage.Severity severity) {
    if (severity == null) {
      return null;
    }
    return getSeverityCssItem(severity, BORDER_INFO, BORDER_WARNING, BORDER_DANGER);
  }

  public static CssItem buttonColor(final FacesMessage.Severity severity) {
    if (severity == null) {
      return null;
    }
    return getSeverityCssItem(severity, BTN_INFO, BTN_WARNING, BTN_DANGER);
  }

  public static CssItem feedbackColor(final FacesMessage.Severity severity) {
    if (severity == null) {
      return null;
    }
    return getSeverityCssItem(severity, INFO_FEEDBACK, WARNING_FEEDBACK, ERROR_FEEDBACK);
  }

  public static CssItem tooltipColor(final FacesMessage.Severity severity) {
    if (severity == null) {
      return null;
    }
    return getSeverityCssItem(severity, INFO_TOOLTIP, WARNING_TOOLTIP, ERROR_TOOLTIP);
  }

  private static CssItem getSeverityCssItem(
      FacesMessage.Severity severity,
      BootstrapClass info, BootstrapClass warning, BootstrapClass error) {
    if (severity.equals(FacesMessage.SEVERITY_INFO)) {
      return info;
    } else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
      return warning;
    } else {
      return error;
    }
  }

  public static CssItem segmentJustify(final SegmentJustify segmentJustify) {
    switch (segmentJustify) {
      case none:
        return null;
      case start:
        return BootstrapClass.JUSTIFY_CONTENT_START;
      case center:
        return BootstrapClass.JUSTIFY_CONTENT_CENTER;
      case end:
        return BootstrapClass.JUSTIFY_CONTENT_END;
      case around:
        return BootstrapClass.JUSTIFY_CONTENT_AROUND;
      case between:
        return BootstrapClass.JUSTIFY_CONTENT_BETWEEN;
      default:
        LOG.warn("Not a bootstrap class defined for {}", segmentJustify);
        return null;
    }
  }

  /**
   * @since 4.0.0
   */
  public static CssItem textAlign(final TextAlign textAlign) {
    if (textAlign != null) {
      switch (textAlign) {
        case left:
          return BootstrapClass.TEXT_START;
        case right:
          return BootstrapClass.TEXT_END;
        case justify:
          return TobagoClass.TEXT__JUSTIFY;
        case center:
          return BootstrapClass.TEXT_CENTER;
        default:
          LOG.warn("Not a bootstrap class defined for {}", textAlign);
          return BootstrapClass.TEXT_START;
      }
    } else {
      return null;
    }
  }

  /**
   * @since 5.0.0
   */
  public static CssItem verticalAlign(final VerticalAlign verticalAlign) {
    if (verticalAlign != null) {
      switch (verticalAlign) {
        case bottom:
          return BootstrapClass.ALIGN_BOTTOM;
        case middle:
          return BootstrapClass.ALIGN_MIDDLE;
        case top:
          return BootstrapClass.ALIGN_TOP;
        default:
          LOG.warn("Not a bootstrap class defined for {}", verticalAlign);
          return BootstrapClass.TEXT_START;
      }
    } else {
      return null;
    }
  }

  public static CssItem textColor(final Markup markup) {
    if (markup == null || markup.contains(Markup.NONE)) {
      return null;
    } else if (markup.contains(Markup.PRIMARY)) {
      return BootstrapClass.TEXT_PRIMARY;
    } else if (markup.contains(Markup.SECONDARY)) {
      return BootstrapClass.TEXT_SECONDARY;
    } else if (markup.contains(Markup.SUCCESS)) {
      return BootstrapClass.TEXT_SUCCESS;
    } else if (markup.contains(Markup.DANGER)) {
      return BootstrapClass.TEXT_DANGER;
    } else if (markup.contains(Markup.WARNING)) {
      return BootstrapClass.TEXT_WARNING;
    } else if (markup.contains(Markup.INFO)) {
      return BootstrapClass.TEXT_INFO;
    } else if (markup.contains(Markup.LIGHT)) {
      return BootstrapClass.TEXT_LIGHT;
    } else if (markup.contains(Markup.DARK)) {
      return BootstrapClass.TEXT_DARK;
    } else {
      return null;
    }
  }

  public static CssItem fontStyle(final Markup markup) {
    if (markup == null || markup.contains(Markup.NONE)) {
      return null;
    } else if (markup.contains(Markup.BOLD)) {
      return BootstrapClass.FW_BOLD;
    } else if (markup.contains(Markup.THIN)) {
      return BootstrapClass.FW_LIGHT;
    } else if (markup.contains(Markup.ITALIC)) {
      return BootstrapClass.FST_ITALIC;
    } else {
      return null;
    }
  }

  public static class Generator {

    private static final BootstrapClass[] OFFSET_EXTRA_SMALL = new BootstrapClass[]{
        null, OFFSET_1, OFFSET_2, OFFSET_3, OFFSET_4, OFFSET_5,
        OFFSET_6, OFFSET_7, OFFSET_8, OFFSET_9, OFFSET_10, OFFSET_11
    };
    private static final BootstrapClass[] OFFSET_SMALL = new BootstrapClass[]{
        OFFSET_SM_0, OFFSET_SM_1, OFFSET_SM_2, OFFSET_SM_3, OFFSET_SM_4, OFFSET_SM_5,
        OFFSET_SM_6, OFFSET_SM_7, OFFSET_SM_8, OFFSET_SM_9, OFFSET_SM_10, OFFSET_SM_11
    };
    private static final BootstrapClass[] OFFSET_MEDIUM = new BootstrapClass[]{
        OFFSET_MD_0, OFFSET_MD_1, OFFSET_MD_2, OFFSET_MD_3, OFFSET_MD_4, OFFSET_MD_5,
        OFFSET_MD_6, OFFSET_MD_7, OFFSET_MD_8, OFFSET_MD_9, OFFSET_MD_10, OFFSET_MD_11
    };
    private static final BootstrapClass[] OFFSET_LARGE = new BootstrapClass[]{
        OFFSET_LG_0, OFFSET_LG_1, OFFSET_LG_2, OFFSET_LG_3, OFFSET_LG_4, OFFSET_LG_5,
        OFFSET_LG_6, OFFSET_LG_7, OFFSET_LG_8, OFFSET_LG_9, OFFSET_LG_10, OFFSET_LG_11
    };
    private static final BootstrapClass[] OFFSET_EXTRA_LARGE = new BootstrapClass[]{
        OFFSET_XL_0, OFFSET_XL_1, OFFSET_XL_2, OFFSET_XL_3, OFFSET_XL_4, OFFSET_XL_5,
        OFFSET_XL_6, OFFSET_XL_7, OFFSET_XL_8, OFFSET_XL_9, OFFSET_XL_10, OFFSET_XL_11
    };
    private static final BootstrapClass[] OFFSET_EXTRA_EXTRA_LARGE = new BootstrapClass[]{
        OFFSET_XXL_0, OFFSET_XXL_1, OFFSET_XXL_2, OFFSET_XXL_3, OFFSET_XXL_4, OFFSET_XXL_5,
        OFFSET_XXL_6, OFFSET_XXL_7, OFFSET_XXL_8, OFFSET_XXL_9, OFFSET_XXL_10, OFFSET_XXL_11
    };

    private final MeasureList extraSmall;
    private final MeasureList small;
    private final MeasureList medium;
    private final MeasureList large;
    private final MeasureList extraLarge;
    private final MeasureList extra2Large;
    private final MarginTokens marginExtraSmall;
    private final MarginTokens marginSmall;
    private final MarginTokens marginMedium;
    private final MarginTokens marginLarge;
    private final MarginTokens marginExtraLarge;
    private final MarginTokens marginExtra2Large;

    private int index = 0;

    public Generator(final SupportsSegmentLayout component) {
      this.extraSmall = component.getExtraSmall();
      this.small = component.getSmall();
      this.medium = component.getMedium();
      this.large = component.getLarge();
      this.extraLarge = component.getExtraLarge();
      this.extra2Large = component.getExtra2Large();
      this.marginExtraSmall = MarginTokens.parse(component.getMarginExtraSmall());
      this.marginSmall = MarginTokens.parse(component.getMarginSmall());
      this.marginMedium = MarginTokens.parse(component.getMarginMedium());
      this.marginLarge = MarginTokens.parse(component.getMarginLarge());
      this.marginExtraLarge = MarginTokens.parse(component.getMarginExtraLarge());
      this.marginExtra2Large = MarginTokens.parse(component.getMarginExtra2Large());
    }

    public void reset() {
      index = 0;
    }

    public void next() {
      index++;
    }

    public BootstrapClass[] generate(final UIComponent child) {
      final ArrayList<BootstrapClass> result = new ArrayList<>(10);
      final Map<String, Object> attributes = child.getAttributes();
      generate(result, extraSmall, attributes, Attributes.overwriteExtraSmall);
      generate(result, small, attributes, Attributes.overwriteSmall);
      generate(result, medium, attributes, Attributes.overwriteMedium);
      generate(result, large, attributes, Attributes.overwriteLarge);
      generate(result, extraLarge, attributes, Attributes.overwriteExtraLarge);
      generate(result, extra2Large, attributes, Attributes.overwriteExtra2Large);

      generate(result, marginExtraSmall, attributes, Attributes.overwriteMarginExtraSmall);
      generate(result, marginSmall, attributes, Attributes.overwriteMarginSmall);
      generate(result, marginMedium, attributes, Attributes.overwriteMarginMedium);
      generate(result, marginLarge, attributes, Attributes.overwriteMarginLarge);
      generate(result, marginExtraLarge, attributes, Attributes.overwriteMarginExtraLarge);
      generate(result, marginExtra2Large, attributes, Attributes.overwriteMarginExtra2Large);

      generateOffset(result, attributes.get(Attributes.offsetExtraSmall.name()), OFFSET_EXTRA_SMALL);
      generateOffset(result, attributes.get(Attributes.offsetSmall.name()), OFFSET_SMALL);
      generateOffset(result, attributes.get(Attributes.offsetMedium.name()), OFFSET_MEDIUM);
      generateOffset(result, attributes.get(Attributes.offsetLarge.name()), OFFSET_LARGE);
      generateOffset(result, attributes.get(Attributes.offsetExtraLarge.name()), OFFSET_EXTRA_LARGE);
      generateOffset(result, attributes.get(Attributes.offsetExtra2Large.name()), OFFSET_EXTRA_EXTRA_LARGE);
      return result.toArray(new BootstrapClass[0]);
    }

    private void generate(
        final List<BootstrapClass> result, final MeasureList tokens,
        final Map<String, Object> attributes, final Attributes attribute) {
      final Object overwrite = attributes.get(attribute.name());

      if (overwrite != null) {
        final Measure measure = Measure.valueOf(overwrite.toString(), Measure.Unit.SEG);
        final BootstrapClass bootstrapClass = valueOf(getSegmentMeasure(measure), attribute);
        result.add(bootstrapClass);
      } else if (tokens != null && tokens.getSize() > 0) {
        final Measure measure = tokens.get(index % tokens.getSize());
        final BootstrapClass bootstrapClass = valueOf(getSegmentMeasure(measure), attribute);
        result.add(bootstrapClass);
      }
    }

    private Measure getSegmentMeasure(final Measure measure) {
      if (measure != null && Measure.Unit.SEG.equals(measure.getUnit())) {
        final int number = (int) measure.getValue();
        if (number < 1) {
          LOG.warn("Segment values must been between 1 and 12. Use '1' instead of '{}'.", number);
          return new Measure(1, Measure.Unit.SEG);
        } else if (number > 12) {
          LOG.warn("Segment values must been between 1 and 12. Use '12' instead of '{}'.", number);
          return new Measure(12, Measure.Unit.SEG);
        }
      }
      return measure;
    }

    private void generate(
        final List<BootstrapClass> result, final MarginTokens margins,
        final Map<String, Object> attributes, final Attributes attribute) {
      final Object overwrite = attributes.get(attribute.name());

      if (overwrite != null) {
        final Margin margin = MarginTokens.parseToken((String) overwrite);
        final BootstrapClass bootstrapClass = valueOf(margin, attribute);
        result.add(bootstrapClass);
      } else if (margins != null && margins.getSize() > 0) {
        final Margin margin = margins.get(index % margins.getSize());
        final BootstrapClass bootstrapClass = valueOf(margin, attribute);
        result.add(bootstrapClass);
      }
    }

    private void generateOffset(final List<BootstrapClass> result, final Object offset, final BootstrapClass[] values) {
      if (offset != null) {
        int offsetIndex = Integer.parseInt((String) offset);
        if (offsetIndex >= 0) {
          offsetIndex = Math.min(offsetIndex, 11);
          result.add(values[offsetIndex]);
        }
      }
    }
  }

  public static BootstrapClass valueOf(final Measure measure, final Attributes attributes) {
    final String size = getSizeSuffix(attributes);

    if (measure != null) {
      if (measure.getUnit() == Measure.Unit.FR) {
        return valueOf("COL" + size);
      } else if (measure.getUnit() == Measure.Unit.AUTO) {
        return valueOf("COL" + size + "_AUTO");
      } else if (measure.getUnit() == Measure.Unit.SEG) {
        final float value = measure.getValue();
        return valueOf(
            "COL" + size + "_" + (value == (long) value ? Long.toString((long) value) : Float.toString(value)));
      }
    }
    return null;
  }

  public static BootstrapClass valueOf(final Margin margin, final Attributes attribute) {
    final String size = getSizeSuffix(attribute);

    switch (margin) {
      case left:
        return valueOf("MS" + size + "_AUTO");
      case right:
        return valueOf("ME" + size + "_AUTO");
      case both:
        return valueOf("MX" + size + "_AUTO");
      default:
        return null;
    }
  }

  private static String getSizeSuffix(final Attributes attribute) {
    switch (attribute) {
      case extra2Large:
      case marginExtra2Large:
      case overwriteExtra2Large:
      case overwriteMarginExtra2Large:
        return "_XXL";
      case extraLarge:
      case marginExtraLarge:
      case overwriteExtraLarge:
      case overwriteMarginExtraLarge:
        return "_XL";
      case large:
      case marginLarge:
      case overwriteLarge:
      case overwriteMarginLarge:
        return "_LG";
      case medium:
      case marginMedium:
      case overwriteMedium:
      case overwriteMarginMedium:
        return "_MD";
      case small:
      case marginSmall:
      case overwriteSmall:
      case overwriteMarginSmall:
        return "_SM";
      case extraSmall:
      case marginExtraSmall:
      case overwriteExtraSmall:
      case overwriteMarginExtraSmall:
      default:
        return "";
    }
  }

  public static CssItem valueOf(final AlignItems alignItems) {
    if (alignItems == null) {
      return null;
    } else {
      switch (alignItems) {
        case baseline:
          return ALIGN_ITEMS_BASELINE;
        case center:
          return ALIGN_ITEMS_CENTER;
        case flexEnd:
          return ALIGN_ITEMS_END;
        case flexStart:
          return ALIGN_ITEMS_START;
        case stretch:
          return ALIGN_ITEMS_STRETCH;
        default:
          LOG.warn("Undefined alignItems: '{}'.", alignItems);
          return null;
      }
    }
  }

  public static CssItem valueOf(final JustifyContent justifyContent) {
    if (justifyContent == null) {
      return null;
    } else {
      switch (justifyContent) {
        case center:
          return JUSTIFY_CONTENT_CENTER;
        case flexEnd:
          return JUSTIFY_CONTENT_END;
        case flexStart:
          return JUSTIFY_CONTENT_START;
        case spaceBetween:
          return JUSTIFY_CONTENT_BETWEEN;
        case spaceAround:
          return JUSTIFY_CONTENT_AROUND;
        default:
          LOG.warn("Undefined justifyContent: '{}'.", justifyContent);
          return null;
      }
    }
  }

}
