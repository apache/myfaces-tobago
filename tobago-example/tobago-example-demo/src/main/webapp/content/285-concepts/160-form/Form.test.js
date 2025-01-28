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

import {querySelectorFn, querySelectorAllFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Form: submit form 1", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm1\\:mainIn1\\:\\:field");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm2\\:mainIn2\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm1\\:mainOut1 .form-control-plaintext");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm2\\:mainOut2 .form-control-plaintext");
  let form1SubmitButtonFn = querySelectorFn("#page\\:mainForm\\:mainForm1\\:mainSubmit1");
  let form2OutputFieldValue = form2OutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFieldFn().textContent !== "Alice",
      () => form1InputFieldFn().value = "Eve",
      "click", form1SubmitButtonFn);
  test.do(() => form1InputFieldFn().value = "Alice");
  test.do(() => form2InputFieldFn().value = "Bob");
  test.event("click", form1SubmitButtonFn, () => form1OutputFieldFn().textContent === "Alice");
  test.do(() => expect(form1InputFieldFn().value).toBe("Alice"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Alice"));
  test.do(() => expect(form2InputFieldFn().value).toBe("Bob"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.start();
});

it("Form: submit form 2", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm1\\:mainIn1\\:\\:field");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm2\\:mainIn2\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm1\\:mainOut1 .form-control-plaintext");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:mainForm2\\:mainOut2 .form-control-plaintext");
  let form2SubmitButtonFn = querySelectorFn("#page\\:mainForm\\:mainForm2\\:mainSubmit2");
  let form1OutputFieldValue = form1OutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2OutputFieldFn().textContent !== "Dave",
      () => form2InputFieldFn().value = "Frank",
      "click", form2SubmitButtonFn);
  test.do(() => form1InputFieldFn().value = "Charlie");
  test.do(() => form2InputFieldFn().value = "Dave");
  test.event("click", form2SubmitButtonFn, () => form2OutputFieldFn().textContent === "Dave");
  test.do(() => expect(form1InputFieldFn().value).toBe("Charlie"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe("Dave"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Dave"));
  test.start();
});

it("Required: submit inner form 1 without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm1\\:reqIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm1\\:reqOut1 .form-control-plaintext");
  let form1SubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm1\\:reqSubmit1");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Alice",
      () => form1InputFn().value = "Bob",
      "click", form1SubmitFn);
  test.do(() => form1InputFn().value = "Alice");
  test.event("click", form1SubmitFn, () => form1OutputFn().textContent === "Alice")
  test.do(() => expect(form1InputFn().value).toBe("Alice"));
  test.do(() => expect(form1OutputFn().textContent).toBe("Alice"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Required: submit inner form 2, violate required field", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqSubmit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form2OutputValue = form2OutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null,
      () => {
        form2InputFn().value = "Bob";
        form2OutputValue = "Bob";
      },
      "click", form2SubmitFn);
  test.do(() => form2InputFn().value = "");
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.event("click", form2SubmitFn, () => form2AlertFn() !== null)
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Required: submit inner form 2 without violations", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqSubmit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form2OutputFn().textContent !== "Bob",
      () => form2InputFn().value = "Charlie",
      "click", form2SubmitFn);
  test.do(() => form2InputFn().value = "Bob");
  test.event("click", form2SubmitFn, () => form2OutputFn().textContent === "Bob");
  test.do(() => expect(form2InputFn().value).toBe("Bob"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe("Bob"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Required: submit outer form, violate both required fields", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null && outerFormAlertFn() === null,
      () => {
        form2InputFn().value = "Charlie";
        outerFormInputFn().value = "Dave";
        form2OutputValue = "Charlie";
        outerFormOutputValue = "Dave"
      },
      "click", outerFormSubmitFn);
  test.do(() => form2InputFn().value = "");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", outerFormSubmitFn, () => form2AlertFn() !== null && outerFormAlertFn() !== null);
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).not.toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(2));
  test.start();
});

it("Required: submit outer form, violate required field in form 2", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqSubmit2");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null,
      () => {
        form2InputFn().value = "Dave";
        form2OutputValue = "Dave";
      },
      "click", form2SubmitFn);
  test.do(() => form2InputFn().value = "");
  test.do(() => outerFormInputFn().value = "Eve");
  test.event("click", outerFormSubmitFn, () => form2AlertFn() !== null && outerFormAlertFn() === null);
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe("Eve"));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Required: submit outer form, violate required field in outer form", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => outerFormAlertFn() === null,
      () => {
        form2InputFn().value = "Frank"
        outerFormInputFn().value = "Eve";
        form2OutputValue = "Frank"
        outerFormOutputValue = "Eve";
      },
      "click", outerFormSubmitFn);
  test.do(() => form2InputFn().value = "Frank");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", outerFormSubmitFn, () => outerFormAlertFn() !== null);
  test.do(() => expect(form2InputFn().value).toBe("Frank"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).not.toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Required: submit outer form without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm1\\:reqIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm1\\:reqOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqInnerForm2\\:reqOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:reqOuterForm\\:reqSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Frank"
          && form2OutputFn().textContent !== "Eve"
          && outerFormOutputFn().textContent !== "Grace",
      () => {
        form1InputFn().value = "Alice";
        form2InputFn().value = "Bob";
        outerFormInputFn().value = "Charlie";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Frank");
  test.do(() => form2InputFn().value = "Eve");
  test.do(() => outerFormInputFn().value = "Grace");
  test.event("click", outerFormSubmitFn,
      () => form1OutputFn().textContent === "Frank"
          && form2OutputFn().textContent === "Eve"
          && outerFormOutputFn().textContent === "Grace")
  test.do(() => expect(form1InputFn().value).toBe("Frank"));
  test.do(() => expect(form1OutputFn().textContent).toBe("Frank"));
  test.do(() => expect(form2InputFn().value).toBe("Eve"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe("Eve"));
  test.do(() => expect(outerFormInputFn().value).toBe("Grace"));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe("Grace"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Ajax: submit inner form 1 without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form1SubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxSubmit1");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Alice",
      () => form1InputFn().value = "Bob",
      "click", form1SubmitFn);
  test.do(() => form1InputFn().value = "Alice");
  test.event("click", form1SubmitFn, () => form1OutputFn().textContent === "Alice");
  test.do(() => expect(form1InputFn().value).toBe("Alice"));
  test.do(() => expect(form1OutputFn().textContent).toBe("Alice"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Ajax: submit inner form 2, violate required field", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxSubmit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form2OutputValue = form2OutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null,
      () => {
        form2InputFn().value = "Bob";
        form2OutputValue = "Bob";
      },
      "click", form2SubmitFn);
  test.do(() => form2InputFn().value = "");
  test.event("click", form2SubmitFn, () => form2AlertFn() !== null);
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Ajax: submit inner form 2 without violations", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxSubmit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form2OutputFn().textContent !== "Bob",
      () => form2InputFn().value = "Alice",
      "click", form2SubmitFn);
  test.do(() => form2InputFn().value = "Bob");
  test.event("click", form2SubmitFn, () => form2OutputFn().textContent === "Bob");
  test.do(() => expect(form2InputFn().value).toBe("Bob"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe("Bob"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Ajax: submit outer form, violate both required fields", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Dave"
          && form2AlertFn() === null && outerFormAlertFn() === null,
      () => {
        form1InputFn().value = "Alice";
        form2InputFn().value = "Bob";
        outerFormInputFn().value = "Charlie";
        form1OutputValue = "Alice"
        form2OutputValue = "Bob";
        outerFormOutputValue = "Charlie"
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Dave");
  test.do(() => form2InputFn().value = "");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", outerFormSubmitFn, () => form2AlertFn() !== null && outerFormAlertFn() !== null);
  test.do(() => expect(form1InputFn().value).toBe("Dave"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).not.toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(2));
  test.start();
});

it("Ajax: submit outer form, violate required field in form 2", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Charlie" && form2AlertFn() === null,
      () => {
        form1InputFn().value = "Alice";
        form2InputFn().value = "Bob";
        outerFormInputFn().value = "Charlie";
        form1OutputValue = "Alice"
        form2OutputValue = "Bob";
        outerFormOutputValue = "Charlie"
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Charlie");
  test.do(() => form2InputFn().value = "");
  test.do(() => outerFormInputFn().value = "Dave");
  test.event("click", outerFormSubmitFn, () => form2AlertFn() !== null);
  test.do(() => expect(form1InputFn().value).toBe("Charlie"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe("Dave"));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Ajax: submit outer form, violate required field in outer form", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => outerFormAlertFn() === null,
      () => {
        form1InputFn().value = "Dave"
        form2InputFn().value = "Eve"
        outerFormInputFn().value = "Frank";
        form1OutputValue = "Dave"
        form2OutputValue = "Eve"
        outerFormOutputValue = "Frank";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Eve");
  test.do(() => form2InputFn().value = "Frank");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", outerFormSubmitFn, () => outerFormAlertFn() !== null);
  test.do(() => expect(form1InputFn().value).toBe("Eve"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe("Frank"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).not.toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Ajax: submit outer form without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Hank"
          && form2OutputFn().textContent !== "Irene"
          && outerFormOutputFn().textContent !== "John",
      () => {
        form1InputFn().value = "Eve";
        form2InputFn().value = "Frank";
        outerFormInputFn().value = "Grace";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Hank");
  test.do(() => form2InputFn().value = "Irene");
  test.do(() => outerFormInputFn().value = "John");
  test.event("click", outerFormSubmitFn, () => form1OutputFn().textContent === "Hank"
      && form2OutputFn().textContent === "Irene" && outerFormOutputFn().textContent === "John");
  test.do(() => expect(form1InputFn().value).toBe("Hank"));
  test.do(() => expect(form1OutputFn().textContent).toBe("Hank"));
  test.do(() => expect(form2InputFn().value).toBe("Irene"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe("Irene"));
  test.do(() => expect(outerFormInputFn().value).toBe("John"));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe("John"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Ajax: submit inner forms, violate required field in form 2", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let submitInnerForms = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmitInnerForms");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Kate" && form2AlertFn() === null,
      () => {
        form1InputFn().value = "Alice";
        form2InputFn().value = "Bob";
        outerFormInputFn().value = "Charlie"
        form1OutputValue = "Alice"
        form2OutputValue = "Bob";
        outerFormOutputValue = "Charlie";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Kate");
  test.do(() => form2InputFn().value = "");
  test.do(() => outerFormInputFn().value = "Leonard");
  test.event("click", submitInnerForms, () => form2AlertFn() !== null);
  test.do(() => expect(form1InputFn().value).toBe("Kate"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe(""));
  test.do(() => expect(form2AlertFn()).not.toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe("Leonard"));
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Ajax: submit inner forms without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let submitInnerForms = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmitInnerForms");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form1OutputFn().textContent !== "Mike"
          && form2OutputFn().textContent !== "Neil"
          && outerFormAlertFn() === null,
      () => {
        form1InputFn().value = "Kate";
        form2InputFn().value = "Mike";
        outerFormInputFn().value = "Leonard";
        outerFormOutputValue = "Leonard";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Mike");
  test.do(() => form2InputFn().value = "Neil");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", submitInnerForms,
      () => form1OutputFn().textContent === "Mike" && form2OutputFn().textContent === "Neil");
  test.do(() => expect(form1InputFn().value).toBe("Mike"));
  test.do(() => expect(form1OutputFn().textContent).toBe("Mike"));
  test.do(() => expect(form2InputFn().value).toBe("Neil"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe("Neil"));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("Ajax: submit outer value, violate required field", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let submitOuterValue = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmitOuterValue");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;
  let outerFormOutputValue = outerFormOutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null && outerFormAlertFn() === null,
      () => {
        form1InputFn().value = "Leonard";
        form2InputFn().value = "Mike";
        outerFormInputFn().value = "Neil";
        form1OutputValue = "Leonard";
        form2OutputValue = "Mike";
        outerFormOutputValue = "Neil";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Oscar");
  test.do(() => form2InputFn().value = "Penny");
  test.do(() => outerFormInputFn().value = "");
  test.event("click", submitOuterValue, () => outerFormAlertFn() !== null);
  test.do(() => expect(form1InputFn().value).toBe("Oscar"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe("Penny"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe(""));
  test.do(() => expect(outerFormAlertFn()).not.toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe(outerFormOutputValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("Ajax: submit outer value without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxIn1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm1\\:ajaxOut1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxIn2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxForm2\\:ajaxOut2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxIn .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxOut .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmit");
  let submitOuterValue = querySelectorFn("#page\\:mainForm\\:ajaxOuterForm\\:ajaxSubmitOuterValue");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");
  let form1OutputValue = form1OutputFn().textContent;
  let form2OutputValue = form2OutputFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => form2AlertFn() === null && form1OutputFn().textContent !== "Quin"
          && form2OutputFn().textContent !== "Sue" && outerFormOutputFn().textContent !== "Ted",
      () => {
        form1InputFn().value = "Neil";
        form2InputFn().value = "Oscar";
        outerFormInputFn().value = "Penny";
        form1OutputValue = "Neil";
        form2OutputValue = "Oscar";
      },
      "click", outerFormSubmitFn);
  test.do(() => form1InputFn().value = "Quin");
  test.do(() => form2InputFn().value = "Sue");
  test.do(() => outerFormInputFn().value = "Ted");
  test.event("click", submitOuterValue, () => outerFormOutputFn().textContent === "Ted")
  test.do(() => expect(form1InputFn().value).toBe("Quin"));
  test.do(() => expect(form1OutputFn().textContent).toBe(form1OutputValue));
  test.do(() => expect(form2InputFn().value).toBe("Sue"));
  test.do(() => expect(form2AlertFn()).toBeNull());
  test.do(() => expect(form2OutputFn().textContent).toBe(form2OutputValue));
  test.do(() => expect(outerFormInputFn().value).toBe("Ted"));
  test.do(() => expect(outerFormAlertFn()).toBeNull());
  test.do(() => expect(outerFormOutputFn().textContent).toBe("Ted"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});
