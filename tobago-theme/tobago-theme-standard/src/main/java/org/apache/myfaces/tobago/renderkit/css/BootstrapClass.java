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

import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;

/**
 * @since 3.0.0
 */
public enum BootstrapClass implements CssItem {

  ACTIVE("active"),
  ALERT("alert"),
  ALERT_DANGER("alert-danger"),
  ALERT_WARNING("alert-warning"),
  ALERT_INFO("alert-info"),
  ALERT_DISMISSIBLE("alert-dismissible"),
  BTN("btn"),
  BTN_DEFAULT("btn-default"),
  BTN_PRIMARY("btn-primary"),
  CHECKBOX("checkbox"),
  CLOSE("close"),
  COLLAPSE("collapse"),
  COLLAPSED("collapsed"),
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
  COL_XS_1("col-xs-1"),
  COL_XS_2("col-xs-2"),
  COL_XS_3("col-xs-3"),
  COL_XS_4("col-xs-4"),
  COL_XS_5("col-xs-5"),
  COL_XS_6("col-xs-6"),
  COL_XS_7("col-xs-7"),
  COL_XS_8("col-xs-8"),
  COL_XS_9("col-xs-9"),
  COL_XS_10("col-xs-10"),
  COL_XS_11("col-xs-11"),
  COL_XS_12("col-xs-12"),
  CONTAINER("container"),
  CONTAINER_FLUID("container-fluid"),
  CONTROL_LABEL("control-label"),
  DANGER("danger"),
  DISABLED("disabled"),
  DROPDOWN("dropdown"),
  DROPDOWN_MENU("dropdown-menu"),
  DROPDOWN_TOGGLE("dropdown-toggle"),
  FADE("fade"),
  FORM_CONTROL("form-control"),
  FORM_CONTROL_STATIC("form-control-static"),
  FORM_GROUP("form-group"),
  FORM_HORIZONTAL("form-horizontal"),
  HAS_ERROR("has-error"),
  HAS_SUCCESS("has-success"),
  HAS_WARNING("has-warning"),
  ICON_BAR("icon-bar"),
  INFO("info"),
  INPUT_GROUP("input-group"),
  INPUT_GROUP_BTN("input-group-btn"),
  INVISIBLE("invisible"),
  MODAL("modal"),
  MODAL_DIALOG("modal-dialog"),
  MODAL_CONTENT("modal-content"),
  NAV("nav"),
  NAV_TABS("nav-tabs"),
  NAVBAR("navbar"),
  NAVBAR_BRAND("navbar-brand"),
  NAVBAR_BTN("navbar-btn"),
  NAVBAR_COLLAPSE("navbar-collapse"),
  NAVBAR_DEFAULT("navbar-default"),
  NAVBAR_FIXED_BOTTOM("navbar-fixed-bottom"),
  NAVBAR_FIXED_TOP("navbar-fixed-top"),
  NAVBAR_FORM("navbar-form"),
  NAVBAR_HEADER("navbar-header"),
  NAVBAR_INVERSE("navbar-inverse"),
  NAVBAR_NAV("navbar-nav"),
  NAVBAR_TEXT("navbar-text"),
  NAVBAR_TOGGLE("navbar-toggle"),
  PAGE_HEADER("page-header"),
  PAGINATION("pagination"),
  PANEL("panel"),
  PANEL_BODY("panel-body"),
  PANEL_DEFAULT("panel-default"),
  PANEL_HEADING("panel-heading"),
  PANEL_TITLE("panel-title"),
  RADIO("radio"),
  ROW("row"),
  SR_ONLY("sr-only"),
  TABLE("table"),
  TABLE_BORDERED("table-bordered"),
  TABLE_CONDENSED("table-condensed"),
  TABLE_HOVER("table-hover"),
  TABLE_STRIPED("table-striped"),
  TEXT_CENTER("text-center"),
  TEXT_JUSTIFY("text-justify"),
  TEXT_LEFT("text-left"),
  TEXT_RIGHT("text-right"),
  WARNING("warning");

  private static final int SEVERITY_ERROR = FacesMessage.SEVERITY_ERROR.getOrdinal();
  private static final int SEVERITY_WARN = FacesMessage.SEVERITY_WARN.getOrdinal();
  private static final int SEVERITY_INFO = FacesMessage.SEVERITY_INFO.getOrdinal();

  private final String name;

  BootstrapClass(final String name) {
    this.name = name;
  }

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

  public static CssItem maximumSeverity(final UIComponent input) {
    final FacesMessage.Severity maximumSeverity = ComponentUtils.getMaximumSeverity(input);
    if (maximumSeverity == null) {
      return null;
    } else if (maximumSeverity.getOrdinal() >= SEVERITY_ERROR) {
      return HAS_ERROR;
    } else if (maximumSeverity.getOrdinal() >= SEVERITY_WARN) {
      return HAS_WARNING;
    } else {
      return null;
    }
  }

  public static CssItem textAlign(final TextAlign textAlign) {
    switch (textAlign) {
      case left:
        return TEXT_LEFT;
      case right:
        return TEXT_RIGHT;
      case center:
        return TEXT_CENTER;
      case justify:
        return TEXT_JUSTIFY;
      default:
        return null;
    }
  }
}
