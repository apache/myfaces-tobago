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

test.describe("900-test/z-misc/ajax/Ajax.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/ajax/Ajax.xhtml");
  });

  test("Submit -> Reset", async ({page}) => {
    const input = page.locator("[id='page:mainForm:form:input::field']");
    const output = page.locator("[id='page:mainForm:form:output'] .form-control-plaintext");
    const submit = page.locator("[id='page:mainForm:form:submit']");
    const reset = page.locator("[id='page:mainForm:form:reset']");

    await expect(input).toHaveValue("");
    await expect(output).toHaveText("");

    await input.fill("Tobago");
    await submit.click();
    await expect(input).toHaveValue("Tobago");
    await expect(output).toHaveText("Tobago");

    await reset.click();
    await expect(input).toHaveValue("");
    await expect(output).toHaveText("");
  });

  test("Ajax page reload", async ({page}) => {
    const html = page.locator("html");
    const body = page.locator("body");
    const ajaxButton = page.locator("[id='page:mainForm:ajaxPageReload']");
    const timestampComponent = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number = Number(await timestampComponent.textContent());

    await expect(html).toHaveCount(1);
    await ajaxButton.click();
    await expect.poll(async () => Number(await timestampComponent.textContent())).toBeGreaterThan(timestampValue);
    await expect(html).toHaveCount(1);
    await expect(body).not.toHaveCSS("overflow", "hidden");
  });
});
