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
import {goto} from "./base/Functions";

test.describe("sheet/columnSelector/ajax/ajax.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await goto(test, page, testInfo, "/content/900-test/sheet/columnSelector/ajax/ajax.xhtml");
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
});
