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

test.describe("120-layout/60-split/Split_Layout.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/120-layout/60-split/Split_Layout.xhtml");
  });

  test("SplitLayout Example 1", async ({page}) => {
    const example = page.locator("[id='page:mainForm:example1']");
    const expectedSplitters = await getElements(example) - 1;

    expect(await getHorizontalSplitters(example)).toBe(expectedSplitters);
    expect(await getVerticalSplitters(example)).toBe(0);
    expect(await hasDoubleSplitter(example)).toBe(false);
  });

  test("SplitLayout Example 2", async ({page}) => {
    const example = page.locator("[id='page:mainForm:example2']");
    const expectedSplitters = await getElements(example) - 1;

    expect(await getHorizontalSplitters(example)).toBe(expectedSplitters);
    expect(await getVerticalSplitters(example)).toBe(0);
    expect(await hasDoubleSplitter(example)).toBe(false);
  });

  test("SplitLayout Example 3a", async ({page}) => {
    const example = page.locator("[id='page:mainForm:example3a']");
    const expectedSplitters = await getElements(example) - 1;

    expect(await getHorizontalSplitters(example)).toBe(expectedSplitters);
    expect(await getVerticalSplitters(example)).toBe(0);
    expect(await hasDoubleSplitter(example)).toBe(false);
  });

  test("SplitLayout Example 3b", async ({page}) => {
    const example = page.locator("[id='page:mainForm:example3b']");
    const expectedSplitters = await getElements(example) - 1;

    expect(await getHorizontalSplitters(example)).toBe(0);
    expect(await getVerticalSplitters(example)).toBe(expectedSplitters);
    expect(await hasDoubleSplitter(example)).toBe(false);
  });

  function getHorizontalSplitters(element: Locator): Promise<number> {
    return element.evaluate(el =>
        el.querySelectorAll(":scope > .tobago-splitLayout-horizontal").length);
  }

  function getVerticalSplitters(element: Locator): Promise<number> {
    return element.evaluate(el =>
        el.querySelectorAll(":scope > .tobago-splitLayout-vertical").length);
  }

  function hasDoubleSplitter(element: Locator): Promise<boolean> {
    return element.evaluate(el => {
      let counter = 0;
      for (const item of el.children) {
        if (item.classList.contains("tobago-splitLayout-horizontal")
            || item.classList.contains("tobago-splitLayout-vertical")) {
          counter++;
          if (counter === 2) {
            return true;
          }
        } else {
          counter = 0;
        }
      }
      return false;
    });
  }

  function getElements(element: Locator): Promise<number> {
    return element.evaluate(el => {
      let counter = 0;
      for (const item of el.children) {
        if (getComputedStyle(item).display !== "none"
            && !item.classList.contains("tobago-splitLayout-horizontal")
            && !item.classList.contains("tobago-splitLayout-vertical")) {
          counter++;
        }
      }
      return counter;
    });
  }
});
