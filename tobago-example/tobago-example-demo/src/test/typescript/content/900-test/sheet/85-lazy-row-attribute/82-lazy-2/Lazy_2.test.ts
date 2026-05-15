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

test.describe("900-test/sheet/85-lazy-row-attribute/82-lazy-2/Lazy_2.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/85-lazy-row-attribute/82-lazy-2/Lazy_2.xhtml");
  });

  test("initial load", async ({page}) => {
    await testLoadedState(page, true, 0, 49);
    await testLoadedState(page, false, 50);
  });

  test("focus row index 500 and scroll up", async ({page}) => {
    const row474 = page.locator("tbody tr[row-index='474']");
    const row500 = page.locator("tbody tr[row-index='500']");

    await focusRowIndex(page, 500); //new area; visible 500 to 513; range 475 to 538
    await expect(row500).toBeVisible();
    await testLoadedState(page, false, 474);
    await testLoadedState(page, true, 475, 538);
    await testLoadedState(page, false, 539);

    await focusRowIndex(page, 493); //discovered area; visible 493 to 503; range: 474 to 523
    await expect(row474).toBeVisible();
    await testLoadedState(page, false, 424);
    await testLoadedState(page, true, 425, 538);
    await testLoadedState(page, false, 539);
  });

  test("focus row index 20, select row 1 and 60, then press 'Period'", async ({page}) => {
    const row1Name = page.locator("tbody tr[row-index='1'] td tobago-out[id$=':name']");
    const row50 = page.locator("tbody tr[row-index='50']");
    const row60Name = page.locator("tbody tr[row-index='60'] td tobago-out[id$=':name']");
    const selectedInput = page.locator("[id='page:mainForm:sheet::selected']");
    const periodButton = page.locator("[id='page:mainForm:sheet:0:period']");
    const selectedRows = page.locator("[id='page:mainForm:selectedRows'] .form-control-plaintext");
    const actionCountOut = page.locator("[id='page:mainForm:actionCount'] .form-control-plaintext");
    const actionListenerCountOut = page.locator("[id='page:mainForm:actionListenerCount'] .form-control-plaintext");

    await focusRowIndex(page, 23); //visible: 23 to 33; range: 4 to 53;
    await expect(row50).toBeVisible();
    await testLoadedState(page, true, 0, 99);
    await testLoadedState(page, false, 100);
    await row1Name.click();
    await expect(selectedInput).toHaveValue("[1]");
    await row60Name.click();
    await expect(selectedInput).toHaveValue("[1,60]");
    await periodButton.click();
    await expect(selectedRows).toHaveText("[1, 60]");
    await expect(actionCountOut).toHaveText("1");
    await expect(actionListenerCountOut).toHaveText("1");
  });

  test("focus row index 800, press 'Ajax'", async ({page}) => {
    const ajax = page.locator("[id='page:mainForm:ajax']");
    const reset = page.locator("[id='page:mainForm:reset']");
    const row800 = page.locator("tbody tr[row-index='800']");
    const actionCountOut = page.locator("[id='page:mainForm:actionCount'] .form-control-plaintext");

    await focusRowIndex(page, 800); //new area; visible 800 to 813; range 775 to 838
    await expect(row800).toBeVisible();
    await testLoadedState(page, false, 774);
    await testLoadedState(page, true, 775, 838);
    await testLoadedState(page, false, 839);

    await expect(actionCountOut).toHaveText("0");
    await ajax.click();
    await expect(actionCountOut).toHaveText("1");
    await testLoadedState(page, false, 774);
    await testLoadedState(page, true, 775, 849);
    await testLoadedState(page, false, 850);
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
