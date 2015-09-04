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

package org.apache.myfaces.tobago.renderkit.html;

import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
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
  CHECKBOX("checkbox"),
  CLOSE("close"),
  COLLAPSE("collapse"),
  COLLAPSED("collapsed"),
  CONTAINER("container"),
  CONTAINER_FLUID("container-fluid"),
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
  GLYPHICON("glyphicon"),
  GLYPHICON_CALENDAR("glyphicon-calendar"),
  GLYPHICON_CHEVRON_DOWN("glyphicon-chevron-down"),
  GLYPHICON_CHEVRON_UP("glyphicon-chevron-up"),
  GLYPHICON_TIME("glyphicon-time"),
  HAS_ERROR("has-error"),
  HAS_SUCCESS("has-success"),
  HAS_WARNING("has-warning"),
  ICON_BAR("icon-bar"),
  INPUT_GROUP("input-group"),
  INPUT_GROUP_BTN("input-group-btn"),
  MODAL("modal"),
  MODAL_DIALOG("modal-dialog"),
  MODAL_CONTENT("modal-content"),
  NAV("nav"),
  NAV_TABS("nav-tabs"),
  NAVBAR("navbar"),
  NAVBAR_BRAND("navbar-brand"),
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
  PANEL_HEADING("panel-heading"),
  PANEL_BODY("panel-body"),
  PANEL_TITLE("panel-title"),
  RADIO("radio"),
  ROW("row"),
  SR_ONLY("sr-only"),
  TABLE("table"),
  TABLE_BORDERED("table-bordered"),
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

  public static CssItem glyphicon(final String name) {

    return new CssItem() {

      @Override
      public String getName() {
        // XXX cleanup, should be resolved with the ResourceManager...
        return name.startsWith("glyphicon-") ? name : "glyphicon-" + name;
      }
    };
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
      case LEFT:
        return TEXT_LEFT;
      case RIGHT:
        return TEXT_RIGHT;
      case CENTER:
        return TEXT_CENTER;
      case JUSTIFY:
        return TEXT_JUSTIFY;
      default:
        return null;
    }
  }
}
