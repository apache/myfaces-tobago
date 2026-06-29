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

test.describe("170-validation/01/JSR_303.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/170-validation/01/JSR_303.xhtml");
  });

  test("Required: Submit without content.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:required:in1::field']");
    const submitFn = page.locator("[id='page:mainForm:required:submit1']");

    await inFn.fill("");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
    await expect(inFn).toHaveValue("");
  });

  test("Required: Submit with content.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:required:in1::field']");
    const submitFn = page.locator("[id='page:mainForm:required:submit1']");

    await inFn.fill("some content");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
    await expect(inFn).toHaveValue("some content");
  });

  test("Length: Submit single character.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:length:in2::field']");
    const submitFn = page.locator("[id='page:mainForm:length:submit2']");

    await inFn.fill("a");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Length: Submit three characters.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:length:in2::field']");
    const submitFn = page.locator("[id='page:mainForm:length:submit2']");

    await inFn.fill("abc");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
  });

  test("Length: Submit five characters.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:length:in2::field']");
    const submitFn = page.locator("[id='page:mainForm:length:submit2']");

    await inFn.fill("abcde");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });
});
