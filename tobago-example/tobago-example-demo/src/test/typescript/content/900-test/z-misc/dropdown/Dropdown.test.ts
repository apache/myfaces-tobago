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

test.describe("900-test/z-misc/dropdown/Dropdown.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/dropdown/Dropdown.xhtml");
  });

  test("Dropdown key events", async ({page}) => {
    const toggleButtonFn = page.locator("[id='page:mainForm:root::command']");
    const dropdownMenuFn = page.locator(".tobago-dropdown-menu[name='page:mainForm:root']");
    const entry1Fn = page.locator("[id='page:mainForm:entry1']");
    const checkboxFn = page.locator("[id='page:mainForm:checkbox::field']");
    const toggleFn = page.locator("[id='page:mainForm:toggle::field']");
    const radioSunFn = page.locator("[id='page:mainForm:radio::0']");
    const radioMoonFn = page.locator("[id='page:mainForm:radio::1']");
    const multiCheckboxFn = page.locator("[id='page:mainForm:multiCheckbox::0']");
    const entry2Fn = page.locator("[id='page:mainForm:entry2']");
    const entry3Fn = page.locator("[id='page:mainForm:entry3::command']");
    const entry3ParentFn = page.locator("[id='page:mainForm:entry3']");
    const subFn = page.locator("[id='page:mainForm:sub::command']");
    const subParentFn = page.locator("[id='page:mainForm:sub']");
    const link311Fn = page.locator("[id='page:mainForm:link311']");
    const link312Fn = page.locator("[id='page:mainForm:link312::command']");
    const link312ParentFn = page.locator("[id='page:mainForm:link312']");
    const link313Fn = page.locator("[id='page:mainForm:link313']");

    await expect(page.locator("[id='page:searchForm:search::field']")).toBeFocused();
    await toggleButtonFn.focus();
    await expect(dropdownMenuFn).not.toContainClass("show");
    expect(await dropdownMenuFn.locator("..").evaluate(element => element.tagName)).toBe("TOBAGO-DROPDOWN");
    await expect(toggleButtonFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(dropdownMenuFn).toContainClass("show");
    await expect(dropdownMenuFn.locator("..")).toContainClass("tobago-page-menuStore");
    await expect(entry1Fn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(checkboxFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(toggleFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(radioSunFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(radioMoonFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(multiCheckboxFn).toBeFocused();

    await page.keyboard.press("ArrowDown");
    await expect(entry2Fn).toBeFocused();

    await expect(entry3ParentFn).not.toContainClass("tobago-show");
    await page.keyboard.press("ArrowDown");
    await expect(entry3Fn).toBeFocused();
    await expect(entry3ParentFn).toContainClass("tobago-show");

    await expect(subParentFn).not.toContainClass("tobago-show");
    await page.keyboard.press("ArrowRight");
    await expect(subFn).toBeFocused();
    await expect(subParentFn).toContainClass("tobago-show");

    await page.keyboard.press("ArrowRight");
    await expect(link311Fn).toBeFocused();

    await page.keyboard.press("ArrowUp");
    await expect(link313Fn).toBeFocused();

    await expect(link312ParentFn).not.toContainClass("tobago-show");
    await page.keyboard.press("ArrowUp");
    await expect(link312Fn).toBeFocused();
    await expect(link312ParentFn).toContainClass("tobago-show");

    await expect(entry3ParentFn).toContainClass("tobago-show");
    await expect(subParentFn).toContainClass("tobago-show");
    await expect(link312ParentFn).toContainClass("tobago-show");
    await page.keyboard.press("ArrowLeft");
    await expect(subFn).toBeFocused();
    await expect(entry3ParentFn).toContainClass("tobago-show");
    await expect(subParentFn).toContainClass("tobago-show");
    await expect(link312ParentFn).not.toContainClass("tobago-show");

    await page.keyboard.press("Escape");
    await expect(dropdownMenuFn).not.toContainClass("show");
    await expect(entry3ParentFn).not.toContainClass("tobago-show");
    await expect(subParentFn).not.toContainClass("tobago-show");
    await expect(link312ParentFn).not.toContainClass("tobago-show");
  });

  test("Composite component inside dropdown menu", async ({page}) => {
    const tobagoDropdown = page.locator("[id='page:mainForm:ccDropdown']");
    const children = tobagoDropdown.locator(":scope > *");
    await expect(children).toHaveCount(2);
    expect(await children.nth(0).evaluate(element => element.tagName)).toBe("BUTTON");
    expect(await children.nth(1).evaluate(element => element.tagName)).toBe("DIV");
    await expect(children.nth(1)).toContainClass("tobago-dropdown-menu");

    const menuChildren = children.nth(1).locator(":scope > *");
    await expect(menuChildren).toHaveCount(5);
    expect(await menuChildren.nth(0).evaluate(element => element.tagName)).toBe("TOBAGO-DROPDOWN");
    await expect(menuChildren.nth(0)).toHaveAttribute("id", /page:mainForm:ccddEntry/);
    expect(await menuChildren.nth(1).evaluate(element => element.tagName)).toBe("TOBAGO-DROPDOWN");
    await expect(menuChildren.nth(1)).toHaveAttribute("id", /page:mainForm:compositeComponent:/);
    expect(await menuChildren.nth(2).evaluate(element => element.tagName)).toBe("DIV");
    await expect(menuChildren.nth(2)).toContainClass("dropdown-item");
    expect(await menuChildren.nth(3).evaluate(element => element.tagName)).toBe("BUTTON");
    await expect(menuChildren.nth(3)).toContainClass("tobago-link");
    expect(await menuChildren.nth(4).evaluate(element => element.tagName)).toBe("BUTTON");
    await expect(menuChildren.nth(4)).toContainClass("tobago-link");
  });
});
