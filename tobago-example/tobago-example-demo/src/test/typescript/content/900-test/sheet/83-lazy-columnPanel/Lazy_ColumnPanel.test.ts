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

test.describe("900-test/sheet/83-lazy-columnPanel/Lazy_ColumnPanel.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/83-lazy-columnPanel/Lazy_ColumnPanel.xhtml");
  });

  test("initial load", async ({page}) => {
    await testLoadedState(page, true, 0, 9);
    await testLoadedState(page, false, 10);
  });

  test("focus row index 200 and scroll up", async ({page}) => {
    await focusRowIndex(page, 200); //new area; 200 to 213 visible; range 195 to 218
    await testLoadedState(page, false, 194);
    await testLoadedState(page, true, 195, 218);
    await testLoadedState(page, false, 219);

    await focusRowIndex(page, 197); //discovered area; 197 to 200 visible; range 194 to 203
    await testLoadedState(page, false, 184);
    await testLoadedState(page, true, 185, 218);
    await testLoadedState(page, false, 219);
  });

  test("focus row index 300 and scroll down", async ({page}) => {
    await focusRowIndex(page, 300); //new area; 300 to 313 visible; range 295 to 318
    await testLoadedState(page, false, 294);
    await testLoadedState(page, true, 295, 318);
    await testLoadedState(page, false, 319);

    await focusRowIndex(page, 313); //discovered area; 313 to 316 visible; range 310 to 319
    await testLoadedState(page, false, 294);
    await testLoadedState(page, true, 295, 328);
    await testLoadedState(page, false, 329);
  });

  async function testLoadedState(page: Page, loaded: boolean, startRow: number, endRow?: number): Promise<void> {
    if (endRow === undefined) {
      endRow = startRow;
    }
    for (let i = startRow; i <= endRow; i++) {
      const row = page.locator("tr[row-index='" + i + "']");
      if (loaded) {
        await expect(row).not.toHaveAttribute("dummy");
        await expect(page.locator("tr[name='" + i + "'].tobago-column-panel")).toHaveCount(1);
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
