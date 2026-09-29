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

test.describe("900-test/sheet/90-sheet-in-sheet/Sheet_in_sheet.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/90-sheet-in-sheet/Sheet_in_sheet.xhtml");
  });

  test("there must be no 'tobago-auto-spacing'", async ({page}) => {
    await expect(page.locator("tobago-sheet .tobago-auto-spacing")).toHaveCount(0);
  });

  test("main sheet: select all; sun sub sheet: select Mercury; venus sub sheet: select earth", async ({page}) => {
    const selectAllCheckbox = page.locator("input[name='page:mainForm:mainSheet::columnSelector']");
    const sunSheetMercuryRow = page.locator("[id='page:mainForm:mainSheet:0:subSheet'] tr[row-index='1']");
    const venusSheetEarthRow = page.locator("[id='page:mainForm:mainSheet:2:subSheet'] tr[row-index='3']");
    const mainSheetHiddenSelectedField = page.locator("[id='page:mainForm:mainSheet::selected']");
    const sunSubSheetHiddenSelectedField = page.locator("[id='page:mainForm:mainSheet:0:subSheet::selected']");
    const venusSubSheetHiddenSelectedField = page.locator("[id='page:mainForm:mainSheet:2:subSheet::selected']");

    expect((await getSelection(mainSheetHiddenSelectedField)).size).toBe(0);
    expect((await getSelection(sunSubSheetHiddenSelectedField)).size).toBe(0);
    expect((await getSelection(venusSubSheetHiddenSelectedField)).size).toBe(0);

    await selectAllCheckbox.click();
    expect((await getSelection(mainSheetHiddenSelectedField)).size).toBe(5);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(0);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(1);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(2);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(3);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(4);
    expect((await getSelection(sunSubSheetHiddenSelectedField)).size).toBe(0);
    expect((await getSelection(venusSubSheetHiddenSelectedField)).size).toBe(0);

    await sunSheetMercuryRow.click();
    expect((await getSelection(sunSubSheetHiddenSelectedField)).size).toBe(1);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(0);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(1);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(2);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(3);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(4);
    expect((await getSelection(sunSubSheetHiddenSelectedField))).toContain(1);
    expect((await getSelection(venusSubSheetHiddenSelectedField)).size).toBe(0);

    await venusSheetEarthRow.click();
    expect((await getSelection(venusSubSheetHiddenSelectedField)).size).toBe(1);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(0);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(1);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(2);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(3);
    expect((await getSelection(mainSheetHiddenSelectedField))).toContain(4);
    expect((await getSelection(sunSubSheetHiddenSelectedField))).toContain(1);
    expect((await getSelection(venusSubSheetHiddenSelectedField))).toContain(3);
  });

  async function getSelection(hiddenSelectedField: Locator) {
    return new Set(JSON.parse(await hiddenSelectedField.inputValue()));
  }
});
