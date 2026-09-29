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

test.describe("900-test/sheet/08-scrollbar-filler/Scrollbar_Filler.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/08-scrollbar-filler/Scrollbar_Filler.xhtml");
  });

  test("Markup: none", async ({page}) => {
    const sheetHeaderCell = page.locator("[id='page:mainForm:sheetMarkupNone'] header th").first();
    const scrollbarFiller = page.locator("[id='page:mainForm:sheetMarkupNone'] .tobago-scrollbar-filler");
    const sheetHeaderCellStyle = await sheetHeaderCell.evaluate((el) => getComputedStyle(el));
    const scrollbarFillerStyle = await scrollbarFiller.evaluate((el) => getComputedStyle(el));

    expect(sheetHeaderCellStyle.backgroundColor).toBe(scrollbarFillerStyle.backgroundColor);

    expect(sheetHeaderCellStyle.borderTopWidth).toBe(scrollbarFillerStyle.borderTopWidth);
    expect(sheetHeaderCellStyle.borderLeftWidth).toBe(scrollbarFillerStyle.borderLeftWidth);
    expect(sheetHeaderCellStyle.borderRightWidth).toBe(scrollbarFillerStyle.borderRightWidth);
    expect(sheetHeaderCellStyle.borderBottomWidth).toBe(scrollbarFillerStyle.borderBottomWidth);

    expect(sheetHeaderCellStyle.borderTopStyle).toBe(scrollbarFillerStyle.borderTopStyle);
    expect(sheetHeaderCellStyle.borderLeftStyle).toBe(scrollbarFillerStyle.borderLeftStyle);
    expect(sheetHeaderCellStyle.borderRightStyle).toBe(scrollbarFillerStyle.borderRightStyle);
    expect(sheetHeaderCellStyle.borderBottomStyle).toBe(scrollbarFillerStyle.borderBottomStyle);

    expect(sheetHeaderCellStyle.borderTopColor).toBe(scrollbarFillerStyle.borderTopColor);
    expect(sheetHeaderCellStyle.borderLeftColor).toBe(scrollbarFillerStyle.borderLeftColor);
    expect(sheetHeaderCellStyle.borderRightColor).toBe(scrollbarFillerStyle.borderRightColor);
    expect(sheetHeaderCellStyle.borderBottomColor).toBe(scrollbarFillerStyle.borderBottomColor);
  });

  test("Markup: bordered", async ({page}) => {
    const sheetHeaderRow = page.locator("[id='page:mainForm:sheetMarkupBordered'] header tr");
    const sheetHeaderCell = page.locator("[id='page:mainForm:sheetMarkupBordered'] header th").first();
    const scrollbarFiller = page.locator("[id='page:mainForm:sheetMarkupBordered'] .tobago-scrollbar-filler");
    const sheetHeaderRowStyle = await sheetHeaderRow.evaluate((el) => getComputedStyle(el));
    const sheetHeaderCellStyle = await sheetHeaderCell.evaluate((el) => getComputedStyle(el));
    const scrollbarFillerStyle = await scrollbarFiller.evaluate((el) => getComputedStyle(el));

    expect(sheetHeaderCellStyle.backgroundColor).toBe(scrollbarFillerStyle.backgroundColor);

    expect(sheetHeaderRowStyle.borderTopWidth).toBe(scrollbarFillerStyle.borderTopWidth);
    expect(sheetHeaderRowStyle.borderBottomWidth).toBe(scrollbarFillerStyle.borderBottomWidth);
    expect(sheetHeaderCellStyle.borderLeftWidth).toBe(scrollbarFillerStyle.borderLeftWidth);
    expect(sheetHeaderCellStyle.borderRightWidth).toBe(scrollbarFillerStyle.borderRightWidth);

    expect(sheetHeaderRowStyle.borderTopStyle).toBe(scrollbarFillerStyle.borderTopStyle);
    expect(sheetHeaderRowStyle.borderBottomStyle).toBe(scrollbarFillerStyle.borderBottomStyle);
    expect(sheetHeaderCellStyle.borderLeftStyle).toBe(scrollbarFillerStyle.borderLeftStyle);
    expect(sheetHeaderCellStyle.borderRightStyle).toBe(scrollbarFillerStyle.borderRightStyle);

    expect(sheetHeaderRowStyle.borderTopColor).toBe(scrollbarFillerStyle.borderTopColor);
    expect(sheetHeaderRowStyle.borderBottomColor).toBe(scrollbarFillerStyle.borderBottomColor);
    expect(sheetHeaderCellStyle.borderLeftColor).toBe(scrollbarFillerStyle.borderLeftColor);
    expect(sheetHeaderCellStyle.borderRightColor).toBe(scrollbarFillerStyle.borderRightColor);
  });

  test("Markup: dark", async ({page}) => {
    const sheetHeaderCell = page.locator("[id='page:mainForm:sheetMarkupDark'] header th").first();
    const scrollbarFiller = page.locator("[id='page:mainForm:sheetMarkupDark'] .tobago-scrollbar-filler");
    const sheetHeaderCellStyle = await sheetHeaderCell.evaluate((el) => getComputedStyle(el));
    const scrollbarFillerStyle = await scrollbarFiller.evaluate((el) => getComputedStyle(el));

    expect(sheetHeaderCellStyle.backgroundColor).toBe(scrollbarFillerStyle.backgroundColor);

    expect(sheetHeaderCellStyle.borderTopWidth).toBe(scrollbarFillerStyle.borderTopWidth);
    expect(sheetHeaderCellStyle.borderLeftWidth).toBe(scrollbarFillerStyle.borderLeftWidth);
    expect(sheetHeaderCellStyle.borderRightWidth).toBe(scrollbarFillerStyle.borderRightWidth);
    expect(sheetHeaderCellStyle.borderBottomWidth).toBe(scrollbarFillerStyle.borderBottomWidth);

    expect(sheetHeaderCellStyle.borderTopStyle).toBe(scrollbarFillerStyle.borderTopStyle);
    expect(sheetHeaderCellStyle.borderLeftStyle).toBe(scrollbarFillerStyle.borderLeftStyle);
    expect(sheetHeaderCellStyle.borderRightStyle).toBe(scrollbarFillerStyle.borderRightStyle);
    expect(sheetHeaderCellStyle.borderBottomStyle).toBe(scrollbarFillerStyle.borderBottomStyle);

    expect(sheetHeaderCellStyle.borderTopColor).toBe(scrollbarFillerStyle.borderTopColor);
    expect(sheetHeaderCellStyle.borderLeftColor).toBe(scrollbarFillerStyle.borderLeftColor);
    expect(sheetHeaderCellStyle.borderRightColor).toBe(scrollbarFillerStyle.borderRightColor);
    expect(sheetHeaderCellStyle.borderBottomColor).toBe(scrollbarFillerStyle.borderBottomColor);
  });

  test("Markup: dark bordered", async ({page}) => {
    const sheetHeaderRow = page.locator("[id='page:mainForm:sheetMarkupDarkBordered'] header tr");
    const sheetHeaderCell = page.locator("[id='page:mainForm:sheetMarkupDarkBordered'] header th").first();
    const scrollbarFiller = page.locator("[id='page:mainForm:sheetMarkupDarkBordered'] .tobago-scrollbar-filler");
    const sheetHeaderRowStyle = await sheetHeaderRow.evaluate((el) => getComputedStyle(el));
    const sheetHeaderCellStyle = await sheetHeaderCell.evaluate((el) => getComputedStyle(el));
    const scrollbarFillerStyle = await scrollbarFiller.evaluate((el) => getComputedStyle(el));

    expect(sheetHeaderCellStyle.backgroundColor).toBe(scrollbarFillerStyle.backgroundColor);

    expect(sheetHeaderRowStyle.borderTopWidth).toBe(scrollbarFillerStyle.borderTopWidth);
    expect(sheetHeaderRowStyle.borderBottomWidth).toBe(scrollbarFillerStyle.borderBottomWidth);
    expect(sheetHeaderCellStyle.borderLeftWidth).toBe(scrollbarFillerStyle.borderLeftWidth);
    expect(sheetHeaderCellStyle.borderRightWidth).toBe(scrollbarFillerStyle.borderRightWidth);

    expect(sheetHeaderRowStyle.borderTopStyle).toBe(scrollbarFillerStyle.borderTopStyle);
    expect(sheetHeaderRowStyle.borderBottomStyle).toBe(scrollbarFillerStyle.borderBottomStyle);
    expect(sheetHeaderCellStyle.borderLeftStyle).toBe(scrollbarFillerStyle.borderLeftStyle);
    expect(sheetHeaderCellStyle.borderRightStyle).toBe(scrollbarFillerStyle.borderRightStyle);

    expect(sheetHeaderRowStyle.borderTopColor).toBe(scrollbarFillerStyle.borderTopColor);
    expect(sheetHeaderRowStyle.borderBottomColor).toBe(scrollbarFillerStyle.borderBottomColor);
    expect(sheetHeaderCellStyle.borderLeftColor).toBe(scrollbarFillerStyle.borderLeftColor);
    expect(sheetHeaderCellStyle.borderRightColor).toBe(scrollbarFillerStyle.borderRightColor);
  });
});
