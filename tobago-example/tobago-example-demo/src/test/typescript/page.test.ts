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

import {expect, Page, test} from "@playwright/test";

test.describe("900-test/page/page.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/page/page.xhtml");
  });

  test("tc:page: includeViewParams", async ({page, browserName}) => {
    const paramFromUrl = page.locator("input[id='page:mainForm:param::field']");
    const addParamLink = page.locator("a[id='page:mainForm:addTobagoParamLink']");
    const reset = page.locator("button[id='page:mainForm:reset']");
    const submit = page.locator("button[id='page:mainForm:submit']");

    let timestampValue: number;

    timestampValue = await getCurrentTimestamp(page);
    await addParamLink.click();
    await expect.poll(() => getCurrentTimestamp(page)).toBeGreaterThan(timestampValue);
    await expect(paramFromUrl).toHaveValue("tobago");

    timestampValue = await getCurrentTimestamp(page);
    await submit.click();
    await expect.poll(() => getCurrentTimestamp(page)).toBeGreaterThan(timestampValue);
    await expect(paramFromUrl).toHaveValue("tobago");

    timestampValue = await getCurrentTimestamp(page);
    await reset.click();
    await expect.poll(() => getCurrentTimestamp(page)).toBeGreaterThan(timestampValue);
    await expect(paramFromUrl).toHaveValue("");

    timestampValue = await getCurrentTimestamp(page);
    await submit.click();
    await expect.poll(() => getCurrentTimestamp(page)).toBeGreaterThan(timestampValue);
    await expect(paramFromUrl).toHaveValue("");
  });

  async function getCurrentTimestamp(page: Page): Promise<number> {
    const timestamp = page.locator("tobago-out[id='page:mainForm:timestamp'] .form-control-plaintext");
    return Number(await timestamp.textContent());
  }
});
