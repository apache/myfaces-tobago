/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function tobago_initTab(elements) {

  var tabGroups = Tobago.selectWidthJQuery(elements, ".tobago-tabGroup");

  // initialize the tab header elements
  // client case
  tabGroups.filter("[switchType='client']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = tobago_tabUpdateHidden(jQuery(this));
    jQuery(this).siblings(".tobago-tab-markup-selected").removeClass("tobago-tab-markup-selected");
    jQuery(this).addClass("tobago-tab-markup-selected");
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    tabGroup.find(".tobago-tab-content-markup-selected").removeClass("tobago-tab-content-markup-selected");
    tabGroup.find(".tobago-tab-content[tabgroupindex=" + activeIndex
        + "]").addClass("tobago-tab-content-markup-selected");
  });

  // initialize the tab header elements
  // reload tab case
  tabGroups.filter("[switchType='reloadTab']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = tobago_tabUpdateHidden(jQuery(this));
    LOG.debug("todo: ajax reload, activeIndex=" + activeIndex); // @DEV_ONLY
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    Tobago.Updater.update(tabGroup, tabGroup.attr("id"), tabGroup.attr("id"), {});
  });

  // initialize the tab header elements
  // reload page case
  tabGroups.filter("[switchType='reloadPage']").find(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
    var activeIndex = tobago_tabUpdateHidden(jQuery(this));
    LOG.debug("todo: full reload, activeIndex=" + activeIndex); // @DEV_ONLY
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    Tobago.submitAction(tabGroup.eq(0), tabGroup.attr("id"));
  });

  // initialize previous button
  // XXX ":first" and eq(1) are dangerous, please define e.g. a unique class for "previous" and "next"
  tabGroups.find(".tobago-tabGroupToolBar-button:first").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the previous siblings, which are not disabled
    selected.prevAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // initialize next button
  // XXX ":first" and eq(1) are dangerous, please define e.g. a unique class for "previous" and "next"
  tabGroups.find(".tobago-tabGroupToolBar-button:eq(1)").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the next siblings, which are not disabled
    selected.nextAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // initialize menu
  // XXX ":last" is dangerous, please define e.g. a unique class for "menu"
//  tabGroups.find(".tobago-tabGroupToolBar-button:last").find(".tobago-menu").click(function() {
//    var index = jQuery(this).prevAll().size();
//    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
//    var selected = tabGroup.find(".tobago-tab").eq(index).click();
//  });

  // XXX hack for webkit to avoid scrollbars in box
//  jQuery('.tobago-tabGroup').hide();
//  jQuery('.tobago-tabGroup').show();
}

/**
 * Update the hidden field for the active index.
 * @param tab is a jQuery object which represents the clicked tab area.
 */
function tobago_tabUpdateHidden(tab) {
  var tabGroup = tab.parents(".tobago-tabGroup:first");
  var hidden = tabGroup.children("input");
  var activeIndex = tab.attr("tabgroupindex");
  hidden.attr("value", activeIndex);
  return activeIndex;
}
