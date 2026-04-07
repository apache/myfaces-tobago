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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/z-misc/dropdown/dropdown-menu-position/DropdownMenuPosition.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/dropdown/dropdown-menu-position/DropdownMenuPosition.xhtml");
  });

  test("Button dropdown on the right", async ({page}) => {
    const button = page.locator("[id='page:mainForm:buttonDropdownRight::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:buttonDropdownRight']");

    await button.focus();
    await expect(button).toBeFocused();
    await page.keyboard.press("Enter");

    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeGreaterThan(0);
    expect(dropdownMenuRect.left).toBeLessThan(await page.evaluate(() => window.innerWidth));
    expect(dropdownMenuRect.right).toBeGreaterThan(0);
    expect(dropdownMenuRect.right).toBeLessThan(await page.evaluate(() => window.innerWidth));
  });

  test("default - SelectOneList: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const secondHeader = page.locator("[id='page:mainForm:secondHeader']");
    const selectOneList = page.locator("[id='page:mainForm:defaultSelectOneList']");
    const selectField = page.locator("[id='page:mainForm:defaultSelectOneList::selectField']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:defaultSelectOneList']");

    await scrollTo(selectOneList, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    const selectFieldRect = await selectField.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(selectFieldRect.left, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(selectFieldRect.bottom, 1);
    expect(dropdownMenuRect.width).toBeCloseTo(selectFieldRect.width, 1);
  });

  test("default - SelectOneList: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const selectOneList = page.locator("[id='page:mainForm:defaultSelectOneList']");
    const selectField = page.locator("[id='page:mainForm:defaultSelectOneList::selectField']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:defaultSelectOneList']");

    await scrollTo(selectOneList, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const selectFieldRect = await selectField.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(selectFieldRect.left, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(selectFieldRect.top, 1);
  });

  test("default - button dropdown: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const button = page.locator("[id='page:mainForm:defaultButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:defaultButtonDropdown']");

    await scrollTo(button, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    expect(dropdownMenuRect.left).toBeCloseTo(buttonRect.left, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(buttonRect.bottom, 1);
  });

  test("default - button dropdown: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const button = page.locator("[id='page:mainForm:defaultButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:defaultButtonDropdown']");

    await scrollTo(button, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(buttonRect.left, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(buttonRect.top, 1);
  });

  test("default - button dropdown in tc:in after-facet: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const button = page.locator("[id='page:mainForm:defaultInAfterButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:defaultInAfterButtonDropdown']");

    await scrollTo(button, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.right).toBeCloseTo(buttonRect.right, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(buttonRect.bottom, 1);
  });

  test("default - button dropdown in tc:in after-facet: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const button = page.locator("[id='page:mainForm:defaultInAfterButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:defaultInAfterButtonDropdown']");

    await scrollTo(button, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.right).toBeCloseTo(buttonRect.right, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(buttonRect.top, 1);
  });

  test("transformX0 - SelectOneList: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const secondHeader = page.locator("[id='page:mainForm:secondHeader']");
    const selectOneList = page.locator("[id='page:mainForm:transformX0selectOneList']");
    const selectField = page.locator("[id='page:mainForm:transformX0selectOneList::selectField']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:transformX0selectOneList']");

    await scrollTo(selectOneList, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    const selectFieldRect = await selectField.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(selectFieldRect.left, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(selectFieldRect.bottom, 1);
    expect(dropdownMenuRect.width).toBeCloseTo(selectFieldRect.width, 1);
  });

  test("transformX0 - SelectOneList: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const selectOneList = page.locator("[id='page:mainForm:transformX0selectOneList']");
    const selectField = page.locator("[id='page:mainForm:transformX0selectOneList::selectField']");
    const dropdownMenu = page.locator(".tobago-options.tobago-dropdown-menu[name='page:mainForm:transformX0selectOneList']");

    await scrollTo(selectOneList, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const selectFieldRect = await selectField.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(selectFieldRect.left, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(selectFieldRect.top, 1);
  });

  test("transformX0 - button dropdown: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const button = page.locator("[id='page:mainForm:transformX0buttonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:transformX0buttonDropdown']");

    await scrollTo(button, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(buttonRect.left, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(buttonRect.bottom, 1);
  });

  test("transformX0 - button dropdown: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const button = page.locator("[id='page:mainForm:transformX0buttonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:transformX0buttonDropdown']");

    await scrollTo(button, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.left).toBeCloseTo(buttonRect.left, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(buttonRect.top, 1);
  });

  test("transformX0 - button dropdown in tc:in after-facet: upper half", async ({page}) => {
    const tobagoHeader = page.locator("tobago-header.sticky-top");
    const button = page.locator("[id='page:mainForm:transformX0inAfterButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:transformX0inAfterButtonDropdown']");

    await scrollTo(button, false);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginTop = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginTop));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.right).toBeCloseTo(buttonRect.right, 1);
    expect(dropdownMenuRect.top - dropdownMenuMarginTop).toBeCloseTo(buttonRect.bottom, 1);
  });

  test("transformX0 - button dropdown in tc:in after-facet: lower half", async ({page}) => {
    const tobagoFooter = page.locator("tobago-footer.fixed-bottom");
    const button = page.locator("[id='page:mainForm:transformX0inAfterButtonDropdown::command']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:transformX0inAfterButtonDropdown']");

    await scrollTo(button, true);
    await expect(dropdownMenu).not.toContainClass("show");
    await button.click();
    await expect(dropdownMenu).toContainClass("show");
    const dropdownMenuRect = await dropdownMenu.evaluate(el => el.getBoundingClientRect());
    const dropdownMenuMarginBottom = await dropdownMenu.evaluate(el => parseInt(getComputedStyle(el).marginBottom));
    const buttonRect = await button.evaluate(el => el.getBoundingClientRect());
    expect(dropdownMenuRect.right).toBeCloseTo(buttonRect.right, 1);
    expect(dropdownMenuRect.bottom + dropdownMenuMarginBottom).toBeCloseTo(buttonRect.top, 1);
  });

  /**
   * @param element to focus
   * @param lowerHalf if true, the element is positioned at the lower half of the screen
   */
  async function scrollTo(element: Locator, lowerHalf: boolean) {
    if (lowerHalf) {
      await element.evaluate((el) => {
        window.scrollTo({top: el.getBoundingClientRect().y - 600, behavior: "instant"});
      });
    } else {
      await element.evaluate((el) => {
        window.scrollTo({top: el.getBoundingClientRect().y - 200, behavior: "instant"});
      });
    }
  }
});
