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

test.describe("130-collapsible/05-collapsible-box/Collapsible_Box.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/130-collapsible/05-collapsible-box/Collapsible_Box.xhtml");
  });

  test("Simple Collapsible Box: hide, show", async ({page}) => {
    const show = page.locator("[id='page:mainForm:controller:show']");
    const hide = page.locator("[id='page:mainForm:controller:hide']");
    const content = page.locator("[id='page:mainForm:controller:content']");

    await expect(content).toBeAttached();
    await expect(content).toBeVisible();
    await hide.click();
    await expect(content).not.toBeAttached();
    await expect(content).not.toBeVisible();
    await show.click();
    await expect(content).toBeAttached();
    await expect(content).toBeVisible();
  });

  test("Full Server Request: close box 1, close box 2, open box 1, open box 2", async ({page}) => {
    const show1 = page.locator("[id='page:mainForm:server:show1']");
    const show2 = page.locator("[id='page:mainForm:server:show2']");
    const hide1 = page.locator("[id='page:mainForm:server:hide1']");
    const hide2 = page.locator("[id='page:mainForm:server:hide2']");
    const content1 = page.locator("[id='page:mainForm:server:content1']");
    const content2 = page.locator("[id='page:mainForm:server:content2']");

    await expect(content1).toBeAttached();
    await expect(content1).toBeVisible();
    await expect(content2).toBeAttached();
    await expect(content2).toBeVisible();
    await hide1.click();
    await expect(content1).not.toBeAttached();
    await expect(content1).not.toBeVisible();
    await expect(content2).toBeAttached();
    await expect(content2).toBeVisible();
    await hide2.click();
    await expect(content1).not.toBeAttached();
    await expect(content1).not.toBeVisible();
    await expect(content2).not.toBeAttached();
    await expect(content2).not.toBeVisible();
    await show1.click();
    await expect(content1).toBeAttached();
    await expect(content1).toBeVisible();
    await expect(content2).not.toBeAttached();
    await expect(content2).not.toBeVisible();
    await show2.click();
    await expect(content1).toBeAttached();
    await expect(content1).toBeVisible();
    await expect(content2).toBeAttached();
    await expect(content2).toBeVisible();
  });

  test("Client Side: show, hide, submit empty string", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:client:showNoRequestBox']");
    const hide = page.locator("[id='page:mainForm:client:hideNoRequestBox']");
    const box = page.locator("[id='page:mainForm:client:noRequestBox']");
    const submit = page.locator("[id='page:mainForm:client:submitNoRequestBox']");

    await expect(box).toContainClass("tobago-collapsed");
    await show.click();
    await expect(box).not.toContainClass("tobago-collapsed");
    await hide.click();
    await expect(box).toContainClass("tobago-collapsed");
    await submit.click();
    await expect(messages).toHaveCount(1);
  });

  test("Ajax: hide, submit, show, submit", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:ajax:showAjaxBox']");
    const hide = page.locator("[id='page:mainForm:ajax:hideAjaxBox']");
    const input = page.locator("[id='page:mainForm:ajax:inAjaxBox::field']");
    const submit = page.locator("[id='page:mainForm:ajax:submitAjaxBox']");

    await expect(input).toBeVisible();
    await hide.click();
    await expect(input).not.toBeVisible();
    await submit.click();
    await expect(messages).toHaveCount(0);
    await show.click();
    await expect(input).toBeVisible();
    await submit.click();
    await expect(messages).toHaveCount(1);
  });
});
