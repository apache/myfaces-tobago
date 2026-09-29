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

test.describe("900-test/image/image.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/image/image.xhtml");
  });

  test("Images must be among themselves", async ({page}) => {
    const image1 = page.locator("[id='page:mainForm:image1'] img");
    const image2 = page.locator("[id='page:mainForm:image2'] img");

    const image1_y = (await image1.boundingBox())?.y as number;
    const image1_height = (await image1.boundingBox())?.height as number;
    const image2_y = (await image2.boundingBox())?.y as number;

    expect(image2_y).toBeGreaterThan(image1_y + image1_height);
  });

  test("Image sizes", async ({page}) => {
    const image1 = page.locator("[id='page:mainForm:image1'] img");
    const image2 = page.locator("[id='page:mainForm:image2'] img");
    const image3 = page.locator("[id='page:mainForm:image3'] img");
    const image4 = page.locator("[id='page:mainForm:image4'] img");
    const image5 = page.locator("[id='page:mainForm:image5'] img");
    const image6 = page.locator("[id='page:mainForm:image6'] img");
    const customClassIcon = page.locator("[id='page:mainForm:customClassIcon']");
    const customClassImage = page.locator("[id='page:mainForm:customClassImage']");

    await expect(image1).toHaveJSProperty("width", 24);
    await expect(image1).toHaveJSProperty("height", 10);
    await expect(image2).toHaveJSProperty("width", 480);
    await expect(image2).toHaveJSProperty("height", 200);
    await expect(image3).toHaveJSProperty("width", 300);
    await expect(image3).toHaveJSProperty("height", 125);
    await expect(image4).toHaveJSProperty("width", 30);
    await expect(image4).toHaveJSProperty("height", 2);
    await expect(image5).toHaveJSProperty("width", 60);
    await expect(image5).toHaveJSProperty("height", 25);
    await expect(image6).toHaveJSProperty("width", 240);
    await expect(image6).toHaveJSProperty("height", 100);
    await expect(customClassIcon).toContainClass("my-custom-class");
    await expect(customClassImage).toContainClass("my-custom-class");
  });
});
