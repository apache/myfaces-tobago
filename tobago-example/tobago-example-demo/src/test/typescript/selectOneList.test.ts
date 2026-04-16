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

test.describe("900-test/selectOneList/dropdown-form/Dropdown_form.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectOneList/dropdown-form/Dropdown_form.xhtml");
  });

  test("Open form dropdown, ArrowDown to open selectOneList dropdown, Escape, Tab to next element", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectOneList = page.locator("tobago-select-one-list[id='page:mainForm:selectOneList']");
    const readonlyFilter = selectOneList.locator("input[id='page:mainForm:selectOneList::filter']");
    const selectOneListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectOneList']");
    const mercuryRow = selectOneListMenu.locator("td[value='Mercury']");
    const afterComp = page.locator("[id='page:mainForm:innerAfterComp::field']");

    await expect(selectOneList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectOneList).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(readonlyFilter).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mercuryRow).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(mercuryRow).not.toBeVisible();
    await expect(readonlyFilter).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(afterComp).toBeFocused();
  });

  test("Open from dropdown, click selectOneList, remove Venus by list, Escape, Escape", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const selectOneList = page.locator("tobago-select-one-list[id='page:mainForm:selectOneList']");
    const selectField = page.locator("div[id='page:mainForm:selectOneList::selectField']");
    const readonlyFilter = selectOneList.locator("input[id='page:mainForm:selectOneList::filter']");
    const selectOneListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectOneList']");
    const mercuryRow = selectOneListMenu.locator("td[value='Mercury']");

    await expect(selectOneList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectOneList).toBeVisible();
    await selectField.click();
    await expect(mercuryRow).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(mercuryRow).not.toBeVisible();
    await expect(selectOneList).toBeVisible();
    await expect(readonlyFilter).toBeFocused();
    await page.keyboard.press("Escape");
    await expect(selectOneList).not.toBeVisible();
    await expect(toggleButton).toBeFocused();
  });

  test("Open form dropdown, Tab to selectOneList, Escape, Enter -> focus must not be set", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectOneList = page.locator("tobago-select-one-list[id='page:mainForm:selectOneList']");
    const readonlyFilter = selectOneList.locator("input[id='page:mainForm:selectOneList::filter']");

    await expect(selectOneList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectOneList).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(readonlyFilter).toBeFocused();
    await expect(selectOneList).toContainClass("tobago-focus");
    await page.keyboard.press("Escape");
    await expect(selectOneList).not.toBeVisible();
    await expect(toggleButton).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(selectOneList).toBeVisible();
    await expect(selectOneList).not.toContainClass("tobago-focus");
  });
});

test.describe("900-test/selectOneList/server-side-filtering/spinner/Spinner.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectOneList/server-side-filtering/spinner/Spinner.xhtml");
  });

  test("Spinner position: standard", async ({page}) => {
    const input = page.locator("tobago-select-one-list[id='page:mainForm:standardSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:standardSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await filter.fill("show spinner");
    await expect(filter).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: input group", async ({page}) => {
    const input = page.locator("tobago-select-one-list[id='page:mainForm:groupSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:groupSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await filter.fill("show spinner");
    await expect(filter).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: labelLayout=top", async ({page}) => {
    const input = page.locator("tobago-select-one-list[id='page:mainForm:labelLayoutTopSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:labelLayoutTopSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await filter.fill("show spinner");
    await expect(filter).toBeVisible();

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
    const input = page.locator("tobago-select-one-list[id='page:mainForm:popup:popupSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:popup:popupSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await button.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(field).toBeVisible();

    await filter.fill("show spinner");
    await expect(spinner).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });
});
