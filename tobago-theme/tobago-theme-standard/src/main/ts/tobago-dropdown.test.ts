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

import "./tobago-dropdown";

beforeAll(() => {
  document.dispatchEvent(new Event("tobago.init"));
  document.body.innerHTML = `<tobago-page><div class="tobago-page-menuStore"></div></tobago-page>`;
});

afterEach(() => {
  document.body.replaceChildren();
});

test("Open dropdown menu via keydown", () => {
  const tobagoDropdownElement = `
<tobago-dropdown id="actions" class="dropdown">
  <button class="dropdown-toggle" aria-expanded="false">Actions</button>
  <div class="tobago-dropdown-menu" data-tobago-for="actions">
    <a class="dropdown-item" href="#first">First</a>
    <a class="dropdown-item" href="#second">Second</a>
  </div>
</tobago-dropdown>`;
  document.querySelector("tobago-page").insertAdjacentHTML("beforeend", tobagoDropdownElement);

  const toggle = document.querySelector<HTMLButtonElement>(".dropdown-toggle");
  let menu = document.querySelector<HTMLDivElement>(".tobago-dropdown-menu");
  const firstItem = document.querySelector<HTMLElement>(".dropdown-item");
  const dropdownMenuStore = document.querySelector(".tobago-page-menuStore");
  expect(dropdownMenuStore).not.toBeNull();

  toggle.dispatchEvent(new KeyboardEvent("keydown", {
    key: "ArrowDown",
    bubbles: true,
    cancelable: true
  }));

  expect(toggle.ariaExpanded).toBe("true");
  expect(menu.classList.contains("show")).toBe(true);
  expect(menu.parentElement.classList.contains("tobago-page-menuStore")).toBe(true);
  expect(document.activeElement).toBe(firstItem);
});
