/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {expect, test} from "@playwright/test";

test.describe("900-test/selectOneList/server-side-filtering/spinner/Spinner.xhtml", () => {

  test.beforeEach(async ({page}, testInfo) => {
    await page.goto("/content/900-test/selectOneList/server-side-filtering/spinner/Spinner.xhtml");
  });

  test("Spinner position: standard", async ({page}) => {
    const input = page.locator("tobago-select-one-list[id='page:mainForm:standardSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:standardSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await filter.fill("show spinner");
    await expect(filter).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: input group", async ({page}) => {
    const input = page.locator("tobago-select-one-list[id='page:mainForm:groupSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:groupSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await filter.fill("show spinner");
    await expect(filter).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });

  test("Spinner position: popup", async ({page}) => {
    const button = page.locator("button[id='page:mainForm:openPopupButton']");
    const popup = page.locator("[id='page:mainForm:popup']");
    const collapse = page.locator("input[id='page:mainForm:popup::collapse']");
    const input = page.locator("tobago-select-one-list[id='page:mainForm:popup:popupSelectOneList']");
    const field = input.locator(".tobago-select-field");
    const filter = input.locator("input[id='page:mainForm:popup:popupSelectOneList::filter']");
    const spinner = input.locator(".spinner");

    await button.click();
    await expect(popup).toContainClass("show");
    await expect(collapse).toHaveValue("false");
    await expect(field).toBeVisible();

    await filter.fill("show spinner");
    await expect(spinner).toBeVisible();

    const fieldBox = await field.boundingBox();
    const spinnerBox = await spinner.boundingBox();
    expect(fieldBox.x).toBeLessThanOrEqual(spinnerBox.x);
    expect(fieldBox.x + fieldBox.width).toBeGreaterThanOrEqual(spinnerBox.x + spinnerBox.width);
    expect(fieldBox.y).toBeLessThanOrEqual(spinnerBox.y);
    expect(fieldBox.y + fieldBox.height).toBeGreaterThanOrEqual(spinnerBox.y + spinnerBox.height);
  });
});
