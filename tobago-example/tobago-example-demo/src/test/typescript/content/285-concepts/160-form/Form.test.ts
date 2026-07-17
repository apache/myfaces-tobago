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

test.describe("285-concepts/160-form/Form.xhtml", () => {

  test.beforeEach(async ({page}) => {
    await page.goto("/content/285-concepts/160-form/Form.xhtml");
  });

  test("Form: submit form 1; submit form 2", async ({page}) => {
    const form1InputField = page.locator("[id='page:mainForm:mainForm1:mainIn1::field']");
    const form1OutputField = page.locator("[id='page:mainForm:mainForm1:mainOut1'] .form-control-plaintext");
    const form1SubmitButton = page.locator("[id='page:mainForm:mainForm1:mainSubmit1']");
    const form2InputField = page.locator("[id='page:mainForm:mainForm2:mainIn2::field']");
    const form2OutputField = page.locator("[id='page:mainForm:mainForm2:mainOut2'] .form-control-plaintext");
    const form2SubmitButton = page.locator("[id='page:mainForm:mainForm2:mainSubmit2']");

    await expect(form1InputField).toHaveValue("");
    await expect(form2InputField).toHaveValue("");
    await expect(form1OutputField).toHaveText("");
    await expect(form2OutputField).toHaveText("");
    await form1InputField.fill("Alice");
    await form2InputField.fill("Bob");
    await form1SubmitButton.click();
    await expect(form1InputField).toHaveValue("Alice");
    await expect(form2InputField).toHaveValue("Bob");
    await expect(form1OutputField).toHaveText("Alice");
    await expect(form2OutputField).toHaveText("");

    await form1InputField.fill("Charlie");
    await form2InputField.fill("Dave");
    await form2SubmitButton.click();
    await expect(form1InputField).toHaveValue("Charlie");
    await expect(form2InputField).toHaveValue("Dave");
    await expect(form1OutputField).toHaveText("Alice");
    await expect(form2OutputField).toHaveText("Dave");
  });

  test("Required", async ({page}) => {
    const alert = page.locator("[id='page:messages'] .alert-danger label");
    const form1Input = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm1:reqIn1::field']");
    const form1Output = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm1:reqOut1'] .form-control-plaintext");
    const form1Submit = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm1:reqSubmit1']");
    const form2Input = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm2:reqIn2::field']");
    const form2Alert = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm2:reqIn2'] .tobago-messages-container");
    const form2Output = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm2:reqOut2'] .form-control-plaintext");
    const form2Submit = page.locator("[id='page:mainForm:reqOuterForm:reqInnerForm2:reqSubmit2']");
    const outerFormInput = page.locator("[id='page:mainForm:reqOuterForm:reqIn::field']");
    const outerFormAlert = page.locator("[id='page:mainForm:reqOuterForm:reqIn'] .tobago-messages-container");
    const outerFormOutput = page.locator("[id='page:mainForm:reqOuterForm:reqOut'] .form-control-plaintext");
    const outerFormSubmit = page.locator("[id='page:mainForm:reqOuterForm:reqSubmit']");

    //valid form 1 submit
    await form1Input.fill("Alice");
    await form1Submit.click();
    await expect(alert).toHaveCount(0);
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("");
    await expect(outerFormOutput).toHaveText("");

    //invalid form 2 submit
    await form2Submit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("");
    await expect(outerFormOutput).toHaveText("");

    //valid form 2 submit
    await form2Input.fill("Bob");
    await form2Submit.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("Bob");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: both required fields invalid
    await form2Input.fill("");
    await outerFormInput.fill("");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(2);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: form 2 field invalid
    await form2Input.fill("");
    await outerFormInput.fill("Eve");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("Eve");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: outer form field invalid
    await form2Input.fill("Frank");
    await outerFormInput.fill("");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("Frank");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //valid outer form submit
    await form1Input.fill("Frank");
    await form2Input.fill("Eve");
    await outerFormInput.fill("Grace");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Frank");
    await expect(form2Input).toHaveValue("Eve");
    await expect(outerFormInput).toHaveValue("Grace");
    await expect(form1Output).toHaveText("Frank");
    await expect(form2Output).toHaveText("Eve");
    await expect(outerFormOutput).toHaveText("Grace");
  });

  test("Ajax", async ({page}) => {
    const alert = page.locator("[id='page:messages'] .alert-danger label");
    const form1Input = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm1:ajaxIn1::field']");
    const form1Output = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm1:ajaxOut1'] .form-control-plaintext");
    const form1Submit = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm1:ajaxSubmit1']");
    const form2Input = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm2:ajaxIn2::field']");
    const form2Alert = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm2:ajaxIn2'] .tobago-messages-container");
    const form2Output = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm2:ajaxOut2'] .form-control-plaintext");
    const form2Submit = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxForm2:ajaxSubmit2']");
    const outerFormInput = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxIn::field']");
    const outerFormAlert = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxIn'] .tobago-messages-container");
    const outerFormOutput = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxOut'] .form-control-plaintext");
    const outerFormSubmit = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxSubmit']");
    const submitInnerForms = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxSubmitInnerForms']");
    const submitOuterValue = page.locator("[id='page:mainForm:ajaxOuterForm:ajaxSubmitOuterValue']");

    //valid form 1 submit
    await form1Input.fill("Alice");
    await form1Submit.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("");
    await expect(outerFormOutput).toHaveText("");

    //invalid form 2 submit
    await form2Input.fill("");
    await form2Submit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("");
    await expect(outerFormOutput).toHaveText("");

    //valid form 2 submit
    await form2Input.fill("Bob");
    await form2Submit.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Alice");
    await expect(form2Input).toHaveValue("Bob");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: both required fields invalid
    await form1Input.fill("Dave");
    await form2Input.fill("");
    await outerFormInput.fill("");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(2);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).toBeVisible();
    await expect(form1Input).toHaveValue("Dave");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: form 2 field invalid
    await form1Input.fill("Charlie");
    await form2Input.fill("");
    await outerFormInput.fill("Dave");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Charlie");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("Dave");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //invalid outer form submit: outer form field invalid
    await form1Input.fill("Eve");
    await form2Input.fill("Frank");
    await outerFormInput.fill("");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).toBeVisible();
    await expect(form1Input).toHaveValue("Eve");
    await expect(form2Input).toHaveValue("Frank");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Alice");
    await expect(form2Output).toHaveText("Bob");
    await expect(outerFormOutput).toHaveText("");

    //valid outer form submit
    await form1Input.fill("Hank");
    await form2Input.fill("Irene");
    await outerFormInput.fill("John");
    await outerFormSubmit.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Hank");
    await expect(form2Input).toHaveValue("Irene");
    await expect(outerFormInput).toHaveValue("John");
    await expect(form1Output).toHaveText("Hank");
    await expect(form2Output).toHaveText("Irene");
    await expect(outerFormOutput).toHaveText("John");

    //invalid inner forms submit: form 2 field invalid
    await form1Input.fill("Kate");
    await form2Input.fill("");
    await outerFormInput.fill("Leonard");
    await submitInnerForms.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Kate");
    await expect(form2Input).toHaveValue("");
    await expect(outerFormInput).toHaveValue("Leonard");
    await expect(form1Output).toHaveText("Hank");
    await expect(form2Output).toHaveText("Irene");
    await expect(outerFormOutput).toHaveText("John");

    //valid inner forms submit
    await form1Input.fill("Mike");
    await form2Input.fill("Neil");
    await outerFormInput.fill("");
    await submitInnerForms.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Mike");
    await expect(form2Input).toHaveValue("Neil");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Mike");
    await expect(form2Output).toHaveText("Neil");
    await expect(outerFormOutput).toHaveText("John");

    //invalid outer forms submit: outer forms input field invalid
    await form1Input.fill("Oscar");
    await form2Input.fill("Penny");
    await outerFormInput.fill("");
    await submitOuterValue.click();
    await expect(alert).toHaveCount(1);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).toBeVisible();
    await expect(form1Input).toHaveValue("Oscar");
    await expect(form2Input).toHaveValue("Penny");
    await expect(outerFormInput).toHaveValue("");
    await expect(form1Output).toHaveText("Mike");
    await expect(form2Output).toHaveText("Neil");
    await expect(outerFormOutput).toHaveText("John");

    //valid outer forms submit
    await form1Input.fill("Quin");
    await form2Input.fill("Sue");
    await outerFormInput.fill("Ted");
    await submitOuterValue.click();
    await expect(alert).toHaveCount(0);
    await expect(form2Alert).not.toBeVisible();
    await expect(outerFormAlert).not.toBeVisible();
    await expect(form1Input).toHaveValue("Quin");
    await expect(form2Input).toHaveValue("Sue");
    await expect(outerFormInput).toHaveValue("Ted");
    await expect(form1Output).toHaveText("Mike");
    await expect(form2Output).toHaveText("Neil");
    await expect(outerFormOutput).toHaveText("Ted");
  });
});
