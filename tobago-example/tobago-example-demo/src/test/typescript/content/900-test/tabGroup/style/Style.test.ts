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

test.describe("900-test/tabGroup/style/Style.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/tabGroup/style/Style.xhtml");
  });

  test("test numbers of tab-group-index", async ({page}) => {
    const tab1Fn = page.locator("[id='page:mainForm:tabOne']");
    const tab2Fn = page.locator("[id='page:mainForm:tabTwo']");
    const tab3Fn = page.locator("[id='page:mainForm:tabThree']");
    const tab4Fn = page.locator("[id='page:mainForm:tabFour']");
    const tab5Fn = page.locator("[id='page:mainForm:tabFive']");
    const tab6Fn = page.locator("[id='page:mainForm:tabSix']");
    const tabContent1Fn = page.locator("[id='page:mainForm:tabOne::content']");
    const tabContent2Fn = page.locator("[id='page:mainForm:tabTwo::content']");
    const tabContent3Fn = page.locator("[id='page:mainForm:tabThree::content']");
    const tabContent4Fn = page.locator("[id='page:mainForm:tabFour::content']");
    const tabContent5Fn = page.locator("[id='page:mainForm:tabFive::content']");
    const tabContent6Fn = page.locator("[id='page:mainForm:tabSix::content']");

    await expect(tab1Fn).toHaveAttribute("index", "0");
    await expect(tab2Fn).toHaveAttribute("index", "1");
    await expect(tab3Fn).toHaveCount(0);
    await expect(tab4Fn).toHaveAttribute("index", "3");
    await expect(tab5Fn).toHaveAttribute("index", "4");
    await expect(tab6Fn).toHaveAttribute("index", "5");
    await expect(tabContent1Fn).toHaveAttribute("data-index", "0");
    await expect(tabContent2Fn).toHaveAttribute("data-index", "1");
    await expect(tabContent3Fn).toHaveCount(0);
    await expect(tabContent4Fn).toHaveCount(0);
    await expect(tabContent5Fn).toHaveAttribute("data-index", "4");
    await expect(tabContent6Fn).toHaveAttribute("data-index", "5");
    await expect(tab1Fn.locator(".nav-link")).toContainClass("active");
    await expect(tab2Fn.locator(".nav-link")).not.toContainClass("active");
    await expect(tab4Fn.locator(".nav-link")).not.toContainClass("active");
    await expect(tab5Fn.locator(".nav-link")).not.toContainClass("active");
    await expect(tab6Fn.locator(".nav-link")).not.toContainClass("active");
    await expect(tabContent1Fn).toContainClass("active");
    await expect(tabContent2Fn).not.toContainClass("active");
    await expect(tabContent4Fn).toHaveCount(0);
    await expect(tabContent5Fn).not.toContainClass("active");
    await expect(tabContent6Fn).not.toContainClass("active");
  });

  test("test custom-css-class", async ({page}) => {
    const tab6Fn = page.locator("[id='page:mainForm:tabSix']");
    const tabContent6Fn = page.locator("[id='page:mainForm:tabSix::content']");

    await expect(tab6Fn).toContainClass("tab-six-custom");
    await expect(tabContent6Fn).toContainClass("tab-six-custom");
  });
});
