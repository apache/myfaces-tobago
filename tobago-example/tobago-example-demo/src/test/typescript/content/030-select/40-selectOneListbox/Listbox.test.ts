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

test.describe("030-select/40-selectOneListbox/Listbox.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/40-selectOneListbox/Listbox.xhtml");
  });

  test("submit: select 'Nile'", async ({page}) => {
    const riverList = page.locator("[id='page:mainForm:riverList::field']");
    const submitFn = page.locator("[id='page:mainForm:riverSubmit']");
    const outputFn = page.locator("[id='page:mainForm:riverOutput'] .form-control-plaintext");

    await expect(outputFn).not.toHaveText("6853 km");
    await riverList.selectOption({index: 0}); // Nile
    await submitFn.click();
    await expect(outputFn).toHaveText("6853 km");
  });

  test("submit: select 'Yangtze'", async ({page}) => {
    const riverList = page.locator("[id='page:mainForm:riverList::field']");
    const submitFn = page.locator("[id='page:mainForm:riverSubmit']");
    const outputFn = page.locator("[id='page:mainForm:riverOutput'] .form-control-plaintext");

    await expect(outputFn).not.toHaveText("6300 km");
    await riverList.selectOption({index: 2}); // Yangtze
    await submitFn.click();
    await expect(outputFn).toHaveText("6300 km");
  });

  test("ajax: select Everest", async ({page}) => {
    const mountainListFn = page.locator("[id='page:mainForm:mountainList::field']");
    const outputFn = page.locator("[id='page:mainForm:selectedMountain'] .form-control-plaintext");

    await expect(outputFn).not.toHaveText("8848 m");
    await mountainListFn.selectOption({index: 0}); // Everest
    await mountainListFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("8848 m");
  });

  test("ajax: select Makalu", async ({page}) => {
    const mountainListFn = page.locator("[id='page:mainForm:mountainList::field']");
    const outputFn = page.locator("[id='page:mainForm:selectedMountain'] .form-control-plaintext");

    await expect(outputFn).not.toHaveText("8481 m");
    await mountainListFn.selectOption({index: 4}); // Makalu
    await mountainListFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("8481 m");
  });
});
