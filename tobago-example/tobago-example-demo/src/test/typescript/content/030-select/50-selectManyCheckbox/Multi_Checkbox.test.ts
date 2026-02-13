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

test.describe("030-select/50-selectManyCheckbox/Multi_Checkbox.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/50-selectManyCheckbox/Multi_Checkbox.xhtml");
  });

  test("submit: select cat", async ({page}) => {
    const animals = page.locator("[id='page:mainForm:animals'] input");
    const submit = page.locator("[id='page:mainForm:submit']");
    const output = page.locator("[id='page:mainForm:animalsOutput'] .form-control-plaintext");

    await expect(output).not.toHaveText("Cat");
    await animals.nth(0).check(); // Cat
    await animals.nth(1).uncheck();
    await animals.nth(2).uncheck();
    await animals.nth(3).uncheck();
    await submit.click();
    await expect(output).toHaveText("Cat");
  });

  test("submit: select fox and rabbit", async ({page}) => {
    const animals = page.locator("[id='page:mainForm:animals'] input");
    const submit = page.locator("[id='page:mainForm:submit']");
    const output = page.locator("[id='page:mainForm:animalsOutput'] .form-control-plaintext");

    await expect(output).not.toHaveText("Fox Rabbit");
    await animals.nth(2).check(); // Fox
    await animals.nth(3).check(); // Rabbit
    await submit.click();
    await expect(output).toHaveText("Fox Rabbit");
  });

  test("Ajax", async ({page}) => {
    const checkboxOne = page.locator("input[type='checkbox'][id='page:mainForm:numbers::0']");
    const checkboxTwo = page.locator("input[type='checkbox'][id='page:mainForm:numbers::1']");
    const checkboxThree = page.locator("input[type='checkbox'][id='page:mainForm:numbers::2']");
    const checkboxFour = page.locator("input[type='checkbox'][id='page:mainForm:numbers::3']");
    const result = page.locator("[id='page:mainForm:resultOutput'] .form-control-plaintext");

    await expect(result).toHaveText("0");
    await checkboxOne.click();
    await expect(result).toHaveText("1");
    await checkboxTwo.click();
    await expect(result).toHaveText("3");
    await checkboxThree.click();
    await expect(result).toHaveText("6");
    await checkboxFour.click();
    await expect(result).toHaveText("10");
    await checkboxOne.click();
    await expect(result).toHaveText("9");
    await checkboxTwo.click();
    await expect(result).toHaveText("7");
    await checkboxThree.click();
    await expect(result).toHaveText("4");
    await checkboxFour.click();
    await expect(result).toHaveText("0");
  });
});
