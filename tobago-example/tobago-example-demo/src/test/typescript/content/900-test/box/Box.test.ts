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

test.describe("900-test/box/Box.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/box/Box.xhtml");
  });

  test("selectOneList dropdown must not force scroll bar on tc:box", async ({page}) => {
    const selectOneList = page.locator("[id='page:mainForm:selectOneList']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:selectOneList']");
    const boxBody = page.locator("[id='page:mainForm:box'] .card-body");

    await selectOneList.click();
    await expect(dropdownMenu).toContainClass("show");
    expect(await hasScrollbar(boxBody)).toBeFalsy();

    await page.keyboard.press("Escape");
    await expect(dropdownMenu).not.toContainClass("show");
    expect(await hasScrollbar(boxBody)).toBeFalsy();
  });

  test("selectManyList dropdown must not force scroll bar on tc:box", async ({page}) => {
    const selectManyList = page.locator("[id='page:mainForm:selectManyList']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:selectManyList']");
    const boxBody = page.locator("[id='page:mainForm:box'] .card-body");

    await selectManyList.click();
    await expect(dropdownMenu).toContainClass("show");
    expect(await hasScrollbar(boxBody)).toBeFalsy();

    await page.keyboard.press("Escape");
    await expect(dropdownMenu).not.toContainClass("show");
    expect(await hasScrollbar(boxBody)).toBeFalsy();
  });

  async function hasScrollbar(locator: Locator): Promise<boolean> {
    return locator.evaluate((element) => element.scrollHeight > element.clientHeight);
  }
});
