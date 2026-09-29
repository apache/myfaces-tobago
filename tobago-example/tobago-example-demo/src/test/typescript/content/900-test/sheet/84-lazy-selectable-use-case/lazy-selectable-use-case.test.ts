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

import {expect, Page, test} from "@playwright/test";

test.describe("900-test/sheet/84-lazy-selectable-use-case/lazy-selectable-use-case.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/84-lazy-selectable-use-case/lazy-selectable-use-case.xhtml");
  });

  test("TOBAGO-2349: Lazy sheet: tobago-behavior not initialized", async ({page}) => {
    const timestampElement = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    const ajaxButton = page.locator("[id='page:mainForm:ajaxButton']");
    const europaOut = page.locator("[id='page:mainForm:solar:18:rowAjax'] td tobago-out");
    const europaUpdateTimestamp = page.locator("[id='page:mainForm:solar:18:updateTimestamp']");
    const name = page.locator("[id='page:mainForm:name::field']");

    let timestampValue: number;

    await focusRowIndex(page, 30); //discovered area; visible rows 30 to 34; range 5 to 59
    await testLoadedState(page, true, 0, 87); //loaded to row 99, but max row is 87
    timestampValue = Number(await timestampElement.textContent());
    await ajaxButton.click();
    await expect.poll(async () => Number(await timestampElement.textContent())).toBeGreaterThan(timestampValue);
    await testLoadedState(page, true, 5, 69);
    await europaOut.click();
    await expect(name).toHaveValue("Europa");
    timestampValue = Number(await timestampElement.textContent());
    await europaUpdateTimestamp.click();
    await expect.poll(async () => Number(await timestampElement.textContent())).toBeGreaterThan(timestampValue);
  });

  async function testLoadedState(page: Page, loaded: boolean, startRow: number, endRow?: number): Promise<void> {
    if (endRow === undefined) {
      endRow = startRow;
    }
    for (let i = startRow; i <= endRow; i++) {
      const row = page.locator("tr[row-index='" + i + "']");
      if (loaded) {
        await expect(row).not.toHaveAttribute("dummy");
      } else {
        await expect(row).toHaveAttribute("dummy");
      }
    }
  }

  async function focusRowIndex(page: Page, rowIndex: number) {
    await page.locator("[id='page:mainForm:solar'] .tobago-body").evaluate((sheetBody, rowIndex) => {
      const row = sheetBody.querySelector("tr[row-index='" + rowIndex + "']") as HTMLTableRowElement;
      sheetBody.scrollTop = row.offsetTop;
    }, rowIndex);
  }
});
