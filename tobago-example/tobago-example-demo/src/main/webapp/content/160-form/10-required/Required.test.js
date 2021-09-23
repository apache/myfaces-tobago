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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("submit inner form 1 without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 .form-control-plaintext");
  let form1SubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:submit1");
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

it("submit inner form 2, violate required field", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
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

it("submit inner form 2 without violations", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
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

it("submit outer form, violate both required fields", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
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

it("submit outer form, violate required field in form 2", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let form2SubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
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

it("submit outer form, violate required field in outer form", function (done) {
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
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

it("submit outer form without violations", function (done) {
  let form1InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form1OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 .form-control-plaintext");
  let form2InputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2AlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2 .tobago-messages-container");
  let form2OutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 .form-control-plaintext");
  let outerFormInputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormAlertFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in .tobago-messages-container");
  let outerFormOutputFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out .form-control-plaintext");
  let outerFormSubmitFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
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
