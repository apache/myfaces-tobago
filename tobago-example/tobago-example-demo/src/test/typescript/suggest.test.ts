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

import {expect, test} from "@playwright/test";

test.describe("900-test/in/suggest/Suggest.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/in/suggest/Suggest.xhtml");
  });

  test("Open suggest list, Escape", async ({page}) => {
    const inputField = page.locator("input[id='page:mainForm:inputField::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const firstSuggest = suggestDropdownMenu.locator("li[data-result-index='0']");

    await inputField.click();
    await expect(inputField).toBeFocused();
    await page.keyboard.press("S");
    await expect(firstSuggest).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(firstSuggest).not.toBeVisible();
  });

  test("Open suggest list, click on 'Sun'", async ({page}) => {
    const inputField = page.locator("input[id='page:mainForm:inputField::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const firstSuggest = suggestDropdownMenu.locator("li[data-result-index='0']");
    const firstSuggestButton = firstSuggest.locator("button");

    await inputField.click();
    await expect(inputField).toBeFocused();
    await page.keyboard.press("S");
    await expect(firstSuggest).toBeVisible();
    await firstSuggestButton.click();
    await expect(firstSuggest).not.toBeVisible();
    await expect(inputField).toHaveValue("Sun");
  });

  test("Open suggest list with 'ma', ArrowDowns/Tabs, Enter", async ({page}) => {
    const inputField = page.locator("input[id='page:mainForm:inputField::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const mars = suggestDropdownMenu.locator("li[data-result-index='0']");
    const amalthea = suggestDropdownMenu.locator("li[data-result-index='1']");
    const himalia = suggestDropdownMenu.locator("li[data-result-index='2']");
    const mimas = suggestDropdownMenu.locator("li[data-result-index='3']");

    await inputField.click();
    await expect(inputField).toBeFocused();
    await page.keyboard.press("m");
    await page.keyboard.press("a");
    await expect(mars).toBeVisible();
    await expect(amalthea).toBeVisible();
    await expect(himalia).toBeVisible();
    await expect(mimas).toBeVisible();
    await page.keyboard.press("ArrowDown");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(mars).not.toBeVisible();
    await expect(amalthea).not.toBeVisible();
    await expect(himalia).not.toBeVisible();
    await expect(mimas).not.toBeVisible();
    await expect(inputField).toHaveValue("Mars");
  });

  test("Open suggest list with 'ma', ArrowUps/shift-Tabs, Enter", async ({page}) => {
    const inputField = page.locator("input[id='page:mainForm:inputField::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const mars = suggestDropdownMenu.locator("li[data-result-index='0']");
    const amalthea = suggestDropdownMenu.locator("li[data-result-index='1']");
    const himalia = suggestDropdownMenu.locator("li[data-result-index='2']");
    const mimas = suggestDropdownMenu.locator("li[data-result-index='3']");

    await inputField.click();
    await expect(inputField).toBeFocused();
    await page.keyboard.press("m");
    await page.keyboard.press("a");
    await expect(mars).toBeVisible();
    await expect(amalthea).toBeVisible();
    await expect(himalia).toBeVisible();
    await expect(mimas).toBeVisible();
    await page.keyboard.press("ArrowUp");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(mars).not.toBeVisible();
    await expect(amalthea).not.toBeVisible();
    await expect(himalia).not.toBeVisible();
    await expect(mimas).not.toBeVisible();
    await expect(inputField).toHaveValue("Mimas");
  });
});

test.describe("900-test/in/suggest/dropdown-form/Dropdown_form.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/in/suggest/dropdown-form/Dropdown_form.xhtml");
  });

  test("Open from dropdown, Tab to suggest, open suggest list, Escape, Escape", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const inputField = page.locator("input[id='page:mainForm:suggestInput::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const firstSuggest = suggestDropdownMenu.locator("li[data-result-index='0']");

    await expect(inputField).not.toBeVisible();
    await toggleButton.click();
    await expect(inputField).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(inputField).toBeFocused();
    await page.keyboard.press("S");
    await expect(firstSuggest).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(firstSuggest).not.toBeVisible();
    await expect(inputField).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(firstSuggest).not.toBeVisible();
    await expect(inputField).not.toBeVisible();
    await expect(toggleButton).toBeFocused();
  });

  test("Open from dropdown, Tab to suggest, 'ma', ArrowDowns/Tabs, Enter, Tab to beforeComp", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const inputField = page.locator("input[id='page:mainForm:suggestInput::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const mars = suggestDropdownMenu.locator("li[data-result-index='0']");
    const amalthea = suggestDropdownMenu.locator("li[data-result-index='1']");
    const himalia = suggestDropdownMenu.locator("li[data-result-index='2']");
    const mimas = suggestDropdownMenu.locator("li[data-result-index='3']");
    const afterComp = page.locator("[id='page:mainForm:innerAfterComp::field']");

    await expect(inputField).not.toBeVisible();
    await toggleButton.click();
    await expect(inputField).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(inputField).toBeFocused();
    await page.keyboard.press("m");
    await page.keyboard.press("a");
    await expect(mars).toBeVisible();
    await expect(amalthea).toBeVisible();
    await expect(himalia).toBeVisible();
    await expect(mimas).toBeVisible();
    await page.keyboard.press("ArrowDown");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(mars).not.toBeVisible();
    await expect(amalthea).not.toBeVisible();
    await expect(himalia).not.toBeVisible();
    await expect(mimas).not.toBeVisible();
    await expect(inputField).toHaveValue("Mars");
    await page.keyboard.press("Tab");
    await expect(afterComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
  });

  test("Open from dropdown, Tab to suggest, 'ma', ArrowUps/shift-Tabs, Enter, Tab to beforeComp", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const inputField = page.locator("input[id='page:mainForm:suggestInput::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:suggest']");
    const mars = suggestDropdownMenu.locator("li[data-result-index='0']");
    const amalthea = suggestDropdownMenu.locator("li[data-result-index='1']");
    const himalia = suggestDropdownMenu.locator("li[data-result-index='2']");
    const mimas = suggestDropdownMenu.locator("li[data-result-index='3']");
    const afterComp = page.locator("[id='page:mainForm:innerAfterComp::field']");

    await expect(inputField).not.toBeVisible();
    await toggleButton.click();
    await expect(inputField).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(inputField).toBeFocused();
    await page.keyboard.press("m");
    await page.keyboard.press("a");
    await expect(mars).toBeVisible();
    await expect(amalthea).toBeVisible();
    await expect(himalia).toBeVisible();
    await expect(mimas).toBeVisible();
    await page.keyboard.press("ArrowUp");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(himalia.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(amalthea.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(mars.locator("button")).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(mimas.locator("button")).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(mars).not.toBeVisible();
    await expect(amalthea).not.toBeVisible();
    await expect(himalia).not.toBeVisible();
    await expect(mimas).not.toBeVisible();
    await expect(inputField).toHaveValue("Mimas");
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(afterComp).toBeFocused();
  });
});

test.describe("900-test/in/suggest/spinner/Spinner.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/in/suggest/spinner/Spinner.xhtml");
  });

  test("Spinner position: standard", async ({page}) => {
    const input = page.locator("tobago-in[id='page:mainForm:standardInput']");
    const field = input.locator("input[type='text']");
    const spinner = input.locator(".spinner");

    await field.fill("show spinner");
    await expect(spinner).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: input group", async ({page}) => {
    const input = page.locator("tobago-in[id='page:mainForm:groupInput']");
    const field = input.locator("input[type='text']");
    const spinner = input.locator(".spinner");

    await field.fill("show spinner");
    await expect(spinner).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: popup", async ({page}) => {
    const button = page.locator("button[id='page:mainForm:openPopupButton']");
    const popup = page.locator("[id='page:mainForm:popup']");
    const collapse = page.locator("input[id='page:mainForm:popup::collapse']");
    const input = page.locator("tobago-in[id='page:mainForm:popup:popupInput']");
    const field = input.locator("input[type='text']");
    const spinner = input.locator(".spinner");

    await button.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(field).toBeVisible();

    await field.fill("show spinner");
    await expect(spinner).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });
});
