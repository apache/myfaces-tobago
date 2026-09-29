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

test.describe("900-test/sheet/50-button-in-sheet/Button_in_sheet.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/50-button-in-sheet/Button_in_sheet.xhtml");
  });

  test("submit button", async ({page}) => {
    const selected = page.locator("[id='page:mainForm:sheet::selected']");
    const sun = page.locator("[id='page:mainForm:sheet:1:outOrbit']");
    const button = page.locator(`[id='page:mainForm:sheet:0:submitButton']`);
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await sun.click();
    await expect(selected).toHaveValue("[1]");
    timestampValue = Number(await timestamp.textContent());
    await button.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveValue("[1]");
  });
  test("ajax button", async ({page}) => {
    const selected = page.locator("[id='page:mainForm:sheet::selected']");
    const sun = page.locator("[id='page:mainForm:sheet:1:outOrbit']");
    const button = page.locator(`[id='page:mainForm:sheet:0:ajaxButton']`);
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await sun.click();
    await expect(selected).toHaveValue("[1]");
    timestampValue = Number(await timestamp.textContent());
    await button.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveValue("[1]");
  });
  test("submit button in panel", async ({page}) => {
    const selected = page.locator("[id='page:mainForm:sheet::selected']");
    const sun = page.locator("[id='page:mainForm:sheet:1:outOrbit']");
    const button = page.locator(`[id='page:mainForm:sheet:0:submitPanelButton']`);
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await sun.click();
    await expect(selected).toHaveValue("[1]");
    timestampValue = Number(await timestamp.textContent());
    await button.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveValue("[1]");
  });
  test("ajax button in panel", async ({page}) => {
    const selected = page.locator("[id='page:mainForm:sheet::selected']");
    const sun = page.locator("[id='page:mainForm:sheet:1:outOrbit']");
    const button = page.locator(`[id='page:mainForm:sheet:0:ajaxPanelButton']`);
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await sun.click();
    await expect(selected).toHaveValue("[1]");
    timestampValue = Number(await timestamp.textContent());
    await button.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveValue("[1]");
  });

  test("select 'gamma' from list", async ({page}) => {
    const selected = page.locator("[id='page:mainForm:sheet::selected']");
    const sun = page.locator("[id='page:mainForm:sheet:1:outOrbit']");
    const list = page.locator("[id='page:mainForm:sheet:0:list::field']");
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await sun.click();
    await expect(selected).toHaveValue("[1]");
    timestampValue = Number(await timestamp.textContent());
    await list.selectOption({index: 2});
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveValue("[1]");
  });
});
