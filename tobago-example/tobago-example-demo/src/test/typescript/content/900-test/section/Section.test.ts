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

test.describe("900-test/section/Section.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/section/Section.xhtml");
  });

  test("Ajax reload for section 2", async ({page}) => {
    const reloadButton = page.locator("[id='page:mainForm:reloadSection2']");
    const section2Header = page.locator("[id='page:mainForm:levelTwoSection'] h3");
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    const timestampValue = Number(await timestamp.textContent());

    await expect(section2Header).toBeVisible();
    await reloadButton.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThanOrEqual(timestampValue);
    await expect(section2Header).toBeVisible();
  });
});
