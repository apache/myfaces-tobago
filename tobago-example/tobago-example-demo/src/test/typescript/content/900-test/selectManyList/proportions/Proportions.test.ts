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

test.describe("900-test/selectManyList/proportions/Proportions.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/proportions/Proportions.xhtml");
  });

  test("Compare to height of tc:in", async ({page}) => {
    const inSmallStyle = await page.locator("[id='page:mainForm:inSmall']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceSmallStyle = await page.locator("[id='page:mainForm:selectOneChoiceSmall']").evaluate((el) => getComputedStyle(el));
    const selectOneListSmallStyle = await page.locator("[id='page:mainForm:selectOneListSmall']").evaluate((el) => getComputedStyle(el));
    const selectManyListSmallStyle = await page.locator("[id='page:mainForm:selectManyListSmall']").evaluate((el) => getComputedStyle(el));
    const inNormalStyle = await page.locator("[id='page:mainForm:inNormal']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceNormalStyle = await page.locator("[id='page:mainForm:selectOneChoiceNormal']").evaluate((el) => getComputedStyle(el));
    const selectOneListNormalStyle = await page.locator("[id='page:mainForm:selectOneListNormal']").evaluate((el) => getComputedStyle(el));
    const selectManyListNormalStyle = await page.locator("[id='page:mainForm:selectManyListNormal']").evaluate((el) => getComputedStyle(el));
    const inLargeStyle = await page.locator("[id='page:mainForm:inLarge']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceLargeStyle = await page.locator("[id='page:mainForm:selectOneChoiceLarge']").evaluate((el) => getComputedStyle(el));
    const selectOneListLargeStyle = await page.locator("[id='page:mainForm:selectOneListLarge']").evaluate((el) => getComputedStyle(el));
    const selectManyListLargeStyle = await page.locator("[id='page:mainForm:selectManyListLarge']").evaluate((el) => getComputedStyle(el));

    expect(selectOneChoiceSmallStyle.height).toBe(inSmallStyle.height);
    expect(selectOneListSmallStyle.height).toBe(inSmallStyle.height);
    expect(selectManyListSmallStyle.height).toBe(inSmallStyle.height);
    expect(selectOneChoiceNormalStyle.height).toBe(inNormalStyle.height);
    expect(selectOneListNormalStyle.height).toBe(inNormalStyle.height);
    expect(selectManyListNormalStyle.height).toBe(inNormalStyle.height);
    expect(selectOneChoiceLargeStyle.height).toBe(inLargeStyle.height);
    expect(selectOneListLargeStyle.height).toBe(inLargeStyle.height);
    expect(selectManyListLargeStyle.height).toBe(inLargeStyle.height);
  });

  test("Compare to height of tc:selectOneChoice", async ({page}) => {
    const inSmallStyle = await page.locator("[id='page:mainForm:inSmall']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceSmallStyle = await page.locator("[id='page:mainForm:selectOneChoiceSmall']").evaluate((el) => getComputedStyle(el));
    const selectOneListSmallStyle = await page.locator("[id='page:mainForm:selectOneListSmall']").evaluate((el) => getComputedStyle(el));
    const selectManyListSmallStyle = await page.locator("[id='page:mainForm:selectManyListSmall']").evaluate((el) => getComputedStyle(el));
    const inNormalStyle = await page.locator("[id='page:mainForm:inNormal']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceNormalStyle = await page.locator("[id='page:mainForm:selectOneChoiceNormal']").evaluate((el) => getComputedStyle(el));
    const selectOneListNormalStyle = await page.locator("[id='page:mainForm:selectOneListNormal']").evaluate((el) => getComputedStyle(el));
    const selectManyListNormalStyle = await page.locator("[id='page:mainForm:selectManyListNormal']").evaluate((el) => getComputedStyle(el));
    const inLargeStyle = await page.locator("[id='page:mainForm:inLarge']").evaluate((el) => getComputedStyle(el));
    const selectOneChoiceLargeStyle = await page.locator("[id='page:mainForm:selectOneChoiceLarge']").evaluate((el) => getComputedStyle(el));
    const selectOneListLargeStyle = await page.locator("[id='page:mainForm:selectOneListLarge']").evaluate((el) => getComputedStyle(el));
    const selectManyListLargeStyle = await page.locator("[id='page:mainForm:selectManyListLarge']").evaluate((el) => getComputedStyle(el));

    expect(inSmallStyle.height).toBe(selectOneChoiceSmallStyle.height);
    expect(selectOneListSmallStyle.height).toBe(selectOneChoiceSmallStyle.height);
    expect(selectManyListSmallStyle.height).toBe(selectOneChoiceSmallStyle.height);
    expect(inNormalStyle.height).toBe(selectOneChoiceNormalStyle.height);
    expect(selectOneListNormalStyle.height).toBe(selectOneChoiceNormalStyle.height);
    expect(selectManyListNormalStyle.height).toBe(selectOneChoiceNormalStyle.height);
    expect(inLargeStyle.height).toBe(selectOneChoiceLargeStyle.height);
    expect(selectOneListLargeStyle.height).toBe(selectOneChoiceLargeStyle.height);
    expect(selectManyListLargeStyle.height).toBe(selectOneChoiceLargeStyle.height);
  });
});
