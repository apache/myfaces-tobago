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

test.describe("900-test/z-misc/java/rendererBase-getCurrentValue/RendererBase_GetCurrentValue.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/java/rendererBase-getCurrentValue/RendererBase_GetCurrentValue.xhtml");
  });

  test("formatted values: out string", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outString']")).toHaveText("simple string");
  });
  test("formatted values: out string from method", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outStringFromMethod']")).toHaveText("HELLO WORLD!");
  });
  test("formatted values: out date", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outDate']")).toHaveText("24.07.1969");
  });
  test("formatted values: out date from method", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outDateFromMethod']")).toHaveText("24.07.2019");
  });
  test("formatted values: out currency", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outCurrency']")).toHaveText("TTD");
  });
  test("formatted values: out currency from method", async ({page}) => {
    await expect(page.locator("[id='page:mainForm:outCurrencyFromMethod']")).toHaveText("ISK");
  });
});
