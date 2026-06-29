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

test.describe("080-sheet/90-lazy/Lazy.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/080-sheet/90-lazy/Lazy.xhtml");
  });

  test("Scroll position", async ({page}) => {
    const sheetBody = page.locator("tobago-sheet[id='page:mainForm:sheet'] .tobago-body");
    const scrollTop = await sheetBody.evaluate(el => el.scrollTop);
    expect(scrollTop).toBe(0);
  });
});
