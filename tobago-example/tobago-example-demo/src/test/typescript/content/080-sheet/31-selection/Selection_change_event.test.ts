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

import {expect, test} from "@playwright/test";

test.describe("080-sheet/31-selection/Selection_change_event.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/31-selection/Selection_change_event.xhtml");
  });

  test("select 'Sun', select 'Venus', select all, deselect 'Mercury'", async ({page}) => {
    const selectedRows = page.locator("[id='page:mainForm:selectedRows'] .form-control-plaintext");
    const resetSelected = page.locator("[id='page:mainForm:resetSelected']");
    const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const sun = page.locator("tr[row-index='0']");
    const mercury = page.locator("tr[row-index='1']");
    const venus = page.locator("tr[row-index='2']");
    const earth = page.locator("tr[row-index='3']");

    await resetSelected.click();
    await expect(hiddenSelectedField).toHaveValue("[]");

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");
    await expect(selectAllCheckbox).not.toBeChecked();

    await sun.click();
    await expect(selectedRows).toHaveText("0");
    await expect(selectAllCheckbox).not.toBeChecked();

    await venus.click();
    await expect(selectedRows).toHaveText("0, 2");
    await expect(selectAllCheckbox).not.toBeChecked();

    await selectAllCheckbox.click();
    await expect(selectedRows).toHaveText("0, 2, 1, 3");
    await expect(selectAllCheckbox).toBeChecked();

    await mercury.click();
    await expect(selectedRows).toHaveText("0, 2, 3");
    await expect(selectAllCheckbox).not.toBeChecked();
  });

  test("selection alternating with keyboard and mouse", async ({page}) => {
    const selectedRows = page.locator("[id='page:mainForm:selectedRows'] .form-control-plaintext");
    const resetSelected = page.locator("[id='page:mainForm:resetSelected']");
    const hiddenSelectedField = page.locator("[id='page:mainForm:sheet::selected']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const sunRow = page.locator("tr[row-index='0']");
    const sunCheckbox = sunRow.locator("input[type='checkbox']");
    const mercuryRow = page.locator("tr[row-index='1']");
    const mercuryCheckbox = mercuryRow.locator("input[type='checkbox']");
    const venusRow = page.locator("tr[row-index='2']");
    const venusCheckbox = venusRow.locator("input[type='checkbox']");
    const earthRow = page.locator("tr[row-index='3']");
    const earthCheckbox = earthRow.locator("input[type='checkbox']");

    await resetSelected.click();
    await expect(hiddenSelectedField).toHaveValue("[]");

    await sunCheckbox.focus();
    await expect(sunCheckbox).toBeFocused();
    await expect(sunRow).not.toContainClass("tobago-selected");
    await page.keyboard.press("Space");
    await expect(sunRow).toContainClass("tobago-selected");
    await expect(selectedRows).toHaveText("0");
    await expect(selectAllCheckbox).not.toBeChecked();

    await expect(mercuryRow).not.toContainClass("tobago-selected");
    await mercuryCheckbox.click();
    await expect(mercuryRow).toContainClass("tobago-selected");
    await expect(selectedRows).toHaveText("0, 1");
    await expect(selectAllCheckbox).not.toBeChecked();

    await expect(venusCheckbox).not.toBeFocused();
    await page.keyboard.press("Tab");
    await expect(venusCheckbox).toBeFocused();
    await expect(venusRow).not.toContainClass("tobago-selected");
    await page.keyboard.press("Space");
    await expect(venusRow).toContainClass("tobago-selected");
    await expect(selectedRows).toHaveText("0, 1, 2");
    await expect(selectAllCheckbox).not.toBeChecked();

    await expect(earthRow).not.toContainClass("tobago-selected");
    await earthCheckbox.click();
    await expect(earthRow).toContainClass("tobago-selected");
    await expect(selectedRows).toHaveText("0, 1, 2, 3");
    await expect(selectAllCheckbox).toBeChecked();
  });
});
