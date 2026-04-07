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

test.describe("070-tab/00-basic-example/Basic_example.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/070-tab/00-basic-example/Basic_example.xhtml");
  });

  test("Client: Select Tab 3", async ({page}) => {
  let hiddenInputFn = page.locator("[id='page:mainForm:tabGroupClient::index']");
  let tab1Fn = page.locator("[id='page:mainForm:tab1Client'] .nav-link");
  let tab3Fn = page.locator("[id='page:mainForm:tab3Client'] .nav-link");


  await tab1Fn.click();
  await expect(hiddenInputFn).toHaveValue("0");
  await expect(tab1Fn).toContainClass("active");
  await expect(tab3Fn).not.toContainClass("active");
  await tab3Fn.click();
  await expect(hiddenInputFn).toHaveValue("3");
  await expect(tab1Fn).not.toContainClass("active");
  await expect(tab3Fn).toContainClass("active");

});

test("Ajax: Select Tab 3", async ({page}) => {
  const tabGroup = page.locator("[id='page:mainForm:tabGroupAjax']");
  const hiddenInputFn = page.locator("[id='page:mainForm:tabGroupAjax::index']");
  const tab1Fn = page.locator("[id='page:mainForm:tab1Ajax'] .nav-link");
  const tab3Fn = page.locator("[id='page:mainForm:tab3Ajax'] .nav-link");


  await tab1Fn.click();
  await expect(hiddenInputFn).toHaveValue("0");
  await expect(tab1Fn).toContainClass("active");
  await expect(tab3Fn).not.toContainClass("active");
  await tab3Fn.click();
  await expect(hiddenInputFn).toHaveValue("3");
  await expect(tab1Fn).not.toContainClass("active");
  await expect(tab3Fn).toContainClass("active");

  await expect(tabGroup).not.toContainClass("position-relative");

  await expect(tabGroup).not.toContainClass("position-relative");


});

test("FullReload: Select Tab 3", async ({page}) => {
  let hiddenInputFn = page.locator("[id='page:mainForm:tabGroupFullReload::index']");
  let tab1Fn = page.locator("[id='page:mainForm:tab1FullReload'] .nav-link");
  let tab3Fn = page.locator("[id='page:mainForm:tab3FullReload'] .nav-link");


  await tab1Fn.click();
  await expect(hiddenInputFn).toHaveValue("0");
  await expect(tab1Fn).toContainClass("active");
  await expect(tab3Fn).not.toContainClass("active");
  await tab3Fn.click();
  await expect(hiddenInputFn).toHaveValue("3");
  await expect(tab1Fn).not.toContainClass("active");
  await expect(tab3Fn).toContainClass("active");

});
});
