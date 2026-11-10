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

import {expect, type Page, test} from "@playwright/test";

test.describe("900-test/sheet/columnSelector/selectable-use-case/selectable-use-case.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/columnSelector/selectable-use-case/selectable-use-case.xhtml");
  });

  test("AJAX, selectable=single", async ({page}) => {
    const selectAjax = page.locator("[id='page:mainForm:changeAjaxOrFsr::0']");
    const selectSingle = page.locator("[id='page:mainForm:changeSelectable::0']");
    const reset = page.locator("[id='page:mainForm:reset']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const mercuryRow = page.locator("[id='page:mainForm:solar:1:rowAjax']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const venusRow = page.locator("[id='page:mainForm:solar:2:rowAjax']");
    const name = page.locator("[id='page:mainForm:name::field']");


    await expect (checkAjax).toBeChecked();
    await selectSingle.click();
    await expect (checkSelectable).toHaveValue("single");

    await expect (selectAllCheckbox).toBeHidden();
    await checkForDefault(page);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);


    await mercuryCheckbox.click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    await venusCheckbox.click();
    await expect (mercuryCheckbox).not.toBeChecked();
    await expect (venusCheckbox).toBeChecked();
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(true);
    await expect (mercuryRow).not.toHaveClass(/tobago-selected/);
    await expect (venusRow).toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    //await venusRow.click();
    await page.getByText('Venus').click();
    await expect (mercuryCheckbox).not.toBeChecked();
    await expect (venusCheckbox).toBeChecked();
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(true);
    await expect (mercuryRow).not.toHaveClass(/tobago-selected/);
    await expect (venusRow).toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Venus");
  });

  test("AJAX, selectable=singleOrNone", async ({page}) => {
    const selectAjax = page.locator("[id='page:mainForm:changeAjaxOrFsr::0']");
    const selectSingleOrNone = page.locator("[id='page:mainForm:changeSelectable::1']");
    const reset = page.locator("[id='page:mainForm:reset']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const mercuryRow = page.locator("[id='page:mainForm:solar:1:rowAjax']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const venusRow = page.locator("[id='page:mainForm:solar:2:rowAjax']");
    const name = page.locator("[id='page:mainForm:name::field']");

    await expect (checkAjax).toBeChecked();
    await selectSingleOrNone.click();
    await expect (checkSelectable).toHaveValue("singleOrNone");

    await expect (selectAllCheckbox).toBeHidden();
    await checkForDefault(page);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);

    await mercuryCheckbox.click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    await venusCheckbox.click();
    await expect (mercuryCheckbox).not.toBeChecked();
    await expect (venusCheckbox).toBeChecked();
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(true);
    await expect (mercuryRow).not.toHaveClass(/tobago-selected/);
    await expect (venusRow).toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    await venusCheckbox.click();
    await checkForDefault(page);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);

    //await venusRow.click();
    await page.getByText('Venus').click();
    await expect (mercuryCheckbox).not.toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).not.toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Venus");
  });

  test("AJAX, selectable=multi", async ({page}) => {
    const selectAjax = page.locator("[id='page:mainForm:changeAjaxOrFsr::0']");
    const selectMulti = page.locator("[id='page:mainForm:changeSelectable::2']");
    const reset = page.locator("[id='page:mainForm:reset']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const mercuryRow = page.locator("[id='page:mainForm:solar:1:rowAjax']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const venusRow = page.locator("[id='page:mainForm:solar:2:rowAjax']");
    const name = page.locator("[id='page:mainForm:name::field']");

    await expect (checkAjax).toBeChecked();
    await selectMulti.click();
    await expect (checkSelectable).toHaveValue("multi");

    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");
    await checkForDefault(page);
    expect(await isSelected(page, 1)).toBe(false);
    expect(await isSelected(page, 2)).toBe(false);

    await mercuryCheckbox.click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    await venusCheckbox.click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(true);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    await venusCheckbox.click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Sun");

    //await venusRow.click();
    await page.getByText('Venus').click();
    await expect (mercuryCheckbox).toBeChecked();
    await expect (venusCheckbox).not.toBeChecked();
    expect(await isSelected(page, 1)).toBe(true);
    expect(await isSelected(page, 2)).toBe(false);
    await expect (mercuryRow).toHaveClass(/tobago-selected/);
    await expect (venusRow).not.toHaveClass(/tobago-selected/);
    await expect (name).toHaveValue("Venus");
  });
});

async function isSelected(page: Page, number: number): Promise<boolean> {
  const hiddenSelectedField = page.locator("[id='page:mainForm:solar::selected']");
  const value = await hiddenSelectedField.inputValue();
  const selectedSet = new Set(JSON.parse(value));
  return selectedSet.has(number);
}

async function checkForDefault(page: Page) {
  const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = page.locator("[id='page:mainForm:solar:1:rowAjax']");
  const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = page.locator("[id='page:mainForm:solar:2:rowAjax']");
  const name = page.locator("[id='page:mainForm:name::field']");


  await expect (mercuryCheckbox).not.toBeChecked();
  await expect (venusCheckbox).not.toBeChecked();
  await expect (mercuryRow).not.toHaveClass(/tobago-selected/);
  await expect (venusRow).not.toHaveClass(/tobago-selected/);
  await expect (name).toHaveValue("Sun");
}
