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

import "./tobago-dropdown-menu";
import {DropdownMenu, DropdownMenuAlignment} from "./tobago-dropdown-menu";

beforeAll(() => {
  document.dispatchEvent(new Event("tobago.init"));
  document.body.innerHTML = `<tobago-page><div class="tobago-page-menuStore"></div></tobago-page>`;
});

afterEach(() => {
  jest.restoreAllMocks();
  document.body.replaceChildren();
});

test("Panel-facet position and opening direction issue TOBAGO-2541", () => {
  const tobagoDropdownElement = `
<tobago-dropdown id="dropdownForm" class="dropdown" data-tobago-auto-close="outside">
  <button type="button" id="dropdownForm::command" name="dropdownForm" aria-expanded="false"
          class="tobago-button btn btn-secondary tobago-auto-spacing dropdown-toggle">
    <tobago-behavior event="click" client-id="dropdownForm" field-id="dropdownForm::command" omit="omit"></tobago-behavior>
    <span>Dropdown Button</span>
  </button>
  <div class="tobago-dropdown-menu" aria-labelledby="dropdownForm::command" name="dropdownForm"
       data-tobago-for="dropdownForm">
    <div class="tobago-panel-facet">
      <tobago-panel id="panelFacet">
        <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.</p>
      </tobago-panel>
    </div>
  </div>
</tobago-dropdown>`;
  document.querySelector("tobago-page").insertAdjacentHTML("beforeend", tobagoDropdownElement);

  const toggle = document.querySelector<HTMLButtonElement>(".dropdown-toggle");
  toggle.click();

  const referenceElement = document.querySelector<HTMLButtonElement>(".dropdown-toggle");
  jest.spyOn(referenceElement, "getBoundingClientRect").mockReturnValue({
    bottom: 549,
    height: 38,
    left: 298.5,
    right: 444.859375,
    top: 511,
    width: 146.359375,
    x: 298.5,
    y: 511,
    toJSON: () => ({})
  });

  let dropdownMenuElement = document.querySelector<HTMLDivElement>(".tobago-dropdown-menu");
  jest.spyOn(dropdownMenuElement, "getBoundingClientRect").mockImplementation(() => ({
    bottom: dropdownMenuElement.style.top === "" && dropdownMenuElement.style.bottom === "0px" ? 637 : 82,
    height: 66,
    left: 16,
    right: 1375,
    top: 16,
    width: dropdownMenuElement.style.maxWidth !== "" ? parseFloat(dropdownMenuElement.style.maxWidth) : 1359,
    x: 16,
    y: 16,
    toJSON: () => ({})
  }));
  /* scrollWidth, clientWidth and offsetWidth are used in dropdownContentFit()
     dropdownContentFit() is called after the first execution of calcHorizontalPositioningAndMaxWidth()
     The values for scrollWidth, clientWidth and offsetWidth are from the Chrome browser. */
  Object.defineProperty(dropdownMenuElement, "scrollWidth", {configurable: true, value: 1091});
  Object.defineProperty(dropdownMenuElement, "clientWidth", {configurable: true, value: 1091});
  /* The issue for TOBAGO-2541 is that offsetWidth is a rounded integer value. Actually, the offsetWidth should be
     "1076.5px" but is rounded up to 1077. With this, the dropdownContentFit() function returns false. */
  Object.defineProperty(dropdownMenuElement, "offsetWidth", {
    configurable: true,
    get: () => Math.round(parseFloat(dropdownMenuElement.style.maxWidth))
  });
  Object.defineProperty(window, "innerHeight", {configurable: true, value: 637});
  /*  getComputedStyle() is called once for the dropdownMenuElement. */
  const computedStyle = document.createElement("div").style;
  computedStyle.marginTop = "16px";
  computedStyle.marginBottom = "2px";
  jest.spyOn(window, "getComputedStyle").mockReturnValue(computedStyle);

  const dropdownMenu = new DropdownMenu(dropdownMenuElement, referenceElement, [dropdownMenuElement, referenceElement], false, DropdownMenuAlignment.start);

  dropdownMenu.show();

  dropdownMenuElement = document.querySelector<HTMLDivElement>(".tobago-dropdown-menu");
  expect(toggle.ariaExpanded).toBe("true");
  expect(dropdownMenuElement.classList.contains("show")).toBe(true);

  expect(dropdownMenuElement.style.maxWidth).toBe("1076.5px");
  expect(dropdownMenuElement.style.maxHeight).toBe("495px");
  expect(dropdownMenuElement.style.left).toBe("282.5px");
  expect(dropdownMenuElement.style.right).toBe("");
  expect(dropdownMenuElement.style.top).toBe("");
  expect(dropdownMenuElement.style.bottom).toBe("126px");
  expect(dropdownMenuElement.style.marginBottom).toBe("var(--tobago-dropdown-menu-component-offset)");
});
