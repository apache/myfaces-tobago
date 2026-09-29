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

test.describe("900-test/z-misc/dropdown/ajax/Ajax_Dropdown.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/dropdown/ajax/Ajax_Dropdown.xhtml");
  });

  test("Execute 'AJAX' entry in dropdown menu", async ({page}) => {
    const dropdownButtonFn = page.locator("[id='page:mainForm:dropdownButton::command']");
    const dropdownMenuFn = page.locator(".tobago-dropdown-menu[name=page\\:mainForm\\:dropdownButton]");
    const dropdownAjaxEntryFn = page.locator("[id='page:mainForm:ajaxEntry']");
    const inputFn = page.locator("[id='page:mainForm:inputAjax::field']");
    const outputFn = page.locator("[id='page:mainForm:outputAjax'] .form-control-plaintext");

    // no test.setup() because controller is @RequestScoped
    await expect(dropdownMenuFn).not.toContainClass("show");
    await expect(outputFn).toHaveText("");

    await dropdownButtonFn.click();
    await expect(dropdownMenuFn).toContainClass("show");
    await expect(dropdownMenuFn.locator("..")).toContainClass("tobago-page-menuStore");

    await inputFn.fill("Tobago");
    await dropdownAjaxEntryFn.click();
    await expect(outputFn).toHaveText("Tobago");
    await expect(dropdownMenuFn).not.toContainClass("show");

    const pageOverlays = page.locator("tobago-overlay");
    await expect(pageOverlays).toHaveCount(0);
  });
});
