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

test.describe("010-input/20-suggest/Suggest.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/010-input/20-suggest/Suggest.xhtml");
  });

  test("Basics: 'M'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("M");
    await expect(resultList).toHaveCount(10);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Mercury");
    await expect(resultList.nth(1)).toHaveText("Mars");
    await expect(resultList.nth(2)).toHaveText("Moon");
    await expect(resultList.nth(3)).toHaveText("Deimos");
    await expect(resultList.nth(4)).toHaveText("Metis");
    await expect(resultList.nth(5)).toHaveText("Amalthea");
    await expect(resultList.nth(6)).toHaveText("Ganymede");
    await expect(resultList.nth(7)).toHaveText("Themisto");
    await expect(resultList.nth(8)).toHaveText("Himalia");
    await expect(resultList.nth(9)).toHaveText("Carme");
    await page.mouse.click(0, 0);
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Basics: 'Ma'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Ma");
    await expect(resultList).toHaveCount(4);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Mars");
    await expect(resultList.nth(1)).toHaveText("Amalthea");
    await expect(resultList.nth(2)).toHaveText("Himalia");
    await expect(resultList.nth(3)).toHaveText("Mimas");
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Basics: 'Mar'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Mar");
    await expect(resultList).toHaveCount(1);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(entry0).toHaveText("Mars");
    await page.mouse.click(0, 0);
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Basics: 'Mars'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Mars");
    await expect(resultList).toHaveCount(1);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(entry0).toHaveText("Mars");
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Basics: Add 'eus' and click first entry.", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] [data-result-index='0'] > .dropdown-item");
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("eus");
    await expect(resultList).toHaveCount(3);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Prometheus");
    await expect(resultList.nth(1)).toHaveText("Epimetheus");
    await expect(resultList.nth(2)).toHaveText("Proteus");
    await entry0.click();
    await expect(inputField).toHaveValue("Prometheus");
  });

  test("Basics: Type 'Mars' and delete all characters", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Mars");
    await expect(resultList).toHaveCount(1);
    await expect(entry0).toBeVisible();
    await expect(entry0).toHaveText("Mars");
    await expect(spinner).not.toBeVisible();
    await page.keyboard.press("Backspace");
    await page.keyboard.press("Backspace");
    await page.keyboard.press("Backspace");
    await page.keyboard.press("Backspace");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await expect(spinner).not.toBeVisible();
  });

  test("Basics: Type 'M', Escape, Enter", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inBasic::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inBasic'] .spinner");

    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("M");
    await expect(resultList).toHaveCount(10);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await page.keyboard.press("Enter");
    await expect(resultList).toHaveCount(10);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
  });

  test("Minimum Characters: 'C'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:minimumCharacters::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:minimumCharacters'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.fill("Ear");
    await expect(resultList).toHaveCount(1);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await inputField.fill("C");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Minimum Characters: 'Ca'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:minimumCharacters::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:minimumCharacters'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.fill("Ear");
    await expect(resultList).toHaveCount(1);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Ca");
    await expect(resultList).toHaveCount(7);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Callisto");
    await expect(resultList.nth(1)).toHaveText("Carme");
    await expect(resultList.nth(2)).toHaveText("Iocaste");
    await expect(resultList.nth(3)).toHaveText("Callirrhoe");
    await expect(resultList.nth(4)).toHaveText("Calypso");
    await expect(resultList.nth(5)).toHaveText("Bianca");
    await expect(resultList.nth(6)).toHaveText("Caliban");
    await page.mouse.click(0, 0);
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Minimum Characters: Zero", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:minimumCharacters0::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:minimumCharacters'] .spinner");

    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.click();
    await expect(resultList).toHaveCount(88);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await expect(inputField).toBeFocused();
    await page.keyboard.down("Shift");
    await page.keyboard.press("Tab");
    await page.keyboard.up("Shift");
    await expect(inputField).not.toBeFocused();
    await page.keyboard.press("Tab");
    await expect(inputField).toBeFocused();
    await expect(resultList).toHaveCount(88);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Client side: 'Ju'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inClient::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inClient'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("Ju");
    await expect(resultList).toHaveCount(2);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Jupiter");
    await expect(resultList.nth(1)).toHaveText("Juliet");
    await page.mouse.click(0, 0);
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Client side - Filter All: ' '", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inClientFilterAll::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inClientFilterAll'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.click();
    await expect(inputField).toBeFocused();
    await page.keyboard.press("Space");
    await expect(resultList).toHaveCount(88);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Sun");
    await expect(resultList.nth(1)).toHaveText("Mercury");
    await expect(resultList.nth(2)).toHaveText("Venus");
    await expect(resultList.nth(3)).toHaveText("Earth");
    await expect(resultList.nth(4)).toHaveText("Mars");
    await expect(resultList.nth(5)).toHaveText("Jupiter");
    await expect(resultList.nth(6)).toHaveText("Saturn");
    await expect(resultList.nth(7)).toHaveText("Uranus");
    await expect(resultList.nth(8)).toHaveText("Neptune");
    await expect(resultList.nth(9)).toHaveText("Pluto");
    await expect(resultList.nth(10)).toHaveText("Moon");
    await expect(resultList.nth(11)).toHaveText("Phobos");
    await expect(resultList.nth(12)).toHaveText("Deimos");
    await expect(resultList.nth(13)).toHaveText("Metis");
    await expect(resultList.nth(14)).toHaveText("Adrastea");
    await expect(resultList.nth(15)).toHaveText("Amalthea");
    await expect(resultList.nth(16)).toHaveText("Thebe");
    await expect(resultList.nth(17)).toHaveText("Io");
    await expect(resultList.nth(18)).toHaveText("Europa");
    await expect(resultList.nth(19)).toHaveText("Ganymede");
    await expect(resultList.nth(20)).toHaveText("Callisto");
    await expect(resultList.nth(21)).toHaveText("Themisto");
    await expect(resultList.nth(22)).toHaveText("Leda");
    await expect(resultList.nth(23)).toHaveText("Himalia");
    await expect(resultList.nth(24)).toHaveText("Lysithea");
    await expect(resultList.nth(25)).toHaveText("Elara");
    await expect(resultList.nth(26)).toHaveText("Ananke");
    await expect(resultList.nth(27)).toHaveText("Carme");
    await expect(resultList.nth(28)).toHaveText("Pasiphae");
    await expect(resultList.nth(29)).toHaveText("Sinope");
    await expect(resultList.nth(30)).toHaveText("Iocaste");
    await expect(resultList.nth(31)).toHaveText("Harpalyke");
    await expect(resultList.nth(32)).toHaveText("Praxidike");
    await expect(resultList.nth(33)).toHaveText("Taygete");
    await expect(resultList.nth(34)).toHaveText("Chaldene");
    await expect(resultList.nth(35)).toHaveText("Kalyke");
    await expect(resultList.nth(36)).toHaveText("Callirrhoe");
    await expect(resultList.nth(37)).toHaveText("Megaclite");
    await expect(resultList.nth(38)).toHaveText("Isonoe");
    await expect(resultList.nth(39)).toHaveText("Erinome");
    await expect(resultList.nth(40)).toHaveText("Pan");
    await expect(resultList.nth(41)).toHaveText("Atlas");
    await expect(resultList.nth(42)).toHaveText("Prometheus");
    await expect(resultList.nth(43)).toHaveText("Pandora");
    await expect(resultList.nth(44)).toHaveText("Epimetheus");
    await expect(resultList.nth(45)).toHaveText("Janus");
    await expect(resultList.nth(46)).toHaveText("Mimas");
    await expect(resultList.nth(47)).toHaveText("Enceladus");
    await expect(resultList.nth(48)).toHaveText("Tethys");
    await expect(resultList.nth(49)).toHaveText("Telesto");
    await expect(resultList.nth(50)).toHaveText("Calypso");
    await expect(resultList.nth(51)).toHaveText("Dione");
    await expect(resultList.nth(52)).toHaveText("Helene");
    await expect(resultList.nth(53)).toHaveText("Rhea");
    await expect(resultList.nth(54)).toHaveText("Titan");
    await expect(resultList.nth(55)).toHaveText("Hyperion");
    await expect(resultList.nth(56)).toHaveText("Iapetus");
    await expect(resultList.nth(57)).toHaveText("Phoebe");
    await expect(resultList.nth(58)).toHaveText("Cordelia");
    await expect(resultList.nth(59)).toHaveText("Ophelia");
    await expect(resultList.nth(60)).toHaveText("Bianca");
    await expect(resultList.nth(61)).toHaveText("Cressida");
    await expect(resultList.nth(62)).toHaveText("Desdemona");
    await expect(resultList.nth(63)).toHaveText("Juliet");
    await expect(resultList.nth(64)).toHaveText("Portia");
    await expect(resultList.nth(65)).toHaveText("Rosalind");
    await expect(resultList.nth(66)).toHaveText("Belinda");
    await expect(resultList.nth(67)).toHaveText("1986U10");
    await expect(resultList.nth(68)).toHaveText("Puck");
    await expect(resultList.nth(69)).toHaveText("Miranda");
    await expect(resultList.nth(70)).toHaveText("Ariel");
    await expect(resultList.nth(71)).toHaveText("Umbriel");
    await expect(resultList.nth(72)).toHaveText("Titania");
    await expect(resultList.nth(73)).toHaveText("Oberon");
    await expect(resultList.nth(74)).toHaveText("Caliban");
    await expect(resultList.nth(75)).toHaveText("Stephano");
    await expect(resultList.nth(76)).toHaveText("Sycorax");
    await expect(resultList.nth(77)).toHaveText("Prospero");
    await expect(resultList.nth(78)).toHaveText("Setebos");
    await expect(resultList.nth(79)).toHaveText("Naiad");
    await expect(resultList.nth(80)).toHaveText("Thalassa");
    await expect(resultList.nth(81)).toHaveText("Despina");
    await expect(resultList.nth(82)).toHaveText("Galatea");
    await expect(resultList.nth(83)).toHaveText("Larissa");
    await expect(resultList.nth(84)).toHaveText("Proteus");
    await expect(resultList.nth(85)).toHaveText("Triton");
    await expect(resultList.nth(86)).toHaveText("Nereid");
    await expect(resultList.nth(87)).toHaveText("Charon");
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Client side - Filter Prefix: 'me'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inClientFilterPrefix::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inClientFilterPrefix'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("me");
    await expect(resultList).toHaveCount(3);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Mercury");
    await expect(resultList.nth(1)).toHaveText("Metis");
    await expect(resultList.nth(2)).toHaveText("Megaclite");
    await page.mouse.click(0, 0);
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });

  test("Client side - Filter Contains: 'me'", async ({page}) => {
    const inputField = page.locator("[id='page:mainForm:inClientFilterContains::field']");
    const resultList = page.locator("[id='" + (await inputField.getAttribute("aria-owns")) + "'] .dropdown-item");
    const entry0 = resultList.nth(0);
    const spinner = page.locator("tobago-in[id='page:mainForm:inClientFilterContains'] .spinner");

    await inputField.scrollIntoViewIfNeeded();
    await inputField.fill(" ");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
    await inputField.fill("me");
    await expect(resultList).toHaveCount(8);
    await expect(entry0).toBeVisible();
    await expect(spinner).not.toBeVisible();
    await expect(resultList.nth(0)).toHaveText("Mercury");
    await expect(resultList.nth(1)).toHaveText("Metis");
    await expect(resultList.nth(2)).toHaveText("Ganymede");
    await expect(resultList.nth(3)).toHaveText("Carme");
    await expect(resultList.nth(4)).toHaveText("Megaclite");
    await expect(resultList.nth(5)).toHaveText("Erinome");
    await expect(resultList.nth(6)).toHaveText("Prometheus");
    await expect(resultList.nth(7)).toHaveText("Epimetheus");
    await page.keyboard.press("Escape");
    await expect(resultList).toHaveCount(0);
    await expect(spinner).not.toBeVisible();
  });
});
