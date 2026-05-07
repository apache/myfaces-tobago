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

import {expect, Locator, Page, test} from "@playwright/test";

test.describe("900-test/button/Button.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/button/Button.xhtml");
  });

  test("Standard Action Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:standardButtonAction']");
    const destinationSectionFn = page.locator("[id='page:mainForm:actionSection']");
    await testStandardCommands(page, commandFn, destinationSectionFn);
  });

  test("Standard Outcome Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:standardButtonOutcome']");
    const destinationSectionFn = page.locator("[id='page:mainForm:outcomeSection']");
    await testStandardCommands(page, commandFn, destinationSectionFn);
  });

  test("Standard Link Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:standardButtonLink']");
    const destinationSectionFn = page.locator("[id='page:mainForm:linkSection']");
    await testStandardCommands(page, commandFn, destinationSectionFn);
  });

  async function testStandardCommands(page: Page, commandFn: Locator, destinationSectionFn: Locator) {
    const backFn = page.locator("[id='page:mainForm:back']");

    await commandFn.click();
    await expect(destinationSectionFn).toBeVisible();
    await backFn.click();
    await expect(commandFn).toBeVisible();
  }

  test("Confirmation attribute Action Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationButtonAction']");
    const destinationSectionFn = page.locator("[id='page:mainForm:actionSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (attribute)");
  });
  test("Confirmation attribute Outcome Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationButtonOutcome']");
    const destinationSectionFn = page.locator("[id='page:mainForm:outcomeSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (attribute)");
  });
  test("Confirmation attribute Link Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationButtonLink']");
    const destinationSectionFn = page.locator("[id='page:mainForm:linkSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (attribute)");
  });
  test("Confirmation facet Action Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationFacetButtonAction']");
    const destinationSectionFn = page.locator("[id='page:mainForm:actionSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (facet)");
  });
  test("Confirmation facet Outcome Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationFacetButtonOutcome']");
    const destinationSectionFn = page.locator("[id='page:mainForm:outcomeSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (facet)");
  });
  test("Confirmation facet Link Button", async ({page}) => {
    const commandFn = page.locator("[id='page:mainForm:confirmationFacetButtonLink']");
    const destinationSectionFn = page.locator("[id='page:mainForm:linkSection']");
    await testConfirmationCommands(page, commandFn, destinationSectionFn, "Are you sure? (facet)");
  });

  async function testConfirmationCommands(page: Page, commandFn: Locator, destinationSectionFn: Locator, message: string) {
    const backFn = page.locator("[id='page:mainForm:back']");

    page.once("dialog", async dialog => {
      expect(dialog.type()).toBe("confirm");
      expect(dialog.message()).toBe(message);
      await dialog.dismiss();
    });
    await commandFn.click();

    page.once("dialog", async dialog => {
      expect(dialog.type()).toBe("confirm");
      expect(dialog.message()).toBe(message);
      await dialog.accept();
    });
    await commandFn.click();

    await expect(destinationSectionFn).toBeVisible();
    await backFn.click();
    await expect(commandFn).toBeVisible();
  }

  test("Target Action Button", async ({page}) => {
    const button = page.locator("[id='page:mainForm:targetButtonAction']");
    const expectedValue = "accessed by action";
    await testTargetCommands(page, button, expectedValue);
  });
  test("Target Outcome Button", async ({page}) => {
    const button = page.locator("[id='page:mainForm:targetButtonOutcome']");
    const expectedValue = "accessed by outcome";
    await testTargetCommands(page, button, expectedValue);
  });
  test("Target Link Button", async ({page}) => {
    const button = page.locator("[id='page:mainForm:targetButtonLink']");
    const expectedValue = "accessed by link";
    await testTargetCommands(page, button, expectedValue);
  });

  async function testTargetCommands(page: Page, button: Locator, expectedValue: string) {
    const iframe = page.frameLocator("[id='page:mainForm:targetFrame']");
    const inputField = iframe.locator("[id='page:textInput::field']");

    await button.click();
    await expect(inputField).toHaveValue(expectedValue);
  }

  test("Style must not be a dropdown item", async ({page}) => {
    const buttonFn = page.locator("[id='page:mainForm:dropdownWithStyle::command']");
    const dropdownMenuFn = page.locator(".tobago-dropdown-menu[data-tobago-for='page:mainForm:dropdownWithStyle']");
    const entry1 = dropdownMenuFn.locator("[id='page:mainForm:dropdownWithStyleEntry1']");
    const entry2 = dropdownMenuFn.locator("[id='page:mainForm:dropdownWithStyleEntry2']");
    const styleAsItemFn = page.locator("[id='page:mainForm:dropdownWithStyle'] .tobago-dropdown-menu .dropdown-item > style");

    await buttonFn.scrollIntoViewIfNeeded();
    await buttonFn.click();
    await expect(dropdownMenuFn).toContainClass("show");
    await expect(dropdownMenuFn).toBeVisible();
    await expect(entry1).toBeVisible();
    await expect(entry2).toBeVisible();
    await expect(styleAsItemFn).not.toBeVisible();
    const buttonBoundingBox = await buttonFn.boundingBox();
    expect(buttonBoundingBox?.width).toEqual(200);
  });
});
