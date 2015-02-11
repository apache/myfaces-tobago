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

import org.apache.myfaces.tobago.renderkit.css.CssItem;

import javax.faces.application.FacesMessage;

/**
 * @since 3.0.0
 */
public enum BootstrapClass implements CssItem {

  ACTIVE("active"),
  ALERT("alert"),
  ALERT_DANGER("alert-danger"),
  ALERT_WARNING("alert-warning"),
  ALERT_INFO("alert-info"),
  CHECKBOX("checkbox"),
  COLLAPSE("collapse"),
  CONTAINER("container"),
  CONTAINER_FLUID("container-fluid"),
  DISABLED("disabled"),
  DROPDOWN("dropdown"),
  DROPDOWN_MENU("dropdown-menu"),
  FADE("fade"),
  FORM_HORIZONTAL("form-horizontal"),
  GLYPHICON("glyphicon"),
  ICON_BAR("icon-bar"),
  MODAL("modal"),
  MODAL_DIALOG("modal-dialog"),
  MODAL_CONTENT("modal-content"),
  NAV("nav"),
  NAV_TABS("nav-tabs"),
  NAVBAR("navbar"),
  NAVBAR_BRAND("navbar-brand"),
  NAVBAR_COLLAPSE("navbar-collapse"),
  NAVBAR_DEFAULT("navbar-default"),
  NAVBAR_FIXED_TOP("navbar-fixed-top"),
  NAVBAR_FORM("navbar-form"),
  NAVBAR_HEADER("navbar-header"),
  NAVBAR_INVERSE("navbar-inverse"),
  NAVBAR_NAV("navbar-nav"),
  NAVBAR_TOGGLE("navbar-toggle"),
  PAGE_HEADER("page-header"),
  PAGINATION("pagination"),
  PANEL_HEADING("panel-heading"),
  PANEL_BODY("panel-body"),
  RADIO("radio"),
  SR_ONLY("sr-only");

  private final String name;

  private BootstrapClass(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static CssItem glyphicon(final String name) {

    return new CssItem() {

      @Override
      public String getName() {
        return "glyphicon-" + name;
      }
    };
  }

  public static CssItem alert(final FacesMessage.Severity severity) {

    switch (severity.getOrdinal()) {
      case 1:
        return ALERT_INFO;
      case 2:
        return ALERT_WARNING;
      case 3:
      case 4:
      default:
        return ALERT_DANGER;
    }
  }

}
