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

test.describe("900-test/selectManyShuttle/SelectManyShuttle.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyShuttle/SelectManyShuttle.xhtml");
  });

  test("JavaScript 'change' event", async ({page}) => {
    const shuttle = page.locator("tobago-select-many-shuttle[id='page:mainForm:shuttle']");
    const unselect = shuttle.locator(".tobago-unselected");
    const select = shuttle.locator(".tobago-selected");
    const addAll = shuttle.locator("button[id='page:mainForm:shuttle::addAll']");
    const add = shuttle.locator("button[id='page:mainForm:shuttle::add']");
    const remove = shuttle.locator("button[id='page:mainForm:shuttle::remove']");
    const removeAll = shuttle.locator("button[id='page:mainForm:shuttle::removeAll']");

    const eventPromise = () => page.evaluate(() => {
      return new Promise(resolve => {
        const shuttle = document.querySelector("tobago-select-many-shuttle[id='page:mainForm:shuttle']");
        shuttle.addEventListener("change", (event: CustomEvent) => {
          resolve(event.detail);
        }, {once: true});
      });
    }) as Promise<any>;

    let promise = eventPromise();
    await unselect.selectOption("Item5");
    await add.click();
    let detail = await promise;
    expect(detail.added).toEqual(["Item5"]);
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual(["Item1", "Item2", "Item3", "Item4", "Item6", "Item7", "Item8", "Item9"]);
    expect(detail.selected).toEqual(["Item5"]);

    promise = eventPromise();
    await addAll.click();
    detail = await promise;
    expect(detail.added).toEqual(["Item1", "Item2", "Item3", "Item4", "Item6", "Item7", "Item8", "Item9"]);
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"]);

    promise = eventPromise();
    await select.selectOption("Item8");
    await remove.click();
    detail = await promise;
    expect(detail.added).toBeNull();
    expect(detail.removed).toEqual(["Item8"]);
    expect(detail.unselected).toEqual(["Item8"]);
    expect(detail.selected).toEqual(["Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item9"]);

    promise = eventPromise();
    await removeAll.click();
    detail = await promise;
    expect(detail.added).toBeNull();
    expect(detail.removed).toEqual(["Item5", "Item1", "Item2", "Item3", "Item4", "Item6", "Item7", "Item9"]);
    expect(detail.unselected).toEqual(["Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"]);
    expect(detail.selected).toEqual([]);
  });
});
