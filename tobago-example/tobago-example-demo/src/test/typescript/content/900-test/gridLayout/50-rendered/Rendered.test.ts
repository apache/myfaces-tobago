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

test.describe("900-test/gridLayout/50-rendered/Rendered.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/gridLayout/50-rendered/Rendered.xhtml");
  });

  test("Position of components in grid layout", async ({page}) => {
  const reference = page.locator("[id='page:mainForm:referenceIn']");
  const referenceRect = await reference.evaluate(el => el.getBoundingClientRect());
  const in1 = page.locator("[id='page:mainForm:in1']");
  const in2 = page.locator("[id='page:mainForm:in2']");
  const in2Rect = await in2.evaluate(el => el.getBoundingClientRect());
  const in3 = page.locator("[id='page:mainForm:in3']");
  const in3Rect = await in3.evaluate(el => el.getBoundingClientRect());
  const in4 = page.locator("[id='page:mainForm:in4']");
  const in4Rect = await in4.evaluate(el => el.getBoundingClientRect());

  await expect(in1).toBeHidden();
  expect(referenceRect.width).toBeGreaterThan(in2Rect.width);
  expect(referenceRect.width).toBeGreaterThan(in3Rect.width);
  expect(referenceRect.width).toBeGreaterThan(in4Rect.width);
  expect(in2Rect.left).toBe(referenceRect.left);
  expect(in3Rect.right).toBe(referenceRect.right);
  expect(in4Rect.left).toBe(referenceRect.left);

  await expect(in2).toHaveCSS("grid-column-start", "1");
  await expect(in2).toHaveCSS("grid-column-end", "auto");
  await expect(in2).toHaveCSS("grid-row-start", "1");
  await expect(in2).toHaveCSS("grid-row-end", "auto");

  await expect(in3).toHaveCSS("grid-column-start", "2");
  await expect(in3).toHaveCSS("grid-column-end", "auto");
  await expect(in3).toHaveCSS("grid-row-start", "1");
  await expect(in3).toHaveCSS("grid-row-end", "auto");

  await expect(in4).toHaveCSS("grid-column-start", "1");
  await expect(in4).toHaveCSS("grid-column-end", "auto");
  await expect(in4).toHaveCSS("grid-row-start", "2");
  await expect(in4).toHaveCSS("grid-row-end", "auto");
});
});
