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

import {expect, Page, test} from "@playwright/test";

test.describe("900-test/labelLayout/4810-labelLayoutTop/LabelLayoutTop.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/labelLayout/4810-labelLayoutTop/LabelLayoutTop.xhtml");
  });

  test("Check width for tc:date", async ({page}) => await expectWidths(page, "date"));
  test("Check width for tc:file", async ({page}) => await expectWidths(page, "file"));
  test("Check width for tc:in", async ({page}) => await expectWidths(page, "in"));
  test("Check width for input group", async ({page}) => await expectWidths(page, "inGroup"));
  test("Check width for tc:out", async ({page}) => await expectWidths(page, "out"));
  test("Check width for tc:selectBooleanCheckbox", async ({page}) => await expectWidths(page, "selectBooleanCheckbox"));
  test("Check width for tc:selectManyCheckbox", async ({page}) => await expectWidths(page, "selectManyCheckbox"));
  test("Check width for tc:selectManyListbox", async ({page}) => await expectWidths(page, "selectManyListbox"));
  test("Check width for tc:selectManyShuttle", async ({page}) => await expectWidths(page, "selectManyShuttle"));
  test("Check width for tc:selectOneChoice", async ({page}) => await expectWidths(page, "selectOneChoice"));
  test("Check width for tc:selectOneListbox", async ({page}) => await expectWidths(page, "selectOneListbox"));
  test("Check width for tc:selectOneRadio", async ({page}) => await expectWidths(page, "selectOneRadio"));
  test("Check width for tc:textarea", async ({page}) => await expectWidths(page, "textarea"));

  async function expectWidths(page: Page, id: string) {
    const compLabel = page.locator(`[id='page:mainForm:${id}'] label`).first();
    const compTopLabel = page.locator(`[id='page:mainForm:${id}Top'] label`).first();

    const compTopLabelWidth = (await compTopLabel.boundingBox())?.width as number;

    await expect(compLabel).toHaveCSS("width", "155px");
    await expect(compTopLabelWidth).toBeGreaterThanOrEqual(324);
    await expect(compTopLabelWidth).toBeLessThanOrEqual(327);
  }
});
