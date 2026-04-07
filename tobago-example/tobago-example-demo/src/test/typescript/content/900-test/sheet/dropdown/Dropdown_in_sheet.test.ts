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

test.describe("900-test/sheet/dropdown/Dropdown_in_sheet.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/dropdown/Dropdown_in_sheet.xhtml");
  });

  test("Click on dropdown menu in sortable 'name' header", async ({page}) => {
  const nameSorter = page.locator("[id='page:mainForm:sheet:name_sorter']");
  const outputName = page.locator("[id='page:mainForm:sheet:_col0']");
  const dropdownButton = page.locator("[id='page:mainForm:sheet:nameDropdown::command']");
  const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:sheet:nameDropdown']");


  await expect(nameSorter).toContainClass("tobago-sortable");
  await expect(nameSorter).not.toContainClass("tobago-ascending");
  await expect(dropdownMenu).not.toContainClass("show");

  await dropdownButton.click();
  await expect(nameSorter).toContainClass("tobago-sortable");
  await expect(nameSorter).not.toContainClass("tobago-ascending");
  await expect(dropdownMenu).toContainClass("show");

  await outputName.click();
  await expect(nameSorter).toContainClass("tobago-sortable");
  await expect(nameSorter).toContainClass("tobago-ascending");
  await expect(dropdownMenu).not.toContainClass("show");


});

test("Click 'Submit' in 'Period' column", async ({page}) => {
  const hiddenInputSelected = page.locator("[id='page:mainForm:sheet::selected']");
  const row1Button = page.locator("[id='page:mainForm:sheet:1:submit']");
  const row3Button = page.locator("[id='page:mainForm:sheet:3:submit']");


  await row3Button.click();
  await expect(hiddenInputSelected).toHaveValue("[3]");
  await row1Button.click();
  await expect(hiddenInputSelected).toHaveValue("[1]");


});

test("Click dropdown menu in row 0 and row 2", async ({page}) => {
  const hiddenInputSelected = page.locator("[id='page:mainForm:sheet::selected']");
  const row1Button = page.locator("[id='page:mainForm:sheet:1:submit']");
  const row0DropdownButton = page.locator("[id='page:mainForm:sheet:0:yearDropdown::command']");
  const row0DropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:sheet:0:yearDropdown']");
  const row2DropdownButton = page.locator("[id='page:mainForm:sheet:2:yearDropdown::command']");
  const row2DropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:sheet:2:yearDropdown']");


  await row1Button.click();
  await expect(hiddenInputSelected).toHaveValue("[1]");
  await expect(row0DropdownMenu).not.toContainClass("show");
  await expect(row2DropdownMenu).not.toContainClass("show");

  await row0DropdownButton.click();
  await expect(hiddenInputSelected).toHaveValue("[1]");
  await expect(row0DropdownMenu).toContainClass("show");
  await expect(row2DropdownMenu).not.toContainClass("show");

  await row2DropdownButton.click();
  await expect(hiddenInputSelected).toHaveValue("[1]");
  await expect(row0DropdownMenu).not.toContainClass("show");
  await expect(row2DropdownMenu).toContainClass("show");


});
});
