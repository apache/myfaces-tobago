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

test.describe("130-collapsible/30-collapsible-section/Collapsible_Section.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/130-collapsible/30-collapsible-section/Collapsible_Section.xhtml");
  });

  test("Simple Section: submit empty input, hide, submit, show, submit valid input", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:simple:showSimple']");
    const hide = page.locator("[id='page:mainForm:simple:hideSimple']");
    const sectionCollapsed = page.locator("[id='page:mainForm:simple:simpleSection::collapse']");
    const input = page.locator("[id='page:mainForm:simple:inSimple::field']");
    const submit = page.locator("[id='page:mainForm:simple:submitSimple']");

    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();
    await expect(messages).toHaveCount(0);
    await input.fill("");
    await submit.click();
    await expect(messages).toHaveCount(1);

    await hide.click();
    await expect(sectionCollapsed).toHaveValue("true");
    await expect(input).not.toBeVisible();

    await submit.click();
    await expect(messages).toHaveCount(0);

    await show.click();
    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill("Tobago");
    await submit.click();
    await expect(messages).toHaveCount(0);
  });

  test("Full Server Request: submit empty input, hide, submit, show, submit valid input", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:server:showServer']");
    const hide = page.locator("[id='page:mainForm:server:hideServer']");
    const sectionCollapsed = page.locator("[id='page:mainForm:server:serverSection::collapse']");
    const input = page.locator("[id='page:mainForm:server:inServer::field']");
    const submit = page.locator("[id='page:mainForm:server:submitServer']");

    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();
    await expect(messages).toHaveCount(0);
    await input.fill("");
    await submit.click();
    await expect(messages).toHaveCount(1);

    await hide.click();
    await expect(sectionCollapsed).toHaveValue("true");
    await expect(input).not.toBeVisible();

    await submit.click();
    await expect(messages).toHaveCount(0);

    await show.click();
    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill("Tobago");
    await submit.click();
    await expect(messages).toHaveCount(0);
  });

  test("Client Sided: submit empty input, hide, submit, show, submit valid input", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:client:showClient']");
    const hide = page.locator("[id='page:mainForm:client:hideClient']");
    const submit = page.locator("[id='page:mainForm:client:submitClient']");
    const sectionCollapsed = page.locator("[id='page:mainForm:client:clientSection::collapse']");
    const input = page.locator("[id='page:mainForm:client:inClient::field']");

    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();
    await expect(messages).toHaveCount(0);
    await input.fill("");
    await submit.click();
    await expect(messages).toHaveCount(1);

    await hide.click();
    await expect(sectionCollapsed).toHaveValue("true");
    await expect(input).not.toBeVisible();

    await submit.click();
    await expect(messages).toHaveCount(1);

    await show.click();
    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill("Tobago");
    await submit.click();
    await expect(messages).toHaveCount(0);
  });

  test("Ajax: submit empty input, hide, submit, show, submit valid input", async ({page}) => {
    const messages = page.locator("[id='page:messages'] .alert");
    const show = page.locator("[id='page:mainForm:ajax:showAjax']");
    const hide = page.locator("[id='page:mainForm:ajax:hideAjax']");
    const sectionCollapsed = page.locator("[id='page:mainForm:ajax:ajaxSection::collapse']");
    const input = page.locator("[id='page:mainForm:ajax:inAjax::field']");
    const submit = page.locator("[id='page:mainForm:ajax:submitAjax']");

    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();
    await expect(messages).toHaveCount(0);
    await input.fill("");
    await submit.click();
    await expect(messages).toHaveCount(1);

    await hide.click();
    await expect(sectionCollapsed).toHaveValue("true");
    await expect(input).not.toBeVisible();

    await submit.click();
    await expect(messages).toHaveCount(0);

    await show.click();
    await expect(sectionCollapsed).toHaveValue("false");
    await expect(input).toBeVisible();

    await input.fill("Tobago");
    await submit.click();
    await expect(messages).toHaveCount(0);
  });
});
