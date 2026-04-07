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

test.describe("010-input/10-in/In.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/010-input/10-in/In.xhtml");
  });

  test("inputfield with label", async ({page}) => {
    const labelFn = page.locator("[id='page:mainForm:iNormal'] > label");
    const inputFieldFn = page.locator("[id='page:mainForm:iNormal::field']");

    await expect(labelFn).toHaveText("Input");
    await expect(inputFieldFn).toHaveValue("Some Text");
    await inputFieldFn.fill("abc");
    await expect(inputFieldFn).toHaveValue("abc");
  });

  test("ajax change event", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:inputAjax::field']");
    const outputFieldFn = page.locator("[id='page:mainForm:outputAjax'] .form-control-plaintext");

    await inputFieldFn.fill("some input text");
    await expect(outputFieldFn).toHaveText("some input text");
  });
});
