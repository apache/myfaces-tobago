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

test.describe("sheet/columnSelector/check-all/check-all.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/sheet/columnSelector/check-all/check-all.xhtml");
  });

  test("All rows unselected", async ({page}) => {
    const initButton = page.locator("button[id='page:mainForm:unselectAll']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const checkbox0 = page.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const checkbox1 = page.locator("input[name='page:mainForm:sheet_data_row_selector_1']");
    const checkbox2 = page.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const checkbox3 = page.locator("input[name='page:mainForm:sheet_data_row_selector_3']");
    const checkbox4 = page.locator("input[name='page:mainForm:sheet_data_row_selector_4']");
    const checkbox5 = page.locator("input[name='page:mainForm:sheet_data_row_selector_5']");
    const checkbox6 = page.locator("input[name='page:mainForm:sheet_data_row_selector_6']");
    const checkbox7 = page.locator("input[name='page:mainForm:sheet_data_row_selector_7']");
    const checkbox8 = page.locator("input[name='page:mainForm:sheet_data_row_selector_8']");
    const checkbox9 = page.locator("input[name='page:mainForm:sheet_data_row_selector_9']");
    const page1Button = page.locator("tobago-paginator-list .page-item:nth-child(1) button");
    const page2Button = page.locator("tobago-paginator-list .page-item:nth-child(2) button");

    await initButton.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await selectAllCheckbox.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();

    await page2Button.click();
    await expect(selectAllCheckbox).toBeDisabled();
    await expect(checkbox5).toBeDisabled();
    await expect(checkbox6).toBeDisabled();
    await expect(checkbox7).toBeDisabled();
    await expect(checkbox8).toBeDisabled();
    await expect(checkbox9).toBeDisabled();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox5).not.toBeChecked();
    await expect(checkbox6).not.toBeChecked();
    await expect(checkbox7).not.toBeChecked();
    await expect(checkbox8).not.toBeChecked();
    await expect(checkbox9).not.toBeChecked();

    await page1Button.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();
  });

  test("All rows selected", async ({page}) => {
    const initButton = page.locator("button[id='page:mainForm:selectAll']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const checkbox0 = page.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const checkbox1 = page.locator("input[name='page:mainForm:sheet_data_row_selector_1']");
    const checkbox2 = page.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const checkbox3 = page.locator("input[name='page:mainForm:sheet_data_row_selector_3']");
    const checkbox4 = page.locator("input[name='page:mainForm:sheet_data_row_selector_4']");
    const checkbox5 = page.locator("input[name='page:mainForm:sheet_data_row_selector_5']");
    const checkbox6 = page.locator("input[name='page:mainForm:sheet_data_row_selector_6']");
    const checkbox7 = page.locator("input[name='page:mainForm:sheet_data_row_selector_7']");
    const checkbox8 = page.locator("input[name='page:mainForm:sheet_data_row_selector_8']");
    const checkbox9 = page.locator("input[name='page:mainForm:sheet_data_row_selector_9']");
    const page1Button = page.locator("tobago-paginator-list .page-item:nth-child(1) button");
    const page2Button = page.locator("tobago-paginator-list .page-item:nth-child(2) button");

    await initButton.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();

    await selectAllCheckbox.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await checkbox4.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();

    await page2Button.click();
    await expect(selectAllCheckbox).toBeDisabled();
    await expect(checkbox5).toBeDisabled();
    await expect(checkbox6).toBeDisabled();
    await expect(checkbox7).toBeDisabled();
    await expect(checkbox8).toBeDisabled();
    await expect(checkbox9).toBeDisabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox5).toBeChecked();
    await expect(checkbox6).toBeChecked();
    await expect(checkbox7).toBeChecked();
    await expect(checkbox8).toBeChecked();
    await expect(checkbox9).toBeChecked();

    await page1Button.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();
  });

  test("Only enabled rows selected", async ({page}) => {
    const initButton = page.locator("button[id='page:mainForm:selectAllEnabled']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const checkbox0 = page.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const checkbox1 = page.locator("input[name='page:mainForm:sheet_data_row_selector_1']");
    const checkbox2 = page.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const checkbox3 = page.locator("input[name='page:mainForm:sheet_data_row_selector_3']");
    const checkbox4 = page.locator("input[name='page:mainForm:sheet_data_row_selector_4']");
    const checkbox5 = page.locator("input[name='page:mainForm:sheet_data_row_selector_5']");
    const checkbox6 = page.locator("input[name='page:mainForm:sheet_data_row_selector_6']");
    const checkbox7 = page.locator("input[name='page:mainForm:sheet_data_row_selector_7']");
    const checkbox8 = page.locator("input[name='page:mainForm:sheet_data_row_selector_8']");
    const checkbox9 = page.locator("input[name='page:mainForm:sheet_data_row_selector_9']");
    const page1Button = page.locator("tobago-paginator-list .page-item:nth-child(1) button");
    const page2Button = page.locator("tobago-paginator-list .page-item:nth-child(2) button");

    await initButton.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();

    await selectAllCheckbox.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await checkbox4.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();

    await page2Button.click();
    await expect(selectAllCheckbox).toBeDisabled();
    await expect(checkbox5).toBeDisabled();
    await expect(checkbox6).toBeDisabled();
    await expect(checkbox7).toBeDisabled();
    await expect(checkbox8).toBeDisabled();
    await expect(checkbox9).toBeDisabled();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox5).not.toBeChecked();
    await expect(checkbox6).not.toBeChecked();
    await expect(checkbox7).not.toBeChecked();
    await expect(checkbox8).not.toBeChecked();
    await expect(checkbox9).not.toBeChecked();

    await page1Button.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).not.toBeChecked();
    await expect(checkbox1).not.toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).not.toBeChecked();
    await expect(checkbox4).toBeChecked();
  });

  test("Only disabled rows selected", async ({page}) => {
    const initButton = page.locator("button[id='page:mainForm:selectAllDisabled']");
    const selectAllCheckbox = page.locator("input[name='page:mainForm:sheet::columnSelector']");
    const checkbox0 = page.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const checkbox1 = page.locator("input[name='page:mainForm:sheet_data_row_selector_1']");
    const checkbox2 = page.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const checkbox3 = page.locator("input[name='page:mainForm:sheet_data_row_selector_3']");
    const checkbox4 = page.locator("input[name='page:mainForm:sheet_data_row_selector_4']");
    const checkbox5 = page.locator("input[name='page:mainForm:sheet_data_row_selector_5']");
    const checkbox6 = page.locator("input[name='page:mainForm:sheet_data_row_selector_6']");
    const checkbox7 = page.locator("input[name='page:mainForm:sheet_data_row_selector_7']");
    const checkbox8 = page.locator("input[name='page:mainForm:sheet_data_row_selector_8']");
    const checkbox9 = page.locator("input[name='page:mainForm:sheet_data_row_selector_9']");
    const page1Button = page.locator("tobago-paginator-list .page-item:nth-child(1) button");
    const page2Button = page.locator("tobago-paginator-list .page-item:nth-child(2) button");

    await initButton.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).not.toBeChecked();

    await selectAllCheckbox.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).not.toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).not.toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();

    await checkbox2.click();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();

    await page2Button.click();
    await expect(selectAllCheckbox).toBeDisabled();
    await expect(checkbox5).toBeDisabled();
    await expect(checkbox6).toBeDisabled();
    await expect(checkbox7).toBeDisabled();
    await expect(checkbox8).toBeDisabled();
    await expect(checkbox9).toBeDisabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox5).toBeChecked();
    await expect(checkbox6).toBeChecked();
    await expect(checkbox7).toBeChecked();
    await expect(checkbox8).toBeChecked();
    await expect(checkbox9).toBeChecked();

    await page1Button.click();
    await expect(selectAllCheckbox).toBeEnabled();
    await expect(checkbox0).toBeDisabled();
    await expect(checkbox1).toBeDisabled();
    await expect(checkbox2).toBeEnabled();
    await expect(checkbox3).toBeDisabled();
    await expect(checkbox4).toBeEnabled();
    await expect(selectAllCheckbox).toBeChecked();
    await expect(checkbox0).toBeChecked();
    await expect(checkbox1).toBeChecked();
    await expect(checkbox2).toBeChecked();
    await expect(checkbox3).toBeChecked();
    await expect(checkbox4).toBeChecked();
  });
});

test.describe("sheet/columnSelector/immediate/immediate.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/sheet/columnSelector/immediate/immediate.xhtml");
  });

  test("immediate=true, ajax=enabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateTrueAjaxEnabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:selectedCount'] span.form-control-plaintext");

    await reset.click();

    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("1");
    await expect(alert).not.toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("2");
    await expect(alert).not.toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("5");
    await expect(alert).not.toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();
  });

  test("immediate=true, ajax=disabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:selectedCount'] span.form-control-plaintext");
    const submit = page.locator("button[id='page:mainForm:submit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("0");
    await submit.click();
    await expect(selectedCount).toHaveText("1");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("1");
    await submit.click();
    await expect(selectedCount).toHaveText("2");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("2");
    await submit.click();
    await expect(selectedCount).toHaveText("5");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("5");
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("immediate=false, ajax=enabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateFalseAjaxEnabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:selectedCount'] span.form-control-plaintext");

    await reset.click();

    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("immediate=false, ajax=disabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateFalseAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:selectedCount'] span.form-control-plaintext");
    const submit = page.locator("button[id='page:mainForm:submit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("Save selected rows (Ajax)", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row1Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_1']");
    const row2Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const savedSelectedCount = page.locator("tobago-out[id='page:mainForm:selectedCountByAction'] span.form-control-plaintext");
    const saveSelectedRowsButton = page.locator(".tobago-button[id='page:mainForm:saveSelectedRowsAjax']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(savedSelectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await row1Checkbox.click();
    await row2Checkbox.click();
    await saveSelectedRowsButton.click();

    await expect(savedSelectedCount).toHaveText("3");
    await expect(alert).not.toBeVisible();
  });

  test("Save selected rows (Submit)", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    const row2Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_2']");
    const savedSelectedCount = page.locator("tobago-out[id='page:mainForm:selectedCountByAction'] span.form-control-plaintext");
    const saveSelectedRowsButton = page.locator(".tobago-button[id='page:mainForm:saveSelectedRowsSubmit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(savedSelectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await row2Checkbox.click();
    await saveSelectedRowsButton.click();

    await expect(savedSelectedCount).toHaveText("2");
    await expect(alert).not.toBeVisible();
  });
});

test.describe("sheet/columnSelector/immediate/immediate_compositeComponent.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/sheet/columnSelector/immediate/immediate_compositeComponent.xhtml");
  });

  test("immediate=true, ajax=enabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateTrueAjaxEnabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:compComp:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCount'] span.form-control-plaintext");

    await reset.click();

    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("1");
    await expect(alert).not.toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("2");
    await expect(alert).not.toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("5");
    await expect(alert).not.toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();
  });

  test("immediate=true, ajax=disabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:compComp:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCount'] span.form-control-plaintext");
    const submit = page.locator("button[id='page:mainForm:compComp:submit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("0");
    await submit.click();
    await expect(selectedCount).toHaveText("1");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("1");
    await submit.click();
    await expect(selectedCount).toHaveText("2");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("2");
    await submit.click();
    await expect(selectedCount).toHaveText("5");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("5");
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("immediate=false, ajax=enabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateFalseAjaxEnabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:compComp:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCount'] span.form-control-plaintext");

    await reset.click();

    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("immediate=false, ajax=disabled", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateFalseAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const toggleAll = sheet.locator("th[id='page:mainForm:compComp:sheet:colSelect'] input");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    const selectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCount'] span.form-control-plaintext");
    const submit = page.locator("button[id='page:mainForm:compComp:submit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await row4Orbit.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();

    await toggleAll.click();
    await submit.click();
    await expect(selectedCount).toHaveText("0");
    await expect(alert).toBeVisible();
  });

  test("Save selected rows (Ajax)", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row1Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_1']");
    const row2Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_2']");
    const savedSelectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCountByAction'] span.form-control-plaintext");
    const saveSelectedRowsButton = page.locator(".tobago-button[id='page:mainForm:compComp:saveSelectedRowsAjax']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(savedSelectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await row1Checkbox.click();
    await row2Checkbox.click();
    await saveSelectedRowsButton.click();

    await expect(savedSelectedCount).toHaveText("3");
    await expect(alert).not.toBeVisible();
  });

  test("Save selected rows (Submit)", async ({page}) => {
    const alert = page.locator("tobago-messages[id='page:messages'] .alert");
    const reset = page.locator(".tobago-button[id='page:mainForm:compComp:resetImmediateTrueAjaxDisabled']");
    const sheet = page.locator("tobago-sheet[id='page:mainForm:compComp:sheet']");
    const row0Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_0']");
    const row2Checkbox = sheet.locator("input[name='page:mainForm:compComp:sheet_data_row_selector_2']");
    const savedSelectedCount = page.locator("tobago-out[id='page:mainForm:compComp:selectedCountByAction'] span.form-control-plaintext");
    const saveSelectedRowsButton = page.locator(".tobago-button[id='page:mainForm:compComp:saveSelectedRowsSubmit']");

    await reset.click();
    await expect(sheet).toBeVisible();
    await expect(savedSelectedCount).toHaveText("0");
    await expect(alert).not.toBeVisible();

    await row0Checkbox.click();
    await row2Checkbox.click();
    await saveSelectedRowsButton.click();

    await expect(savedSelectedCount).toHaveText("2");
    await expect(alert).not.toBeVisible();
  });
});
