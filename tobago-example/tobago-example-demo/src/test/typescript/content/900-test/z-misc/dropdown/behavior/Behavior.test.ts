/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {expect, test} from "@playwright/test";

test.describe("900-test/z-misc/dropdown/behavior/Behavior.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/dropdown/behavior/Behavior.xhtml");
  });

  test("Open and close every dropdown menu", async ({page}) => {
    const buttonCounter = page.locator("tobago-out[id='page:mainForm:buttonCounter'] .form-control-plaintext");
    const button = page.locator("[id='page:mainForm:button::command']");
    const buttonDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:button']");
    await expect(buttonDropdownMenu).not.toContainClass("show");
    await expect(buttonDropdownMenu).not.toBeVisible();
    await button.click();
    await expect(buttonDropdownMenu).toContainClass("show");
    await expect(buttonDropdownMenu).toBeVisible();
    await expect(buttonCounter).toHaveText("0");
    await page.keyboard.press("Escape");
    await expect(buttonDropdownMenu).not.toContainClass("show");
    await expect(buttonDropdownMenu).not.toBeVisible();
    await expect(buttonCounter).toHaveText("1");

    const linkCounter = page.locator("tobago-out[id='page:mainForm:linkCounter'] .form-control-plaintext");
    const link = page.locator("[id='page:mainForm:link::command']");
    const linkDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:link']");
    await expect(linkDropdownMenu).not.toContainClass("show");
    await expect(linkDropdownMenu).not.toBeVisible();
    await link.click();
    await expect(linkDropdownMenu).toContainClass("show");
    await expect(linkDropdownMenu).toBeVisible();
    await expect(linkCounter).toHaveText("0");
    await page.keyboard.press("Escape");
    await expect(linkDropdownMenu).not.toContainClass("show");
    await expect(linkDropdownMenu).not.toBeVisible();
    await expect(linkCounter).toHaveText("1");

    const suggestCounter = page.locator("tobago-out[id='page:mainForm:suggestCounter'] .form-control-plaintext");
    const suggestInputField = page.locator("[id='page:mainForm:suggestInput::field']");
    const suggestDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:suggest']");
    await page.keyboard.press("Tab");
    await expect(suggestInputField).toBeFocused();
    await expect(suggestDropdownMenu).not.toContainClass("show");
    await expect(suggestDropdownMenu).not.toBeVisible();
    await page.keyboard.press("x");
    await expect(suggestDropdownMenu).toContainClass("show");
    await expect(suggestDropdownMenu).toBeVisible();
    await expect(suggestCounter).toHaveText("0");
    await page.keyboard.press("Escape");
    await expect(suggestDropdownMenu).not.toContainClass("show");
    await expect(suggestDropdownMenu).not.toBeVisible();
    await expect(suggestCounter).toHaveText("1");

    const selectOneListCounter = page.locator("tobago-out[id='page:mainForm:selectOneListCounter'] .form-control-plaintext");
    const selectOneListField = page.locator(".tobago-select-field[id='page:mainForm:selectOneList::selectField']");
    const selectOneListDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectOneList']");
    await expect(selectOneListDropdownMenu).not.toContainClass("show");
    await expect(selectOneListDropdownMenu).not.toBeVisible();
    await selectOneListField.click();
    await expect(selectOneListDropdownMenu).toContainClass("show");
    await expect(selectOneListDropdownMenu).toBeVisible();
    await expect(selectOneListCounter).toHaveText("0");
    await page.keyboard.press("Escape");
    await expect(selectOneListDropdownMenu).not.toContainClass("show");
    await expect(selectOneListDropdownMenu).not.toBeVisible();
    await expect(selectOneListCounter).toHaveText("1");

    const selectManyListCounter = page.locator("tobago-out[id='page:mainForm:selectManyListCounter'] .form-control-plaintext");
    const selectManyListField = page.locator(".tobago-select-field[id='page:mainForm:selectManyList::selectField']");
    const selectManyListDropdownMenu = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:selectManyList']");
    await expect(selectManyListDropdownMenu).not.toContainClass("show");
    await expect(selectManyListDropdownMenu).not.toBeVisible();
    await selectManyListField.click();
    await expect(selectManyListDropdownMenu).toContainClass("show");
    await expect(selectManyListDropdownMenu).toBeVisible();
    await expect(selectManyListCounter).toHaveText("0");
    await page.keyboard.press("Escape");
    await expect(selectManyListDropdownMenu).not.toContainClass("show");
    await expect(selectManyListDropdownMenu).not.toBeVisible();
    await expect(selectManyListCounter).toHaveText("1");
  });
});
