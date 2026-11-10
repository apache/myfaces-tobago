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

import {expect, type Locator, type Page, test} from "@playwright/test";

test.describe("900-test/sheet/columnSelector/selectable-use-case/selectable-use-case.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/columnSelector/selectable-use-case/selectable-use-case.xhtml");
  });

  test("AJAX, selectable=single", async ({page}) => {
    const selectSingle = page.locator("[id='page:mainForm:changeSelectable::0']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");

    await expect(checkAjax).toBeChecked();
    await selectSingle.click();
    await expect(checkSelectable).toHaveValue("single");
    await expect(selectAllCheckbox).toBeHidden();
    await expectSelection(page, {mercury: false, venus: false, name: "Sun"});

    await mercuryCheckbox.click();
    await expectSelection(page, {mercury: true, venus: false, name: "Sun"});

    await venusCheckbox.click();
    await expectSelection(page, {mercury: false, venus: true, name: "Sun"});

    await page.getByText('Venus').click();
    await expectSelection(page, {mercury: false, venus: true, name: "Venus"});
  });

  test("AJAX, selectable=singleOrNone", async ({page}) => {
    const selectSingleOrNone = page.locator("[id='page:mainForm:changeSelectable::1']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");

    await expect(checkAjax).toBeChecked();
    await selectSingleOrNone.click();
    await expect(checkSelectable).toHaveValue("singleOrNone");
    await expect(selectAllCheckbox).toBeHidden();
    await expectSelection(page, {mercury: false, venus: false, name: "Sun"});

    await mercuryCheckbox.click();
    await expectSelection(page, {mercury: true, venus: false, name: "Sun"});

    await venusCheckbox.click();
    await expectSelection(page, {mercury: false, venus: true, name: "Sun"});

    await venusCheckbox.click();
    await expectSelection(page, {mercury: false, venus: false, name: "Sun"});

    await page.getByText('Venus').click();
    await expectSelection(page, {mercury: false, venus: false, name: "Venus"});
  });

  test("AJAX, selectable=multi", async ({page}) => {
    const selectMulti = page.locator("[id='page:mainForm:changeSelectable::2']");
    const checkAjax = page.locator("[id='page:mainForm:checkAjax::field']");
    const checkSelectable = page.locator("[id='page:mainForm:checkSelectable::field']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");

    await expect(checkAjax).toBeChecked();
    await selectMulti.click();
    await expect(checkSelectable).toHaveValue("multi");
    await expect(selectAllCheckbox).toHaveAttribute("type", "checkbox");
    await expectSelection(page, {mercury: false, venus: false, name: "Sun"});

    await mercuryCheckbox.click();
    await expectSelection(page, {mercury: true, venus: false, name: "Sun"});

    await venusCheckbox.click();
    await expectSelection(page, {mercury: true, venus: true, name: "Sun"});

    await venusCheckbox.click();
    await expectSelection(page, {mercury: true, venus: false, name: "Sun"});

    await page.getByText('Venus').click();
    await expectSelection(page, {mercury: true, venus: false, name: "Venus"})
  });
});

async function isSelected(page: Page, number: number): Promise<boolean> {
  const hiddenSelectedField = page.locator("[id='page:mainForm:solar::selected']");
  const value = await hiddenSelectedField.inputValue();
  const selectedSet = new Set(JSON.parse(value));
  return selectedSet.has(number);
}

type SelectionExpectation = {
  mercury: boolean;
  venus: boolean;
  name: string;
};

async function expectSelection(page: Page, expected: SelectionExpectation): Promise<void> {
  const mercuryCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_1']");
  const mercuryRow = page.locator("[id='page:mainForm:solar:1:rowAjax']");
  const venusCheckbox = page.locator("input[name='page:mainForm:solar_data_row_selector_2']");
  const venusRow = page.locator("[id='page:mainForm:solar:2:rowAjax']");
  const name = page.locator("[id='page:mainForm:name::field']");

  await expect(mercuryCheckbox).toBeChecked({checked: expected.mercury});
  await expect(venusCheckbox).toBeChecked({checked: expected.venus});
  expect(await isSelected(page, 1)).toBe(expected.mercury);
  expect(await isSelected(page, 2)).toBe(expected.venus);
  await expectRowSelected(mercuryRow, expected.mercury);
  await expectRowSelected(venusRow, expected.venus);
  await expect(name).toHaveValue(expected.name);
}

async function expectRowSelected(row: Locator, selected: boolean): Promise<void> {
  if (selected) {
    await expect(row).toHaveClass(/tobago-selected/);
  } else {
    await expect(row).not.toHaveClass(/tobago-selected/);
  }
}
