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

Tobago.Dropdown = {};

Tobago.Dropdown.init = function (elements) {
  var $dropdownMenus = jQuery(".dropdown-menu");
  $dropdownMenus.each(function () {
    var $dropdownMenu = jQuery(this);
    var $parent = $dropdownMenu.parent();

    if (!$parent.hasClass('tobago-dropdown-submenu')
        && !jQuery.contains(jQuery('.tobago-page-menuStore')[0], $dropdownMenu[0])) {

      var dropdownId = $parent.attr('id') + "::dropdown";
      $dropdownMenu.attr('id', dropdownId);
      $dropdownMenu.appendTo('.tobago-page-menuStore');

      var $button = jQuery(Tobago.Utils.escapeClientId($parent.attr('id') + "::command"));
      $button.attr('aria-controls', dropdownId);

      var showDropdown = function (event) {
        $dropdownMenu.css("display", "block");
        $dropdownMenu.css("top", $button.offset().top + $button.outerHeight() + "px");

        if ($dropdownMenu.hasClass("dropdown-menu-right")) {
          $dropdownMenu.css("left", $button.offset().left + $button.outerWidth() - $dropdownMenu.outerWidth() + "px");
        } else {
          $dropdownMenu.css("left", $button.offset().left + "px");
        }
      };

      var hideDropdown = function (event) {
        if (event && !jQuery.contains($dropdownMenu[0], event.target) && $dropdownMenu.css("display") === "block") {
          $dropdownMenu.css("display", "");
        }
      };

      $parent.on('shown.bs.dropdown', showDropdown).on('hidden.bs.dropdown', hideDropdown);
    }
  });
};

Tobago.registerListener(Tobago.Dropdown.init, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.NORMAL);
Tobago.registerListener(Tobago.Dropdown.init, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.NORMAL);
