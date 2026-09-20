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

test.describe("900-test/z-misc/form-check/form-check.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/form-check/form-check.xhtml");
  });

  test("Required fields", async ({page}) => {
    await testFormChecks(
        page.locator("[id='page:mainForm:selectBooleanCheckbox::field']"),
        page.locator("[id='page:mainForm:selectBooleanCheckboxChecked::field']"),
        page.locator("[id='page:mainForm:selectBooleanCheckboxDisabled::field']"),
        page.locator("[id='page:mainForm:selectBooleanCheckboxCheckedDisabled::field']")
    );

    await testFormChecks(
        page.locator("[id='page:mainForm:selectBooleanToggle::field']"),
        page.locator("[id='page:mainForm:selectBooleanToggleChecked::field']"),
        page.locator("[id='page:mainForm:selectBooleanToggleDisabled::field']"),
        page.locator("[id='page:mainForm:selectBooleanToggleCheckedDisabled::field']")
    );

    await testFormChecks(
        page.locator("[id='page:mainForm:selectOneRadio::0']"),
        page.locator("[id='page:mainForm:selectOneRadio::1']"),
        page.locator("[id='page:mainForm:selectOneRadioDisabled::0']"),
        page.locator("[id='page:mainForm:selectOneRadioDisabled::1']")
    );

    await testFormChecks(
        page.locator("[id='page:mainForm:selectManyCheckbox::0']"),
        page.locator("[id='page:mainForm:selectManyCheckbox::1']"),
        page.locator("[id='page:mainForm:selectManyCheckbox::2']"),
        page.locator("[id='page:mainForm:selectManyCheckbox::3']")
    );

    await testFormChecks(
        page.locator("input[name='page:mainForm:sheetMulti_data_row_selector_0']"),
        page.locator("input[name='page:mainForm:sheetMulti_data_row_selector_1']"),
        page.locator("input[name='page:mainForm:sheetMulti_data_row_selector_2']"),
        page.locator("input[name='page:mainForm:sheetMulti_data_row_selector_3']"),
    );

    await testFormChecks(
        page.locator("input[name='page:mainForm:sheetSingle_data_row_selector_0']"),
        page.locator("input[name='page:mainForm:sheetSingle_data_row_selector_1']"),
        page.locator("input[name='page:mainForm:sheetSingle_data_row_selector_2']"),
        page.locator("input[name='page:mainForm:sheetSingle_data_row_selector_3']"),
    );

    await testFormChecks(
        page.locator("input[name='page:mainForm:autoWidthColSelHeadEnabledUnchecked::columnSelector']"),
        page.locator("input[name='page:mainForm:autoWidthColSelHeadEnabledChecked::columnSelector']"),
        page.locator("input[name='page:mainForm:autoWidthColSelHeadDisabledUnchecked::columnSelector']"),
        page.locator("input[name='page:mainForm:autoWidthColSelHeadDisabledChecked::columnSelector']"),
    );

    await testFormChecks(
        page.locator("input[name='page:mainForm:customWidthColSelHeadEnabledUnchecked::columnSelector']"),
        page.locator("input[name='page:mainForm:customWidthColSelHeadEnabledChecked::columnSelector']"),
        page.locator("input[name='page:mainForm:customWidthColSelHeadDisabledUnchecked::columnSelector']"),
        page.locator("input[name='page:mainForm:customWidthColSelHeadDisabledChecked::columnSelector']"),
    );
  });

  async function testFormChecks(
      uncheckedEnabled: Locator, checkedEnabled: Locator, uncheckedDisabled: Locator, checkedDisabled: Locator) {
    const white = "rgb(255, 255, 255)";
    const blue = "rgb(13, 110, 253)";
    const gray = "rgb(222, 226, 230)";
    const grayTransparent = "rgba(222, 226, 230, 0.5)";

    await expect(uncheckedEnabled).toHaveCSS("background-color", white);
    await expect(uncheckedEnabled).toHaveCSS("border-color", gray);
    await expect(uncheckedEnabled).toHaveCSS("opacity", "1");

    await expect(checkedEnabled).toHaveCSS("background-color", blue);
    await expect(checkedEnabled).toHaveCSS("border-color", blue);
    await expect(checkedEnabled).toHaveCSS("opacity", "1");

    await expect(uncheckedDisabled).toHaveCSS("background-color", grayTransparent);
    await expect(uncheckedDisabled).toHaveCSS("border-color", gray);
    await expect(uncheckedDisabled).toHaveCSS("opacity", "0.5");

    await expect(checkedDisabled).toHaveCSS("background-color", blue);
    await expect(checkedDisabled).toHaveCSS("border-color", blue);
    await expect(checkedDisabled).toHaveCSS("opacity", "0.5");
  }
});
