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

test.describe("900-test/sheet/52_sheet-stop-propagation/Sheet_stop_propagation.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/52_sheet-stop-propagation/Sheet_stop_propagation.xhtml");
  });

  test("Test stop-propagation attribute", async ({page}) => {
    const plain = page.locator("[id='page:mainForm:spPlain'] tobago-behavior");
    const ajax = page.locator("[id='page:mainForm:spAjax'] tobago-behavior");
    const eventFalse = page.locator("[id='page:mainForm:spEventFalse'] tobago-behavior");
    const eventTrue = page.locator("[id='page:mainForm:spEventTrue'] tobago-behavior");
    const eventFalseAjax = page.locator("[id='page:mainForm:spEventFalseAjax'] tobago-behavior");
    const eventTrueAjax = page.locator("[id='page:mainForm:spEventTrueAjax'] tobago-behavior");

    const sheetPlain = page.locator("[id='page:mainForm:sheet:sheetSpPlain'] tobago-behavior");
    const sheetAjax = page.locator("[id='page:mainForm:sheet:sheetSpAjax'] tobago-behavior");
    const sheetEventFalse = page.locator("[id='page:mainForm:sheet:sheetSpEventFalse'] tobago-behavior");
    const sheetEventTrue = page.locator("[id='page:mainForm:sheet:sheetSpEventTrue'] tobago-behavior");
    const sheetEventFalseAjax = page.locator("[id='page:mainForm:sheet:sheetSpEventFalseAjax'] tobago-behavior");
    const sheetEventTrueAjax = page.locator("[id='page:mainForm:sheet:sheetSpEventTrueAjax'] tobago-behavior");

    await expect(plain).not.toHaveAttribute("stop-propagation");
    await expect(ajax).not.toHaveAttribute("stop-propagation");
    await expect(eventFalse).not.toHaveAttribute("stop-propagation");
    await expect(eventTrue).toHaveAttribute("stop-propagation");
    await expect(eventFalseAjax).not.toHaveAttribute("stop-propagation");
    await expect(eventTrueAjax).toHaveAttribute("stop-propagation");

    await expect(sheetPlain).not.toHaveAttribute("stop-propagation");
    await expect(sheetAjax).not.toHaveAttribute("stop-propagation");
    await expect(sheetEventFalse).not.toHaveAttribute("stop-propagation");
    await expect(sheetEventTrue).toHaveAttribute("stop-propagation");
    await expect(sheetEventFalseAjax).not.toHaveAttribute("stop-propagation");
    await expect(sheetEventTrueAjax).toHaveAttribute("stop-propagation");
  });
});
