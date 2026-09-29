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

test.describe("900-test/shuttle/Shuttle.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/shuttle/Shuttle.xhtml");
  });

  test("Select 'Bordered' move it to the right", async ({page}) => {
    const unselectedFn = page.locator("[id='page:mainForm:shuttle::unselected']");
    const selectedOptionsFn = page.locator(".tobago-selected option");
    const addButtonFn = page.locator("[id='page:mainForm:shuttle::add']");
    const outputFn = page.locator("[id='page:mainForm:reloadCounter'] .form-control-plaintext");

    const counter = Number(await outputFn.textContent());
    await unselectedFn.selectOption({index: 2});
    await addButtonFn.click();
    await expect(selectedOptionsFn).toHaveCount(1);
    await expect(selectedOptionsFn.first()).toHaveAttribute("value", "bordered");
    await expect(outputFn).toHaveText((counter + 1).toString());
  });
});
