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

test.describe("900-test/in/suggest/quotMark/QuotMark.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("content/900-test/in/suggest/quotMark/QuotMark.xhtml");
  });

  test("Basics: 'M'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:input::field']");
    const resultList = page.locator(".tobago-options[id='" + (await inputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const entry0 = resultList.nth(0);

    await inputField.fill("M");
    await expect(resultList).toHaveCount(3);
    await expect(resultList.nth(0)).toHaveText("Mercury");
    await expect(resultList.nth(1)).toHaveText("Mars");
    await expect(resultList.nth(2)).toHaveText("Quotation\"Mark");
    await page.mouse.click(0, 0);
    await expect(entry0).not.toBeVisible();
  });

  test("Basics: 'Ma'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:input::field']");
    const resultList = page.locator(".tobago-options[id='" + (await inputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const entry0 = resultList.nth(0);

    await inputField.fill("Ma");
    await expect(resultList).toHaveCount(2);
    await expect(resultList.nth(0)).toHaveText("Mars");
    await expect(resultList.nth(1)).toHaveText("Quotation\"Mark");
    await page.mouse.click(0, 0);
    await expect(entry0).not.toBeVisible();
  });

  test("Basics: 'Mar'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:input::field']");
    const resultList = page.locator(".tobago-options[id='" + (await inputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const entry0 = resultList.nth(0);

    await inputField.fill("Mar");
    await expect(resultList).toHaveCount(2);
    await expect(resultList.nth(0)).toHaveText("Mars");
    await expect(resultList.nth(1)).toHaveText("Quotation\"Mark");
    await page.mouse.click(0, 0);
    await expect(entry0).not.toBeVisible();
  });

  test("Basics: 'Mars'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:input::field']");
    const resultList = page.locator(".tobago-options[id='" + (await inputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const entry0 = resultList.nth(0);

    await inputField.fill("Mars");
    await expect(resultList).toHaveCount(1);
    await expect(resultList.nth(0)).toHaveText("Mars");
    await page.mouse.click(0, 0);
    await expect(entry0).not.toBeVisible();
  });
});
