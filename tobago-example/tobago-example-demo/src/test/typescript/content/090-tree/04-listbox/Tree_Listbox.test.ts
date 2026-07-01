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

import {expect, type Locator, type Page, test} from "@playwright/test";

test.describe("090-tree/04-listbox/Tree_Listbox.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/04-listbox/Tree_Listbox.xhtml");
  });

  test("Select 2,2,0 and submit", async ({page}) => {
    const hiddenInput = page.locator("[id='page:mainForm:listbox::selected']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const rootListbox = page.locator("[id='page:mainForm:listbox:0:node::parent']");
    const musicListbox = page.locator("[id='page:mainForm:listbox:3:node::parent']");
    const worldListbox = page.locator("[id='page:mainForm:listbox:6:node::parent']");

    await rootListbox.selectOption({label: "Sports"});
    await expect(hiddenInput).toHaveValue("[0]");

    await expect(musicListbox).not.toBeVisible();
    await rootListbox.selectOption({label: "Music →"});
    await expect(musicListbox).toBeVisible();
    await expect(hiddenInput).toHaveValue("[2]");

    await expect(worldListbox).not.toBeVisible();
    await musicListbox.selectOption({label: "World →"});
    await expect(worldListbox).toBeVisible();
    await expect(hiddenInput).toHaveValue("[2,2]");

    await worldListbox.selectOption({label: "Carib"});
    await expect(hiddenInput).toHaveValue("[2,2,0]");

    await submit.click();
    await expect(output).toHaveText("[[2, 2, 0]]");
  });

  test("Select 3 and submit", async ({page}) => {
    const hiddenInput = page.locator("[id='page:mainForm:listbox::selected']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const rootListbox = page.locator("[id='page:mainForm:listbox:0:node::parent']");

    await rootListbox.selectOption({label: "Games"});
    await expect(hiddenInput).toHaveValue("[3]");

    await submit.click();
    await expect(output).toHaveText("[[3]]");
  });

  test("Select 4,2,1,1 and submit", async ({page}) => {
    const hiddenInput = page.locator("[id='page:mainForm:listbox::selected']");
    const submit = page.locator("[id='page:mainForm:submit']");
    const output = page.locator("[id='page:mainForm:output'] .form-control-plaintext");
    const rootListbox = page.locator("[id='page:mainForm:listbox:0:node::parent']");
    const scienceListbox = page.locator("[id='page:mainForm:listbox:10:node::parent']");
    const astronomyListbox = page.locator("[id='page:mainForm:listbox:15:node::parent']");
    const picturesListbox = page.locator("[id='page:mainForm:listbox:17:node::parent']");

    await expect(scienceListbox).not.toBeVisible();
    await rootListbox.selectOption({label: "Science →"});
    await expect(scienceListbox).toBeVisible();
    await expect(hiddenInput).toHaveValue("[4]");

    await expect(astronomyListbox).not.toBeVisible();
    await scienceListbox.selectOption({label: "Astronomy →"});
    await expect(astronomyListbox).toBeVisible();
    await expect(hiddenInput).toHaveValue("[4,2]");

    await expect(picturesListbox).not.toBeVisible();
    await astronomyListbox.selectOption({label: "Pictures →"});
    await expect(picturesListbox).toBeVisible();
    await expect(hiddenInput).toHaveValue("[4,2,1]");

    await picturesListbox.selectOption({label: "Messier"});
    await expect(hiddenInput).toHaveValue("[4,2,1,1]");

    await submit.click();
    await expect(output).toHaveText("[[4, 2, 1, 1]]");
  });
});
