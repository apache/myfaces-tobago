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

test.describe("900-test/segmentLayout/SegmentLayout.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/segmentLayout/SegmentLayout.xhtml");
  });

  test("Check DOM of segment layout", async ({page}) => {
    const segmentLayout = page.locator("[id='page:mainForm:segmentLayout']");
    const segLayoutIn = page.locator("[id='page:mainForm:segLayoutIn:in']");
    const segLayoutOut = page.locator("[id='page:mainForm:segLayoutOut:out']");
    const segLayoutNotRendered = page.locator("[id='page:mainForm:segLayoutNotRendered:in']");
    const style = page.locator("[id='page:mainForm:style']");
    const segmentLayoutOutputs = page.locator("tobago-segment-layout[id^='page:mainForm:segmentLayoutComposite'] tobago-out span");

    await expect(segmentLayout.locator(":scope > *")).toHaveCount(4);
    await expect(segmentLayout.locator("div")).toHaveCount(3);
    await expect(segLayoutIn).toBeVisible();
    await expect(segLayoutOut).toBeVisible();
    await expect(segLayoutNotRendered).toHaveCount(0);
    await expect(style).not.toHaveCount(0);
    expect(await style.locator("..").evaluate(element => element.tagName)).toBe("TOBAGO-SEGMENT-LAYOUT");
    await expect(segmentLayoutOutputs).toHaveCount(2);
    await expect(segmentLayoutOutputs).toHaveText(["composite-composite output value", "composite out value"]);
  });
});
