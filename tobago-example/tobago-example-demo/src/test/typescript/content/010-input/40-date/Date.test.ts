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

test.describe("010-input/40-date/Date.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/010-input/40-date/Date.xhtml");
  });

  test("inputfield with label", async ({page}) => {
    const labelFn = page.locator("[id='page:mainForm:dNormal'] > label");
    const dateFieldFn = page.locator("[id='page:mainForm:dNormal::field']");
    const sputnik = "1969-07-20";
    const other = "1999-12-31";

    await expect(labelFn).toHaveText("Date");
    await expect(dateFieldFn).toHaveValue(sputnik);
    await dateFieldFn.fill(other);
    await expect(dateFieldFn).toHaveValue(other);
  });

  test("submit", async ({page}) => {
    const dateFieldFn = page.locator("[id='page:mainForm:formSubmit:input::field']");
    const outputFn = page.locator("[id='page:mainForm:formSubmit:output'] .form-control-plaintext");
    const submitFn = page.locator("[id='page:mainForm:formSubmit:button']");

    await dateFieldFn.fill("2016-05-22");
    await submitFn.click();
    await expect(outputFn).toHaveText("2016-05-22");
    await dateFieldFn.fill("1952-07-29");
    await submitFn.click();
    await expect(outputFn).toHaveText("1952-07-29");
  });

  test("ajax", async ({page}) => {
    const dateFieldFn = page.locator("[id='page:mainForm:ajaxinput::field']");
    const outputFn = page.locator("[id='page:mainForm:outputfield'] .form-control-plaintext");

    await dateFieldFn.fill("");
    await dateFieldFn.dispatchEvent("change");
    await expect(outputFn).toHaveText("");
    await dateFieldFn.fill("1857-03-04");
    await expect(outputFn).toHaveText("1857-03-04");
  });
});
