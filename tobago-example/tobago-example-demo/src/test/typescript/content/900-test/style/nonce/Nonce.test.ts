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

test.describe("900-test/style/nonce/Nonce.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/style/nonce/Nonce.xhtml");
  });

  test("height by style tag normal", async ({page}) => {
    const box1Fn = page.locator("[id='page:mainForm:box1']");
    await expect(box1Fn).toHaveCSS("height", "300px");
  });

  test("height by style tag ajax", async ({page}) => {
    const box2Fn = page.locator("[id='page:mainForm:box2']");
    const ajaxFn = page.locator("[id='page:mainForm:ajax']");
    const box2titleFn = page.locator("[id='page:mainForm:box2'] h3");

    await expect(box2Fn).toHaveCSS("height", "300px");
    await ajaxFn.click();
    await expect(box2titleFn).toHaveText("Box ajax=true");
    await expect(box2Fn).toHaveCSS("height", "300px");
  });

  test("height by style tag fake", async ({page}) => {
    const box3Fn = page.locator("[id='page:mainForm:box3']");
    await expect(box3Fn).not.toHaveCSS("height", "300px");
  });
});
