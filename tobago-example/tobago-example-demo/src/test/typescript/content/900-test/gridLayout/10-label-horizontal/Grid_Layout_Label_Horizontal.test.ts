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

test.describe("900-test/gridLayout/10-label-horizontal/Grid_Layout_Label_Horizontal.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/gridLayout/10-label-horizontal/Grid_Layout_Label_Horizontal.xhtml");
  });

  test("test CSS of the fields and labels", async ({page}) => {
    await checkGridCss(page.locator("[id='page:mainForm:first1']"), "2", "auto", "3", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first1::label']"), "1", "auto", "3", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last1']"), "3", "auto", "3", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last1::label']"), "4", "auto", "3", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first2']"), "1", "auto", "5", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first2::label']"), "2", "auto", "5", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last2']"), "4", "auto", "5", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last2::label']"), "3", "auto", "5", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first3']"), "2", "span 3", "7", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first3::label']"), "1", "auto", "7", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last3']"), "1", "span 3", "8", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last3::label']"), "4", "auto", "8", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first4']"), "2", "span 2", "10", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first4::label']"), "1", "auto", "10", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last4']"), "1", "span 2", "11", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last4::label']"), "3", "auto", "11", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first5']"), "3", "span 2", "13", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first5::label']"), "2", "auto", "13", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last5']"), "2", "span 2", "14", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last5::label']"), "4", "auto", "14", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first6']"), "2", "span 4", "16", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first6::label']"), "1", "auto", "16", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last6']"), "1", "span 4", "17", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last6::label']"), "5", "auto", "17", "auto");
  });

  async function checkGridCss(element: Locator, columnStart: string, columnEnd: string, rowStart: string, rowEnd: string): Promise<void> {
    await expect(element).toHaveCSS("grid-column-start", columnStart);
    await expect(element).toHaveCSS("grid-column-end", columnEnd);
    await expect(element).toHaveCSS("grid-row-start", rowStart);
    await expect(element).toHaveCSS("grid-row-end", rowEnd);
  }
});
