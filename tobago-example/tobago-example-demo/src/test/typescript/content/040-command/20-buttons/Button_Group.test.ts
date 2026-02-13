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

test.describe("040-command/20-buttons/Button_Group.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/040-command/20-buttons/Button_Group.xhtml");
  });

  test("Split button has rounded corners", async ({page}) => {
    const main = page.locator("[id='page:mainForm:mainPrimary']");
    const split = page.locator("[id='page:mainForm:splitPrimary::command']");

    await expect(main).toHaveCSS("border-top-left-radius", "6px");
    await expect(main).toHaveCSS("border-bottom-left-radius", "6px");
    await expect(main).toHaveCSS("border-top-right-radius", "0px");
    await expect(main).toHaveCSS("border-bottom-right-radius", "0px");
    await expect(split).toHaveCSS("border-top-left-radius", "0px");
    await expect(split).toHaveCSS("border-bottom-left-radius", "0px");
    await expect(split).toHaveCSS("border-top-right-radius", "6px");
    await expect(split).toHaveCSS("border-bottom-right-radius", "6px");
  });
});
