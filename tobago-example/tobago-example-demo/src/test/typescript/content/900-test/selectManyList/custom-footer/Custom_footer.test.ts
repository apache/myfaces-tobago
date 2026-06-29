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

test.describe("900-test/selectManyList/custom-footer/Custom_footer.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/custom-footer/Custom_footer.xhtml");
  });

  test("rendered=true, open dropdown, footer visible", async ({page}) => {
    const filterInput = page.locator("[id='page:mainForm:renderedTrue::filter']");
    const tobagoDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedTrue']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedTrue'] .tobago-select-item");
    const customFooter = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedTrue'] .tobago-custom-footer");

    await filterInput.dispatchEvent("input");
    await expect(tobagoDropdownMenu).toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooter).toHaveText("filter for results");
  });

  test("rendered=false, open dropdown, footer not visible", async ({page}) => {
    const filterInput = page.locator("[id='page:mainForm:renderedFalse::filter']");
    const tobagoDropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedFalse']");
    const tobagoSelectItems = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedFalse'] .tobago-select-item");
    const customFooterCell = page.locator(".tobago-dropdown-menu[name='page:mainForm:renderedFalse'] .tobago-custom-footer td");

    await filterInput.dispatchEvent("input");
    await expect(tobagoDropdownMenu).toContainClass("show");
    await expect(tobagoSelectItems).toHaveCount(0);
    await expect(customFooterCell).toHaveCSS("display", "none");
  });
});
