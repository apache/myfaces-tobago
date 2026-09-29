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

test.describe("900-test/sheet/91-sheet-in-tab/Sheet_in_tab.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/91-sheet-in-tab/Sheet_in_tab.xhtml");
  });

  test("Tab with working sheet", async ({page}) => {
    const tabGroupIndex = page.locator("[id='page:mainForm:tabGroup::index']");
    const navLink = page.locator("[id='page:mainForm:tabWithWorkingSheet'] .nav-link");
    const cols = page.locator("[id='page:mainForm:tabWithWorkingSheet:sheetWorking'] header colgroup col");
    const firstTh = page.locator("[id='page:mainForm:tabWithWorkingSheet:sheetWorking'] header tr th").first();
    const firstTd = page.locator("[id='page:mainForm:tabWithWorkingSheet:sheetWorking'] .tobago-body tbody tr td").first();

    await navLink.click();
    await expect(tabGroupIndex).toHaveValue("0");

    const col0Width = (await cols.nth(0).boundingBox())?.width;
    const col1Width = (await cols.nth(1).boundingBox())?.width;
    const col2Width = (await cols.nth(2).boundingBox())?.width;
    const firstThWidth = (await firstTh.boundingBox())?.width;
    const firstTdWidth = (await firstTd.boundingBox())?.width;

    await expect(col0Width).toBeCloseTo(col1Width, 1);
    await expect(col0Width).toBeCloseTo(col2Width, 1);
    await expect(firstThWidth).toBe(firstTdWidth);
  });

  test("Tab with sheet", async ({page}) => {
    const tabGroupIndex = page.locator("[id='page:mainForm:tabGroup::index']");
    const navLink = page.locator("[id='page:mainForm:tabWithSheet'] .nav-link");
    const cols = page.locator("[id='page:mainForm:tabWithSheet:sheet'] header colgroup col");
    const firstTh = page.locator("[id='page:mainForm:tabWithSheet:sheet'] header tr th").first();
    const firstTd = page.locator("[id='page:mainForm:tabWithSheet:sheet'] .tobago-body tbody tr td").first();

    await navLink.click();
    await expect(tabGroupIndex).toHaveValue("1");

    const col0Width = (await cols.nth(0).boundingBox())?.width;
    const col1Width = (await cols.nth(1).boundingBox())?.width;
    const col2Width = (await cols.nth(2).boundingBox())?.width;
    const firstThWidth = (await firstTh.boundingBox())?.width;
    const firstTdWidth = (await firstTd.boundingBox())?.width;

    await expect(col0Width).toBeCloseTo(col1Width, 1);
    await expect(col0Width).toBeCloseTo(col2Width, 1);
    await expect(firstThWidth).toBe(firstTdWidth);
  });

  test("Tab with lazy sheet", async ({page}) => {
    const tabGroupIndex = page.locator("[id='page:mainForm:tabGroup::index']");
    const navLink = page.locator("[id='page:mainForm:tabWithLazySheet'] .nav-link");
    const cols = page.locator("[id='page:mainForm:tabWithLazySheet:lazySheet'] header colgroup col");
    const firstTh = page.locator("[id='page:mainForm:tabWithLazySheet:lazySheet'] header tr th").first();
    const firstTd = page.locator("[id='page:mainForm:tabWithLazySheet:lazySheet'] .tobago-body tbody tr td").first();

    await navLink.click();
    await expect(tabGroupIndex).toHaveValue("2");

    const col0Width = (await cols.nth(0).boundingBox())?.width;
    const col1Width = (await cols.nth(1).boundingBox())?.width;
    const col2Width = (await cols.nth(2).boundingBox())?.width;
    const firstThWidth = (await firstTh.boundingBox())?.width;
    const firstTdWidth = (await firstTd.boundingBox())?.width;

    await expect(col0Width).toBeCloseTo(col1Width, 1);
    await expect(col0Width).toBeCloseTo(col2Width, 1);
    await expect(firstThWidth).toBe(firstTdWidth);
  });
});
