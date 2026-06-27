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

test.describe("030-select/70-selectManyShuttle/Shuttle.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/030-select/70-selectManyShuttle/Shuttle.xhtml");
  });

  test("submit: addAll, removeAll, addItem0to4, removeItem2to3", async ({page}) => {
    const unselectedOptions = page.locator("[id='page:mainForm:submitExample::unselected'] option");
    const selectedOptions = page.locator("[id='page:mainForm:submitExample::selected'] option");
    const addAllButton = page.locator("[id='page:mainForm:submitExample::addAll']");
    const addButton = page.locator("[id='page:mainForm:submitExample::add']");
    const removeButton = page.locator("[id='page:mainForm:submitExample::remove']");
    const removeAllButton = page.locator("[id='page:mainForm:submitExample::removeAll']");
    const submitButton = page.locator("[id='page:mainForm:submitButton']");
    const output = page.locator("[id='page:mainForm:submitExampleOutput'] .form-control-plaintext");

    await expect(output).toHaveText("[]");
    await addAllButton.click();
    await expect(unselectedOptions).toHaveCount(0);
    await expect(selectedOptions).toHaveCount(9);
    await submitButton.click();
    await expect(output).toHaveText("[Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto]");
    await removeAllButton.click();
    await expect(unselectedOptions).toHaveCount(9);
    await expect(selectedOptions).toHaveCount(0);
    await submitButton.click();
    await expect(output).toHaveText("[]");

    await page.locator("[id='page:mainForm:submitExample::unselected']").selectOption([{index: 0}, {index: 1}, {index: 2}, {index: 3}, {index: 4}]);
    await addButton.click();
    await expect(unselectedOptions).toHaveCount(4);
    await expect(selectedOptions).toHaveCount(5);
    await submitButton.click();
    await expect(output).toHaveText("[Mercury, Venus, Earth, Mars, Jupiter]");

    await page.locator("[id='page:mainForm:submitExample::selected']").selectOption([{index: 2}, {index: 3}]);
    await removeButton.click();
    await expect(unselectedOptions).toHaveCount(6);
    await expect(selectedOptions).toHaveCount(3);
    await submitButton.click();
    await expect(output).toHaveText("[Mercury, Venus, Jupiter]");

    const pageOverlays = page.locator("tobago-overlay");
    await expect(pageOverlays).toHaveCount(0);
  });

  test("ajax: addAll, removeAll, addItem1to2, removeItem0", async ({page}) => {
    const unselectedOptions = page.locator("[id='page:mainForm:ajaxExample::unselected'] option");
    const selectedOptions = page.locator("[id='page:mainForm:ajaxExample::selected'] option");
    const addAllButton = page.locator("[id='page:mainForm:ajaxExample::addAll']");
    const addButton = page.locator("[id='page:mainForm:ajaxExample::add']");
    const removeButton = page.locator("[id='page:mainForm:ajaxExample::remove']");
    const removeAllButton = page.locator("[id='page:mainForm:ajaxExample::removeAll']");
    const output = page.locator("[id='page:mainForm:outputStars'] .form-control-plaintext");

    await expect(output).toHaveText("[]");
    await addAllButton.click();
    await expect(unselectedOptions).toHaveCount(0);
    await expect(selectedOptions).toHaveCount(4);
    await expect(output).toHaveText("[Proxima Centauri, Alpha Centauri, Wolf 359, Sirius]");

    await removeAllButton.click();
    await expect(unselectedOptions).toHaveCount(4);
    await expect(selectedOptions).toHaveCount(0);
    await expect(output).toHaveText("[]");

    await page.locator("[id='page:mainForm:ajaxExample::unselected']").selectOption([{index: 1}, {index: 2}]);
    await addButton.click();
    await expect(unselectedOptions).toHaveCount(2);
    await expect(selectedOptions).toHaveCount(2);
    await expect(output).toHaveText("[Alpha Centauri, Wolf 359]");

    await page.locator("[id='page:mainForm:ajaxExample::selected']").selectOption([{index: 0}]);
    await removeButton.click();
    await expect(unselectedOptions).toHaveCount(3);
    await expect(selectedOptions).toHaveCount(1);
    await expect(output).toHaveText("[Wolf 359]");

    const pageOverlays = page.locator("tobago-overlay");
    await expect(pageOverlays).toHaveCount(0);
  });

  test("Orderable Selection: addAll, removeAll, addItem0to4, removeItem2to3", async ({page}) => {
    const unselectedOptions = page.locator("[id='page:mainForm:favoriteExample::unselected'] option");
    const selectedOptions = page.locator("[id='page:mainForm:favoriteExample::selected'] option");
    const addAllButton = page.locator("[id='page:mainForm:favoriteExample::addAll']");
    const topButton = page.locator("[id='page:mainForm:favoriteExample::top']");
    const upButton = page.locator("[id='page:mainForm:favoriteExample::up']");
    const downButton = page.locator("[id='page:mainForm:favoriteExample::down']");
    const bottomButton = page.locator("[id='page:mainForm:favoriteExample::bottom']");
    const output = page.locator("[id='page:mainForm:favoriteOutput'] .form-control-plaintext");
    const submitButton = page.locator("[id='page:mainForm:submitButton']");

    await expect(output).toHaveText("[]");
    await addAllButton.click();
    await expect(unselectedOptions).toHaveCount(0);
    await expect(selectedOptions).toHaveCount(9);
    await submitButton.click();
    await expect(output).toHaveText("[Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto]");

    //Neptun, Pluto -> top
    await page.locator("[id='page:mainForm:favoriteExample::selected']").selectOption([{index: 7}, {index: 8}]);
    await topButton.click();
    await submitButton.click();
    await expect(output).toHaveText("[Neptune, Pluto, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus]");

    //Earth -> 2 times up
    await page.locator("[id='page:mainForm:favoriteExample::selected']").selectOption([{index: 4}]);
    await upButton.click();
    await upButton.click();
    await submitButton.click();
    await expect(output).toHaveText("[Neptune, Pluto, Earth, Mercury, Venus,  Mars, Jupiter, Saturn, Uranus]");

    //Venus, Mars -> 1 times down
    await page.locator("[id='page:mainForm:favoriteExample::selected']").selectOption([{index: 4}, {index: 5}]);
    await downButton.click();
    await submitButton.click();
    await expect(output).toHaveText("[Neptune, Pluto, Earth, Mercury, Jupiter, Venus, Mars, Saturn, Uranus]");

    //Mercury -> bottom
    await page.locator("[id='page:mainForm:favoriteExample::selected']").selectOption([{index: 3}]);
    await bottomButton.click();
    await submitButton.click();
    await expect(output).toHaveText("[Neptune, Pluto, Earth, Jupiter, Venus, Mars, Saturn, Uranus, Mercury]");
  });
});
