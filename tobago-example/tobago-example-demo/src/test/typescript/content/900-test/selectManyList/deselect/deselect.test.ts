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

test.describe("900-test/selectManyList/deselect/Deselect.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/deselect/Deselect.xhtml");
  });

  test("disabled selectItem", async ({page}) => {
    const resetButton = page.locator("[id='page:mainForm:reset']");
    const selectManyList = page.locator("[id='page:mainForm:selectManyList']");
    const badges = page.locator("[id='page:mainForm:selectManyList::selectField'] .tobago-badges tobago-badge");
    const options = page.locator(".tobago-options[data-tobago-for='page:mainForm:selectManyList']");
    const rows = options.locator("tr");
    const earthRow = options.locator("tr[data-tobago-value='Earth']");

    await expect(badges).toHaveCount(3);
    await expect(rows.nth(0)).not.toBeVisible();

    await resetButton.focus();
    await page.keyboard.press("Tab");
    await expect(selectManyList).toContainClass("tobago-focus");

    await page.keyboard.press("ArrowDown");
    await expect(rows.nth(0)).toBeVisible();
    await expect(rows.nth(0)).toBeFocused(); //Mercury

    await page.keyboard.press("ArrowDown");
    await expect(rows.nth(1)).toBeFocused(); //Venus
    await expect(rows.nth(1)).toContainClass("table-primary"); //Venus

    await page.keyboard.press("Space");
    await expect(rows.nth(1)).not.toContainClass("table-primary"); //Venus
    await expect(badges).toHaveCount(2);

    await page.keyboard.press("ArrowDown"); //jump over Earth
    await expect(rows.nth(3)).toBeFocused(); //Mars
    await expect(earthRow).toHaveCSS("pointer-events", "none");
  });
});
