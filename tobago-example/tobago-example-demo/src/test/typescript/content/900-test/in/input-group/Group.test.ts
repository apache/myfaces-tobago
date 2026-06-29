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

test.describe("900-test/in/input-group/Group.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/in/input-group/Group.xhtml");
  });

  test("border-radius", async ({page}) => {
    const dropdown1Fn = page.locator("[id='page:mainForm:dd1::command']");
    const dropdown2Fn = page.locator("[id='page:mainForm:dd2::command']");
    const dropdown3Fn = page.locator("[id='page:mainForm:dd3::command']");
    const dropdown4Fn = page.locator("[id='page:mainForm:dd4::command']");

    await expect(dropdown1Fn).not.toHaveCSS("border-top-left-radius", "0px");
    await expect(dropdown1Fn).toHaveCSS("border-top-right-radius", "0px");
    await expect(dropdown1Fn).not.toHaveCSS("border-bottom-left-radius", "0px");
    await expect(dropdown1Fn).toHaveCSS("border-bottom-right-radius", "0px");

    await expect(dropdown2Fn).toHaveCSS("border-top-left-radius", "0px");
    await expect(dropdown2Fn).toHaveCSS("border-top-right-radius", "0px");
    await expect(dropdown2Fn).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(dropdown2Fn).toHaveCSS("border-bottom-right-radius", "0px");

    await expect(dropdown3Fn).toHaveCSS("border-top-left-radius", "0px");
    await expect(dropdown3Fn).toHaveCSS("border-top-right-radius", "0px");
    await expect(dropdown3Fn).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(dropdown3Fn).toHaveCSS("border-bottom-right-radius", "0px");

    await expect(dropdown4Fn).toHaveCSS("border-top-left-radius", "0px");
    await expect(dropdown4Fn).not.toHaveCSS("border-top-right-radius", "0px");
    await expect(dropdown4Fn).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(dropdown4Fn).not.toHaveCSS("border-bottom-right-radius", "0px");
  });
});
