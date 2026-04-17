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
    const filterInput = page.locator("[id='page:mainForm:component::filter']");
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

  test("width of filter input field must be '0px'", async ({page}) => {
    const filterInput = page.locator("[id='page:mainForm:component::filter']");
    await expect(filterInput).toHaveCSS("width", "0px");
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
});
