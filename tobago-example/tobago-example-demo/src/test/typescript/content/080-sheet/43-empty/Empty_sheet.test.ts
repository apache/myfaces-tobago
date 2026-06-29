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

import {expect, test} from "@playwright/test";

test.describe("080-sheet/43-empty/Empty_sheet.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/43-empty/Empty_sheet.xhtml");
  });

  test("Default: No selection of empty-row-message", async ({page}) => {
    const noEntriesRow = page.locator("[id='page:mainForm:default'] .tobago-no-entries");

    await page.evaluate(() => {
      (window as any).__rowSelectionChangeCount = 0;
      const sheet = document.querySelector("[id='page:mainForm:default']")!;
      sheet.addEventListener("tobago.sheet.rowSelectionChange", () => {
        (window as any).__rowSelectionChangeCount++;
      });
    });
    await page.evaluate(() => {
      (window as any).__noEntriesRowClickCount = 0;
      const noEntriesRow = document.querySelector("[id='page:mainForm:default'] .tobago-no-entries")!;
      noEntriesRow.addEventListener("click", () => {
        (window as any).__noEntriesRowClickCount++;
      });
    });

    await noEntriesRow.click();

    const noEntriesRowClickCount = await page.evaluate(() => (window as any).__noEntriesRowClickCount);
    expect(noEntriesRowClickCount).toBe(1);
    const rowSelectionChangeCount = await page.evaluate(() => (window as any).__rowSelectionChangeCount);
    expect(rowSelectionChangeCount).toBe(0);
  });

  test("Custom: No selection of empty-row-message", async ({page}) => {
    const noEntriesRow = page.locator("[id='page:mainForm:facet'] .tobago-no-entries");

    await page.evaluate(() => {
      (window as any).__rowSelectionChangeCount = 0;
      const sheet = document.querySelector("[id='page:mainForm:facet']")!;
      sheet.addEventListener("tobago.sheet.rowSelectionChange", () => {
        (window as any).__rowSelectionChangeCount++;
      });
    });
    await page.evaluate(() => {
      (window as any).__noEntriesRowClickCount = 0;
      const noEntriesRow = document.querySelector("[id='page:mainForm:facet'] .tobago-no-entries")!;
      noEntriesRow.addEventListener("click", () => {
        (window as any).__noEntriesRowClickCount++;
      });
    });

    await noEntriesRow.click();

    const noEntriesRowClickCount = await page.evaluate(() => (window as any).__noEntriesRowClickCount);
    expect(noEntriesRowClickCount).toBe(1);
    const rowSelectionChangeCount = await page.evaluate(() => (window as any).__rowSelectionChangeCount);
    expect(rowSelectionChangeCount).toBe(0);
  });
});
