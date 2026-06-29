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

test.describe("170-validation/00/Content_Validation.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/170-validation/00/Content_Validation.xhtml");
  });

  test("Required: Submit without content.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const textareaFn = page.locator("[id='page:mainForm:required:textarea::field']");
    const submitFn = page.locator("[id='page:mainForm:required:submit_r']");

    await textareaFn.fill("");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
    await expect(textareaFn).toHaveValue("");
  });

  test("Required: Submit with content.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const textareaFn = page.locator("[id='page:mainForm:required:textarea::field']");
    const submitFn = page.locator("[id='page:mainForm:required:submit_r']");

    await textareaFn.fill("some content");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
    await expect(textareaFn).toHaveValue("some content");
  });

  test("Validate Length: Submit single character.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateLength:in_vl::field']");
    const submitFn = page.locator("[id='page:mainForm:validateLength:submit_vl']");

    await inFn.fill("a");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Validate Length: Submit two character.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateLength:in_vl::field']");
    const submitFn = page.locator("[id='page:mainForm:validateLength:submit_vl']");

    await inFn.fill("ab");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
  });

  test("Validate Range: Submit no number.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateRange:in_vr::field']");
    const submitFn = page.locator("[id='page:mainForm:validateRange:submit_vr']");

    await inFn.fill("no number");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Validate Range: Submit number '2' which is out of range.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateRange:in_vr::field']");
    const submitFn = page.locator("[id='page:mainForm:validateRange:submit_vr']");

    await inFn.fill("2");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Validate Range: Submit number '77778' which is out of range.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateRange:in_vr::field']");
    const submitFn = page.locator("[id='page:mainForm:validateRange:submit_vr']");

    await inFn.fill("77778");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Validate Range: Submit number '64' which is within the range.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:validateRange:in_vr::field']");
    const submitFn = page.locator("[id='page:mainForm:validateRange:submit_vr']");

    await inFn.fill("64");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
  });

  test("Regex Validation: Submit 'T' which violates the pattern.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:regexValidation:in_rv::field']");
    const submitFn = page.locator("[id='page:mainForm:regexValidation:submit_rv']");

    await inFn.fill("T");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Regex Validation: Submit '3' which violates the pattern.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:regexValidation:in_rv::field']");
    const submitFn = page.locator("[id='page:mainForm:regexValidation:submit_rv']");

    await inFn.fill("3");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Regex Validation: Submit 'T3' which is accepted.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:regexValidation:in_rv::field']");
    const submitFn = page.locator("[id='page:mainForm:regexValidation:submit_rv']");

    await inFn.fill("T3");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
  });

  test("Custom Validator: Submit rejected string.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:customValidator:in_cv::field']");
    const submitFn = page.locator("[id='page:mainForm:customValidator:submit_cv']");

    await inFn.fill("java");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(1);
  });

  test("Custom Validator: Submit accepted string.", async ({page}) => {
    const messagesFn = page.locator("[id='page:messages'] .alert");
    const inFn = page.locator("[id='page:mainForm:customValidator:in_cv::field']");
    const submitFn = page.locator("[id='page:mainForm:customValidator:submit_cv']");

    await inFn.fill("tobago");
    await submitFn.click();
    await expect(messagesFn).toHaveCount(0);
  });
});
