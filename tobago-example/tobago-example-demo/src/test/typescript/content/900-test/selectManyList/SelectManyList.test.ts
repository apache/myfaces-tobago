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

test.describe("900-test/selectManyList/SelectManyList.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/SelectManyList.xhtml");
  });

  test("open dropdown, select 'one', close dropdown, click on white space", async ({page}) => {
    const omitButton = page.locator("[id='page:mainForm:omit']");
    const selectField = page.locator("[id='page:mainForm:component::selectField']");
    const badges = page.locator("[id='page:mainForm:component::selectField'] .btn-group");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:component']");
    const entryOne = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:component'] td[value='one']");

    await expect(badges).toHaveCount(0);
    await selectField.click();
    await expect(dropdownMenu).toBeVisible();
    await entryOne.click();
    await expect(badges).toHaveCount(1);
    await omitButton.click();
    await expect(dropdownMenu).not.toBeVisible();
  });

  test("TOBAGO-2279: make sure menu opens on click on filter input element'", async ({page}) => {
    const filterInput = page.locator("[id='page:mainForm:component::filter']");
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:component']");

    await expect(options).not.toBeVisible();
    await filterInput.click();
    await expect(options).toBeVisible();
  });

  test("Standard: remove 'Earth'", async ({page}) => {
    const removeEarthButtonFn = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .btn-group[data-tobago-value='Earth'] .tobago-button");
    const badgeVenusFn = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .btn-group[data-tobago-value='Venus']");
    const badgeEarthFn = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .btn-group[data-tobago-value='Earth']");
    const badgeJupiterFn = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .btn-group[data-tobago-value='Jupiter']");
    const selectedRowsFn = page.locator("[id='page:mainForm:basic:selectedStandard'] tr.table-primary");
    const disabledBadgeVenusFn = page.locator("[id='page:mainForm:basic:selectedDisabled::selectField'] .btn-group[data-tobago-value='Venus']");
    const disabledBadgeEarthFn = page.locator("[id='page:mainForm:basic:selectedDisabled::selectField'] .btn-group[data-tobago-value='Earth']");
    const disabledBadgeJupiterFn = page.locator("[id='page:mainForm:basic:selectedDisabled::selectField'] .btn-group[data-tobago-value='Jupiter']");
    const submitFn = page.locator("[id='page:mainForm:basic:submit']");
    const resetFn = page.locator("[id='page:mainForm:basic:reset']");

    await expect(badgeVenusFn).toBeVisible();
    await expect(badgeEarthFn).toBeVisible();
    await expect(badgeJupiterFn).toBeVisible();
    await expect(selectedRowsFn.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(selectedRowsFn.nth(1)).toHaveAttribute("data-tobago-value", "Earth");
    await expect(selectedRowsFn.nth(2)).toHaveAttribute("data-tobago-value", "Jupiter");
    await expect(disabledBadgeVenusFn).toBeVisible();
    await expect(disabledBadgeEarthFn).toBeVisible();
    await expect(disabledBadgeJupiterFn).toBeVisible();

    await removeEarthButtonFn.click();
    await expect(selectedRowsFn).toHaveCount(2);
    await expect(badgeVenusFn).toBeVisible();
    await expect(badgeEarthFn).not.toBeVisible();
    await expect(badgeJupiterFn).toBeVisible();
    await expect(selectedRowsFn.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(selectedRowsFn.nth(1)).toHaveAttribute("data-tobago-value", "Jupiter");
    await expect(disabledBadgeVenusFn).toBeVisible();
    await expect(disabledBadgeEarthFn).toBeVisible();
    await expect(disabledBadgeJupiterFn).toBeVisible();

    await submitFn.click();
    await expect(badgeVenusFn).toBeVisible();
    await expect(badgeEarthFn).not.toBeVisible();
    await expect(badgeJupiterFn).toBeVisible();
    await expect(selectedRowsFn.nth(0)).toHaveAttribute("data-tobago-value", "Venus");
    await expect(selectedRowsFn.nth(1)).toHaveAttribute("data-tobago-value", "Jupiter");
    await expect(disabledBadgeVenusFn).toBeVisible();
    await expect(disabledBadgeEarthFn).not.toBeVisible();
    await expect(disabledBadgeJupiterFn).toBeVisible();
  });

  test("Filter (contains)", async ({page}) => {
    const searchFn = page.locator("[id='page:mainForm:selectedFilter::filter']");
    const hiddenRowsFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr.d-none:not(.tobago-no-entries)");
    const mercuryRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Mercury']");
    const venusRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Venus']");
    const earthRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Earth']");
    const marsRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Mars']");
    const jupiterRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Jupiter']");
    const saturnRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Saturn']");
    const uranusRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Uranus']");
    const neptuneRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Neptune']");
    const plutoRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr[data-tobago-value='Pluto']");
    const noEntriesRowFn = page.locator(".tobago-options[name='page:mainForm:selectedFilter'] tr.tobago-no-entries");

    await searchFn.fill("");
    await searchFn.dispatchEvent("input");
    await expect(hiddenRowsFn).toHaveCount(0);

    await expect(mercuryRowFn).not.toContainClass("d-none");
    await expect(venusRowFn).not.toContainClass("d-none");
    await expect(earthRowFn).not.toContainClass("d-none");
    await expect(marsRowFn).not.toContainClass("d-none");
    await expect(jupiterRowFn).not.toContainClass("d-none");
    await expect(saturnRowFn).not.toContainClass("d-none");
    await expect(uranusRowFn).not.toContainClass("d-none");
    await expect(neptuneRowFn).not.toContainClass("d-none");
    await expect(plutoRowFn).not.toContainClass("d-none");
    await expect(noEntriesRowFn).toContainClass("d-none");

    await searchFn.fill("M");
    await searchFn.dispatchEvent("input");

    await expect(mercuryRowFn).not.toContainClass("d-none");
    await expect(venusRowFn).toContainClass("d-none");
    await expect(earthRowFn).toContainClass("d-none");
    await expect(marsRowFn).not.toContainClass("d-none");
    await expect(jupiterRowFn).toContainClass("d-none");
    await expect(saturnRowFn).toContainClass("d-none");
    await expect(uranusRowFn).toContainClass("d-none");
    await expect(neptuneRowFn).toContainClass("d-none");
    await expect(plutoRowFn).toContainClass("d-none");
    await expect(noEntriesRowFn).toContainClass("d-none");

    await searchFn.fill("Ma");
    await searchFn.dispatchEvent("input");

    await expect(mercuryRowFn).toContainClass("d-none");
    await expect(venusRowFn).toContainClass("d-none");
    await expect(earthRowFn).toContainClass("d-none");
    await expect(marsRowFn).not.toContainClass("d-none");
    await expect(jupiterRowFn).toContainClass("d-none");
    await expect(saturnRowFn).toContainClass("d-none");
    await expect(uranusRowFn).toContainClass("d-none");
    await expect(neptuneRowFn).toContainClass("d-none");
    await expect(plutoRowFn).toContainClass("d-none");
    await expect(noEntriesRowFn).toContainClass("d-none");

    await searchFn.fill("Max");
    await searchFn.dispatchEvent("input");

    await expect(mercuryRowFn).toContainClass("d-none");
    await expect(venusRowFn).toContainClass("d-none");
    await expect(earthRowFn).toContainClass("d-none");
    await expect(marsRowFn).toContainClass("d-none");
    await expect(jupiterRowFn).toContainClass("d-none");
    await expect(saturnRowFn).toContainClass("d-none");
    await expect(uranusRowFn).toContainClass("d-none");
    await expect(neptuneRowFn).toContainClass("d-none");
    await expect(plutoRowFn).toContainClass("d-none");
    await expect(noEntriesRowFn).not.toContainClass("d-none");
  });

  test("Corner radius of select field must be '0px'", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:inputGroup::selectField']");
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:inputGroup']");

    await expect(selectField).toHaveCSS("border-top-left-radius", "0px");
    await expect(selectField).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(selectField).toHaveCSS("border-top-right-radius", "0px");
    await expect(selectField).toHaveCSS("border-bottom-right-radius", "0px");
    await expect(options).not.toBeVisible();
    await selectField.click();
    await expect(options).toBeVisible();
    await expect(selectField).toHaveCSS("border-top-left-radius", "0px");
    await expect(selectField).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(selectField).toHaveCSS("border-top-right-radius", "0px");
    await expect(selectField).toHaveCSS("border-bottom-right-radius", "0px");
  });

  test("Placeholder (no filter)", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:placeholderNoFilter::selectField']");
    const filter = page.locator("[id='page:mainForm:placeholderNoFilter::filter']");
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:placeholderNoFilter']");
    const earthRow = page.locator(".tobago-options[name='page:mainForm:placeholderNoFilter'] tr[data-tobago-value='Earth']");

    await expect(page.locator("[id='page:searchForm:search::field']")).toBeFocused();
    await expect(filter).toHaveAttribute("placeholder", "placeholder text");
    await expect(filter).not.toContainClass("tobago-hide-placeholder");

    await selectField.scrollIntoViewIfNeeded();
    await selectField.click();
    await expect(options).toBeVisible();
    await expect(filter).not.toContainClass("tobago-hide-placeholder");

    await earthRow.click();
    await expect(options).toBeVisible();
    await expect(filter).toContainClass("tobago-hide-placeholder");

    await earthRow.click();
    await expect(filter).not.toContainClass("tobago-hide-placeholder");
  });

  test("Placeholder (with filter)", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:placeholderWithFilter::selectField']");
    const filter = page.locator("[id='page:mainForm:placeholderWithFilter::filter']");
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:placeholderWithFilter']");
    const earthRow = page.locator(".tobago-options[name='page:mainForm:placeholderWithFilter'] tr[data-tobago-value='Earth']");
    const visibleResults = page.locator(".tobago-options[name='page:mainForm:placeholderWithFilter'] tr:not(.d-none)");

    await expect(page.locator("[id='page:searchForm:search::field']")).toBeFocused();
    await expect(filter).toHaveAttribute("placeholder", "placeholder text");
    await expect(filter).not.toContainClass("tobago-hide-placeholder");

    await selectField.scrollIntoViewIfNeeded();
    await selectField.click();
    await expect(options).toBeVisible();
    await expect(visibleResults).toHaveCount(9);
    await expect(filter).not.toContainClass("tobago-hide-placeholder");

    await filter.fill("E");
    await expect(options).toBeVisible();
    await expect(visibleResults).toHaveCount(5);

    await earthRow.click();
    await expect(options).toBeVisible();
    await expect(filter).toHaveValue("E");
    await expect(filter).toContainClass("tobago-hide-placeholder");

    await filter.fill("a");
    await expect(options).toBeVisible();
    await expect(visibleResults).toHaveCount(4);

    await earthRow.click();
    await expect(options).toBeVisible();
    await expect(filter).toHaveValue("a");
    await expect(filter).not.toContainClass("tobago-hide-placeholder");
  });

  test("No placeholder for readonly or disabled", async ({page}) => {
    const readonlyFilter = page.locator("[id='page:mainForm:placeholderReadonly::filter']");
    const disabledFilter = page.locator("[id='page:mainForm:placeholderDisabled::filter']");

    await expect(readonlyFilter).not.toHaveAttribute("placeholder");
    await expect(disabledFilter).not.toHaveAttribute("placeholder");
  });

  test("Menu stays open after selection", async ({page}) => {
    const firstBadge = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .tobago-badges tobago-badge");
    const firstCloseButton = page.locator("[id='page:mainForm:basic:selectedStandard::selectField'] .tobago-badges .tobago-button.badge").first();
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:basic:selectedStandard']");
    const venusRow = page.locator(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Venus']");

    await expect(page.locator("[id='page:searchForm:search::field']")).toBeFocused();
    await expect(firstBadge).toHaveCount(3);
    await expect(options).not.toBeVisible();

    //preparation
    await firstCloseButton.click();
    await expect(firstBadge).toHaveCount(2);
    await firstCloseButton.click();
    await expect(firstBadge).toHaveCount(1);
    await firstCloseButton.click();
    await expect(firstBadge).toHaveCount(0);

    //test keyboard
    await page.keyboard.press("ArrowDown");
    await expect(options).toBeVisible();
    await page.keyboard.press("Enter");
    await expect(firstBadge).toHaveText("Mercury");
    await expect(options).toBeVisible();
    await page.keyboard.press("Enter");
    await expect(firstBadge).toHaveCount(0);
    await expect(options).toBeVisible();
    await page.keyboard.press("Space");
    await expect(firstBadge).toHaveText("Mercury");
    await expect(options).toBeVisible();
    await page.keyboard.press("Space");
    await expect(firstBadge).toHaveCount(0);
    await expect(options).toBeVisible();

    //test mouse
    await venusRow.click();
    await expect(firstBadge).toHaveText("Venus");
    await expect(options).toBeVisible();
    await venusRow.click();
    await expect(firstBadge).toHaveCount(0);
    await expect(options).toBeVisible();
  });
});
