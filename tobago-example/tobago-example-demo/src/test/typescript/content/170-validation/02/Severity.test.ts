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

test.describe("170-validation/02/Severity.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/170-validation/02/Severity.xhtml");
  });

  test("Check severity CSS classes", async ({page}) => {
    const alerts = page.locator("[id='page:messages'] .alert");
    const submit = page.locator("[id='page:mainForm:submit']");
    const fatalInputField = page.locator("[id='page:mainForm:fatal::field']");
    const errorInputField = page.locator("[id='page:mainForm:error::field']");
    const warnInputField = page.locator("[id='page:mainForm:warn::field']");
    const infoInputField = page.locator("[id='page:mainForm:info::field']");
    const fatalButton = page.locator("[id='page:mainForm:fatal'] tobago-popover .btn");
    const errorButton = page.locator("[id='page:mainForm:error'] tobago-popover .btn");
    const warnButton = page.locator("[id='page:mainForm:warn'] tobago-popover .btn");
    const infoButton = page.locator("[id='page:mainForm:info'] tobago-popover .btn");

    await submit.click();
    await expect(alerts).not.toHaveCount(0);
    await expect(fatalInputField).toContainClass("is-error");
    await expect(errorInputField).toContainClass("is-error");
    await expect(warnInputField).toContainClass("is-warning");
    await expect(infoInputField).toContainClass("is-info");
    await expect(fatalButton).toContainClass("btn-danger");
    await expect(errorButton).toContainClass("btn-danger");
    await expect(warnButton).toContainClass("btn-warning");
    await expect(infoButton).toContainClass("btn-info");
  });
});
