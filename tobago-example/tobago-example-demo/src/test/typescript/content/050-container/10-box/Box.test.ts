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

test.describe("050-container/10-box/Box.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/050-container/10-box/Box.xhtml");
  });

  test("Accordion: Box 1: 'hide' to 'show' to 'hide'", async ({page}) => {
    const showBoxFn = page.locator("[id='page:mainForm:showBox1']");
    const hideBoxFn = page.locator("[id='page:mainForm:hideBox1']");
    const boxBodyFn = page.locator("[id='page:mainForm:accordionBox1'] .card-body");

    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
    await expect(boxBodyFn).toHaveText("");
    await showBoxFn.click();
    await expect(showBoxFn).toBeHidden();
    await expect(hideBoxFn).toBeVisible();
    await expect(boxBodyFn).not.toHaveText("");
    await hideBoxFn.click();
    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
    await expect(boxBodyFn).toHaveText("");
  });

  test("Accordion: Box 2: 'hide' to 'show' to 'hide'", async ({page}) => {
    const showBoxFn = page.locator("[id='page:mainForm:showBox2']");
    const hideBoxFn = page.locator("[id='page:mainForm:hideBox2']");
    const boxBodyFn = page.locator("[id='page:mainForm:accordionBox2'] .card-body");

    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
    await expect(boxBodyFn).toHaveText("");
    await showBoxFn.click();
    await expect(showBoxFn).toBeHidden();
    await expect(hideBoxFn).toBeVisible();
    await expect(boxBodyFn).not.toHaveText("");
    await hideBoxFn.click();
    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
    await expect(boxBodyFn).toHaveText("");
  });

  test("Accordion: Box 3: 'hide' to 'show' to 'hide'", async ({page}) => {
    const boxFn = page.locator("[id='page:mainForm:accordionBox3']");
    const showBoxFn = page.locator("[id='page:mainForm:showBox3']");
    const hideBoxFn = page.locator("[id='page:mainForm:hideBox3']");

    await expect(boxFn).toContainClass("tobago-collapsed");
    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
    await showBoxFn.click();
    await expect(boxFn).not.toContainClass("tobago-collapsed");
    await expect(showBoxFn).toBeHidden();
    await expect(hideBoxFn).toBeVisible();
    await hideBoxFn.click();
    await expect(showBoxFn).toBeVisible();
    await expect(hideBoxFn).toBeHidden();
  });
});
