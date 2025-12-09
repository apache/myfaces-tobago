/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {expect, test} from "@playwright/test";

test.describe("900-test/2100-selectManyList/deselect/deselect.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyList/deselect/deselect.xhtml");
  });

  test("tc:selectManyList: deselect", async ({page}) => {
    const reset = page.locator("button[id='page:mainForm:reset']");
    await reset.click();

    const selectManyList = page.locator("tobago-select-many-list[id='page:mainForm:selectManyList']");
    const venus = selectManyList.locator(".btn-group[data-tobago-value='Venus']");
    const earth = selectManyList.locator(".btn-group[data-tobago-value='Earth']");
    const jupiter = selectManyList.locator(".btn-group[data-tobago-value='Jupiter']");
    await expect(venus).toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).toBeVisible();

    await venus.locator(".tobago-button").click();
    await expect(venus).not.toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).toBeVisible();

    await jupiter.locator(".tobago-button").click();
    await expect(venus).not.toBeVisible();
    await expect(earth).toBeVisible();
    await expect(jupiter).not.toBeVisible();
  });
});
