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

test.describe("900-test/sheet/80-lazy-sort/Lazy_Sort.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/80-lazy-sort/Lazy_Sort.xhtml");
  });

  test("focus row index 25 and scroll up", async ({page}) => {
    const submit = page.locator("[id='page:mainForm:submit']");

    await testLoadedState(page, false, 100);
    await focusRowIndex(page, 100);
    await testLoadedState(page, true, 100);
    await submit.click();
    await testLoadedState(page, true, 100);
    await testLoadedState(page, false, 15);

    await focusRowIndex(page, 15);
    await testLoadedState(page, false, 4);
    await testLoadedState(page, true, 5, 38);
    await testLoadedState(page, false, 39);

    await focusRowIndex(page, 8); //visible rows: 8 to 17 (known area)
    await testLoadedState(page, true, 0, 38);
    await testLoadedState(page, false, 39);
  });

  test("focus row index 9970 (max row index is 9999) and scroll down", async ({page}) => {
    await focusRowIndex(page, 9970); //visible rows: 9970 to 9983 (new area)
    await testLoadedState(page, false, 9959);
    await testLoadedState(page, true, 9960, 9993);
    await testLoadedState(page, false, 9994);

    await focusRowIndex(page, 9980); //visible rows: 9980 to 9989 (known area)
    await testLoadedState(page, false, 9959);
    await testLoadedState(page, true, 9960, 9999);
  });

  test("focus row index 500 and go up", async ({page}) => {
    await focusRowIndex(page, 500); //visible rows: 500 to 513 (new area)
    await testLoadedState(page, false, 489);
    await testLoadedState(page, true, 490, 523);
    await testLoadedState(page, false, 524);

    await focusRowIndex(page, 494); //visible rows: 494 to 503 (known area)
    await testLoadedState(page, false, 469);
    await testLoadedState(page, true, 470, 523);
    await testLoadedState(page, false, 524);
  });

  test("focus row index 1000 and go down", async ({page}) => {
    await focusRowIndex(page, 1000); //visible rows: 1000 to 1013 (new area)
    await testLoadedState(page, false, 989);
    await testLoadedState(page, true, 990, 1023);
    await testLoadedState(page, false, 1024);

    await focusRowIndex(page, 1010); //visible rows: 1010 to 1019 (known area)
    await testLoadedState(page, false, 989);
    await testLoadedState(page, true, 990, 1043);
    await testLoadedState(page, false, 1044);
  });

  test("focus row index 1500 and sort name asc, focus row index 2000 and sort desc", async ({page}) => {
    const nameColumn = page.locator("[id='page:mainForm:sheet:nameColumn_sorter']");
    const nameOutputs = page.locator("tr tobago-out[id$=':name']");

    await focusRowIndex(page, 1500);
    await testLoadedState(page, false, 1489);
    await testLoadedState(page, true, 1490, 1523);
    await testLoadedState(page, false, 1524);

    await nameColumn.click();
    await expect(nameColumn).toContainClass("tobago-ascending");
    await testLoadedState(page, false, 1489);
    await testLoadedState(page, true, 1490, 1519); //preloaded from server
    await testLoadedState(page, false, 1520);
    await expect(nameOutputs).toHaveCount(30);
    await expect(nameOutputs.nth(0)).not.toHaveAttribute("id", "page:mainForm:sheet:0:name");

    await focusRowIndex(page, 2000); //visible rows: 2000 to 2013 (new area)
    await testLoadedState(page, false, 1989);
    await testLoadedState(page, true, 1990, 2023);
    await testLoadedState(page, false, 2024);

    await nameColumn.click();
    await expect(nameColumn).toContainClass("tobago-descending");
    await testLoadedState(page, false, 1989);
    await testLoadedState(page, true, 1990, 2019); //preloaded from server
    await testLoadedState(page, false, 2020);
    await expect(nameOutputs).toHaveCount(30);
    await expect(nameOutputs.nth(0)).not.toHaveAttribute("id", "page:mainForm:sheet:0:name");
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
