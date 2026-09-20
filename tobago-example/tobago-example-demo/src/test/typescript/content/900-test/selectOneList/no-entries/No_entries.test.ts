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

test.describe("900-test/selectOneList/no-entries/No_entries.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneList/no-entries/No_entries.xhtml");
  });

  test("SelectOneList: no-entries hint after leaving", async ({page}) => {
    const rootComponent = page.locator("[id='page:mainForm:selectOneList']");
    const selectField = page.locator("[id='page:mainForm:selectOneList::selectField']");
    const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectOneList']");
    const noEntriesFooter = dropdownMenu.locator(".tobago-no-entries");
    const inputForFocus = page.locator("[id='page:mainForm:inputForFocus::field']");

    await expect(dropdownMenu).not.toContainClass("show");
    await expect(noEntriesFooter).toContainClass("d-none");

    await filterInput.focus();
    await expect(rootComponent).toContainClass("tobago-focus");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    await expect(noEntriesFooter).toContainClass("d-none");

    await inputForFocus.focus(); //trigger blur on filterInput
    await expect(rootComponent).not.toContainClass("tobago-focus");
    await expect(dropdownMenu).not.toContainClass("show");
    await expect(noEntriesFooter).toContainClass("d-none");

    await filterInput.focus();
    await expect(rootComponent).toContainClass("tobago-focus");
    await selectField.click();
    await expect(dropdownMenu).toContainClass("show");
    await expect(noEntriesFooter).toContainClass("d-none");
  });
});
