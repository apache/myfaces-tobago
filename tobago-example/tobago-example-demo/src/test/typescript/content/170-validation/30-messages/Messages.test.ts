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

test.describe("170-validation/30-messages/Messages.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/170-validation/30-messages/Messages.xhtml");
  });

  test("Press '7 Messages' Button and close the first, the last and the fourth", async ({page}) => {
    const tab = page.locator("[id='page:mainForm:woAttr'] > .nav-link");
    const alerts = page.locator("[id='page:mainForm:woAttr:woAttrMessage'] .alert");
    const closeButtons = page.locator("[id='page:mainForm:woAttr:woAttrMessage'] .alert .btn-close");
    const alertLabels = page.locator("[id='page:mainForm:woAttr:woAttrMessage'] .alert label");
    const messagesButton = page.locator("[id='page:mainForm:add7messages']");

    await expect(alerts).toHaveCount(0);
    await messagesButton.click();
    await expect(alerts).toHaveCount(7);
    await expect(alertLabels.nth(0)).toHaveText("First Message - Info");
    await expect(alertLabels.nth(1)).toHaveText("Second Message - Fatal");
    await expect(alertLabels.nth(2)).toHaveText("Third Message - Warn");
    await expect(alertLabels.nth(3)).toHaveText("Fourth Message - Fatal");
    await expect(alertLabels.nth(4)).toHaveText("Fifth Message - Error");
    await expect(alertLabels.nth(5)).toHaveText("Sixth Message - Info");
    await expect(alertLabels.nth(6)).toHaveText("Seventh Message - Warn");
    await closeButtons.first().click();
    await expect(alerts).toHaveCount(6);
    await expect(alertLabels.nth(0)).toHaveText("Second Message - Fatal");
    await expect(alertLabels.nth(1)).toHaveText("Third Message - Warn");
    await expect(alertLabels.nth(2)).toHaveText("Fourth Message - Fatal");
    await expect(alertLabels.nth(3)).toHaveText("Fifth Message - Error");
    await expect(alertLabels.nth(4)).toHaveText("Sixth Message - Info");
    await expect(alertLabels.nth(5)).toHaveText("Seventh Message - Warn");
    await closeButtons.nth(2).click();
    await expect(alerts).toHaveCount(5);
    await expect(alertLabels.nth(0)).toHaveText("Second Message - Fatal");
    await expect(alertLabels.nth(1)).toHaveText("Third Message - Warn");
    await expect(alertLabels.nth(2)).toHaveText("Fifth Message - Error");
    await expect(alertLabels.nth(3)).toHaveText("Sixth Message - Info");
    await expect(alertLabels.nth(4)).toHaveText("Seventh Message - Warn");
    await closeButtons.nth(4).click();
    await expect(alerts).toHaveCount(4);
    await expect(alertLabels.nth(0)).toHaveText("Second Message - Fatal");
    await expect(alertLabels.nth(1)).toHaveText("Third Message - Warn");
    await expect(alertLabels.nth(2)).toHaveText("Fifth Message - Error");
    await expect(alertLabels.nth(3)).toHaveText("Sixth Message - Info");
  });
});
