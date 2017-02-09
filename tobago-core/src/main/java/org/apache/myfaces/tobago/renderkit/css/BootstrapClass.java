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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.ColumnPartition;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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
  BG_INVERSE("bg-inverse"),
  BTN("btn"),
  BTN_GROUP("btn-group"),
  BTN_LINK("btn-link"),
  BTN_PRIMARY("btn-primary"),
  BTN_SECONDARY("btn-secondary"),
  BTN_TOOLBAR("btn-toolbar"),
  CARD("card"),
  CARD_BLOCK("card-block"),
  CARD_HEADER("card-header"),
  CARD_TITLE("card-title"),
  CLOSE("close"),
  COLLAPSE("collapse"),
  COL_FORM_LABEL("col-form-label"),
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
  D_INLINE("d-inline"),
  DISABLED("disabled"),
  DROPDOWN("dropdown"),
  DROPDOWN_ITEM("dropdown-item"),
  DROPDOWN_MENU("dropdown-menu"),
  DROPDOWN_TOGGLE("dropdown-toggle"),
  FADE("fade"),
  FIGURE("figure"),
  FIGURE_CAPTION("figure-caption"),
  FIGURE_IMG("figure-img"),
  FORM_CHECK("form-check"),
  FORM_CHECK_INLINE("form-check-inline"),
  FORM_CHECK_INPUT("form-check-input"),
  FORM_CHECK_LABEL("form-check-label"),
  FORM_CONTROL("form-control"),
  FORM_CONTROL_STATIC("form-control-static"),
  FORM_GROUP("form-group"),
  FORM_INLINE("form-inline"),
  HAS_DANGER("has-danger"),
  HAS_SUCCESS("has-success"),
  HAS_WARNING("has-warning"),
  HIDDEN_SM_UP("hidden-sm-up"),
  INPUT_GROUP("input-group"),
  INPUT_GROUP_ADDON("input-group-addon"),
  INPUT_GROUP_BTN("input-group-btn"),
  INVISIBLE("invisible"),
  MODAL("modal"),
  MODAL_CONTENT("modal-content"),
  MODAL_DIALOG("modal-dialog"),
  MODAL_LG("modal-lg"),
  MODAL_SM("modal-sm"),
  NAV("nav"),
  NAV_ITEM("nav-item"),
  NAV_LINK("nav-link"),
  NAV_TABS("nav-tabs"),
  NAVBAR("navbar"),
  NAVBAR_BRAND("navbar-brand"),
  NAVBAR_DARK("navbar-dark"),
  NAVBAR_FIXED_BOTTOM("navbar-fixed-bottom"),
  NAVBAR_FIXED_TOP("navbar-fixed-top"),
  NAVBAR_NAV("navbar-nav"),
  NAVBAR_TOGGLEABLE_XS("navbar-toggleable-xs"),
  NAVBAR_TOGGLER("navbar-toggler"),
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
  OFFSET_XS_1("offset-xs-1"),
  OFFSET_XS_2("offset-xs-2"),
  OFFSET_XS_3("offset-xs-3"),
  OFFSET_XS_4("offset-xs-4"),
  OFFSET_XS_5("offset-xs-5"),
  OFFSET_XS_6("offset-xs-6"),
  OFFSET_XS_7("offset-xs-7"),
  OFFSET_XS_8("offset-xs-8"),
  OFFSET_XS_9("offset-xs-9"),
  OFFSET_XS_10("offset-xs-10"),
  OFFSET_XS_11("offset-xs-11"),
  OPEN("open"),
  PAGE_ITEM("page-item"),
  PAGE_LINK("page-link"),
  PAGINATION("pagination"),
  PROGRESS("progress"),
  ROW("row"),
  SR_ONLY("sr-only"),
  TAB_CONTENT("tab-content"),
  TAB_PANE("tab-pane"),
  TABLE("table"),
  TABLE_BORDERED("table-bordered"),
  TABLE_HOVER("table-hover"),
  TABLE_INFO("table-info"),
  TABLE_INVERSE("table-inverse"),
  TABLE_SM("table-sm"),
  TABLE_STRIPED("table-striped");

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

  public static CssItem maximumSeverity(final UIComponent input) {
    final FacesMessage.Severity maximumSeverity = ComponentUtils.getMaximumSeverity(input);
    if (maximumSeverity == null) {
      return null;
    } else if (maximumSeverity.getOrdinal() >= SEVERITY_ERROR) {
      return HAS_DANGER;
    } else if (maximumSeverity.getOrdinal() >= SEVERITY_WARN) {
      return HAS_WARNING;
    } else if (maximumSeverity.getOrdinal() >= SEVERITY_INFO) {
      return TobagoClass.HAS__INFO;
    } else {
      return null;
    }
  }

  public static class Generator {

    private static final BootstrapClass[] EXTRA_SMALL = new BootstrapClass[]{
        COL_XS_1, COL_XS_2, COL_XS_3, COL_XS_4,
        COL_XS_5, COL_XS_6, COL_XS_7, COL_XS_8,
        COL_XS_9, COL_XS_10, COL_XS_11, COL_XS_12,
    };
    private static final BootstrapClass[] SMALL = new BootstrapClass[]{
        COL_SM_1, COL_SM_2, COL_SM_3, COL_SM_4,
        COL_SM_5, COL_SM_6, COL_SM_7, COL_SM_8,
        COL_SM_9, COL_SM_10, COL_SM_11, COL_SM_12,
    };
    private static final BootstrapClass[] MEDIUM = new BootstrapClass[]{
        COL_MD_1, COL_MD_2, COL_MD_3, COL_MD_4,
        COL_MD_5, COL_MD_6, COL_MD_7, COL_MD_8,
        COL_MD_9, COL_MD_10, COL_MD_11, COL_MD_12,
    };
    private static final BootstrapClass[] LARGE = new BootstrapClass[]{
        COL_LG_1, COL_LG_2, COL_LG_3, COL_LG_4,
        COL_LG_5, COL_LG_6, COL_LG_7, COL_LG_8,
        COL_LG_9, COL_LG_10, COL_LG_11, COL_LG_12,
    };
    private static final BootstrapClass[] OFFSET_EXTRA_SMALL = new BootstrapClass[]{
        OFFSET_XS_1, OFFSET_XS_1, OFFSET_XS_2, OFFSET_XS_3, OFFSET_XS_4, OFFSET_XS_5,
        OFFSET_XS_6, OFFSET_XS_7, OFFSET_XS_8, OFFSET_XS_9, OFFSET_XS_10, OFFSET_XS_11
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

    private final ColumnPartition extraSmall;
    private final ColumnPartition small;
    private final ColumnPartition medium;
    private final ColumnPartition large;

    private int index = 0;

    public Generator(
        final ColumnPartition extraSmall, final ColumnPartition small, final ColumnPartition medium,
        final ColumnPartition large) {
      if (extraSmall == null && small == null && medium == null && large == null) {
        this.extraSmall = ColumnPartition.PARTITION_12;
      } else {
        this.extraSmall = extraSmall;
      }
      this.small = small;
      this.medium = medium;
      this.large = large;
    }

    public void reset() {
      index = 0;
    }

    public void next() {
      index++;
    }

    public BootstrapClass[] generate(final UIComponent child) {
      ArrayList<BootstrapClass> result = new ArrayList<BootstrapClass>(4);
      generate(result, extraSmall, EXTRA_SMALL);
      generate(result, small, SMALL);
      generate(result, medium, MEDIUM);
      generate(result, large, LARGE);
      final Map<String, Object> attributes = child.getAttributes();
      generateOffset(result, attributes.get(Attributes.offsetExtraSmall.name()), OFFSET_EXTRA_SMALL);
      generateOffset(result, attributes.get(Attributes.offsetSmall.name()), OFFSET_SMALL);
      generateOffset(result, attributes.get(Attributes.offsetMedium.name()), OFFSET_MEDIUM);
      generateOffset(result, attributes.get(Attributes.offsetLarge.name()), OFFSET_LARGE);
      return result.toArray(new BootstrapClass[result.size()]);
    }

    private void generate(
        final List<BootstrapClass> result, final ColumnPartition partition, final BootstrapClass[] values) {
      if (partition != null) {
        result.add(values[partition.getPart(index % partition.getSize()) - 1]);
      }
    }

    private void generateOffset(final List<BootstrapClass> result, final Object offset, final BootstrapClass[] values) {
      if (offset != null) {
        int offsetIndex = Integer.valueOf((String) offset);
        if (offsetIndex >= 0) {
          offsetIndex = offsetIndex > 11 ? 11 : offsetIndex;
          result.add(values[offsetIndex]);
        }
      }
    }
  }
}
