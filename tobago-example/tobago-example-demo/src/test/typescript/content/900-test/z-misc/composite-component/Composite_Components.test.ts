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

test.describe("900-test/z-misc/composite-component/Composite_Components.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/z-misc/composite-component/Composite_Components.xhtml");
  });

  for (const [name, input, output, notRendered] of [
    ["no layout manager", "noLayoutIn", "noLayoutOut", "noLayoutNotRendered"],
    ["flowlayout", "flowLayoutIn", "flowLayoutOut", "flowLayoutNotRendered"],
    ["flexlayout", "flexLayoutIn", "flexLayoutOut", "flexLayoutNotRendered"],
    ["segmentlayout", "segLayoutIn", "segLayoutOut", "segLayoutNotRendered"],
    ["gridlayout", "gridLayoutIn", "gridLayoutOut", "gridLayoutNotRendered"],
    ["splitlayout", "splitLayoutIn", "splitLayoutOut", "splitLayoutNotRendered"],
  ]) {
    test(name, async ({page}) => {
      await expect(page.locator(`[id='page:mainForm:${input}:in::field']`)).toBeVisible();
      await expect(page.locator(`[id='page:mainForm:${output}:out']`)).toBeVisible();
      await expect(page.locator(`[id='page:mainForm:${notRendered}:in::field']`)).toHaveCount(0);
    });
  }
});
