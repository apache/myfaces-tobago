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

test.describe("040-command/10-default/Default_Command.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/040-command/10-default/Default_Command.xhtml");
  });

  test("Basics", async ({page}) => {
    const inputFn = page.locator("[id='page:mainForm:basics:binput::field']");
    const outputFn = page.locator("[id='page:mainForm:basics:boutput'] .form-control-plaintext");
    const submitFn = page.locator("[id='page:mainForm:basics:bsubmit']");

    await expect(outputFn).not.toHaveText("Tobago");
    await inputFn.fill("Tobago");
    await submitFn.click();
    await expect(outputFn).toHaveText("Tobago");
  });

  test("Multiple default buttons", async ({page}) => {
    const inputAFn = page.locator("[id='page:mainForm:outerform:forma:inputA::field']");
    const outputAFn = page.locator("[id='page:mainForm:outerform:forma:outputA'] .form-control-plaintext");
    const submitAFn = page.locator("[id='page:mainForm:outerform:forma:submitA']");
    const inputBFn = page.locator("[id='page:mainForm:outerform:formb:inputB::field']");
    const outputBFn = page.locator("[id='page:mainForm:outerform:formb:outputB'] .form-control-plaintext");
    const submitBFn = page.locator("[id='page:mainForm:outerform:submitB']");

    await expect(outputAFn).toHaveText("");
    await expect(outputBFn).toHaveText("");

    await inputAFn.fill("a");
    await inputBFn.fill("b");
    await submitAFn.click();
    await expect(outputAFn).toHaveText("a");
    await expect(outputBFn).toHaveText("");

    await inputAFn.fill("c");
    await inputBFn.fill("d");
    await submitBFn.click();
    await expect(outputAFn).toHaveText("c");
    await expect(outputBFn).toHaveText("d");
  });
});
