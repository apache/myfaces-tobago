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

test.describe("900-test/gridLayout/20-label-vertical/Grid_Layout_Label_Vertical.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/gridLayout/20-label-vertical/Grid_Layout_Label_Vertical.xhtml");
  });

  test("test CSS of the fields and labels", async ({page}) => {
    await checkGridCss(page.locator("[id='page:mainForm:first1']"), "1", "auto", "2", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:first1::label']"), "1", "auto", "1", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last1']"), "2", "auto", "1", "auto");
    await checkGridCss(page.locator("[id='page:mainForm:last1::label']"), "2", "auto", "2", "auto");
  });

  async function checkGridCss(element: Locator, columnStart: string, columnEnd: string, rowStart: string, rowEnd: string): Promise<void> {
    await expect(element).toHaveCSS("grid-column-start", columnStart);
    await expect(element).toHaveCSS("grid-column-end", columnEnd);
    await expect(element).toHaveCSS("grid-row-start", rowStart);
    await expect(element).toHaveCSS("grid-row-end", rowEnd);
  }
});
