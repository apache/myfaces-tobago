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
import {SelectManyShuttleDetail} from "../../../../../tobago-theme/tobago-theme-standard/src/main/ts/tobago-select-many-shuttle";

test.describe("900-test/selectManyShuttle/SelectManyShuttleOrderable.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectManyShuttle/SelectManyShuttleOrderable.xhtml");
  });

  test("JavaScript 'change' event", async ({page}) => {
    const shuttle = page.locator("tobago-select-many-shuttle[id='page:mainForm:shuttle']");
    await  expect(shuttle).toBeVisible();
    const unselect = shuttle.locator(".tobago-unselected");
    const select = shuttle.locator(".tobago-selected");
    const addAll = shuttle.locator("button[id='page:mainForm:shuttle::addAll']");
    const add = shuttle.locator("button[id='page:mainForm:shuttle::add']");
    const remove = shuttle.locator("button[id='page:mainForm:shuttle::remove']");
    const removeAll = shuttle.locator("button[id='page:mainForm:shuttle::removeAll']");
    const top = shuttle.locator("button[id='page:mainForm:shuttle::top']");
    const up = shuttle.locator("button[id='page:mainForm:shuttle::up']");
    const down = shuttle.locator("button[id='page:mainForm:shuttle::down']");
    const bottom = shuttle.locator("button[id='page:mainForm:shuttle::bottom']");
    const submit = page.locator("button[id='page:mainForm:submit']");
    const out = page.locator("tobago-out[id='page:mainForm:out']");

    const eventPromise = () => page.evaluate(() => {
      return new Promise(resolve => {
        const shuttle = document.querySelector("tobago-select-many-shuttle[id='page:mainForm:shuttle']");
        shuttle.addEventListener("change", (event: CustomEvent) => {
          resolve(event.detail);
        }, {once: true});
      });
    }) as Promise<any>;

    let promise;
    let detail: SelectManyShuttleDetail;

    promise = eventPromise();
    await addAll.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Add*/"add");
    expect(detail.added).toEqual(["Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"]);
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9"]);

    // top

    promise = eventPromise();
    await select.selectOption("Item5");
    await top.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item5", "Item1", "Item2", "Item3", "Item4", "Item6", "Item7", "Item8", "Item9"]);


    promise = eventPromise();
    await select.selectOption(["Item2", "Item4", "Item6"]);
    await top.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item2", "Item4", "Item6", "Item5", "Item1", "Item3", "Item7", "Item8", "Item9"]);

    promise = eventPromise();
    await select.selectOption(["Item2", "Item4", "Item6"]);
    await top.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item2", "Item4", "Item6", "Item5", "Item1", "Item3", "Item7", "Item8", "Item9"]);

    promise = eventPromise();
    await select.selectOption([]);
    await top.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item2", "Item4", "Item6", "Item5", "Item1", "Item3", "Item7", "Item8", "Item9"]);

    // down

    promise = eventPromise();
    await select.selectOption(["Item2", "Item6", "Item1"]);
    await down.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item2", "Item5", "Item6", "Item3", "Item1", "Item7", "Item8", "Item9"]);

    promise = eventPromise();
    await select.selectOption(["Item2", "Item6", "Item1"]);
    await down.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item2", "Item3", "Item6", "Item7", "Item1", "Item8", "Item9"]);

    promise = eventPromise();
    await select.selectOption([]);
    await down.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item2", "Item3", "Item6", "Item7", "Item1", "Item8", "Item9"]);

    // bottom

    promise = eventPromise();
    await select.selectOption(["Item2", "Item6", "Item1"]);
    await bottom.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item3", "Item7", "Item8", "Item9", "Item2", "Item6", "Item1"]);

    promise = eventPromise();
    await select.selectOption([]);
    await bottom.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item3", "Item7", "Item8", "Item9", "Item2", "Item6", "Item1"]);

    promise = eventPromise();
    await select.selectOption(["Item2", "Item6", "Item1"]);
    await bottom.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item3", "Item7", "Item8", "Item9", "Item2", "Item6", "Item1"]);

    // up

    promise = eventPromise();
    await select.selectOption(["Item4", "Item6", "Item1"]);
    await up.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item3", "Item7", "Item8", "Item9", "Item6", "Item1", "Item2"]);

    promise = eventPromise();
    await select.selectOption([]);
    await up.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Reorder*/"reorder");
    expect(detail.added).toBeNull();
    expect(detail.removed).toBeNull();
    expect(detail.unselected).toEqual([]);
    expect(detail.selected).toEqual(["Item4", "Item5", "Item3", "Item7", "Item8", "Item9", "Item6", "Item1", "Item2"]);

    // submit

    await expect(select.locator("option")).toHaveCount(9);
    await expect(select.locator("option").nth(0)).toHaveAttribute("value", "Item4");
    await expect(select.locator("option").nth(1)).toHaveAttribute("value", "Item5");
    await expect(select.locator("option").nth(2)).toHaveAttribute("value", "Item3");
    await expect(select.locator("option").nth(3)).toHaveAttribute("value", "Item7");
    await expect(select.locator("option").nth(4)).toHaveAttribute("value", "Item8");
    await expect(select.locator("option").nth(5)).toHaveAttribute("value", "Item9");
    await expect(select.locator("option").nth(6)).toHaveAttribute("value", "Item6");
    await expect(select.locator("option").nth(7)).toHaveAttribute("value", "Item1");
    await expect(select.locator("option").nth(8)).toHaveAttribute("value", "Item2");

    await expect(out).toBeVisible();
    await expect(out.locator("[class='form-control-plaintext']")).toHaveText("");

    await expect(submit).toBeVisible();
    await submit.click();
    await expect(out.locator("[class='form-control-plaintext']")).toHaveText(
        "[Item4, Item5, Item3, Item7, Item8, Item9, Item6, Item1, Item2]");

    // remove odds

    promise = eventPromise();
    await select.selectOption(["Item1", "Item9", "Item3", "Item5", "Item7"]);
    await remove.click();
    detail = await promise;
    expect(detail.type).toEqual(/*SelectManyShuttleDetailType.Remove*/"remove");
    expect(detail.added).toBeNull();
    expect(detail.removed).toEqual(["Item5", "Item3", "Item7", "Item9", "Item1"]);
    expect(detail.unselected).toEqual(["Item3", "Item7", "Item5", "Item1", "Item9"]);
    expect(detail.selected).toEqual(["Item4", "Item8", "Item6", "Item2"]);

    // submit

    await expect(select.locator("option")).toHaveCount(4);
    await expect(select.locator("option").nth(0)).toHaveAttribute("value", "Item4");
    await expect(select.locator("option").nth(1)).toHaveAttribute("value", "Item8");
    await expect(select.locator("option").nth(2)).toHaveAttribute("value", "Item6");
    await expect(select.locator("option").nth(3)).toHaveAttribute("value", "Item2");

    await submit.click();

    await expect(out.locator("[class='form-control-plaintext']")).toHaveText(
        "[Item4, Item8, Item6, Item2]");

    await expect(select.locator("option")).toHaveCount(4);
    await expect(select.locator("option").nth(0)).toHaveAttribute("value", "Item4");
    await expect(select.locator("option").nth(1)).toHaveAttribute("value", "Item8");
    await expect(select.locator("option").nth(2)).toHaveAttribute("value", "Item6");
    await expect(select.locator("option").nth(3)).toHaveAttribute("value", "Item2");

  });
});
