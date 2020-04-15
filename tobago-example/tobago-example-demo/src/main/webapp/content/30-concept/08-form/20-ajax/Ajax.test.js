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
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:submit1");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Alice");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Alice");
  test.do(() => expect(form1InputFieldFn().value).toBe("Alice"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Alice"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit inner form 2, violate required field", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "");
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit inner form 2 without violations", function (done) {
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form2InputFieldFn().value = "Bob");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form2InputFieldFn() && form2InputFieldFn().value === "Bob");
  test.do(() => expect(form2InputFieldFn().value).toBe("Bob"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Bob"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit outer form, violate both required fields", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Charlie");
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Charlie");
  test.do(() => expect(form1InputFieldFn().value).toBe("Charlie"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(2));
  test.start();
});

it("submit outer form, violate required field in form 2", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Dave");
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => outerFormInputFieldFn().value = "Eve");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Dave");
  test.do(() => expect(form1InputFieldFn().value).toBe("Dave"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("Eve"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit outer form, violate required field in outer form", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Frank");
  test.do(() => form2InputFieldFn().value = "Grace");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Frank");
  test.do(() => expect(form1InputFieldFn().value).toBe("Frank"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe("Grace"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit outer form without violations", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Hank");
  test.do(() => form2InputFieldFn().value = "Irene");
  test.do(() => outerFormInputFieldFn().value = "John");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Hank");
  test.do(() => expect(form1InputFieldFn().value).toBe("Hank"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Hank"));
  test.do(() => expect(form2InputFieldFn().value).toBe("Irene"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Irene"));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("John"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe("John"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit inner forms, violate required field in form 2", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Kate");
  test.do(() => form2InputFieldFn().value = "");
  test.do(() => outerFormInputFieldFn().value = "Leonard");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Kate");
  test.do(() => expect(form1InputFieldFn().value).toBe("Kate"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe(""));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("Leonard"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit inner forms without violations", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Mike");
  test.do(() => form2InputFieldFn().value = "Neil");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Mike");
  test.do(() => expect(form1InputFieldFn().value).toBe("Mike"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe("Mike"));
  test.do(() => expect(form2InputFieldFn().value).toBe("Neil"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe("Neil"));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});

it("submit outer value, violate required field", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Oscar");
  test.do(() => form2InputFieldFn().value = "Penny");
  test.do(() => outerFormInputFieldFn().value = "");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Oscar");
  test.do(() => expect(form1InputFieldFn().value).toBe("Oscar"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe("Penny"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe(""));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe(outerFormOutputFieldValue));
  test.do(() => expect(alertFn().length).toBe(1));
  test.start();
});

it("submit outer value without violations", function (done) {
  let form1InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = querySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => form1InputFieldFn().value = "Quin");
  test.do(() => form2InputFieldFn().value = "Sue");
  test.do(() => outerFormInputFieldFn().value = "Ted");
  test.do(() => buttonFn().dispatchEvent(new Event("click", {bubbles: true})));
  test.wait(() => form1InputFieldFn() && form1InputFieldFn().value === "Quin");
  test.do(() => expect(form1InputFieldFn().value).toBe("Quin"));
  test.do(() => expect(form1OutputFieldFn().textContent).toBe(form1OutputFieldValue));
  test.do(() => expect(form2InputFieldFn().value).toBe("Sue"));
  test.do(() => expect(form2OutputFieldFn().textContent).toBe(form2OutputFieldValue));
  test.do(() => expect(outerFormInputFieldFn().value).toBe("Ted"));
  test.do(() => expect(outerFormOutputFieldFn().textContent).toBe("Ted"));
  test.do(() => expect(alertFn().length).toBe(0));
  test.start();
});
