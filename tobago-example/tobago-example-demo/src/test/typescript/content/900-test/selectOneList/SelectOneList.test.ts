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

test.describe("900-test/selectOneList/SelectOneList.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneList/SelectOneList.xhtml");
  });

  test("Standard: select 'Mercury', select 'Venus', select 'Earth'", async ({page}) => {
    const selectField = page.locator("[id='page:mainForm:selectedStandard::selectField']");
    const options = page.locator(".tobago-options[name='page:mainForm:selectedStandard']");
    const mercuryRow = page.locator(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Mercury']");
    const venusRow = page.locator(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Venus']");
    const earthRow = page.locator(".tobago-options[name='page:mainForm:selectedStandard'] tr[data-tobago-value='Earth']");
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");

    await expect(options).not.toBeVisible();
    await selectField.click();
    await expect(options).toBeVisible();
    await mercuryRow.click();
    await expect(options).not.toBeVisible();
    await expect(output).toHaveText("Mercury");
    await selectField.click();
    await expect(options).toBeVisible();
    await expect(mercuryRow).toContainClass("table-primary");
    await expect(venusRow).not.toContainClass("table-primary");
    await expect(earthRow).not.toContainClass("table-primary");
    await venusRow.click();
    await expect(options).not.toBeVisible();
    await expect(output).toHaveText("Venus");
    await selectField.click();
    await expect(options).toBeVisible();
    await expect(mercuryRow).not.toContainClass("table-primary");
    await expect(venusRow).toContainClass("table-primary");
    await expect(earthRow).not.toContainClass("table-primary");
    await earthRow.click();
    await expect(options).not.toBeVisible();
    await expect(output).toHaveText("Earth");
  });

  test("Standard: select 'Neptune', select 'Mars', select 'Saturn'", async ({page}) => {
    const selectedField = page.locator(".tobago-select-field[name='page:mainForm:basic:selectedStandard'] span");
    const marsRow = page.locator(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Mars']");
    const saturnRow = page.locator(".tobago-options[name='page:mainForm:basic:selectedStandard'] tr[data-tobago-value='Saturn']");
    const disabledSelectedField = page.locator(".tobago-select-field[name='page:mainForm:basic:selectedDisabled'] span");
    const submit = page.locator("[id='page:mainForm:basic:submit']");

    await selectedField.click();
    await marsRow.click();
    await expect(selectedField).toHaveText("Mars");
    await submit.click();
    await expect(disabledSelectedField).toHaveText("Mars");

    await selectedField.click();
    await saturnRow.click();
    await expect(selectedField).toHaveText("Saturn");
    await submit.click();
    await expect(disabledSelectedField).toHaveText("Saturn");
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
});
