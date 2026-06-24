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

test.describe("900-test/toast/Toast.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/toast/Toast.xhtml");
  });

  test("Update toast", async ({page}) => {
    const toasts = page.locator(".tobago-page-toastStore .toast");
    const createButton = page.locator("[id='page:mainForm:createHeaderlessToast']");

    await createButton.click();
    await expect(toasts).toHaveCount(1);
    await expect(toasts.nth(0).locator(".toast-body")).toBeVisible();
    await expect(toasts.nth(0).locator(".form-control-plaintext"))
        .toHaveText("Toast without header");
    await createButton.click();
    await expect(toasts).toHaveCount(2);
    await expect(toasts.nth(0).locator(".toast-body")).toBeVisible();
    await expect(toasts.nth(0).locator(".form-control-plaintext"))
        .toHaveText("Toast without header");
    await expect(toasts.nth(1).locator(".toast-body")).toBeVisible();
    await expect(toasts.nth(1).locator(".form-control-plaintext"))
        .toHaveText("Toast without header");
    await toasts.nth(0).locator(".tobago-close").click();
    await expect(toasts).toHaveCount(1);
    await toasts.nth(0).locator(".tobago-close").click();
    await expect(toasts).toHaveCount(0);
  });
});
