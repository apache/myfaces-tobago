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

test.describe("900-test/sheet/82-lazy-2/Lazy_2.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/82-lazy-2/Lazy_2.xhtml");
  });

  test("initial load", async ({page}) => {
    await testLoadedState(page, true, 0, 29);
    await testLoadedState(page, false, 30);
  });

  test("focus row index 500 and scroll up", async ({page}) => {
    const row494 = page.locator("tr[row-index='494']");
    const row500 = page.locator("tr[row-index='500']");

    await focusRowIndex(page, 500); //new area; rows 500 to 513 visible; range 485 to 528
    await expect(row500).toBeVisible();
    await testLoadedState(page, false, 484);
    await testLoadedState(page, true, 485, 528);
    await testLoadedState(page, false, 529);

    await focusRowIndex(page, 494); //discovered area: 494 to 504 visible; range 484 to 514
    await expect(row494).toBeVisible();
    await testLoadedState(page, false, 454);
    await testLoadedState(page, true, 455, 528);
    await testLoadedState(page, false, 529);
  });

  test("scroll down, select row 1 and 40, then press 'Period'", async ({page}) => {
    const row1Name = page.locator("tbody tr[row-index='1'] td tobago-out[id$=':name']");
    const row30 = page.locator("tbody tr[row-index='30']");
    const row40Name = page.locator("tbody tr[row-index='40'] td tobago-out[id$=':name']");
    const selectedInput = page.locator("[id='page:mainForm:sheet::selected']");
    const periodButton = page.locator("[id='page:mainForm:sheet:0:period']");
    const selectedRows = page.locator("[id='page:mainForm:selectedRows'] .form-control-plaintext");
    const actionCountOut = page.locator("[id='page:mainForm:actionCount'] .form-control-plaintext");
    const actionListenerCountOut = page.locator("[id='page:mainForm:actionListenerCount'] .form-control-plaintext");

    await focusRowIndex(page, 12); //discovered area: rows 12 to 21 visible; range 2 to 31
    await expect(row30).toBeVisible();
    await testLoadedState(page, true, 0, 59);
    await testLoadedState(page, false, 60);
    await row1Name.click();
    await expect(selectedInput).toHaveValue("[1]");
    await row40Name.click();
    await expect(selectedInput).toHaveValue("[1,40]");
    await periodButton.click();
    await expect(selectedRows).toHaveText("[1, 40]");
    await expect(actionCountOut).toHaveText("1");
    await expect(actionListenerCountOut).toHaveText("1");
  });

  test("focus row index 800, press 'Ajax'", async ({page}) => {
    const ajax = page.locator("[id='page:mainForm:ajax']");
    const row800 = page.locator("tbody tr[row-index='800']");
    const actionCountOut = page.locator("[id='page:mainForm:actionCount'] .form-control-plaintext");

    await focusRowIndex(page, 800); //new area: rows 800 to 813 visible; range 785 to 828 (+-15)
    await expect(row800).toBeVisible();
    await testLoadedState(page, false, 784);
    await testLoadedState(page, true, 785, 828);
    await testLoadedState(page, false, 829);

    await expect(actionCountOut).toHaveText("0");
    await ajax.click();
    await expect(actionCountOut).toHaveText("1");
    await testLoadedState(page, false, 784);
    await testLoadedState(page, true, 785, 829); //preloaded
    await testLoadedState(page, false, 830);
    await expect(row800).toBeVisible();
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
