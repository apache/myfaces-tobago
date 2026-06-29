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

test.describe("400-general-example/310-for-each/For_Each.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/400-general-example/310-for-each/For_Each.xhtml");
  });

  test("Add a river and reset.", async ({page}) => {
    const nameFn = page.locator("[id='page:mainForm:add:inName::field']");
    const lengthFn = page.locator("[id='page:mainForm:add:inLength::field']");
    const dischargeFn = page.locator("[id='page:mainForm:add:inDischarge::field']");
    const addFn = page.locator("[id='page:mainForm:add:buttonAdd']");
    const resetFn = page.locator("[id='page:mainForm:reset:buttonReset']");
    const forEachBoxesFn = page.locator("[id='page:mainForm:forEach'] tobago-box");
    const uiRepeatSectionsFn = page.locator("[id='page:mainForm:uiRepeat'] tobago-section");

    await expect(forEachBoxesFn).toHaveCount(3);
    await expect(uiRepeatSectionsFn).toHaveCount(3);
    await nameFn.fill("Mississippi");
    await lengthFn.fill("6275");
    await dischargeFn.fill("16200");
    await addFn.click();
    await expect(forEachBoxesFn).toHaveCount(4);
    await expect(uiRepeatSectionsFn).toHaveCount(4);
    await resetFn.click();
    await expect(forEachBoxesFn).toHaveCount(3);
    await expect(uiRepeatSectionsFn).toHaveCount(3);
  });
});
