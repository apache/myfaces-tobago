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

test.describe("900-test/gridLayout/200-labelLayout-ajax/GridLabelLayout_Ajax.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/gridLayout/200-labelLayout-ajax/GridLabelLayout_Ajax.xhtml");
  });

  test("Ajax rendering of labelLayout=gridLeft", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridLeft']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });

  test("Ajax rendering of labelLayout=gridRight", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridRight']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });

  test("Ajax rendering of labelLayout=gridTop", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridTop']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });

  test("Ajax rendering of labelLayout=gridBottom", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridBottom']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });

  test("Ajax rendering of labelLayout=gridLeft without label attribute", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridLeftNoLabel']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });

  test("Ajax rendering of the whole grid layout component", async ({page}) => {
    const timestamp = page.locator("[id='page:mainForm:timestamp'] .form-control-plaintext");
    let timestampValue: number = Number(await timestamp.textContent());
    await page.locator("[id='page:mainForm:ajaxGridLayout']").click();
    await expect.poll(async () => Number(await timestamp.textContent())).toBeGreaterThan(timestampValue);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::label']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridRightInput::field']")).toHaveCount(1);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::label']")).toHaveCount(0);
    await expect(page.locator("[id='page:mainForm:gridLeftNoLabel::field']")).toHaveCount(1);
  });
});
