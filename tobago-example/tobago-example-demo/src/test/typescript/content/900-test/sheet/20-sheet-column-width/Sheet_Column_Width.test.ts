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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/sheet/20-sheet-column-width/Sheet_Column_Width.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/20-sheet-column-width/Sheet_Column_Width.xhtml");
  });

  test("Column width index", async ({page}) => {
    const hideButton = page.locator("[id='page:mainForm:hideColumn']");
    const showColumn = page.locator("[id='page:mainForm:showColumn']");
    const columnHeads = page.locator("[id='page:mainForm:columnRenderedFalse'] header table thead tr th");

    await expect(page.locator("[id='page:searchForm:search::field']")).toBeFocused();
    await expect(columnHeads).toHaveCount(5);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "200px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "300px");

    await hideButton.click();
    await expect(columnHeads).toHaveCount(4);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "300px");

    await resize(columnHeads.nth(0), +5);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "105px");
    await resize(columnHeads.nth(1), +5);
    await expect(columnHeads).toHaveCount(4);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "105px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "305px");

    await showColumn.click();
    await expect(columnHeads).toHaveCount(5);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "105px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "200px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "305px");

    await resize(columnHeads.nth(0), +10);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "115px");
    await resize(columnHeads.nth(1), +10);
    await expect(columnHeads.nth(1)).toHaveCSS("width", "210px");
    await resize(columnHeads.nth(2), +10);
    await expect(columnHeads).toHaveCount(5);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "115px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "210px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "315px");

    await hideButton.click();
    await expect(columnHeads).toHaveCount(4);
    await expect(columnHeads.nth(0)).toHaveCSS("width", "115px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "315px");
  });

  test("Only pixel values set", async ({page}) => {
    const columnHeads = page.locator("[id='page:mainForm:pixelOnly'] header table thead tr th");
    await expect(columnHeads.nth(0)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "20px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "30px");
  });

  test("Only percent values set for a 1000px-sheet", async ({page}) => {
    const columnHeads = page.locator("[id='page:mainForm:percentOnly'] header table thead tr th");
    await expect(columnHeads.nth(0)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "200px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "300px");
  });

  test("Only auto value set for a 900px-sheet", async ({page}) => {
    const columnHeads = page.locator("[id='page:mainForm:autoOnly'] header table thead tr th");
    await expect(columnHeads.nth(0)).toHaveCSS("width", "300px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "300px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "300px");
  });

  test("'100px 20% 4fr 1fr auto' for a 1000px-sheet", async ({page}) => {
    const columnHeads = page.locator("[id='page:mainForm:fixFr100px20p4fr1frAuto'] header table thead tr th");
    await expect(columnHeads.nth(0)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "200px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "400px");
    await expect(columnHeads.nth(3)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(4)).toHaveCSS("width", "200px");
  });

  test("'150px 20% 4fr 1fr' for a 1000px-sheet", async ({page}) => {
    const columnHeads = page.locator("[id='page:mainForm:fixFr150px20p4fr1fr'] header table thead tr th");
    await expect(columnHeads.nth(0)).toHaveCSS("width", "150px");
    await expect(columnHeads.nth(1)).toHaveCSS("width", "200px");
    await expect(columnHeads.nth(2)).toHaveCSS("width", "400px");
    await expect(columnHeads.nth(3)).toHaveCSS("width", "100px");
    await expect(columnHeads.nth(4)).toHaveCSS("width", "150px");
  });

  test("There must be no horizontal scrollbar", async ({page}) => {
    const body = page.locator("[id='page:mainForm:testSheetColumnWidth'] .tobago-body");
    expect(await body.evaluate(e => e.clientWidth >= e.scrollWidth)).toBeTruthy();
  });

  async function resize(columnHead: Locator, movePx: number) {
    const page = columnHead.page();
    const resizeElement = columnHead.locator(".tobago-resize");

    const box = await resizeElement.boundingBox();
    if (!box) {
      throw new Error("Resize handle has no bounding box");
    }
    const x = box.x + (box.width / 2);
    const y = box.y + (box.height / 2);

    await expect(resizeElement).toBeVisible();
    await page.mouse.move(x, y);
    await page.mouse.down();
    await page.mouse.move(x + movePx, y);
    await page.mouse.up();
  }
});
