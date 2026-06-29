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

test.describe("900-test/popup/property-not-writable-exception/PropertyNotWritableException.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/popup/property-not-writable-exception/PropertyNotWritableException.xhtml");
  });

  test("Open popup and press 'Close'.", async ({page}) => {
    const popupFn = page.locator("[id='page:mainForm:popup']");
    const collapseFn = page.locator("[id='page:mainForm:popup::collapse']");
    const openButtonFn = page.locator("[id='page:mainForm:showPopupButton']");
    const closeButtonFn = page.locator("[id='page:mainForm:popup:hidePopupButton']");

    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");

    await openButtonFn.click();
    await expect(popupFn).toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "false");

    await closeButtonFn.click();
    await expect(popupFn).not.toContainClass("show");
    await expect(collapseFn).toHaveAttribute("value", "true");
  });
});
