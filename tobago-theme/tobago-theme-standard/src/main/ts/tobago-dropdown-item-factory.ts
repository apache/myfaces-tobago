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

import {DropdownItem} from "./tobago-dropdown-item";
import {Css} from "./tobago-css";

export class DropdownItemFactory {
  static create(dropdownMenu: HTMLDivElement): DropdownItem[] {
    if (dropdownMenu.classList.contains(Css.TOBAGO_DROPDOWN_MENU)) {
      const dropdownItems: DropdownItem[] = [];
      dropdownMenu.querySelectorAll<HTMLElement>(
          ":scope > a.dropdown-item, "
          + ":scope > button.dropdown-item, "
          + ":scope > tobago-select-boolean-checkbox.dropdown-item > input, "
          + ":scope > tobago-select-boolean-toggle.dropdown-item > input, "
          + ":scope > tobago-select-one-radio > .dropdown-item > input, "
          + ":scope > tobago-select-many-checkbox > .dropdown-item > input, "
          + ":scope > tobago-dropdown.tobago-dropdown-submenu > .dropdown-item").forEach(
          (element) => {
            dropdownItems.push(new DropdownItem(element));
          }
      );
      return dropdownItems;
    } else {
      console.error("Given element is no dropdown menu.");
    }
  }
}
