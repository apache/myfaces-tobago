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

test.describe("900-test/z-misc/ajax/execute/Execute.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/ajax/execute/Execute.xhtml");
  });

  test("ajax execute", async ({page}) => {
    const in1Fn = page.locator("[id='page:mainForm:in1::field']");
    const in2Fn = page.locator("[id='page:mainForm:in2::field']");
    const in3Fn = page.locator("[id='page:mainForm:in3::field']");
    const in4Fn = page.locator("[id='page:mainForm:in4::field']");
    const clearButtonFn = page.locator("[id='page:mainForm:clear']");
    const submitButtonFn = page.locator("[id='page:mainForm:submit']");
    const reloadButtonFn = page.locator("[id='page:mainForm:reload']");

    await expect(in1Fn).toHaveValue("");
    await expect(in2Fn).toHaveValue("");
    await expect(in3Fn).toHaveValue("");
    await expect(in4Fn).toHaveValue("");

    await in1Fn.fill("Alice");
    await in2Fn.fill("Bob");
    await in3Fn.fill("Charlie");
    await in4Fn.fill("Dave");

    await submitButtonFn.click();
    await expect(in1Fn).toHaveValue("Alice");
    await expect(in2Fn).toHaveValue("Bob");
    await expect(in3Fn).toHaveValue("Charlie");
    await expect(in4Fn).toHaveValue("");

    await reloadButtonFn.click();
    await expect(in1Fn).toHaveValue("Alice");
    await expect(in2Fn).toHaveValue("");
    await expect(in3Fn).toHaveValue("Charlie");
    await expect(in4Fn).toHaveValue("");
  });
});
