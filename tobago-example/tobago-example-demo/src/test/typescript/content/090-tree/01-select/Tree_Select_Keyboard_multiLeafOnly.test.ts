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

test.describe("090-tree/01-select/Tree_Select_Keyboard_multiLeafOnly.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/01-select/Tree_Select.xhtml");
  });

  test("multiLeafOnly: Select Sports, Select Movies, Select Messier, Select Music, Deselect Movies", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");
    const selectableMultiLeaf = page.locator("[id='page:mainForm:selectable::4']");


    const category = page.locator("[id='page:mainForm:categoriesTree:0:j_id_3f']");
    const sports = page.locator("[id='page:mainForm:categoriesTree:1:select']");
    const movies = page.locator("[id='page:mainForm:categoriesTree:2:select']");
    const music = page.locator("[id='page:mainForm:categoriesTree:3:j_id_3f']");
    const classic = page.locator("[id='page:mainForm:categoriesTree:4:select']");
    const pop = page.locator("[id='page:mainForm:categoriesTree:5:select']");
    const world = page.locator("[id='page:mainForm:categoriesTree:6:j_id_3f']");
    const carib = page.locator("[id='page:mainForm:categoriesTree:7:select']");
    const africa = page.locator("[id='page:mainForm:categoriesTree:8:select']");
    const games = page.locator("[id='page:mainForm:categoriesTree:9:select']");
    const science = page.locator("[id='page:mainForm:categoriesTree:10:j_id_3f']");
    const mathematics = page.locator("[id='page:mainForm:categoriesTree:11:j_id_3f']");
    const analysis = page.locator("[id='page:mainForm:categoriesTree:12:select']");
    const algebra = page.locator("[id='page:mainForm:categoriesTree:13:select']");
    const geography = page.locator("[id='page:mainForm:categoriesTree:14:select']");
    const astronomy = page.locator("[id='page:mainForm:categoriesTree:15:j_id_3f']");
    const education = page.locator("[id='page:mainForm:categoriesTree:16:select']");
    const pictures = page.locator("[id='page:mainForm:categoriesTree:17:j_id_3f']");
    const ngc = page.locator("[id='page:mainForm:categoriesTree:18:select']");
    const messier = page.locator("[id='page:mainForm:categoriesTree:19:select']");


    await selectableMultiLeaf.check();
    await expect(checkboxes).toHaveCount(6);

    await sports.check();
    await expect(output).toHaveText("Sports");
    await page.keyboard.press("ArrowUp");
    await expect(category).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(sports).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(movies).toBeFocused();
    await page.keyboard.press("Enter");               //Check Movies
    await expect(output).toHaveText("Sports, Movies");
    await page.keyboard.press("ArrowDown");
    await expect(music).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(classic).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pop).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(world).toBeFocused();
    await page.keyboard.press("Space");               //Check World
    await expect(output).toHaveText("Sports, Movies");
    await page.keyboard.press("ArrowRight");
    await expect(world).toBeFocused();
    await expect(checkboxes).toHaveCount(8);
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
    await expect(checkboxes).toHaveCount(10);
    await page.keyboard.press("ArrowDown");
    await expect(analysis).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(algebra).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(geography).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(astronomy).toBeFocused();
    await expect(checkboxes).toHaveCount(11);
    await page.keyboard.press("ArrowDown");
    await expect(education).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(pictures).toBeFocused();
    await expect(checkboxes).toHaveCount(13);
    await page.keyboard.press("ArrowDown");
    await expect(ngc).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(messier).toBeFocused();
    await page.keyboard.press("Enter");               //Check Messier
    await expect(output).toHaveText("Sports, Movies, Messier");


    //Now from Bottom to Top
    await page.keyboard.press("ArrowLeft");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(science).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(science).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await page.keyboard.press("ArrowUp");
    await page.keyboard.press("ArrowLeft");
    await expect(world).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(world).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(music).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(music).toBeFocused();
    await page.keyboard.press("Enter");          //Check Music
    await expect(output).toHaveText("Sports, Movies, Messier");
    await page.keyboard.press("ArrowUp");
    await expect(movies).toBeFocused();
    await page.keyboard.press("Enter");          //Check Movies
    await expect(output).toHaveText("Sports, Messier");

  });
});
