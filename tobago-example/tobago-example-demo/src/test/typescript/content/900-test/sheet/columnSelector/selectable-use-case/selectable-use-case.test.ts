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
    await expectSelection(page, false, false, "Sun");

    await mercuryCheckbox.click();
    await expectSelection(page, true, false, "Sun");

    await venusCheckbox.click();
    await expectSelection(page, false, true, "Sun");

    await page.getByText("Venus").click();
    await expectSelection(page, false, true, "Venus");
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
    await expectSelection(page, false, false, "Sun");

    await mercuryCheckbox.click();
    await expectSelection(page, true, false, "Sun");

    await venusCheckbox.click();
    await expectSelection(page, false, true, "Sun");

    await venusCheckbox.click();
    await expectSelection(page, false, false, "Sun");

    await page.getByText("Venus").click();
    await expectSelection(page, false, false, "Venus");
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
    await expectSelection(page, false, false, "Sun");

    await mercuryCheckbox.click();
    await expectSelection(page, true, false, "Sun");

    await venusCheckbox.click();
    await expectSelection(page, true, true, "Sun");

    await venusCheckbox.click();
    await expectSelection(page, true, false, "Sun");

    await page.getByText("Venus").click();
    await expectSelection(page, true, false, "Venus");
  });

  test("Full server request, selectable=single", async ({page}) => {
    const fsrOption = page.locator("[id='page:mainForm:changeAjaxOrFsr::1']");
    const selectableSingleOption = page.locator("[id='page:mainForm:changeSelectable::0']");
    const fsrCheckbox = page.locator("[id='page:mainForm:checkFullServerRequest::field']");
    const selectableReadonlyInput = page.locator("[id='page:mainForm:checkSelectable::field']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:solar']");
    const hiddenSelectedField = sheet.locator("[id='page:mainForm:solar::selected']");
    const selectAllCheckbox = sheet.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const selectedOutput = page.locator("[id='page:mainForm:selected'] .form-control-plaintext");

    await fsrOption.click();
    await expect(fsrCheckbox).toBeChecked();
    await selectableSingleOption.click();
    await expect(selectableReadonlyInput).toHaveValue("single");
    await expect(selectAllCheckbox).not.toBeVisible();
    await expect(hiddenSelectedField).toHaveValue("[]");
    await expect(selectedOutput).toHaveText("[]");

    await mercuryOption.click();
    await expectSelection(page, true, false, "Sun");

    await venusOption.click();
    await expectSelection(page, false, true, "Sun");

    await sheet.getByText("Venus").click();
    await expectSelection(page, false, true, "Venus");
  });

  test("Full server request, selectable=singleOrNone", async ({page}) => {
    const fsrOption = page.locator("[id='page:mainForm:changeAjaxOrFsr::1']");
    const selectableSingleOrNoneOption = page.locator("[id='page:mainForm:changeSelectable::1']");
    const fsrCheckbox = page.locator("[id='page:mainForm:checkFullServerRequest::field']");
    const selectableReadonlyInput = page.locator("[id='page:mainForm:checkSelectable::field']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:solar']");
    const hiddenSelectedField = sheet.locator("[id='page:mainForm:solar::selected']");
    const selectAllCheckbox = sheet.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const selectedOutput = page.locator("[id='page:mainForm:selected'] .form-control-plaintext");

    await fsrOption.click();
    await expect(fsrCheckbox).toBeChecked();
    await selectableSingleOrNoneOption.click();
    await expect(selectableReadonlyInput).toHaveValue("singleOrNone");
    await expect(selectAllCheckbox).not.toBeVisible();
    await expect(hiddenSelectedField).toHaveValue("[]");
    await expect(selectedOutput).toHaveText("[]");

    await mercuryOption.click();
    await expectSelection(page, true, false, "Sun");

    await venusOption.click();
    await expectSelection(page, false, true, "Sun");

    await venusOption.click();
    await expectSelection(page, false, false, "Sun");

    await sheet.getByText("Venus").click();
    await expectSelection(page, false, false, "Venus");
  });

  test("Full server request, selectable=multi", async ({page}) => {
    const fsrOption = page.locator("[id='page:mainForm:changeAjaxOrFsr::1']");
    const fsrCheckbox = page.locator("[id='page:mainForm:checkFullServerRequest::field']");
    const selectableReadonlyInput = page.locator("[id='page:mainForm:checkSelectable::field']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:solar']");
    const hiddenSelectedField = sheet.locator("[id='page:mainForm:solar::selected']");
    const selectAllCheckbox = sheet.locator("input[name='page:mainForm:solar::columnSelector']");
    const mercuryOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const venusOption = sheet.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const selectedOutput = page.locator("[id='page:mainForm:selected'] .form-control-plaintext");

    await fsrOption.click();
    await expect(fsrCheckbox).toBeChecked();
    await expect(selectableReadonlyInput).toHaveValue("multi");
    await expect(selectAllCheckbox).toBeVisible();
    await expect(hiddenSelectedField).toHaveValue("[]");
    await expect(selectedOutput).toHaveText("[]");

    await mercuryOption.click();
    await expectSelection(page, true, false, "Sun");

    await venusOption.click();
    await expectSelection(page, true, true, "Sun");

    await venusOption.click();
    await expectSelection(page, true, false, "Sun");

    await sheet.getByText("Venus").click();
    await expectSelection(page, true, false, "Venus");
  });

  async function expectSelection(page: Page, mercury: boolean, venus: boolean, name: string): Promise<void> {
    const sheet = page.locator("tobago-sheet[id='page:mainForm:solar']");
    const hiddenSelectedField = sheet.locator("[id='page:mainForm:solar::selected']");
    const mercuryCheckbox = sheet.locator("input[name='page:mainForm:solar_data_row_selector_1']");
    const mercuryRow = sheet.locator("tr[row-index='1']");
    const venusCheckbox = sheet.locator("input[name='page:mainForm:solar_data_row_selector_2']");
    const venusRow = sheet.locator("tr[row-index='2']");
    const selectedOutput = page.locator("[id='page:mainForm:selected'] .form-control-plaintext");
    const detailsName = page.locator("[id='page:mainForm:name::field']");

    expect(new Set(JSON.parse(await hiddenSelectedField.inputValue())).has(1)).toBe(mercury);
    await expect(mercuryCheckbox).toBeChecked({checked: mercury});
    await (mercury ? expect(mercuryRow) : expect(mercuryRow).not).toContainClass("tobago-selected");
    await expect.poll(async () => new Set(JSON.parse(await selectedOutput.textContent() as string)).has(1)).toBe(mercury);

    expect(new Set(JSON.parse(await hiddenSelectedField.inputValue())).has(2)).toBe(venus);
    await expect(venusCheckbox).toBeChecked({checked: venus});
    await (venus ? expect(venusRow) : expect(venusRow).not).toContainClass("tobago-selected");
    await expect.poll(async () => new Set(JSON.parse(await selectedOutput.textContent() as string)).has(2)).toBe(venus);

    await expect(detailsName).toHaveValue(name);

    //second time to make sure ordering of the checks didn't matter
    expect(new Set(JSON.parse(await hiddenSelectedField.inputValue())).has(1)).toBe(mercury);
    await expect(mercuryCheckbox).toBeChecked({checked: mercury});
    await (mercury ? expect(mercuryRow) : expect(mercuryRow).not).toContainClass("tobago-selected");
    await expect.poll(async () => new Set(JSON.parse(await selectedOutput.textContent() as string)).has(1)).toBe(mercury);

    expect(new Set(JSON.parse(await hiddenSelectedField.inputValue())).has(2)).toBe(venus);
    await expect(venusCheckbox).toBeChecked({checked: venus});
    await (venus ? expect(venusRow) : expect(venusRow).not).toContainClass("tobago-selected");
    await expect.poll(async () => new Set(JSON.parse(await selectedOutput.textContent() as string)).has(2)).toBe(venus);

    await expect(detailsName).toHaveValue(name);
  }
});
