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

test.describe("030-select/15-selectBooleanToggle/Toggle.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/15-selectBooleanToggle/Toggle.xhtml");
  });

  test("submit: select A", async ({page}) => {
    const selectAFn = page.locator("[id='page:mainForm:selectA'] input");
    const selectBFn = page.locator("[id='page:mainForm:selectB'] input");
    const selectCFn = page.locator("[id='page:mainForm:selectC'] input");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const outputFn = page.locator("[id='page:mainForm:submitOutput'] .form-control-plaintext");

    await selectAFn.check();
    await selectBFn.uncheck();
    await selectCFn.uncheck();
    await submitFn.click();
    await expect(outputFn).toHaveText("A");
  });

  test("submit: select B and C", async ({page}) => {
    const selectAFn = page.locator("[id='page:mainForm:selectA'] input");
    const selectBFn = page.locator("[id='page:mainForm:selectB'] input");
    const selectCFn = page.locator("[id='page:mainForm:selectC'] input");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const outputFn = page.locator("[id='page:mainForm:submitOutput'] .form-control-plaintext");

    await selectAFn.uncheck();
    await selectBFn.check();
    await selectCFn.check();
    await submitFn.click();
    await expect(outputFn).toHaveText("B C");
  });

  test("ajax: select D", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectD'] input");
    const outputFn = page.locator("[id='page:mainForm:outputD'] .form-control-plaintext");

    await selectFn.check();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("true");
  });

  test("ajax: deselect D", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectD'] input");
    const outputFn = page.locator("[id='page:mainForm:outputD'] .form-control-plaintext");

    await selectFn.uncheck();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("false");
  });

  test("ajax: select E", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectE'] input");
    const outputFn = page.locator("[id='page:mainForm:outputE'] .form-control-plaintext");

    await selectFn.check();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("true");
  });

  test("ajax: deselect E", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectE'] input");
    const outputFn = page.locator("[id='page:mainForm:outputE'] .form-control-plaintext");

    await selectFn.uncheck();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("false");
  });

  test("ajax: select F", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectF'] input");
    const outputFn = page.locator("[id='page:mainForm:outputF'] .form-control-plaintext");

    await selectFn.check();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("true");
  });

  test("ajax: deselect F", async ({page}) => {
    const selectFn = page.locator("[id='page:mainForm:selectF'] input");
    const outputFn = page.locator("[id='page:mainForm:outputF'] .form-control-plaintext");

    await selectFn.uncheck();
    await selectFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("false");
  });
});
