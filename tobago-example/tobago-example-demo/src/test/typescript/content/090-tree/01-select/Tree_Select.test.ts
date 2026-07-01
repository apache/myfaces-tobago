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

test.describe("090-tree/01-select/Tree_Select.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/01-select/Tree_Select.xhtml");
  });

  test("single: select Music, select Mathematics", async ({page}) => {
    const radios = page.locator("[id='page:mainForm:categoriesTree'] input[type=radio]");
    const selectableSingle = page.locator("[id='page:mainForm:selectable::1']");
    const music = page.locator("[id='page:mainForm:categoriesTree:3:select']");
    const mathematics = page.locator("[id='page:mainForm:categoriesTree:9:select']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    await selectableSingle.check();
    await expect(radios).toHaveCount(12);

    await music.check();
    await expect(output).toHaveText("Music");

    await mathematics.check();
    await expect(output).toHaveText("Mathematics");
  });

  test("singleLeafOnly: select Classic, select Geography", async ({page}) => {
    const radios = page.locator("[id='page:mainForm:categoriesTree'] input[type=radio]");
    const selectableSingleLeafOnly = page.locator("[id='page:mainForm:selectable::2']");
    const classic = page.locator("[id='page:mainForm:categoriesTree:4:select']");
    const geography = page.locator("[id='page:mainForm:categoriesTree:10:select']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    await selectableSingleLeafOnly.check();
    await expect(radios).toHaveCount(6);

    await classic.check();
    await expect(output).toHaveText("Classic");

    await geography.check();
    await expect(output).toHaveText("Geography");
  });

  test("multi: select Music, select Geography, deselect Music", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const music = page.locator("[id='page:mainForm:categoriesTree:3:select']");
    const geography = page.locator("[id='page:mainForm:categoriesTree:10:select']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    // selectable=multi is already set
    await expect(checkboxes).toHaveCount(12);

    await music.check();
    await expect(output).toHaveText("Music");

    await geography.check();
    await expect(output).toHaveText("Music, Geography");

    await music.uncheck();
    await expect(output).toHaveText("Geography");
  });

  test("multiLeafOnly: select Classic, select Geography, deselect Classic", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const selectableMultiLeafOnly = page.locator("[id='page:mainForm:selectable::4']");
    const classic = page.locator("[id='page:mainForm:categoriesTree:4:select']");
    const geography = page.locator("[id='page:mainForm:categoriesTree:10:select']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    await selectableMultiLeafOnly.check();
    await expect(checkboxes).toHaveCount(6);

    await classic.check();
    await expect(output).toHaveText("Classic");

    await geography.check();
    await expect(output).toHaveText("Classic, Geography");

    await classic.uncheck();
    await expect(output).toHaveText("Geography");
  });

  test("multiCascade: select Music, select Mathematics, deselect Classic", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const selectableMultiCascade = page.locator("[id='page:mainForm:selectable::5']");
    const music = page.locator("[id='page:mainForm:categoriesTree:3:select']");
    const classic = page.locator("[id='page:mainForm:categoriesTree:4:select']");
    const mathematics = page.locator("[id='page:mainForm:categoriesTree:9:select']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    await selectableMultiCascade.check();
    await expect(checkboxes).toHaveCount(12);

    await music.check();
    await expect(output).toHaveText("Music, Classic, Pop, World");

    await mathematics.check();
    await expect(output).toHaveText("Music, Classic, Pop, World, Mathematics");

    await classic.uncheck();
    await expect(output).toHaveText("Music, Pop, World, Mathematics");
  });
});
