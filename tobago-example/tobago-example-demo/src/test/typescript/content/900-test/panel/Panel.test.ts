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

test.describe("900-test/panel/Panel.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/panel/Panel.xhtml");
  });

  test("TOBAGO-2543: Panel must be collapse/expand if f:ajax is used", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    const panel = page.locator("[id='page:mainForm:tobago2543']");
    const panelHiddenCollapseInput = page.locator("[id='page:mainForm:tobago2543::collapse']");
    const inputField = page.locator("[id='page:mainForm:tobago2543Input']");
    const show = page.locator("[id='page:mainForm:showButton']");
    const hide = page.locator("[id='page:mainForm:hideButton']");

    let timestampValue = await timestamp.textContent() as string;
    await expect(timestamp).toHaveText(timestampValue);
    await expect(panelHiddenCollapseInput).toHaveAttribute("value", "false");
    await expect(inputField).toBeVisible();

    await hide.click();
    await expect(timestamp).not.toHaveText(timestampValue);
    timestampValue = await timestamp.textContent() as string;
    await expect(timestamp).toHaveText(timestampValue);
    await expect(panelHiddenCollapseInput).toHaveAttribute("value", "true");
    await expect(inputField).not.toBeVisible();

    await show.click();
    await expect(timestamp).not.toHaveText(timestampValue);
    timestampValue = await timestamp.textContent() as string;
    await expect(timestamp).toHaveText(timestampValue);
    await expect(panelHiddenCollapseInput).toHaveAttribute("value", "false");
    await expect(inputField).toBeVisible();
  });
});
