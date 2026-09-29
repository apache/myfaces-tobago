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

test.describe("900-test/labelLayout/300-label-input-spacing/Label_Input_Spacing.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/labelLayout/300-label-input-spacing/Label_Input_Spacing.xhtml");
  });

  test("Spacing between input and label", async ({page}) => {
    const labelInputLeft = page.locator("label[for='page:mainForm:inputLeft::field']");
    const labelInputRight = page.locator("label[for='page:mainForm:inputRight::field']");
    const labelSuggestLeft = page.locator("label[for='page:mainForm:suggestLeft::field']");
    const labelSuggestRight = page.locator("label[for='page:mainForm:suggestRight::field']");

    await expect(labelInputLeft).toHaveCSS("margin-right", "8px");
    await expect(labelInputRight).toHaveCSS("margin-left", "8px");
    await expect(labelSuggestLeft).toHaveCSS("margin-right", "8px");
    await expect(labelSuggestRight).toHaveCSS("margin-left", "8px");
  });
});
