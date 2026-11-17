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

test.describe("900-test/selectOneList/deselect/Deselect.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneList/deselect/Deselect.xhtml");
  });

  test("deselection if required=false", async ({page}) => {
    const hidden = page.locator("[id='page:mainForm:selectOneList::field']");
    const selected = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const saturnRow = page.locator(`.tobago-options[name='page:mainForm:selectOneList'] tr[data-tobago-value='Saturn']`);
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const checkbox = page.locator("[id='page:mainForm:requiredCheckbox::field']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await expect(checkbox).not.toBeChecked();
    await expect(hidden).toHaveJSProperty("required", false);
    await expect(selected).toHaveText("");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await saturnRow.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("Saturn");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await saturnRow.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await submit.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("Saturn"); // should this be changed? TOBAGO-2544
    await expect(output).toHaveText("Saturn");
  });

  test("deselection if required=true", async ({page}) => {
    const hidden = page.locator("[id='page:mainForm:selectOneList::field']");
    const selected = page.locator("[id='page:mainForm:selectOneList::selectField'] span");
    const jupiterRow = page.locator(`.tobago-options[name='page:mainForm:selectOneList'] tr[data-tobago-value='Jupiter']`);
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const checkbox = page.locator("[id='page:mainForm:requiredCheckbox::field']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");

    let timestampValue: number;

    await checkbox.check();
    await expect(checkbox).toBeChecked();
    await expect(hidden).toHaveJSProperty("required", true);
    await expect(selected).toHaveText("");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await jupiterRow.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("Jupiter");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await jupiterRow.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("Jupiter");
    await expect(output).toHaveText("");

    timestampValue = Number(await timestamp.textContent());
    await submit.click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(selected).toHaveText("Jupiter");
    await expect(output).toHaveText("Jupiter");
  });
});
