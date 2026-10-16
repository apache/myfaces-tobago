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

  test("single:select Category, select Music, select World, select Mathematics", async ({page}) => {
    const radios = page.locator("[id='page:mainForm:categoriesTree'] input[type=radio]");
    const selectableNone = page.locator("[id='page:mainForm:selectable::0']");
    const selectableSingle = page.locator("[id='page:mainForm:selectable::1']");
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
    const geography = page.locator("[id='page:mainForm:categoriesTree:12:select']");
    const astronomy = page.locator("[id='page:mainForm:categoriesTree:13:select']");
    const plusBeforeWorld = page.locator("[id='page:mainForm:categoriesTree:6:j_id_3f']");

    await selectableNone.check();
    await expect(radios).toHaveCount(0);
    await page.getByRole('heading', {name: 'selectable'}).click();
    await page.keyboard.press("Tab");
    await expect(selectableNone).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(selectableSingle).toBeFocused();
    await expect(radios).toHaveCount(12);

    await category.check();
    await expect(output).toHaveText("Category");

    await page.keyboard.press("ArrowDown");
    await expect(sports).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(movies).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(music).toBeFocused();
    await page.keyboard.press("Enter");
    await expect(output).toHaveText("Music");
    await page.keyboard.press("ArrowDown");
    await expect(classic).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pop).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(world).toBeFocused();
    await page.keyboard.press("Space");
    await expect(output).toHaveText("World");

    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(plusBeforeWorld).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(plusBeforeWorld).toBeFocused();

    await page.keyboard.press("ArrowRight");
    await expect(world).toBeFocused();
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
    await page.keyboard.press("Space");
    await expect(output).toHaveText("Mathematics");
    await page.keyboard.press("ArrowDown");
    await expect(geography).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(astronomy).toBeFocused();
  });

  test("singleLeafOnly: Select Sports, Select Movies, Select Messier, Select Music, Select Movies", async ({page}) => {
    const radios = page.locator("[id='page:mainForm:categoriesTree'] input[type=radio]");
    const selectableSingleLeaf = page.locator("[id='page:mainForm:selectable::2']");
    const output = page.locator("[id='page:mainForm:selectedNodesOutput'] .form-control-plaintext");

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

    await selectableSingleLeaf.check();
    await expect(radios).toHaveCount(6);

    await sports.check();
    await expect(output).toHaveText("Sports");
    await page.keyboard.press("ArrowUp");
    await expect(category).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(sports).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(movies).toBeFocused();
    await page.keyboard.press("Enter");               //Check Movies
    await expect(output).toHaveText("Movies");
    await page.keyboard.press("ArrowDown");
    await expect(music).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(classic).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pop).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(world).toBeFocused();
    await page.keyboard.press("Space");               //Check World
    await expect(output).not.toHaveText("World");
    await page.keyboard.press("ArrowRight");
    await expect(world).toBeFocused();
    await expect(radios).toHaveCount(8);
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
    await expect(radios).toHaveCount(10);
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
    await expect(radios).toHaveCount(11);
    await page.keyboard.press("ArrowDown");
    await expect(education).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(pictures).toBeFocused();
    await page.keyboard.press("ArrowRight");
    await expect(pictures).toBeFocused();
    await expect(radios).toHaveCount(13);
    await page.keyboard.press("ArrowDown");
    await expect(ngc).toBeFocused();
    await page.keyboard.press("ArrowDown");
    await expect(messier).toBeFocused();
    await page.keyboard.press("Enter");               //Check Messier
    await expect(output).toHaveText("Messier");

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
    await expect(output).toHaveText("Messier");
    await page.keyboard.press("ArrowUp");
    await expect(movies).toBeFocused();
    await page.keyboard.press("Enter");          //Check Movies

    //This output should be "Movies" because the selection mode is singleLeafOnly and the last selected leaf node is "Movies"
    //But because of Issue "TOBAGO-2558" the last picked leaf node "Messier" is not deselected
    //So now we have to outputs, with only one allowed
    await expect(output).toHaveText("Movies, Messier");
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
    await expect(output).toHaveText("Music, Geography");
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
    await expect(output).toHaveText("Geography");
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
    await page.keyboard.press("Enter");          //Uncheck Movies
    await expect(output).toHaveText("Sports, Messier");
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
