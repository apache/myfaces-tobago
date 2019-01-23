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

Tobago4.Dropdown = {};

Tobago4.Dropdown.init = function (elements) {
  elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
  var $dropdownMenus = jQuery(":not(.tobago-page-menuStore) > .dropdown-menu");
  var $tobagoPageMenuStore = jQuery(".tobago-page-menuStore");

  $dropdownMenus.each(function () {
    var $dropdownMenu = jQuery(this);
    var $parent = $dropdownMenu.parent();

    if (!$parent.hasClass('tobago-dropdown-submenu')
        && $parent.closest('.navbar').length === 0) {

      // remove duplicated dropdown menus from menu store
      // this could happen if the dropdown component is updated by ajax
      removeDuplicates($dropdownMenu);

      $parent.on('shown.bs.dropdown', function (event) {
        $tobagoPageMenuStore.append($dropdownMenu.detach());
      }).on('hidden.bs.dropdown', function (event) {
        $parent.append($dropdownMenu.detach());
      });
    }
  });
};

function removeDuplicates($dropdownMenu) {
  var $menuStoreDropdowns = jQuery(".tobago-page-menuStore .dropdown-menu");
  $menuStoreDropdowns.each(function () {
    var $menuStoreDropdown = jQuery(this);

    var dropdownIds = getIds($dropdownMenu);
    var menuStoreIds = getIds($menuStoreDropdown);

    for (var i = 0; i < dropdownIds.length; i++) {
      if (jQuery.inArray(dropdownIds[i], menuStoreIds) >= 0) {
        $menuStoreDropdown.remove();
        return false;
      }
    }
  });
}

function getIds($dropdownMenu) {
  return $dropdownMenu.find("[id]").map(function () {
    return this.id;
  });
}

Tobago.Listener.register(Tobago4.Dropdown.init, Tobago.Phase.DOCUMENT_READY, Tobago.Order.NORMAL);
Tobago.Listener.register(Tobago4.Dropdown.init, Tobago.Phase.AFTER_UPDATE, Tobago.Order.NORMAL);
