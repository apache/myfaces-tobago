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

test.describe("900-test/2100-selectManyList/deselect/deselect.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyList/deselect/deselect.xhtml");
  });

  test("tc:selectManyList: deselect", async ({page}) => {
    const reset = page.locator("button[id='page:mainForm:reset']");
    await reset.click();

    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const venus = selectManyList.locator(".btn-group[data-tobago-value='Venus']");
    const earth = selectManyList.locator(".btn-group[data-tobago-value='Earth']");
    const jupiter = selectManyList.locator(".btn-group[data-tobago-value='Jupiter']");
    await expect(venus).toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).toBeVisible();

    await venus.locator(".tobago-button").click();
    await expect(venus).not.toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).toBeVisible();

    await jupiter.locator(".tobago-button").click();
    await expect(venus).not.toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).not.toBeVisible();
  });
});

test.describe("900-test/selectManyList/dropdown-form/Dropdown_form.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyList/dropdown-form/Dropdown_form.xhtml");
  });

  test("Open form dropdown, close and reopen", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const badges = selectManyList.locator(".tobago-badges .btn-group");

    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await expect(badges).toHaveCount(3);
    await toggleButton.click();
    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await expect(badges).toHaveCount(3);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(badges.nth(1)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(badges.nth(2)).toHaveAttribute("data-tobago-value", "Jupiter");
  });

  test("Open form dropdown, ArrowDown to open selectManyList dropdown, Escape, Tab to next element", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const deselectVenus = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Venus']");
    const deselectEarth = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Earth']");
    const deselectJupiter = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Jupiter']");
    const selectManyListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectManyList']");
    const mercuryRow = selectManyListMenu.locator("td[value='Mercury']");
    const afterComp = page.locator("[id='page:mainForm:innerAfterComp::field']");

    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(deselectVenus).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mercuryRow).toBeVisible();
    await page.keyboard.press("Escape");
    await expect(mercuryRow).not.toBeVisible();
    await expect(deselectVenus).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(deselectEarth).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(deselectJupiter).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(afterComp).toBeFocused();
  });

  test("Open from dropdown, focus selectManyList, ArrowDown, remove Venus by list, Escape, Escape", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const badges = selectManyList.locator(".tobago-badges .btn-group");
    const deselectVenus = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Venus']");
    const deselectEarth = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Earth']");
    const selectManyListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectManyList']");
    const mercuryRow = selectManyListMenu.locator("td[value='Mercury']");
    const venusRow = selectManyListMenu.locator("td[value='Venus']");

    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(deselectVenus).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mercuryRow).toBeVisible();
    await expect(badges).toHaveCount(3);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(badges.nth(1)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(badges.nth(2)).toHaveAttribute("data-tobago-value", "Jupiter");
    await venusRow.click();
    await expect(badges).toHaveCount(2);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(badges.nth(1)).toHaveAttribute("data-tobago-value", "Jupiter");
    await page.keyboard.press("Escape");
    await expect(mercuryRow).not.toBeVisible();
    await expect(selectManyList).toBeVisible();
    await expect(deselectEarth).toBeFocused();
    await page.keyboard.press("Escape");
    await expect(selectManyList).not.toBeVisible();
    await expect(toggleButton).toBeFocused();
  });

  test("Open form dropdown, Tab to selectOneList, Escape, Enter -> focus must not be set", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const deselectVenus = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Venus']");
    const selectManyListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectManyList']");

    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await page.keyboard.press("Tab");
    await expect(beforeComp).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(deselectVenus).toBeFocused();
    await expect(selectManyList).toContainClass("tobago-focus");
    await page.keyboard.press("Escape");
    await expect(selectManyList).not.toBeVisible();
    await expect(toggleButton).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(selectManyList).toBeVisible();
    await expect(selectManyList).not.toContainClass("tobago-focus");
  });

  test("open from dropdown, remove Venus by badge, Tab to next element", async ({page}) => {
    const toggleButton = page.locator("button[id='page:mainForm:dropdownForm::command']");
    const beforeComp = page.locator("[id='page:mainForm:innerBeforeComp::field']");
    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const badges = selectManyList.locator(".tobago-badges .btn-group");
    const deselectVenus = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Venus']");
    const deselectEarth = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Earth']");
    const deselectJupiter = selectManyList.locator(".tobago-badges .tobago-button[aria-label='deselect Jupiter']");
    const selectManyListMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectManyList']");
    const mercuryRow = selectManyListMenu.locator("td[value='Mercury']");
    const afterComp = page.locator("[id='page:mainForm:innerAfterComp::field']");

    await expect(selectManyList).not.toBeVisible();
    await toggleButton.click();
    await expect(selectManyList).toBeVisible();
    await expect(badges).toHaveCount(3);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(badges.nth(1)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(badges.nth(2)).toHaveAttribute("data-tobago-value", "Jupiter");
    await deselectVenus.click();
    await expect(badges).toHaveCount(2);
    await expect(badges.nth(0)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(badges.nth(1)).toHaveAttribute("data-tobago-value", "Jupiter");
    await page.keyboard.press("Tab");
    await expect(deselectJupiter).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(afterComp).toBeFocused();
  });
});

test.describe("900-test/selectManyList/server-side-filtering/spinner/Spinner.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyList/server-side-filtering/spinner/Spinner.xhtml");
  });

  test("Spinner position: standard", async ({page}) => {
    const input = page.locator("tobago-select-many-list[id='page:mainForm:standardSelectManyList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:standardSelectManyList::filter']");
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
    const input = page.locator("tobago-select-many-list[id='page:mainForm:groupSelectManyList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:groupSelectManyList::filter']");
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
    const input = page.locator("tobago-select-many-list[id='page:mainForm:labelLayoutTopSelectManyList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:labelLayoutTopSelectManyList::filter']");
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
    const input = page.locator("tobago-select-many-list[id='page:mainForm:popup:popupSelectManyList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:popup:popupSelectManyList::filter']");
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
