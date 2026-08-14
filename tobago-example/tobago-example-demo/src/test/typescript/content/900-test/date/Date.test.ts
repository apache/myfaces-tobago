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

test.describe("900-test/date/Date.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/900-test/date/Date.xhtml");
  });

  test("#1 model=java.time.LocalDate", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localDateForm:localDate::field']");
    const outputFn = page.locator("[id='page:mainForm:localDateForm:localDateOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localDateForm:localDateButton']");
    const date = "2020-07-07";

    await inputFieldFn.fill(date);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(date);
  });

  test("#2 model=java.time.LocalTime", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localTimeForm:localTime::field']");
    const outputFn = page.locator("[id='page:mainForm:localTimeForm:localTimeOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localTimeForm:localTimeButton']");
    const time = "07:07";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#3 model=java.time.LocalTime step=1", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localTimeStepAForm:localTimeStepA::field']");
    const outputFn = page.locator("[id='page:mainForm:localTimeStepAForm:localTimeStepAOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localTimeStepAForm:localTimeButtonStepA']");
    const time = "07:07:07";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#4 model=java.time.LocalTime step=0.001", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localTimeStepBForm:localTimeStepB::field']");
    const outputFn = page.locator("[id='page:mainForm:localTimeStepBForm:localTimeStepBOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localTimeStepBForm:localTimeButtonStepB']");
    const time = "07:07:07.007";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#5 model=java.time.LocalDateTime", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localDateTimeForm:localDateTime::field']");
    const outputFn = page.locator("[id='page:mainForm:localDateTimeForm:localDateTimeOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localDateTimeForm:localDateTimeButton']");
    const time = "2020-07-07T07:07";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#6 model=java.time.LocalDateTime step=1", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localDateTimeStepAForm:localDateTimeStepA::field']");
    const outputFn = page.locator("[id='page:mainForm:localDateTimeStepAForm:localDateTimeStepAOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localDateTimeStepAForm:localDateTimeButtonStepA']");
    const time = "2020-07-07T07:07:07";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#7 model=java.time.LocalDateTime step=0.001", async ({page}) => {
    const inputFieldFn = page.locator("[id='page:mainForm:localDateTimeStepBForm:localDateTimeStepB::field']");
    const outputFn = page.locator("[id='page:mainForm:localDateTimeStepBForm:localDateTimeStepBOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:localDateTimeStepBForm:localDateTimeButtonStepB']");
    const time = "2020-07-07T07:07:07.007";

    await inputFieldFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#11 model=java.util.Date type=time", async ({page}) => {
    const dateFn = page.locator("[id='page:mainForm:dateTimeForm:dateTime::field']");
    const outputFn = page.locator("[id='page:mainForm:dateTimeForm:dateTimeOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:dateTimeForm:dateTimeButton']");
    const time = "12:34";

    await dateFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#12 model=java.util.Date type=time step=1", async ({page}) => {
    const dateFn = page.locator("[id='page:mainForm:dateTimeStep1Form:dateTimeStep1::field']");
    const outputFn = page.locator("[id='page:mainForm:dateTimeStep1Form:dateTimeStep1Output'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:dateTimeStep1Form:dateTimeStep1Button']");
    const time = "12:34:56";

    await dateFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#13 model=java.util.Date type=datetime-local", async ({page}) => {
    const dateFn = page.locator("[id='page:mainForm:dateDateTimeForm:dateDateTime::field']");
    const outputFn = page.locator("[id='page:mainForm:dateDateTimeForm:dateDateTimeOutput'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:dateDateTimeForm:dateDateTimeButton']");
    const time = "2010-05-30T23:45";

    await dateFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });

  test("#14 model=java.util.Date type=datetime-local step=1", async ({page}) => {
    const dateFn = page.locator("[id='page:mainForm:dateDateTimeStep1Form:dateDateTimeStep1::field']");
    const outputFn = page.locator("[id='page:mainForm:dateDateTimeStep1Form:dateDateTimeStep1Output'] .form-control-plaintext");
    const submitButtonFn = page.locator("[id='page:mainForm:dateDateTimeStep1Form:dateDateTimeStep1Button']");
    const time = "2010-05-30T23:45:32";

    await dateFn.fill(time);
    await submitButtonFn.click();
    await expect(outputFn).toHaveText(time);
  });
});
