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

test.describe("900-test/z-misc/requiredMessage/requiredMessage.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/z-misc/requiredMessage/requiredMessage.xhtml");
  });

  test("requiredMessage attribute", async ({page}) => {
    const messages = page.locator("tobago-messages[id='page:messages']");
    const submitButton = page.locator("button[id='page:mainForm:submit']");

    await expect(messages).not.toBeVisible();
    await submitButton.click();
    await expect(messages).toBeVisible();

    await testRequiredMessage(page.locator("tobago-in[id='page:mainForm:in']"));
    await testRequiredMessage(page.locator("tobago-date[id='page:mainForm:date']"));
    await testRequiredMessage(page.locator("tobago-file[id='page:mainForm:file']"));
    await testRequiredMessage(page.locator("tobago-select-boolean-checkbox[id='page:mainForm:selectBooleanCheckbox']"));
    await testRequiredMessage(page.locator("tobago-select-boolean-toggle[id='page:mainForm:selectBooleanToggle']"));
    await testRequiredMessage(page.locator("tobago-select-one-choice[id='page:mainForm:selectOneChoice']"));
    await testRequiredMessage(page.locator("tobago-select-one-list[id='page:mainForm:selectOneList']"));
    await testRequiredMessage(page.locator("tobago-select-one-listbox[id='page:mainForm:selectOneListbox']"));
    await testRequiredMessage(page.locator("tobago-select-one-radio[id='page:mainForm:selectOneRadio']"));
    await testRequiredMessage(page.locator("tobago-select-many-checkbox[id='page:mainForm:selectManyCheckbox']"));
    await testRequiredMessage(page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']"));
    await testRequiredMessage(page.locator("tobago-select-many-listbox[id='page:mainForm:selectManyListbox']"));
    await testRequiredMessage(page.locator("tobago-select-many-shuttle[id='page:mainForm:selectManyShuttle']"));
    await testRequiredMessage(page.locator("tobago-stars[id='page:mainForm:stars']"));
    await testRequiredMessage(page.locator("tobago-textarea[id='page:mainForm:textarea']"));
  });

  async function testRequiredMessage(component: Locator): Promise<void> {
    const id = await component.getAttribute("id");
    const expectedMessage = "required message for " + id.substring("page:mainForm:".length);

    const popover = component.locator("tobago-popover");
    const errorMessageLabel =
        await component.page().locator(".alert label[for='" + id + "']").isVisible()
            ? component.page().locator(".alert label[for='" + id + "']")
            : component.page().locator(".alert label[for='" + id + "::field']");

    await expect(popover).toHaveAttribute("value", expectedMessage);
    await expect(errorMessageLabel).toHaveAttribute("title", expectedMessage);
    await expect(errorMessageLabel).toHaveText(expectedMessage);
  }
});
