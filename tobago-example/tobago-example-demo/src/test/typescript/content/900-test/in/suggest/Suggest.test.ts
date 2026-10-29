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

test.describe("900-test/in/suggest/", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("content/900-test/in/suggest/Suggest.xhtml");
  });

  test("Suggest in before-facet of a input group with tc:suggest", async ({page}) => {
    const beforeFacetInputField = page.locator("[id='page:mainForm:inputBeforeFacet::field']");
    const beforeFacetResultList = page.locator(".tobago-options[id='" + (await beforeFacetInputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const beforeFacetResultListFirstEntry = beforeFacetResultList.first();
    const inputGroupInputField = page.locator("[id='page:mainForm:inputGroup::field']");
    const inputGroupResultList = page.locator(".tobago-options[id='" + (await inputGroupInputField.getAttribute("aria-owns")) + "'] .tobago-select-item:not(.d-none)");
    const inputGroupResultListLastEntry = inputGroupResultList.last();

    await beforeFacetInputField.fill("M");
    await expect(beforeFacetResultList).toHaveCount(18);
    await page.keyboard.press("ArrowDown");
    await expect(beforeFacetResultListFirstEntry).toBeVisible();
    await page.keyboard.press("ArrowDown");
    await page.keyboard.press("Enter");
    await expect(beforeFacetResultListFirstEntry).not.toBeVisible();
    await expect(beforeFacetInputField).toHaveValue("Mars");
    await page.keyboard.press("Tab");
    await page.keyboard.press("H");
    await expect(inputGroupResultList).toHaveCount(22);
    await page.keyboard.press("ArrowUp");
    await expect(inputGroupResultListLastEntry).toBeVisible();
    await page.keyboard.press("ArrowUp");
    await page.keyboard.press("Enter");
    await expect(inputGroupResultListLastEntry).not.toBeVisible();
    await expect(inputGroupInputField).toHaveValue("Thalassa");
  });
});
