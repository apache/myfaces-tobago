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

test.describe("090-tree/00-command/Tree_Command_Types.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/090-tree/00-command/Tree_Command_Types.xhtml");
  });

  test("clicking tree commands increments both action counters", async ({page}) => {
    const action1 = page.locator("[id='page:mainForm:tree:2:actionCommand']");
    const action2 = page.locator("[id='page:mainForm:tree:3:actionCommand']");
    const actionCount1 = page.locator("[id='page:mainForm:actionCount1'] .form-control-plaintext");
    const actionCount2 = page.locator("[id='page:mainForm:actionCount2'] .form-control-plaintext");

    await action1.click();
    await expect(actionCount1).toHaveText("1");
    await expect(actionCount2).toHaveText("0");

    await action2.click();
    await expect(actionCount1).toHaveText("1");
    await expect(actionCount2).toHaveText("1");

    await action1.click();
    await expect(actionCount1).toHaveText("2");
    await expect(actionCount2).toHaveText("1");

    await action2.click();
    await expect(actionCount1).toHaveText("2");
    await expect(actionCount2).toHaveText("2");
  });
});
