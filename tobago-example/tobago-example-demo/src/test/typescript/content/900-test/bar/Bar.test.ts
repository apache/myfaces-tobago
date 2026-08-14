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

import {expect, Locator, test} from "@playwright/test";

test.describe("900-test/bar/Bar.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/bar/Bar.xhtml");
  });

  test("Vertical alignment of Labels", async ({page}) => {
    const linkSpan = page.locator("[id='page:mainForm:link'] span");
    const actionSpan = page.locator("[id='page:mainForm:action'] span");
    const dropdownSpan = page.locator("[id='page:mainForm:dropdown::command'] span");

    const linkY = await getBoundingBoxY(linkSpan);
    const actionY = await getBoundingBoxY(actionSpan);
    const dropdownY = await getBoundingBoxY(dropdownSpan);

    expect(linkY).toBe(actionY);
    expect(linkY).toBe(dropdownY);
    expect(actionY).toBe(dropdownY);
  });

  async function getBoundingBoxY(locator: Locator): Promise<number> {
    const boundingBox = (await locator.boundingBox());
    if (boundingBox === null) {
      throw new Error("Bounding box: null");
    } else {
      return boundingBox.y;
    }
  }
});
