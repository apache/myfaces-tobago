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

test.describe("080-sheet/70-tree/Sheet_Tree.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/70-tree/Sheet_Tree.xhtml");
  });

  test("Collapse tree", async ({page}) => {
    const row0nameFn = page.locator("[id='page:mainForm:sheet:0:nameOut']");
    const row0centralBodyFn = page.locator("[id='page:mainForm:sheet:0:centralBodyOut']");
    const row0distanceFn = page.locator("[id='page:mainForm:sheet:0:distanceOut']");
    const row0periodFn = page.locator("[id='page:mainForm:sheet:0:periodOut']");
    const row0discovererFn = page.locator("[id='page:mainForm:sheet:0:discovererOut']");
    const row0yearFn = page.locator("[id='page:mainForm:sheet:0:yearOut']");
    const row1 = page.locator("[id='page:mainForm:sheet'] .tobago-body tr[row-index='1']");
    const row1nameFn = page.locator("[id='page:mainForm:sheet:1:nameOut']");
    const row1centralBodyFn = page.locator("[id='page:mainForm:sheet:1:centralBodyOut']");
    const row1distanceFn = page.locator("[id='page:mainForm:sheet:1:distanceOut']");
    const row1periodFn = page.locator("[id='page:mainForm:sheet:1:periodOut']");
    const row1discovererFn = page.locator("[id='page:mainForm:sheet:1:discovererOut']");
    const row1yearFn = page.locator("[id='page:mainForm:sheet:1:yearOut']");
    const rootTreeButtonFn = page.locator("[id='page:mainForm:sheet:0:nameCol'] .tobago-toggle");

    await expect(row0nameFn).toHaveText("Sun");
    await expect(row0centralBodyFn).toHaveText("-");
    await expect(row0distanceFn).toHaveText("0");
    await expect(row0periodFn).toHaveText("0.0");
    await expect(row0discovererFn).toHaveText("-");
    await expect(row0yearFn).toHaveText("");
    await expect(row1nameFn).toHaveText("Mercury");
    await expect(row1centralBodyFn).toHaveText("Sun");
    await expect(row1distanceFn).toHaveText("57910");
    await expect(row1periodFn).toHaveText("87.97");
    await expect(row1discovererFn).toHaveText("-");
    await expect(row1yearFn).toHaveText("");
    await expect(row1).not.toContainClass("d-none");
    await expect(row1).not.toHaveCSS("display", "none");
    await rootTreeButtonFn.click();
    await expect(row1).toContainClass("d-none");
    await expect(row1).toHaveCSS("display", "none");
    await expect(row0centralBodyFn).toHaveText("-");
    await expect(row0distanceFn).toHaveText("0");
    await expect(row0periodFn).toHaveText("0.0");
    await expect(row0discovererFn).toHaveText("-");
    await expect(row0yearFn).toHaveText("");
  });
});
