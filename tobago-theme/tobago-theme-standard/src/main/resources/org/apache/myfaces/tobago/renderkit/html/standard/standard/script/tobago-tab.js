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

Tobago.TabGroup = {};

/**
 * Initializes the tab-groups.
 * @param elements  a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 */
Tobago.TabGroup.init = function(elements) {

  var tabGroups = Tobago.Utils.selectWithJQuery(elements, ".tobago-tabGroup");

  // initialize the tab header elements
  // reload tab case
  tabGroups.filter("[switchType='reloadTab']").each(function() {
    jQuery(this)
        .find(".tobago-tabGroup-header")
        .first()
        .children(".tobago-tab")
        .not(".tobago-tab-markup-disabled")
        .click(
            function (event) {
              var tab = jQuery(this);
              var activeIndex = Tobago.TabGroup.updateHidden(tab);
              console.debug("todo: ajax reload, activeIndex=" + activeIndex); // @DEV_ONLY
              var tabGroup = tab.parents(".tobago-tabGroup:first");
              var tabGroupId = tabGroup.attr("id");
              var executeIds = tabGroupId;
              var renderIds = tabGroupId;
              var behaviorCommands = tabGroup.data("tobago-behavior-commands");
              if (behaviorCommands && behaviorCommands.reload) {
                if (behaviorCommands.reload.execute) {
                  executeIds = behaviorCommands.reload.execute;
                }
                if (behaviorCommands.reload.render) {
                  renderIds +=  " " + behaviorCommands.reload.render;
                }
              }

              jsf.ajax.request(
                  tabGroupId,
                  event,
                  {
                    execute: executeIds,
                    render: renderIds
                  });
            })
  });

  // initialize the tab header elements
  // reload page case
  tabGroups.filter("[switchType='reloadPage']").each(function() {
    jQuery(this).find(".tobago-tabGroup-header").first()
      .children(".tobago-tab").not(".tobago-tab-markup-disabled").click(function() {
          var activeIndex = Tobago.TabGroup.updateHidden(jQuery(this));
          console.debug("todo: full reload, activeIndex=" + activeIndex); // @DEV_ONLY
          var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
          Tobago.submitAction(tabGroup.eq(0), tabGroup.attr("id"));
        })
  });

  // initialize previous button
  tabGroups.find("[data-tobago-tabgroup-toolbar-prev]").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the previous siblings, which are not disabled
    selected.prevAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // initialize next button
  tabGroups.find("[data-tobago-tabgroup-toolbar-next]").click(function() {
    var tabGroup = jQuery(this).parents(".tobago-tabGroup:first");
    var selected = tabGroup.find(".tobago-tab-markup-selected");
    // the nearest of the next siblings, which are not disabled
    selected.nextAll(":not(.tobago-tab-markup-disabled):first").click();
  });

  // init scroll position
  var header = tabGroups.find(".tobago-tabGroup-header");
  header.each(function() {
    Tobago.TabGroup.ensureScrollPosition(jQuery(this));
  });

    // tool tips
  tabGroups.each(function() {
    var tabGroup = jQuery(this);
    tabGroup.find(".tobago-tab").each(function() {
      var tab = jQuery(this);
      var tabContent = tabGroup.find(".tobago-tab-content[tabgroupindex=" + tab.attr("tabgroupindex") + "]");
      tabContent.attr("title", tab.attr("title"));
    });
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
};

/**
 * Update the hidden field for the active index.
 * @param tab is a jQuery object which represents the clicked tab area.
 */
Tobago.TabGroup.updateHidden = function(tab) {
  var tabGroup = tab.parents(".tobago-tabGroup:first");
  var hidden = tabGroup.children("input");
  var activeIndex = tab.attr("tabgroupindex");
  hidden.val(activeIndex);
  return activeIndex;
};

Tobago.TabGroup.ensureScrollPosition = function (header) {
  var tab = header.find(".tobago-tab-markup-selected");
  if (tab.length > 0) {
    var tabRight = tab.position().left + tab.outerWidth() - header.outerWidth();
    if (tabRight > 0) {
      header.scrollLeft(header.scrollLeft() + tabRight + 1); // +1 to avoid rounding problems
    }
    var tabLeft = tab.position().left;
    if (tabLeft < 0) {
      header.scrollLeft(header.scrollLeft() + tabLeft);
    }
  }
};

Tobago.registerListener(Tobago.TabGroup.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.TabGroup.init, Tobago.Phase.AFTER_UPDATE);
