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

test.describe("090-tree/01-select/Tree_Select_Keyboard_multiCascade.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/01-select/Tree_Select.xhtml");
  });

  test("multiCascade: select Category, deselect Category, select Music, select Geography, deselect Music", async ({page}) => {
    const checkboxes = page.locator("[id='page:mainForm:categoriesTree'] input[type=checkbox]");
    const selectableMultiCascade = page.locator("[id='page:mainForm:selectable::5']");
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

    const classicColapse = page.locator("[id='page:mainForm:categoriesTree:4:node']");
    const popColapse = page.locator("[id='page:mainForm:categoriesTree:5:node']");
    const worldColapse = page.locator("[id='page:mainForm:categoriesTree:6:node']");
    const caribColapse = page.locator("[id='page:mainForm:categoriesTree:7:node']");
    const africaColapse = page.locator("[id='page:mainForm:categoriesTree:8:node']");
    const mathematicsColapse = page.locator("[id='page:mainForm:categoriesTree:11:node']");
    const astronomyColapse = page.locator("[id='page:mainForm:categoriesTree:15:node']");
    const educationColapse = page.locator("[id='page:mainForm:categoriesTree:16:node']");
    const picturesColapse = page.locator("[id='page:mainForm:categoriesTree:17:node']");
    const ngcColapse = page.locator("[id='page:mainForm:categoriesTree:18:node']");
    const messierColapse = page.locator("[id='page:mainForm:categoriesTree:19:node']");


    await selectableMultiCascade.check();
    await expect(checkboxes).toHaveCount(12);

    await category.check();
    await expect(output).toHaveText("Category, Sports, Movies, Music, Classic, Pop," +
        " World, Games, Science, Mathematics, Geography, Astronomy"); //Check Category

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
    await expect(output).toHaveText("Music, Classic, Pop, World");
    await page.keyboard.press("ArrowDown");
    await expect(classic).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pop).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(world).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(world).toBeFocused();
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
    await expect(checkboxes).toHaveCount(16);
    await page.keyboard.press("ArrowDown");
    await expect(analysis).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(algebra).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(geography).toBeFocused();
    await page.keyboard.press("Space");              //Check Geography
    await expect(output).toHaveText("Music, Classic, Pop, World, Geography");
    await page.keyboard.press("ArrowDown");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(astronomy).toBeFocused();
    await expect(checkboxes).toHaveCount(18);
    await page.keyboard.press("ArrowDown");
    await expect(education).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(pictures).toBeFocused();
    await expect(checkboxes).toHaveCount(20);
    await page.keyboard.press("ArrowDown");
    await expect(ngc).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(messier).toBeFocused();

    //Now Bottom to Top to Uncheck Music
    await page.keyboard.press("ArrowLeft");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(pictures).toBeFocused();
    await expect(ngcColapse).toHaveCSS("display", "none");
    await expect(messierColapse).toHaveCSS("display", "none");
    await page.keyboard.press("ArrowLeft");
    await expect(astronomy).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(astronomy).toBeFocused();
    await expect(picturesColapse).toHaveCSS("display", "none");
    await expect(educationColapse).toHaveCSS("display", "none");
    await page.keyboard.press("ArrowLeft");
    await expect(science).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(science).toBeFocused();
    await expect(mathematicsColapse).toHaveCSS("display", "none");
    await expect(astronomyColapse).toHaveCSS("display", "none");
    await page.keyboard.press("ArrowUp");
    await expect(games).toBeFocused();
    await page.keyboard.press("ArrowUp");
    await expect(africa).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(world).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(world).toBeFocused();
    await expect(caribColapse).toHaveCSS("display", "none");
    await expect(africaColapse).toHaveCSS("display", "none");
    await page.keyboard.press("ArrowLeft");
    await expect(music).toBeFocused();
    await page.keyboard.press("ArrowLeft");
    await expect(music).toBeFocused();
    await expect(classicColapse).toHaveCSS("display", "none");
    await expect(popColapse).toHaveCSS("display", "none");
    await expect(worldColapse).toHaveCSS("display", "none");
    await page.keyboard.press("Space");             //Uncheck Music


    //"Value should be 'Geography' but is 'Classic, Pop, World, Geography' because of https://issues.apache.org/jira/browse/TOBAGO-2015"
    await expect(output).toHaveText("Classic, Pop, World, Geography");
  });
});
