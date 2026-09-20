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

test.describe("900-test/labelLayout/200-input-heigth/Input_Height.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/labelLayout/200-input-heigth/Input_Height.xhtml");
  });

  test("height of input fields must be the same", async ({page}) => {
    const first = page.locator("[id='page:mainForm:first::field']");
    const second = page.locator("[id='page:mainForm:second::field']");

    const firstHeight = (await first.boundingBox())?.height as number;
    const secondHeight = (await second.boundingBox())?.height as number;
    await expect(secondHeight).toBe(firstHeight);
  });
});
