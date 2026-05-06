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

test.describe("030-select/20-selectOneChoice/Dropdown.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/20-selectOneChoice/Dropdown.xhtml");
  });

  test("submit: Alice", async ({page}) => {
    const selectPerson = page.locator("[id='page:mainForm:selectPerson::field']");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const outputFn = page.locator("[id='page:mainForm:outputPerson'] .form-control-plaintext");

    await expect(selectPerson).toHaveValue("Alice Anderson");
    await submitFn.click();
    await expect(outputFn).toHaveText("Alice Anderson");
  });

  test("submit: Bob", async ({page}) => {
    const selectPerson = page.locator("[id='page:mainForm:selectPerson::field']");
    const submitFn = page.locator("[id='page:mainForm:submit']");
    const outputFn = page.locator("[id='page:mainForm:outputPerson'] .form-control-plaintext");

    await expect(selectPerson).toHaveValue("Alice Anderson");
    await selectPerson.selectOption({label: "Bob"});
    await expect(selectPerson).toHaveValue("Bob Brunch");
    await submitFn.click();
    await expect(outputFn).toHaveText("Bob Brunch");
  });

  test("ajax: select Mars", async ({page}) => {
    const planet = page.locator("[id='page:mainForm:selectPlanet::field']");
    const moonsFn = page.locator("[id='page:mainForm:moonbox::field'] option");

    await expect(planet).toHaveValue("0");
    await planet.selectOption({label: "Mars"});
    await expect(planet).toHaveValue("1");
    await expect(moonsFn.nth(0)).toHaveText("Phobos");
    await expect(moonsFn.nth(1)).toHaveText("Deimos");
  });

  test("ajax: select Jupiter", async ({page}) => {
    const planet = page.locator("[id='page:mainForm:selectPlanet::field']");
    const moonsFn = page.locator("[id='page:mainForm:moonbox::field'] option");

    await expect(planet).toHaveValue("0");
    await planet.selectOption({label: "Jupiter"});
    await expect(planet).toHaveValue("2");
    await expect(moonsFn.nth(0)).toHaveText("Europa");
    await expect(moonsFn.nth(1)).toHaveText("Ganymed");
    await expect(moonsFn.nth(2)).toHaveText("Io");
    await expect(moonsFn.nth(3)).toHaveText("Kallisto");
  });
});
