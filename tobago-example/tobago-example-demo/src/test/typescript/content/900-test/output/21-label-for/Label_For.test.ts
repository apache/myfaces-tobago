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

test.describe("900-test/output/21-label-for/Label_For.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/output/21-label-for/Label_For.xhtml");
  });

  test("Test for required CSS class", async ({page}) => {
    const inLabel = page.locator("[id='page:mainForm:inLabel']");
    const dateLabel = page.locator("[id='page:mainForm:dateLabel']");
    const fileLabel = page.locator("[id='page:mainForm:fileLabel']");
    const textareaLabel = page.locator("[id='page:mainForm:textareaLabel']");
    const selectBooleanCheckboxLabel = page.locator("[id='page:mainForm:selectBooleanCheckboxLabel']");
    const selectBooleanToggleLabel = page.locator("[id='page:mainForm:selectBooleanToggleLabel']");
    const selectOneRadioLabel = page.locator("[id='page:mainForm:selectOneRadioLabel']");
    const selectManyCheckboxLabel = page.locator("[id='page:mainForm:selectManyCheckboxLabel']");
    const selectOneChoiceLabel = page.locator("[id='page:mainForm:selectOneChoiceLabel']");
    const selectOneListboxLabel = page.locator("[id='page:mainForm:selectOneListboxLabel']");
    const selectManyListboxLabel = page.locator("[id='page:mainForm:selectManyListboxLabel']");
    const selectManyShuttleLabel = page.locator("[id='page:mainForm:selectManyShuttleLabel']");
    const starsLabel = page.locator("[id='page:mainForm:starsLabel']");
    const labelForIdOne = page.locator("[id='page:mainForm:labelForIdOne']");
    const labelForIdTwo = page.locator("[id='page:mainForm:labelForIdTwo']");

    await expect(inLabel).toContainClass("tobago-required");
    await expect(dateLabel).toContainClass("tobago-required");
    await expect(fileLabel).toContainClass("tobago-required");
    await expect(textareaLabel).toContainClass("tobago-required");
    await expect(selectBooleanCheckboxLabel).toContainClass("tobago-required");
    await expect(selectBooleanToggleLabel).toContainClass("tobago-required");
    await expect(selectOneRadioLabel).toContainClass("tobago-required");
    await expect(selectManyCheckboxLabel).toContainClass("tobago-required");
    await expect(selectOneChoiceLabel).toContainClass("tobago-required");
    await expect(selectOneListboxLabel).toContainClass("tobago-required");
    await expect(selectManyListboxLabel).toContainClass("tobago-required");
    await expect(selectManyShuttleLabel).toContainClass("tobago-required");
    await expect(starsLabel).toContainClass("tobago-required");
    await expect(labelForIdOne).toHaveAttribute("for", "page:mainForm:id1::field");
    await expect(labelForIdTwo).toHaveAttribute("for", "page:mainForm:id2::field");
  });
});
