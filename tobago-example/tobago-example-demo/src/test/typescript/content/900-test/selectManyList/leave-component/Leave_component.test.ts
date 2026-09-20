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

test.describe("900-test/selectManyList/leave-component/Leave_component.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectManyList/leave-component/Leave_component.xhtml");
  });

  test("Open dropdown, filter, leave component", async ({page}) => {
    const selectManyList = page.locator("[id='page:mainForm:selectManyList']");
    const selectField = page.locator("[id='page:mainForm:selectManyList::selectField']");
    const filterInput = page.locator("[id='page:mainForm:selectManyList::filter']");
    const dropdownMenu = page.locator(".tobago-dropdown-menu[name='page:mainForm:selectManyList']");
    const hiddenSelectItems = dropdownMenu.locator(".tobago-select-item.d-none");
    const submit = page.locator("[id='page:mainForm:submit']");

    await submit.focus();
    await expect(submit).toBeFocused();
    await expect(selectManyList).not.toContainClass("tobago-focus");
    await expect(filterInput).toHaveValue("");
    await expect(dropdownMenu).not.toContainClass("show");
    await expect(hiddenSelectItems).toHaveCount(0);

    await selectField.click();
    await expect(filterInput).toBeFocused();
    await expect(selectManyList).toContainClass("tobago-focus");
    await expect(filterInput).toHaveValue("");
    await expect(dropdownMenu).toContainClass("show");
    await expect(hiddenSelectItems).toHaveCount(0);

    await filterInput.fill("a");
    await expect(filterInput).toBeFocused();
    await expect(selectManyList).toContainClass("tobago-focus");
    await expect(filterInput).toHaveValue("a");
    await expect(dropdownMenu).toContainClass("show");
    await expect(hiddenSelectItems).toHaveCount(5);

    await submit.focus();
    await expect(submit).toBeFocused();
    await expect(selectManyList).not.toContainClass("tobago-focus");
    await expect(filterInput).toHaveValue("");
    await expect(dropdownMenu).not.toContainClass("show");
    await expect(hiddenSelectItems).toHaveCount(0);
  });
});
