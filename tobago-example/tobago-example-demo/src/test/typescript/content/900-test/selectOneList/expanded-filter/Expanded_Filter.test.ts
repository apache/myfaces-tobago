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

test.describe("900-test/selectOneList/expanded-filter/Expanded_Filter.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/selectOneList/expanded-filter/Expanded_Filter.xhtml");
  });

  test("filter for 'asdf'; no-entries footer must be visible", async ({page}) => {
  const noEntriesFooter = page.locator("[id='page:mainForm:selectOneList'] .tobago-no-entries");
  const filterInput = page.locator("[id='page:mainForm:selectOneList::filter']");


  await expect(noEntriesFooter).toContainClass("d-none");
  await filterInput.fill('asdf');
  await filterInput.dispatchEvent("input");
  await expect(noEntriesFooter).not.toContainClass("d-none");

});
});
