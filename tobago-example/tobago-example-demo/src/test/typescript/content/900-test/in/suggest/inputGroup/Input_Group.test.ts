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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/in/suggest/inputGroup/Input_Group.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/in/suggest/inputGroup/Input_Group.xhtml");
  });

  test("before input group", async ({page}) => {
    await expectRadius(page.locator("[id='page:mainForm:beforeInputGroup'] .input-group-text"), true, true, false, false);
    await expectRadius(page.locator("[id='page:mainForm:beforeInputGroup::field']"), false, false, true, true);
  });

  test("after input group", async ({page}) => {
    await expectRadius(page.locator("[id='page:mainForm:afterInputGroup::field']"), true, true, false, false);
    await expectRadius(page.locator("[id='page:mainForm:afterInputGroup'] .input-group-text"), false, false, true, true);
  });

  test("before and after input group", async ({page}) => {
    await expectRadius(page.locator("[id='page:mainForm:bothInputGroup'] .input-group-text:first-child"), true, true, false, false);
    await expectRadius(page.locator("[id='page:mainForm:bothInputGroup::field']"), false, false, false, false);
    await expectRadius(page.locator("[id='page:mainForm:bothInputGroup'] .input-group-text:last-child"), false, false, true, true);
  });
});

async function expectRadius(element: Locator, topLeft: boolean, buttomLeft: boolean, topRight: boolean, bottomRight: boolean) {
  await expect(element).toHaveCSS("border-top-left-radius", topLeft ? /^[1-9]\d*px$/ : "0px");
  await expect(element).toHaveCSS("border-bottom-left-radius", buttomLeft ? /^[1-9]\d*px$/ : "0px");
  await expect(element).toHaveCSS("border-top-right-radius", topRight ? /^[1-9]\d*px$/ : "0px");
  await expect(element).toHaveCSS("border-bottom-right-radius", bottomRight ? /^[1-9]\d*px$/ : "0px");
}
