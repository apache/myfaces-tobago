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

import {expect, Page, test} from "@playwright/test";

test.describe("900-test/sheet/85-lazy-row-attribute/lazy-row-attribute.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/85-lazy-row-attribute/lazy-row-attribute.xhtml");
  });

  test("focus row index 200 and scroll up", async ({page}) => {
    await focusRowIndex(page, 200); //new area; visible: 200 to 213; range: 175 to 238
    await testLoadedState(page, false, 174);
    await testLoadedState(page, true, 175, 238);
    await testLoadedState(page, false, 239);

    await focusRowIndex(page, 189); //discovered area; visible 189 to 202; range: 174 to 217
    await testLoadedState(page, false, 124);
    await testLoadedState(page, true, 125, 238);
    await testLoadedState(page, false, 239);
  });

  test("focus row index 400 and scroll down", async ({page}) => {
    await focusRowIndex(page, 400); //new area; visible: 400 to 413; range: 375 to 438
    await testLoadedState(page, false, 374);
    await testLoadedState(page, true, 375, 438);
    await testLoadedState(page, false, 439);

    await focusRowIndex(page, 411); //discovered area; visible 411 to 424; range: 396 to 439
    await testLoadedState(page, false, 374);
    await testLoadedState(page, true, 375, 488);
    await testLoadedState(page, false, 489);
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
    await page.locator("[id='page:mainForm:sheet'] .tobago-body").evaluate((sheetBody, rowIndex) => {
      const row = sheetBody.querySelector("tr[row-index='" + rowIndex + "']") as HTMLTableRowElement;
      sheetBody.scrollTop = row.offsetTop;
    }, rowIndex);
  }
});
