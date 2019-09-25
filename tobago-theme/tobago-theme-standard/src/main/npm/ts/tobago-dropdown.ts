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

import {Listener, Order, Phase} from "./tobago-listener";

class Dropdown {

  static init(element: HTMLElement): void {
    const $dropdownMenus = jQuery(":not(.tobago-page-menuStore) > .dropdown-menu");
    const $tobagoPageMenuStore = jQuery(".tobago-page-menuStore");

    $dropdownMenus.each(function (): void {
      const $dropdownMenu = jQuery(this);
      const $parent = $dropdownMenu.parent();

      if (!$parent.hasClass("tobago-dropdown-submenu")
          && $parent.closest(".navbar").length === 0) {

        // remove duplicated dropdown menus from menu store
        // this could happen if the dropdown component is updated by ajax
        removeDuplicates($dropdownMenu);

        $parent.on("shown.bs.dropdown", function (event: Event): void {
          $tobagoPageMenuStore.append($dropdownMenu.detach());
        }).on("hidden.bs.dropdown", function (event: Event): void {
          $parent.append($dropdownMenu.detach());
        });
      }
    });
  }
}

function removeDuplicates($dropdownMenu): void {
  const $menuStoreDropdowns = jQuery(".tobago-page-menuStore .dropdown-menu");
  // XXX todo: remove ts-ignore
  // @ts-ignore
  $menuStoreDropdowns.each(function (): boolean {
    const $menuStoreDropdown = jQuery(this);

    const dropdownIds = getIds($dropdownMenu);
    const menuStoreIds = getIds($menuStoreDropdown);

    for (let i = 0; i < dropdownIds.length; i++) {
      if (jQuery.inArray(dropdownIds[i], menuStoreIds) >= 0) {
        $menuStoreDropdown.remove();
        return false;
      }
    }
  });
}

// XXX todo: remove tslint
// tslint:disable-next-line:typedef
function getIds($dropdownMenu) {
  return $dropdownMenu.find("[id]").map(function (): string {
    return this.id;
  });
}

Listener.register(Dropdown.init, Phase.DOCUMENT_READY, Order.NORMAL);
Listener.register(Dropdown.init, Phase.AFTER_UPDATE, Order.NORMAL);
