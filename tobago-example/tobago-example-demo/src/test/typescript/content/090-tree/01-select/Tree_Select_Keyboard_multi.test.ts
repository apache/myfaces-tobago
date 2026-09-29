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

test.describe("090-tree/01-select/Tree_Select_Keyboard_multi.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/01-select/Tree_Select.xhtml");
  });

  test("multi: select Category, deselect Category, select Music, select Geography, deselect Music", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

    const category = page.locator("[id='page:mainForm:categoriesTree:0:select']");
    const sports = page.locator("[id='page:mainForm:categoriesTree:1:select']");
    const movies = page.locator("[id='page:mainForm:categoriesTree:2:select']");
    const music = page.locator("[id='page:mainForm:categoriesTree:3:select']");
    const classic = page.locator("[id='page:mainForm:categoriesTree:4:select']");
    const pop = page.locator("[id='page:mainForm:categoriesTree:5:select']");
    const world = page.locator("[id='page:mainForm:categoriesTree:6:select']");
    const carib = page.locator("[id='page:mainForm:categoriesTree:7:select']");
    const africa = page.locator("[id='page:mainForm:categoriesTree:8:select']");
    const games = page.locator("[id='page:mainForm:categoriesTree:9:select']");
    const science = page.locator("[id='page:mainForm:categoriesTree:10:select']");
    const mathematics = page.locator("[id='page:mainForm:categoriesTree:11:select']");
    const analysis = page.locator("[id='page:mainForm:categoriesTree:12:select']");
    const algebra = page.locator("[id='page:mainForm:categoriesTree:13:select']");
    const geography = page.locator("[id='page:mainForm:categoriesTree:14:select']");
    const astronomy = page.locator("[id='page:mainForm:categoriesTree:15:select']");
    const education = page.locator("[id='page:mainForm:categoriesTree:16:select']");
    const pictures = page.locator("[id='page:mainForm:categoriesTree:17:select']");
    const ngc = page.locator("[id='page:mainForm:categoriesTree:18:select']");
    const messier = page.locator("[id='page:mainForm:categoriesTree:19:select']");

    const expandableMusic = page.locator("[id='page:mainForm:categoriesTree:3:j_id_3f']");
    const expandableWorld = page.locator("[id='page:mainForm:categoriesTree:6:j_id_3f']");
    const expandableScience = page.locator("[id='page:mainForm:categoriesTree:10:j_id_3f']");
    const expandableAstronomy = page.locator("[id='page:mainForm:categoriesTree:15:j_id_3f']");
    const expandablePictures = page.locator("[id='page:mainForm:categoriesTree:17:j_id_3f']");

    // selectable=multi is already set
    await expect(checkboxes).toHaveCount(12);

    await category.check();
    await expect(output).toHaveText("Category"); //Check Category

    await page.keyboard.press("ArrowDown");
    await expect(sports).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(category).toBeFocused();
    await page.keyboard.press("Space");              //Uncheck Category
    await expect(output).toHaveText(" ");

    await page.keyboard.press("ArrowDown");
    await expect(sports).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(movies).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(music).toBeFocused();
    await page.keyboard.press("Enter");              //Check Music
    await expect(output).toHaveText("Music");
    await page.keyboard.press("ArrowDown");
    await expect(classic).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pop).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(world).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(world).toBeFocused();
    // To be sure it is expanded and all checkboxes are visible (older Pcs may need more time to expand the tree)
    await expect(checkboxes).toHaveCount(14);
    await page.keyboard.press("ArrowDown");
    await expect(carib).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(africa).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(games).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(science).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(mathematics).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(mathematics).toBeFocused();
    // To be sure it is expanded and all checkboxes are visible (older Pcs may need more time to expand the tree)
    await expect(checkboxes).toHaveCount(16);
    await page.keyboard.press("ArrowDown");
    await expect(analysis).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(algebra).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(geography).toBeFocused();
    await page.keyboard.press("Space");              //Check Geography
    await expect(output).toHaveText("Music, Geography");
    await page.keyboard.press("ArrowDown");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(astronomy).toBeFocused();
    // To be sure it is expanded and all checkboxes are visible (older Pcs may need more time to expand the tree)
    await expect(checkboxes).toHaveCount(18);
    await page.keyboard.press("ArrowDown");
    await expect(education).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(pictures).toBeFocused();
    // To be sure it is expanded and all checkboxes are visible (older Pcs may need more time to expand the tree)
    await expect(checkboxes).toHaveCount(20);
    await page.keyboard.press("ArrowDown");
    await expect(ngc).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(messier).toBeFocused();

    //Now Bottom to Top to Uncheck Music
    await page.keyboard.press("ArrowLeft");
    await expect(expandablePictures).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandablePictures).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableAstronomy).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableAstronomy).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableScience).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableScience).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(science).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(games).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(africa).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableWorld).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableWorld).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableMusic).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(expandableMusic).toBeFocused();
    await page.keyboard.press("Tab");
    await expect(music).toBeFocused();
    await page.keyboard.press("Space");             //Uncheck Music
    await expect(output).toHaveText("Geography");
  });
});
