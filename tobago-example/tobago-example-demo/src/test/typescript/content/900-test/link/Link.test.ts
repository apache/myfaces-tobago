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

import {expect, Page, test} from "@playwright/test";

test.describe("900-test/link/Link.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/link/Link.xhtml");
  });

  test("Standard Action Link", async ({page}) => await testStandardCommand(page, "standardLinkAction", "actionSection"));
  test("Standard Outcome Link", async ({page}) => await testStandardCommand(page, "standardLinkOutcome", "outcomeSection"));
  test("Standard Link Link", async ({page}) => await testStandardCommand(page, "standardLinkLink", "linkSection"));

  async function testStandardCommand(page: Page, commandId: string, destinationId: string) {
    const command = page.locator(`[id='page:mainForm:${commandId}']`);
    await command.click();
    await expect(page.locator(`[id='page:mainForm:${destinationId}']`)).toBeVisible();
    await page.locator("[id='page:mainForm:back']").click();
    await expect(command).toBeVisible();
  }

  test("Target Action Link", async ({page}) => await testTargetCommand(page, "targetLinkAction", "accessed by action"));
  test("Target Outcome Link", async ({page}) => await testTargetCommand(page, "targetLinkOutcome", "accessed by outcome"));
  test("Target Link Link", async ({page}) => await testTargetCommand(page, "targetLinkLink", "accessed by link"));

  async function testTargetCommand(page: Page, linkId: string, expectedValue: string) {
    const input = page.frameLocator("[id='page:mainForm:targetFrame']").locator("[id='page:textInput::field']");
    await expect(input).toHaveCount(0);
    await page.locator(`[id='page:mainForm:${linkId}']`).click();
    await expect(input).toHaveValue(expectedValue);
  }

  test("compare a.link and button.link", async ({page}) => {
    const aLinkText = page.locator("[id='page:mainForm:aLink200px'] span");
    const buttonLinkText = page.locator("[id='page:mainForm:buttonLink200px'] span");

    const aLinkTextLeft = (await aLinkText.boundingBox())?.x as number;
    const buttonLinkTextLeft = (await buttonLinkText.boundingBox())?.x as number;
    expect(aLinkTextLeft).toBe(buttonLinkTextLeft);
  });

  test("Dropdown menu must have three entries", async ({page}) => {
    const dropdown = page.locator("[id='page:mainForm:dropdownRepeat']");
    await expect(dropdown).toBeVisible();
    await expect(dropdown.locator(".dropdown-item")).toHaveText(["Nile", "Amazon", "Yangtze"]);
  });

  test("Test h1", async ({page}) => await testFont(page, "1"));
  test("Test h2", async ({page}) => await testFont(page, "2"));
  test("Test h3", async ({page}) => await testFont(page, "3"));
  test("Test h4", async ({page}) => await testFont(page, "4"));
  test("Test h5", async ({page}) => await testFont(page, "5"));
  test("Test h6", async ({page}) => await testFont(page, "6"));
  test("Test no heading", async ({page}) => await testFont(page, "0"));

  async function testFont(page: Page, linkButtonNumber: string) {
    const aLink = page.locator("[id='page:mainForm:link" + linkButtonNumber + "']");
    const buttonLink = page.locator("[id='page:mainForm:actionLink" + linkButtonNumber + "']");

    const aLinkStyle = await aLink.evaluate((el) => getComputedStyle(el));
    const buttonLinkStyle = await buttonLink.evaluate((el) => getComputedStyle(el));
    expect(aLinkStyle.color).toEqual(buttonLinkStyle.color);
    expect(aLinkStyle.fontFamily).toEqual(buttonLinkStyle.fontFamily);
    expect(aLinkStyle.fontSize).toEqual(buttonLinkStyle.fontSize);
    expect(aLinkStyle.fontWeight).toEqual(buttonLinkStyle.fontWeight);
    expect(aLinkStyle.textDecoration).toEqual(buttonLinkStyle.textDecoration);
  }
});
