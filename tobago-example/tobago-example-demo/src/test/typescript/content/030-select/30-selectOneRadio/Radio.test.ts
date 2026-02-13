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

test.describe("030-select/30-selectOneRadio/Radio.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/30-selectOneRadio/Radio.xhtml");
  });

  test("submit: Addition (2 + 4)", async ({page}) => {
    const number1Fn = page.locator("[id='page:mainForm:selectNum1'] input");
    const number2Fn = page.locator("[id='page:mainForm:selectNum2'] input");
    const submitAddFn = page.locator("[id='page:mainForm:submitAdd']");
    const outputFn = page.locator("[id='page:mainForm:resultOutput'] .form-control-plaintext");

    await number1Fn.nth(1).check(); // Select 2
    await number2Fn.nth(2).check(); // Select 4
    await submitAddFn.click();
    await expect(outputFn).toHaveText("6");
  });

  test("submit: Subtraction (4 - 1)", async ({page}) => {
    const number1Fn = page.locator("[id='page:mainForm:selectNum1'] input");
    const number2Fn = page.locator("[id='page:mainForm:selectNum2'] input");
    const submitSubFn = page.locator("[id='page:mainForm:submitSub']");
    const outputFn = page.locator("[id='page:mainForm:resultOutput'] .form-control-plaintext");

    await number1Fn.nth(2).check(); // Select 4
    await number2Fn.nth(0).check(); // Select 1
    await submitSubFn.click();
    await expect(outputFn).toHaveText("3");
  });

  test("ajax: select Mars", async ({page}) => {
    const marsFn = page.locator("[id='page:mainForm:selectPlanet::1']");
    const moonsFn = page.locator("[id='page:mainForm:moonradio'] .form-check-label");

    await marsFn.check();
    await marsFn.dispatchEvent("change");
    await expect(moonsFn.nth(0)).toHaveText("Phobos");
    await expect(moonsFn.nth(1)).toHaveText("Deimos");
  });

  test("ajax: select Jupiter", async ({page}) => {
    const jupiterFn = page.locator("[id='page:mainForm:selectPlanet::2']");
    const moonsFn = page.locator("[id='page:mainForm:moonradio'] .form-check-label");

    await jupiterFn.check();
    await jupiterFn.dispatchEvent("change");
    await expect(moonsFn.nth(0)).toHaveText("Europa");
    await expect(moonsFn.nth(1)).toHaveText("Ganymed");
    await expect(moonsFn.nth(2)).toHaveText("Io");
    await expect(moonsFn.nth(3)).toHaveText("Kallisto");
  });
});
