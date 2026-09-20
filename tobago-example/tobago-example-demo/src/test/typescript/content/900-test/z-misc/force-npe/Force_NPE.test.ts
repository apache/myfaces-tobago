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

test.describe("900-test/z-misc/force-npe/Force_NPE.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/force-npe/Force_NPE.xhtml");
  });

  test("menu store must be available", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    const submit = page.locator("[id='page:mainForm:submitButton']");
    const forceNpeButton = page.locator("[id='page:mainForm:forceNpeButton']");
    const menuStore = page.locator(".tobago-page-menuStore");
    const fixedFooter = page.locator("tobago-footer.fixed-bottom");

    let timestampValue = Number(await timestamp.textContent());
    await expect(menuStore).toHaveCount(1);
    await expect(fixedFooter).toHaveCount(1);
    await forceNpeButton.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(menuStore).toHaveCount(1);
    await expect(fixedFooter).toHaveCount(0);

    // click submit to unbreak the page -> footer must be rendered
    timestampValue = await Number(await timestamp.textContent());
    await submit.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(menuStore).toHaveCount(1);
    await expect(fixedFooter).toHaveCount(1);
  });
});
