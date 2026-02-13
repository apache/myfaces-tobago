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

test.describe("040-command/25-links/Link_Group.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/040-command/25-links/Link_Group.xhtml");
  });

  test("vertical link group", async ({page}) => {
    const topFn = page.locator("[id='page:mainForm:verticalGroupTop']");
    const middleFn = page.locator("[id='page:mainForm:verticalGroupMiddle']");
    const bottomFn = page.locator("[id='page:mainForm:verticalGroupBottom']");

    const topY = await topFn.evaluate(el => el.getBoundingClientRect().y);
    const middleY = await middleFn.evaluate(el => el.getBoundingClientRect().y);
    const bottomY = await bottomFn.evaluate(el => el.getBoundingClientRect().y);

    expect(topY).toBeLessThan(middleY);
    expect(middleY).toBeLessThan(bottomY);
  });
});
