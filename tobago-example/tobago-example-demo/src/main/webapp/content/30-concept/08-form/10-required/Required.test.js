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
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form1SubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:submit1");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Alice");
  test.do(() => form1SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Alice");
  test.do(() => expect(form1InputFieldFn().value).toBe("Alice"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Alice"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit inner form 2, violate required field", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2SubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => form2SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "");
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit inner form 2 without violations", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2SubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "Bob");
  test.do(() => expect(form2InputFieldFn().value).toBe("Bob"));
  test.do(() => form2SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "Bob");
  test.do(() => expect(form2InputFieldFn().value).toBe("Bob"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Bob"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit outer form, violate both required fields", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "");
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(2));
  test.start();
});

it("submit outer form, violate required field in form 2", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => outerFormInputFieldFn().value = "Charlie");
  test.do(() => outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "");
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("Charlie"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit outer form, violate required field in outer form", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "Dave");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "Dave");
  test.do(() => expect(form2InputFieldFn().value).toBe("Dave"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit outer form without violations", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Eve");
  test.do(() => form2InputFieldFn().value = "Frank");
  test.do(() => outerFormInputFieldFn().value = "Grace");
  test.do(() => outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Eve");
  test.do(() => expect(form1InputFieldFn().value).toBe("Eve"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Eve"));
  test.do(() => expect(form2InputFieldFn().value).toBe("Frank"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Frank"));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("Grace"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe("Grace"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});
