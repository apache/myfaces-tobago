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

test.describe("900-test/selectOneChoice/SelectOneChoice.xhtml", () => {
  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneChoice/SelectOneChoice.xhtml");
  });

  test("Required fields", async ({page}) => {
    const alerts = page.locator("[id='page:messages'] .alert");
    const socStandard = page.locator("[id='page:mainForm:socStandard::field']");
    const socReadonly = page.locator("[id='page:mainForm:socReadonly::field']");
    const socDisabled = page.locator("[id='page:mainForm:socDisabled::field']");
    const submit = page.locator("[id='page:mainForm:submit']");

    await submit.click();
    await expect(alerts).not.toHaveCount(0);
    await expect(socStandard).toContainClass("is-error");
    await expect(socReadonly).not.toContainClass("is-error");
    await expect(socDisabled).not.toContainClass("is-error");
  });
});
