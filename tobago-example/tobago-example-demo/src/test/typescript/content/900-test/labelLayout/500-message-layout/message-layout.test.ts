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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/labelLayout/500-message-layout/message-layout.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/labelLayout/500-message-layout/message-layout.xhtml");
  });

  test("Input", async ({page}) => {
    const alertsFn = page.locator("tobago-messages .alert");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const compareFn = page.locator("[id='page:mainForm:inCompare']");
    const labelFn = page.locator("[id='page:mainForm:in'] label");
    const inputFn = page.locator("[id='page:mainForm:in::field']");
    const popoverFn = page.locator("[id='page:mainForm:in'] tobago-popover");

    await submitFn.click();
    await expect(alertsFn).not.toHaveCount(0);
    expect(await getFullWidth(compareFn))
        .toBeCloseTo(await getFullWidth(labelFn) + await getFullWidth(inputFn) + await getFullWidth(popoverFn));
  });

  test("Input Group", async ({page}) => {
    const alertsFn = page.locator("tobago-messages .alert");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const compareFn = page.locator("[id='page:mainForm:inputGroupCompare']");
    const labelFn = page.locator("[id='page:mainForm:inputGroup'] label");
    const inputFn = page.locator("[id='page:mainForm:inputGroup'] .input-group");
    const popoverFn = page.locator("[id='page:mainForm:inputGroup'] tobago-popover");

    await submitFn.click();
    await expect(alertsFn).not.toHaveCount(0);
    expect(await getFullWidth(compareFn))
        .toBeCloseTo(await getFullWidth(labelFn) + await getFullWidth(inputFn) + await getFullWidth(popoverFn));
  });

  test("Date", async ({page}) => {
    const alertsFn = page.locator("tobago-messages .alert");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const compareFn = page.locator("[id='page:mainForm:dateCompare']");
    const labelFn = page.locator("[id='page:mainForm:date'] label");
    const inputFn = page.locator("[id='page:mainForm:date::field']");
    const popoverFn = page.locator("[id='page:mainForm:date'] tobago-popover");

    await submitFn.click();
    await expect(alertsFn).not.toHaveCount(0);
    expect(await getFullWidth(compareFn))
        .toBeCloseTo(await getFullWidth(labelFn) + await getFullWidth(inputFn) + await getFullWidth(popoverFn));
  });

  test("Shuttle", async ({page}) => {
    const alertsFn = page.locator("tobago-messages .alert");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const compareFn = page.locator("[id='page:mainForm:shuttleCompare']");
    const labelFn = page.locator("[id='page:mainForm:shuttle'] label");
    const inputFn = page.locator("[id='page:mainForm:shuttle'] .tobago-body");
    const popoverFn = page.locator("[id='page:mainForm:shuttle'] tobago-popover");

    await submitFn.click();
    await expect(alertsFn).not.toHaveCount(0);
    expect(await getFullWidth(compareFn))
        .toBeCloseTo(await getFullWidth(labelFn) + await getFullWidth(inputFn) + await getFullWidth(popoverFn));
  });

  async function getFullWidth(locator: Locator): Promise<number> {
    return locator.evaluate(element => {
      const style = getComputedStyle(element);
      return parseFloat(style.marginLeft) + parseFloat(style.width) + parseFloat(style.marginRight);
    });
  }
});
