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

test.describe("900-test/z-misc/ajax/special-character/Special_Character.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/ajax/special-character/Special_Character.xhtml");
  });

  test("ajax execute", async ({page}) => {
    let timestampFn = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let textFn = page.locator("[id='page:mainForm:outText'] .form-control-plaintext");
    let tipFn = page.locator("[id='page:mainForm:outTip'] .form-control-plaintext");
    let buttonFn = page.locator("[id='page:mainForm:ajaxButton']");

    const timestampValue = Number(await timestampFn.textContent());
    const textValue = await textFn.textContent() as string
    const tipValue = await tipFn.getAttribute("title") as string;

    await buttonFn.click();
    await expect.poll(async () => Number(await timestampFn.textContent())).toBeGreaterThan(timestampValue);
    await expect(textFn).toHaveText(textValue);
    await expect(tipFn).toHaveAttribute("title", tipValue);
  });
});
