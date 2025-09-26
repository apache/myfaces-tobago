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

test.describe("900-test/3000-sheet/38-column-selector/ajax/ajax.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("http://localhost:8080/content/900-test/3000-sheet/38-column-selector/ajax/ajax.xhtml");
  });

  test("Select row 0, row 4, select all, deselect all", async ({page}) => {
    const reset = page.locator(".tobago-button[id='page:mainForm:reset']");
    await reset.click();

    const sheet = page.locator("tobago-sheet[id='page:mainForm:sheet']");
    await expect(sheet).toBeVisible();
    const selectedCount = page.locator("tobago-out[id='page:mainForm:selectedCount'] span.form-control-plaintext");
    await expect(selectedCount).toHaveText("0");

    const row0Checkbox = sheet.locator("input[name='page:mainForm:sheet_data_row_selector_0']");
    await row0Checkbox.click();
    await expect(selectedCount).toHaveText("1");

    const row4Orbit = sheet.locator("tr[row-index='4'] td:nth-child(3) tobago-out");
    await row4Orbit.click();
    await expect(selectedCount).toHaveText("2");

    const toggleAll = sheet.locator("th[id='page:mainForm:sheet:colSelect::columnSelectorToggle'] input");
    await toggleAll.click();
    await expect(selectedCount).toHaveText("5");

    await toggleAll.click();
    await expect(selectedCount).toHaveText("0");
  });
});
