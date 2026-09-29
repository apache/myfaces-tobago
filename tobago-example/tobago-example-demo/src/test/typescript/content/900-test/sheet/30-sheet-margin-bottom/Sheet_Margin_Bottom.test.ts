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

test.describe("900-test/sheet/30-sheet-margin-bottom/Sheet_Margin_Bottom.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/sheet/30-sheet-margin-bottom/Sheet_Margin_Bottom.xhtml");
  });

  test("Margin from button to sheet/paging must be the same", async ({page}) => {
    const sheet1pagingFn = page.locator("[id='page:mainForm:sheet1'] tobago-paginator-page .pagination");
    const button1Fn = page.locator("[id='page:mainForm:button1']");
    const sheet2Fn = page.locator("[id='page:mainForm:sheet2']");
    const button2Fn = page.locator("[id='page:mainForm:button2']");

    const button1Top = (await button1Fn.boundingBox())?.y as number;
    const sheet1pagingTop = (await sheet1pagingFn.boundingBox())?.y as number;
    const sheet1pagingHeight = (await sheet1pagingFn.boundingBox())?.height as number;
    const sheet1Button1Gap = button1Top - (sheet1pagingTop + sheet1pagingHeight);

    const button2Top = (await button2Fn.boundingBox())?.y as number;
    const sheet2Top = (await sheet2Fn.boundingBox())?.y as number;
    const sheet2Height = (await sheet2Fn.boundingBox())?.height as number;
    const sheet2Button2Gap = button2Top - (sheet2Top + sheet2Height);

    expect(sheet1Button1Gap).toBe(sheet2Button2Gap);
  });
});
