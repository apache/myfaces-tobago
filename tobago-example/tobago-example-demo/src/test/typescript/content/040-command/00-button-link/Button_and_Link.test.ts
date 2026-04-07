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

test.describe("040-command/00-button-link/Button_and_Link.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/040-command/00-button-link/Button_and_Link.xhtml");
  });

  test("Spaces between buttons", async ({page}) => {
    const b1 = page.locator("[id='page:mainForm:b1']");
    const b2 = page.locator("[id='page:mainForm:b2']");
    const b4 = page.locator("[id='page:mainForm:b4']");
    const b5 = page.locator("[id='page:mainForm:b5']");

    const b1Rect = await b1.evaluate(el => el.getBoundingClientRect());
    const b2Rect = await b2.evaluate(el => el.getBoundingClientRect());
    const b4Rect = await b4.evaluate(el => el.getBoundingClientRect());
    const b5Rect = await b5.evaluate(el => el.getBoundingClientRect());

    expect(b1Rect.right).toBeLessThan(b2Rect.left);
    expect(b2Rect.right).toBeLessThan(b4Rect.left);
    expect(b4Rect.right).toBeLessThan(b5Rect.left);
  });
});
